package com.cylee.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.cylee.game.interfaces.IResourceLoadListener;
import com.cylee.game.screen.MenuScreen;
import com.cylee.game.screen.MenuScreen.OnMenuClickListener;
import com.cylee.game.screen.StageScreen;
import com.cylee.game.screen.StageScreen.OnGameProGressChangeListener;
import com.cylee.game.screen.WelcomScreen;
import com.cylee.game.util.Assets;

public class GdxGame extends Game implements OnMenuClickListener, OnGameProGressChangeListener, IResourceLoadListener{
	private StageScreen mScreen;
	private MenuScreen mMenuScreen;
	private Screen mWelcomScreen;
	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		mWelcomScreen = new WelcomScreen(this);
		setScreen(mWelcomScreen);
	}
	
	@Override
	public void dispose() {
		if (mScreen != null) {
			mScreen.dispose();
			mScreen = null;
		}
		
		if (mMenuScreen != null) {
			mMenuScreen.dispose();
		}
		
		if (mWelcomScreen != null) {
			mWelcomScreen.dispose();
		}
		Assets.getInstance().dispose();
		super.dispose();
	}

	@Override
	public void onMenuCLick(int identify) {
		if (identify == MenuScreen.BN_IDENTIFY_CLASSIFY
				|| identify == MenuScreen.BN_IDENTIFY_CHANLENGE) {
			startNewGame(identify);
		} else if (identify == MenuScreen.BN_IDENTIFY_EXIT) {
			Gdx.app.exit();
		}
	}

	private void startNewGame(int mode) {
		if (mScreen == null) {
			mScreen = new StageScreen(mode);
			mScreen.setOnGameProgressChangeListener(this);
		} else {
			mScreen.setMode(mode);
			mScreen.startNew();
		}
		setScreen(mScreen);
	}
	
	@Override
	public void onGameRetry() {
		setScreen(mMenuScreen);
	}

	@Override
	public void resourceLoadComplete() {
		if (mMenuScreen == null) {
			mMenuScreen = new MenuScreen();
		}
		mMenuScreen.setMenuClickListener(this);
		setScreen(mMenuScreen);
	}
	
	@Override
	public void resume() {
		super.resume();
	}
}
