package com.gj.linterna;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.gj.linterna.screens.InGameScreen;
import com.gj.linterna.screens.MenuScreen;

public class LinternaMain extends Game {

	@Override
	public void create() {
		setScreen(this.getMenuScreen(null));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
	
	public Screen getInGameScreen(Screen prev){
		if(prev!=null){
			prev = null;
		}
		return new InGameScreen(this);
	}
	
	public Screen getMenuScreen(Screen prev){
		if(prev!=null){
			prev = null;
		}
		return new MenuScreen(this);
	}
}
