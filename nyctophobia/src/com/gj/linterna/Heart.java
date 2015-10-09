package com.gj.linterna;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Heart {
	
	private Animation heartAnimation;
	private float stateTime;
	private float delayTime; // el tiempo entre dos latidos
	private float maxDelayTime = 2f; // el tiempo entre dos latidos
	
	private TextureRegion[] imageFrames;
	
	public Heart(){
		
		Texture heartSprite = new Texture(Gdx.files.internal("data/heart_sprite.png"));
		
		TextureRegion[][] tmp = TextureRegion.split(heartSprite, heartSprite.getWidth() / 4, heartSprite.getHeight());
		imageFrames = new TextureRegion[9];
		
		imageFrames[0] = tmp[0][0];
		imageFrames[1] = tmp[0][1];
		imageFrames[2] = tmp[0][2];
		imageFrames[3] = tmp[0][3];
		imageFrames[4] = tmp[0][2];
		imageFrames[5] = tmp[0][3];
		imageFrames[6] = tmp[0][2];
		imageFrames[7] = tmp[0][1];
		imageFrames[8] = tmp[0][0];
		
		this.heartAnimation = new Animation(0.1f, imageFrames);
		this.heartAnimation.setPlayMode(Animation.LOOP);
		this.setBeatSpeed(0);
		
		this.delayTime = this.maxDelayTime;
	}
	
	/**
	 * Dibuja el corazon
	 */
	public void draw(SpriteBatch batch){
		this.delayTime -= Gdx.graphics.getDeltaTime();
		if (this.delayTime < 0) this.stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = this.heartAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, 0, Gdx.graphics.getHeight() - currentFrame.getRegionHeight());
		if (this.delayTime < -this.heartAnimation.animationDuration) this.delayTime = this.maxDelayTime;
	}
	
	/**
	 * Setea la velocidad de pulsacion
	 */
	public void setBeatSpeed(float speed){
		this.maxDelayTime = 3 - (speed * 4);
		if (this.maxDelayTime < 0) this.maxDelayTime = 0;
	}

}
