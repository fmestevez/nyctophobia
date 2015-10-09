package com.gj.linterna;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Battery {

	private Vector2 position;
	private TextureRegion textureRegion;

	public Battery(Vector2 position, World world) {
		this.position = position;
		textureRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/pila2.png")));
	}

	public void drawBattery(SpriteBatch batch) {
		batch.draw(textureRegion, position.x, position.y,
				position.x + textureRegion.getRegionHeight() / 2,
				position.y + textureRegion.getRegionWidth() / 2, 1.5f, 1.5f, 1f, 1f, 0f);
	}

	public TextureRegion getImage() {
		return textureRegion;
	}

	public Vector2 getPosition() {
		return position;
	}
}
