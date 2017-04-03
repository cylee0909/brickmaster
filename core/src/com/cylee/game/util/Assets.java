package com.cylee.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public Texture mBrick;
	public Texture mBrickPress;
	public Texture mDialogBg;
	public Texture mConfirm;
	public Texture mGroundBlock;
	public Texture mCloud1;
	public Texture mCloud2;
	public Texture mBg;
	public Texture mMenuClassify;
	public Texture mMenuClassifyF;
	public Texture mMenuChalenge;
	public Texture mMenuChalengeF;
	public Texture mMenuExit;
	public Texture mMenuExitF;
	public Texture mGuideBoard;
	public Texture mStick;
	public Texture mWelcom;
	public Texture[] mNums;
	
	public TextureRegion mBrickRegion;
	public TextureRegion mBrickPressRegion;
	public TextureRegion mGroundBlockRegion;
	public TextureRegion mCloudRegion1;
	public TextureRegion mCloudRegion2;
	public TextureRegion mDialogBgRegion;
	
	public TextureRegion mMenuClassifyRegion;
	public TextureRegion mMenuClassifyFRegion;
	public TextureRegion mMenuChalengeRegion;
	public TextureRegion mMenuChalengeFRegion;
	public TextureRegion mMenuExitRegion;
	public TextureRegion mMenuExitFRegion;
	public TextureRegion mGuideBoardRegion;
	public TextureRegion mStickRegion;
	public TextureRegion mConfirmRegion;
	
	public BitmapFont mFont;
	private static Assets mInstance;
	private Assets(){}
	private boolean mLoaded;
	private static AssetManager manager;
	public static Assets getInstance() {
		if (mInstance == null) {
			synchronized (Assets.class) {
				manager = new AssetManager();
				mInstance = new Assets();
			}
		}
		return mInstance;
	}
	
	public void addAssets() {
		addAssetsTexture("brick.png");
		addAssetsTexture("brick_press.png");
		addAssetsTexture("ground_block.png");
		addAssetsTexture("cloud1.png");
		addAssetsTexture("cloud2.png");
		addAssetsTexture("bg.png");
		addAssetsTexture("dialog_bg.png");
		addAssetsTexture("confirm.png");
		addAssetsTexture("menu_chalenge.png");
		addAssetsTexture("menu_chalenge_f.png");
		addAssetsTexture("menu_classify.png");
		addAssetsTexture("menu_classify_f.png");
		addAssetsTexture("menu_exit.png");
		addAssetsTexture("menu_exit_f.png");
		addAssetsTexture("guideboard.png");
		addAssetsTexture("stick.png");
		
		for (int i = 0; i < 10; i++) {
			addAssetsTexture("n_"+i+".png");
		}
		
		manager.load("fonts/brick.fnt", BitmapFont.class);
	}
	
	private void addAssetsTexture(String fileName) {
		manager.load("images/"+fileName, Texture.class);
	}
	
	public boolean update() {
		return manager.update();
	}
	
	public void load() {
		if (mLoaded) return;
		mNums = new Texture[10];
		mBrick = loadImage("brick.png");
		mBrickPress = loadImage("brick_press.png");
		mGroundBlock = loadImage("ground_block.png");
		mCloud1 = loadImage("cloud1.png");
		mCloud2 = loadImage("cloud2.png");
		mBg = loadImage("bg.png");
		mDialogBg = loadImage("dialog_bg.png");
		mConfirm = loadImage("confirm.png");
		
		mMenuChalenge = loadImage("menu_chalenge.png");
		mMenuChalengeF = loadImage("menu_chalenge_f.png");
		mMenuClassify = loadImage("menu_classify.png");
		mMenuClassifyF = loadImage("menu_classify_f.png");
		mMenuExit = loadImage("menu_exit.png");
		mMenuExitF = loadImage("menu_exit_f.png");
		mGuideBoard = loadImage("guideboard.png");
		mStick = loadImage("stick.png");
		
		mBrick.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mBg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mCloud1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mCloud2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		mBrickRegion = new TextureRegion(mBrick);
		mBrickPressRegion = new TextureRegion(mBrickPress);
		mGroundBlockRegion = new TextureRegion(mGroundBlock);
		mCloudRegion1 = new TextureRegion(mCloud1);
		mCloudRegion2 = new TextureRegion(mCloud2);
		mDialogBgRegion = new TextureRegion(mDialogBg);
		
		mMenuClassifyRegion = new TextureRegion(mMenuClassify);
		mMenuClassifyFRegion = new TextureRegion(mMenuClassifyF);
		mMenuChalengeRegion = new TextureRegion(mMenuChalenge);
		mMenuChalengeFRegion = new TextureRegion(mMenuChalengeF);
		mMenuExitRegion = new TextureRegion(mMenuExit);
		mMenuExitFRegion = new TextureRegion(mMenuExitF);
		mGuideBoardRegion = new TextureRegion(mGuideBoard);
		mStickRegion = new TextureRegion(mStick);
		mConfirmRegion = new TextureRegion(mConfirm);
		
		for (int i = 0; i < 10; i++) {
			mNums[i] = loadImage("n_"+i+".png");
		}
		
		mFont = manager.get("fonts/brick.fnt", BitmapFont.class);
		
		mGuideBoard.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mCloud1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mCloud2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mMenuChalenge.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mMenuClassify.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mMenuExit.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mLoaded = true;
	}
	
	private Texture loadImage(String name) {
		return manager.get("images/"+name, Texture.class);
	}
	
	public Texture loadAssetsTexture(String fileName) {
		return new Texture(Gdx.files.internal("images/"+fileName));
	}
	
	public void dispose() {
		manager.clear();
		mInstance = null;
	}
}
