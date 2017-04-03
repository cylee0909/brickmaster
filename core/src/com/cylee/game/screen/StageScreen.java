package com.cylee.game.screen;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cylee.game.BrickLaunch;
import com.cylee.game.WorldManager;
import com.cylee.game.actor.CloudActor;
import com.cylee.game.actor.FixActor;
import com.cylee.game.interfaces.GameProgressListener;
import com.cylee.game.interfaces.OnScoreChangeListener;
import com.cylee.game.stage.MainStage;
import com.cylee.game.util.Assets;
import com.cylee.game.util.Config;

public class StageScreen implements Screen , OnScoreChangeListener, GameProgressListener{
	private static final Random mRandom = new Random(System.currentTimeMillis());
	private MainStage mStage;
	private Stage mFixedStage;
//	private Box2DDebugRenderer mDebugRenderer;
	private BrickLaunch mLaunch;
	private SpriteBatch mSpriteBatch;
	private CloudActor[] mClouds;
	private int mScore;
	private int mMode;
	private Window mWindow;
	private boolean mWindowShowing;
	private WorldManager mWorldManager;
	private OnGameProGressChangeListener mGameProcessChangeListener;
	private long mStartTime;
	private int mRemainTime;
	private boolean mGameOver;
	private float mViewPortY;
	public StageScreen(int mode) {
		mMode = mode;
		startNew();
	}
	
	public void setMode(int mode) {
		mMode = mode;
	}
	
	public void startNew() {
		mScore = 0;
		mGameOver = false;
		mWindowShowing = false;
		mStartTime = System.currentTimeMillis();
		mWorldManager = new WorldManager();
		mSpriteBatch = new SpriteBatch();
		mStage = new MainStage(mWorldManager);
		mStage.setGameProgressListener(this);
		
		mFixedStage = new Stage(){
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				return super.touchDown(screenX, screenY, pointer, button);
			}
			
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.BACK) {
					return true;
				}
				return super.keyDown(keyCode);
			}
			
			@Override
			public boolean keyUp(int keyCode) {
				if (keyCode == Input.Keys.BACK) {
					if (mGameProcessChangeListener != null) {
						mGameProcessChangeListener.onGameRetry();
					}
					return true;
				}
				return super.keyUp(keyCode);
			}
		};
		
		Viewport port = new FitViewport(Config.VIEWPORT_WIDTH, Config.VIEWPORT_HEIGHT);
		mStage.setViewport(port);
		mStage.setOnScoreChangeListener(this);
		
		Viewport fixedPort = new FitViewport(Config.VIEWPORT_WIDTH, Config.VIEWPORT_HEIGHT);
		mFixedStage.setViewport(fixedPort);
		
		initCloud();
		mLaunch = new BrickLaunch(mFixedStage, mStage);
	}
	
	public WorldManager getWorldManager() {
		return mWorldManager;
	}
	
	public Stage getFixStage() {
		return mFixedStage;
	}
	
	public MainStage getMainStage() {
		return mStage;
	}
	
	@Override
	public boolean isGameOver() {
		return mGameOver;
	}
	
	public int getMode() {
		return mMode;
	}
	
	private void initCloud() {
		mClouds = new CloudActor[2];
		float baseSpeed = mFixedStage.getWidth() /5f / Gdx.graphics.getWidth();
		float cloud1SpeedX = baseSpeed * (mRandom.nextFloat() * 2 + 0.5f);
		float cloud2SpeedX = baseSpeed * (mRandom.nextFloat() * 2 + 0.5f);
		TextureRegion cloudR1 = Assets.getInstance().mCloudRegion1;
		TextureRegion cloudR2 = Assets.getInstance().mCloudRegion2;
		
		float ch = 2f;
		
		int cw1 = cloudR1.getRegionWidth();
		int ch1 = cloudR1.getRegionHeight();
		
		float rw1 = ch / ch1 * cw1;
		
		mClouds[0] = new CloudActor(cloudR1 ,rw1 * mRandom.nextFloat() , mFixedStage.getWidth() - (ch + 1) * (mRandom.nextFloat() + 0.5f), rw1, ch);
		mClouds[0].setSpeedX(cloud1SpeedX);
		mClouds[0].setDirection(mRandom.nextFloat() > 0.5 ? -1 : 1);
		mClouds[0].setMinBoundX((int)(-rw1*2));
		mClouds[0].setMaxBoundX((int)(mFixedStage.getWidth() + rw1));
		
		int cw2 = cloudR2.getRegionWidth();
		int ch2 = cloudR2.getRegionHeight();
		
		float rw2 = ch / ch2 * cw2;
		
		mClouds[1] = new CloudActor(cloudR2 ,rw2 + mFixedStage.getWidth() , mFixedStage.getWidth() - 2 *(ch + 1) * (mRandom.nextFloat() + 0.5f), rw2, ch);
		mClouds[1].setSpeedX(cloud2SpeedX);
		mClouds[1].setDirection(mRandom.nextFloat() > 0.5 ? -1 : 1);
		mClouds[1].setMinBoundX((int)(-rw2*2));
		mClouds[1].setMaxBoundX((int)(mFixedStage.getWidth() + rw2));
		
		for (CloudActor actor : mClouds) {
			mFixedStage.addActor(actor);
		}
	}

	@Override
	public void dispose() {
		mStage.dispose();
		mFixedStage.dispose();
		mSpriteBatch.dispose();
		mWorldManager.dispose();
		mScore = 0;
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		mViewPortY= mStage.getCamera().position.y;
	}

	@Override
	public void render(float delt) {
		Gdx.gl20.glClearColor(0f, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mSpriteBatch.setProjectionMatrix(mFixedStage.getViewport().getCamera().combined);
		
		mSpriteBatch.begin();
		renderDecorate();
		mSpriteBatch.end();
		
		mWorldManager.step(delt);
		mStage.act(delt);
		mFixedStage.act(delt);
		mFixedStage.draw();
		mStage.draw();
		
		mSpriteBatch.begin();
		drawScore(mSpriteBatch);
		if (mMode == MenuScreen.BN_IDENTIFY_CHANLENGE) {
			drawTime(mSpriteBatch);
		}
		mSpriteBatch.end();
		mLaunch.step(delt);
	}

	private void drawTime(SpriteBatch batch) {
		int timeUsed = (int)((System.currentTimeMillis() - mStartTime) / 1000f);
		mRemainTime = Math.max(32 - timeUsed, 0);
		int ten = mRemainTime / 10;
		int n = mRemainTime % 10;
		Texture[] nums = Assets.getInstance().mNums;
		Texture texture = nums[0];
		int imgWidth = texture.getWidth();
		int imgHeight = texture.getHeight();
		
		float topMargin = 0.2f;
		float gap = 0.1f;
		float drawHeight = 1f;
		float drawWidth = drawHeight / imgHeight * imgWidth;
		
		float y = Config.VIEWPORT_HEIGHT - (topMargin + drawHeight);
		float x = topMargin;
		batch.draw(nums[ten], x, y, drawWidth, drawHeight);
		batch.draw(nums[n], x + (gap + drawWidth), y, drawWidth, drawHeight);
		if (mRemainTime <= 0) {
			mGameOver = true;
			showGameOverDialog();
		}
	}
	
	private void drawScore(SpriteBatch batch) {
		int thousand = mScore / 1000;
		int hundred = (mScore % 1000) / 100;
		int ten = (mScore % 100) / 10;
		int n = mScore % 10;
		Texture[] nums = Assets.getInstance().mNums;
		Texture texture = nums[0];
		int imgWidth = texture.getWidth();
		int imgHeight = texture.getHeight();
		
		float topMargin = 0.2f;
		float gap = 0.1f;
		float drawHeight = 1f;
		float drawWidth = drawHeight / imgHeight * imgWidth;
		
		float y = Config.VIEWPORT_HEIGHT - (topMargin + drawHeight);
		float x = Config.VIEWPORT_WIDTH - topMargin - drawWidth;
		batch.draw(nums[n], x, y, drawWidth, drawHeight);
		batch.draw(nums[ten], x - (gap + drawWidth), y, drawWidth, drawHeight);
		batch.draw(nums[hundred], x - (gap + drawWidth) * 2, y, drawWidth, drawHeight);
		batch.draw(nums[thousand], x - (gap + drawWidth) * 3, y, drawWidth, drawHeight);
	}

	private void renderDecorate() {
		mSpriteBatch.disableBlending();
		mSpriteBatch.draw(Assets.getInstance().mBg, 0, 0, Config.VIEWPORT_WIDTH, Config.VIEWPORT_HEIGHT);
		mSpriteBatch.enableBlending();
	}

	@Override
	public void resize(int w, int h) {
		mStage.getViewport().update(w, h, true);
		mFixedStage.getViewport().update(w, h, true);
	}

	@Override
	public void resume() {
		mStage.getCamera().position.y = mViewPortY;;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(mFixedStage, mStage));
	}
	
	@Override
	public void onScoreChanged(int score) {
		if (score == Config.CONTACT_GROUND) {
			if (mMode == MenuScreen.BN_IDENTIFY_CLASSIFY) {
				mGameOver = true;
				showGameOverDialog();
			}
		} else {
			mScore = score;
		}
	}
	
	private void showGameOverDialog() {
		if (mWindow == null) {
			TextureRegionDrawable bg = new TextureRegionDrawable(Assets.getInstance().mDialogBgRegion);
			Window.WindowStyle style = new Window.WindowStyle();
			style.background = bg;
			style.titleFont = Assets.getInstance().mFont;
			Window window = new Window("", style);
			window.setWidth(6);
			window.setHeight(4);
			window.setPosition(Config.VIEWPORT_WIDTH / 2, Config.VIEWPORT_HEIGHT / 2);

			FixActor confirm = new FixActor(0,0, 2.4f, 1);
			confirm.setTextureRegion(Assets.getInstance().mConfirmRegion);
			confirm.addListener(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					return true;
				}
				
				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					if (mGameProcessChangeListener != null) {
						mGameProcessChangeListener.onGameRetry();
					}
				}
			});
			window.add(confirm);
			mWindow = window;
		}
		if (!mWindowShowing) {
			mFixedStage.addActor(mWindow);
			mWindowShowing = true;
		}
	}
	
	public void setOnGameProgressChangeListener(OnGameProGressChangeListener listener) {
		mGameProcessChangeListener = listener;
	}
	
	public interface OnGameProGressChangeListener {
		void onGameRetry();
	}
}
