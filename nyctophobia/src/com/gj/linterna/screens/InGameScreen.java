package com.gj.linterna.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gj.linterna.Battery;
import com.gj.linterna.Heart;
import com.gj.linterna.LinternaMain;
import com.gj.linterna.Player;
import com.gj.linterna.Sounds;
import com.gj.linterna.util.WorldCreator;
import com.gj.linterna.util.mazegenerator.MazeGenerator;

public class InGameScreen implements Screen {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private World world;
	private int[][] maze;
	private Matrix4 normalProjection = new Matrix4();
	private WorldCreator worldCreator;
	Box2DDebugRenderer debug;
	Matrix4 debugMatrix;
	Player player;

	private OrthographicCamera uiCamera;
	private SpriteBatch uiBatch;
	private Heart heart;

	private Sprite winSprite; // para poner todo blanco al terminar
	private float winCounter = 0.01f; // para aumentar el alpha de a poco
	private boolean levelFinish = false;
	private LinternaMain game;

	public InGameScreen(final LinternaMain game) {

		this.game = game;
		this.heart = new Heart();
		
		Sounds.initInGame();

		winSprite = new Sprite(new Texture(
				Gdx.files.internal("data/blanco.jpg")));
		winSprite.setSize(2000f, 2000f);

		camera = new OrthographicCamera(48, 32);
		camera.position.set(0, 8, 0);
		camera.update();

		uiCamera = new OrthographicCamera(4, 3);
		uiCamera.position.set(0, 0, 0);
		uiCamera.update();

		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
		player = new Player();

		player.loadTexture();
		createPhysicsWorld();
		player.initRayCaster(camera, world);

		MazeGenerator mazegen = new MazeGenerator();
		maze = mazegen.generateMaze(21, 21, 10);
		worldCreator = new WorldCreator();
		worldCreator.createWorld(world, maze, player);

		debugMatrix = new Matrix4(camera.combined);
		debug = new Box2DDebugRenderer(true, true, true, true);

		normalProjection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		Sounds.playDoor();
		Sounds.playMusic();

	}

	@Override
	public void render(float delta) {
		if (this.levelFinish) {
			this.winCounter += 0.0003f;
			uiBatch.begin();
			this.winSprite.draw(uiBatch, this.winCounter);
			uiBatch.end();
			if (this.winCounter >= 0.1f) {
				Sounds.stopMusic();
				Sounds.stopStepOne();
				Sounds.stopHeart();
				game.setScreen(game.getMenuScreen(game.getScreen()));
			}
			return;
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Sounds.stopMusic();
			Sounds.stopStepOne();
			Sounds.stopHeart();
			game.setScreen(game.getMenuScreen(game.getScreen()));
		}

		player.updateCamera(camera);
		player.updateBeatSpeed(this.heart);
		camera.update();

		debugMatrix.set(camera.combined);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		player.manageInput(camera, game);
		player.updateBattery();

		batch.disableBlending();
		batch.begin();

		for (Sprite floor : worldCreator.getFloors()) {
			floor.draw(batch);
		}

		batch.enableBlending();
		for (Battery bat : worldCreator.getBatteries()) {
			bat.drawBattery(batch);
		}
		player.drawPlayer(batch);
		batch.end();

		/* Importante */
		world.step(1 / 60f, 10, 8);
		player.updateLights(camera);

		Battery forDelete = null;
		/* detección colisión bat */
		for (Battery bat : worldCreator.getBatteries()) {
			if (bat.getPosition().dst(player.getActor().getPosition()) < 2) {
				player.renewBatteryLife();
				if (!Sounds.click) {
					Sounds.playClick();
				} else
					Sounds.stopClick();
				forDelete = bat;
			}
		}

		if (worldCreator.getExit().getPosition()
				.dst(player.getActor().getPosition()) < 2) {
			this.levelFinish = true;
		}

		if (forDelete != null)
			worldCreator.getBatteries().remove(forDelete);

		// UI
		uiBatch.setProjectionMatrix(normalProjection);
		uiBatch.enableBlending();
		uiBatch.begin();
		this.heart.draw(uiBatch);
		uiBatch.end();

	}

	private void createPhysicsWorld() {

		world = new World(new Vector2(0, 0), true);
		player.createPlayer(world);
	}

	@Override
	public void dispose() {
		this.batch.dispose();
		this.player.dispose();
		this.world.dispose();
		Sounds.disposeInGame();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
