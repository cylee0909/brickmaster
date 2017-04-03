package com.cylee.game;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cylee.game.actor.BrickActor;
import com.cylee.game.actor.FixActor;
import com.cylee.game.entity.BodyUserData;
import com.cylee.game.stage.MainStage;
import com.cylee.game.stage.MainStage.StageGestureListener;
import com.cylee.game.util.Config;

public class BrickLaunch implements StageGestureListener {
	private static final Random mRandon = new Random(System.currentTimeMillis());
	private static final float TOP_MARGIN = Config.VIEWPORT_HEIGHT / 10;
	private static final int INVALID_BRICK_WIDTH = -1;
	private static final int DEFAULT_BRICK_HEIGHT = 1;
	/** 操作台厚度 */
	private static final float PLATFORM_HEIGHT = 0.5f;
	private Stage mFixStage;
	private MainStage mMainStage;
	private Actor mCurrentLaunchPreviewActor;
	private LaunchInputListener mLaunchInputListener;
	private boolean mDraged;
	private long mDoubleChargeTime;
	private float mDownX;
	private Actor mDroppingActor;
	private WorldManager mWorldManager;
	public BrickLaunch(Stage fixStage, MainStage mainStage) {
		mWorldManager = mainStage.getWorldManager();
		mFixStage = fixStage;
		mMainStage = mainStage;
		init();
	}
	
	private void init() {
		mMainStage.setStageGestureListener(this);
		mLaunchInputListener = new LaunchInputListener();
		initLaunchPlate();
		launch();
	}
	
	private void initLaunchPlate() {
		mFixStage.addActor(new FixActor(Config.VIEWPORT_WIDTH / 2f, Config.VIEWPORT_HEIGHT - TOP_MARGIN
				- Config.MAX_BRICK_HEIGHT - PLATFORM_HEIGHT / 2, Config.VIEWPORT_WIDTH, 0.5f));
		mFixStage.addListener(mLaunchInputListener);
	}
	
	public void launch() {
		launch(DEFAULT_BRICK_HEIGHT);
	}
	
	public void launch(float h) {
		launch(INVALID_BRICK_WIDTH, h);
	}
	
	public void step(float delt) {
		if (mDroppingActor != null) {
			float chargeY = Config.VIEWPORT_HEIGHT - TOP_MARGIN
					- Config.MAX_BRICK_HEIGHT - mDroppingActor.getHeight() - PLATFORM_HEIGHT;
			float viewPortDeltY = mFixStage.getViewport().getCamera().position.y
					- mMainStage.getViewport().getCamera().position.y; // 计算两个相机视角差
			if (mDroppingActor.getY() + viewPortDeltY <= chargeY) {
				launch();
				mDroppingActor = null;
			}
		}
	}
	
	/**
	 * 使用指定的宽高发射砖块
	 * @param w
	 * @param h
	 */
	public void launch(float w, float h) {
		float bottom = TOP_MARGIN + Config.MAX_BRICK_HEIGHT - h / 2;
		if (w == INVALID_BRICK_WIDTH) {
			int seed = mRandon.nextInt(100);
			if (seed < 60) {
				w = 4;
			} else if (seed < 90) {
				w = 2;
			} else {
				w = 1;
			}
		}
		
		float x = mRandon.nextInt((int)(Config.VIEWPORT_WIDTH - w)) + w / 2;
		mCurrentLaunchPreviewActor = new FixActor(x, Config.VIEWPORT_HEIGHT - bottom, w, h);
		mFixStage.addActor(mCurrentLaunchPreviewActor);
	}
	
	private void launchActor(Actor actor) {
		if (mMainStage.isViewPortInScroll()) {
			return;
		}
		
		if (mMainStage.isViewPortChangedByHand()) {
			mMainStage.resetViewPort();
			return;
		}
		
		float x = actor.getX() + actor.getWidth() / 2;
		float y = actor.getY() + actor.getHeight() / 2;
		float w = actor.getWidth();
		float h = actor.getHeight();
		float viewPortDeltY = mFixStage.getViewport().getCamera().position.y
				- mMainStage.getViewport().getCamera().position.y; // 计算两个相机视角差
		BrickActor brickActor = new BrickActor(mWorldManager, x, y - viewPortDeltY, w, h);
		brickActor.setUserData(new BodyUserData(Config.USER_DATA_DROPPING_BRICK_IDENDITY, true));
		mMainStage.addActor(brickActor);
		mDroppingActor = brickActor;
		actor.remove();
	}
	
	private void moveActorInViewPort(Actor actor, float dx, float dy) {
		if (actor == null) return;
		float ox = actor.getX();
		float oy = actor.getY();
		float resultX = ox + dx;
		float resultY = oy + dy;
		float mx = 0, my = 0;
		if (resultX > 0 && resultX < Config.VIEWPORT_WIDTH - actor.getWidth()) {
			mx = dx;
		}
		if (resultY - actor.getHeight()> 0 && resultY < Config.VIEWPORT_HEIGHT) {
			my = dy;
		}
		actor.moveBy(mx, my);
	}
	
	/**
	 * 固定Stage的事件监听，用于发射器的移动控制，双击发射监听
	 * @author air
	 *
	 */
	class LaunchInputListener extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			if (mMainStage.isGameOver()) {
				return false;
			}
			
			Actor target = mFixStage.hit(x, y, true);
			if (mCurrentLaunchPreviewActor == target && target != null) {
				long current = System.currentTimeMillis();
				long flash = current - mDoubleChargeTime;
				if (flash > 0 && flash < Config.DOUBLIC_MAX_TIME) {
					mDraged = false;
					launchActor(mCurrentLaunchPreviewActor);
					return true;
				}
				mDoubleChargeTime = current;
				mDraged = true;
				mDownX = x;
				return true;
			}
			return false;
		}
		
		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			if (mDraged) {
				if (mMainStage.isViewPortChangedByHand()) {
					mMainStage.resetViewPort();
					mDraged = false;
					mDoubleChargeTime = -1;
					return;
				}
				if (mCurrentLaunchPreviewActor != null) {
					float dx = x - mDownX;
					moveActorInViewPort(mCurrentLaunchPreviewActor, dx, 0);
					mDownX = x;
				}
			}
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {
			if (mDraged) {
				long current = System.currentTimeMillis();
				if (current - mDoubleChargeTime < Config.TAB_MAX_TIME) {
					launchActor(mCurrentLaunchPreviewActor);
				}
			}
			mDraged = false;
		}
	}

	@Override
	public void onStageTab(MainStage stage) {
		if (mCurrentLaunchPreviewActor != null) {
			launchActor(mCurrentLaunchPreviewActor);
		}
	}

	@Override
	public void onDoubleClick(MainStage stage) {
		
	}

	@Override
	public void onHorMove(MainStage stage, float dx) {
		if (mMainStage.isViewPortChangedByHand()) {
			mMainStage.resetViewPort();
			return;
		}
		moveActorInViewPort(mCurrentLaunchPreviewActor, dx, 0);
	}
}
