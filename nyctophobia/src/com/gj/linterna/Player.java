package com.gj.linterna;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
	private static final int FLASHLIGHT_RAYS = 500;
	private static final float radius = 1f;
	private Body actor;
	private RayHandler rayHandler;
	private Light light;
	private float friction = 16f;
	private float batteryLife = 1f;
	private int frames = 3;
	Animation animacionPlayer = null;
	float stateTime;
	private TextureRegion currentFrame;
	private TextureRegion stand;
	
	private float ambientLightRedCounter = 0; // para la muerte
	
	public void loadTexture() {
		Texture image = new Texture(Gdx.files.internal("data/player.png"));
		TextureRegion[][] tmp = TextureRegion.split(image, 
				image.getWidth() / frames, image.getHeight());
		TextureRegion[] imageFrames = new TextureRegion[frames];
		int index = 0;
		for(int i = 0; i < frames; i++){
			imageFrames[index++] = tmp[0][i];
		}
		stand = imageFrames[1];
		animacionPlayer = new Animation(0.1f, imageFrames);
		animacionPlayer.setPlayMode(Animation.LOOP_PINGPONG);
	}

	public void initRayCaster(OrthographicCamera camera, World world) {
		/** BOX2D LIGHT STUFF BEGIN */
		RayHandler.setColorPrecisionMediump();
		RayHandler.setGammaCorrection(true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.0f);
//		rayHandler.setAmbientLight(1.0f, 0f, 0f, 1f);
		rayHandler.setCulling(true);
//		 rayHandler.setShadows(false);
		camera.update(true);
		rayHandler.setCombinedMatrix(camera.combined, camera.position.x,
				camera.position.y, camera.viewportWidth * camera.zoom,
				camera.viewportHeight * camera.zoom);
		light = new ConeLight(rayHandler, FLASHLIGHT_RAYS, Color.WHITE, 50f,
				50f, 50f, 0f, 50f);
		new PointLight(rayHandler, FLASHLIGHT_RAYS,
				new Color(1f, 1f, 1f, 0.2f), 20f, 0f, 0f).attachToBody(actor,
				0, 0);
		light.attachToBody(actor, 0, 0);
	}

	public void updateCamera(OrthographicCamera camera) {
		camera.position.set(actor.getPosition().x, actor.getPosition().y, 0f);
	}

	public void manageInput(OrthographicCamera camera, final LinternaMain game) {
		if (batteryLife <= 0){
			this.ambientLightRedCounter += 0.02;
			this.rayHandler.setAmbientLight(this.ambientLightRedCounter, 0f, 0f, this.ambientLightRedCounter);
			if (this.ambientLightRedCounter > 1.6f){
				Sounds.stopMusic();
				Sounds.stopStepOne();
				Sounds.stopHeart();
				Gdx.app.exit();
			}
		}else{
			final Vector2 accel = new Vector2();
	
			if (Gdx.input.isKeyPressed(Keys.A)) {
				accel.x += -10;
				Sounds.playStepOne();
			}else if (Gdx.input.isKeyPressed(Keys.D)) {
				accel.x += 10;
				Sounds.playStepOne();
			}else if (Gdx.input.isKeyPressed(Keys.W)) {
				accel.y += 10;
				Sounds.playStepOne();
			}else if (Gdx.input.isKeyPressed(Keys.S)) {
				accel.y += -10;
				Sounds.playStepOne();
			}else{
				Sounds.stopStepOne();
			}
	
			actor.setLinearVelocity(accel);
	
			Vector2 vel = actor.getLinearVelocity();
			Vector2 velInv = vel.mul(-1);
			velInv.mul(friction);
			actor.applyForce(velInv, actor.getWorldCenter());
	
			Vector3 worldCoordinates = new Vector3(Gdx.input.getX(),
					Gdx.input.getY(), 0);
			camera.unproject(worldCoordinates);
	
			Vector2 toTarget = new Vector2(worldCoordinates.x
					- actor.getPosition().x, worldCoordinates.y
					- actor.getPosition().y);
			float desiredAngle = (float) (Math.atan2(-toTarget.x, toTarget.y));
			desiredAngle = (float) (desiredAngle + (Math.PI / 2));
	
			actor.setTransform(actor.getPosition(), desiredAngle);
		}
	}
	
	public void updateBattery(){
		if (batteryLife < 0){
			Sounds.stopHeart();
			batteryLife = 0;
			if(!Sounds.grito){
				Sounds.playGrito();
			}		
		} else {
			batteryLife -= 0.0005;
			Sounds.setHeartPitch((float)Math.sqrt(Math.log((1 / this.batteryLife)/2) + 1));
		}
		/* Actualizo la batería */
		light.setColor(1f, 1f, 1f, batteryLife);
	}

	public void drawPlayer(SpriteBatch batch) {
		final Vector2 position = actor.getPosition();
		final float angle = MathUtils.radiansToDegrees * (float)(actor.getAngle() - Math.PI/2);
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animacionPlayer.getKeyFrame(stateTime, true);
		if(this.actor.getLinearVelocity().x == 0 && this.actor.getLinearVelocity().y == 0){
//			stand.scroll(stand.get, yAmount)
			batch.draw(stand, position.x - radius, position.y - radius,
					radius, radius, radius * 2, radius * 2, 1, 1, angle);
		} else {
			batch.draw(currentFrame, position.x - radius, position.y - radius,
					radius, radius, radius * 2, radius * 2, 1, 1, angle);
		}
	}

	public void updateLights(OrthographicCamera camera) {
		rayHandler.update();
		/* ---------- */

		rayHandler.setCombinedMatrix(camera.combined, camera.position.x,
				camera.position.y, camera.viewportWidth * camera.zoom,
				camera.viewportHeight * camera.zoom);

		rayHandler.render();
	}

	public void dispose() {
		rayHandler.dispose();
	}

	public void createPlayer(World world) {
		PolygonShape actorShape = new PolygonShape();
		actorShape.setAsBox(0.5f, 0.5f, new Vector2(0.5f, 0.5f), 0f);

		FixtureDef def = new FixtureDef();
		def.restitution = 0.9f;
		def.friction = 0.01f;
		def.shape = actorShape;
		def.density = 1f;
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;

		boxBodyDef.position.x = 0;
		boxBodyDef.position.y = 0;
		boxBodyDef.fixedRotation = true;
		actor = world.createBody(boxBodyDef);
		actor.createFixture(def);
		actorShape.dispose();
		
		if(!Sounds.heart){
			Sounds.playHeart();
		} else Sounds.stopHeart();
				
	}
	
	public void updateBeatSpeed(Heart heart){
		if (batteryLife <= 0){
			heart.setBeatSpeed(-1000);
		}else{
			heart.setBeatSpeed(1 - this.batteryLife);
		}
	}

	public Body getActor() {
		return actor;
	}
	
	public void renewBatteryLife(){
		this.batteryLife = 1f;
	}
	
	public void createLight(float x, float y, float directionDegree){
		new ConeLight(rayHandler, FLASHLIGHT_RAYS, Color.WHITE, 30f, x, y, directionDegree, 40f);
	}
}
