package com.gj.linterna.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gj.linterna.LinternaMain;
import com.gj.linterna.Sounds;

public class MenuScreen implements Screen {

	private LinternaMain game;
	private Texture logo = null;
	private Texture presstart = null;
	private float displayPress = 0f;

	public MenuScreen(LinternaMain game) {
		this.game = game;
		logo = new Texture(Gdx.files.internal("data/Nyctophobia.psd.png"));
		presstart = new Texture(Gdx.files.internal("data/pressstart.png"));
		Sounds.initMenu();
		Sounds.playIntro();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		SpriteBatch sb = new SpriteBatch();
		sb.enableBlending();
		sb.begin();
		sb.draw(logo, Gdx.graphics.getWidth() / 15f,
				Gdx.graphics.getHeight() / 2f, logo.getWidth() / 2,
				logo.getHeight() / 2);
		if(displayPress < 10){
			sb.draw(presstart, Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 5f);
			displayPress += 0.1f;
		} else if(displayPress < 20){
			displayPress += 0.1f;
		} else displayPress = 0f;
		
		sb.end();
		
		if(Gdx.input.isKeyPressed(Keys.ENTER)){
			Sounds.stopIntro();
			game.setScreen(game.getInGameScreen(game.getScreen()));
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Sounds.stopIntro();
			Gdx.app.exit();
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		Sounds.stopIntro();

	}

	@Override
	public void pause() {
		Sounds.stopIntro();

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		Sounds.disposeMenu();
	}

}
