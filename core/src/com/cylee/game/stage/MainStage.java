package com.cylee.game.stage;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.Array;
import com.cylee.game.WorldManager;
import com.cylee.game.WorldManager.DroppingBrickContactListener;
import com.cylee.game.actor.BrickActor;
import com.cylee.game.actor.GroundActor;
import com.cylee.game.interfaces.GameProgressListener;
import com.cylee.game.interfaces.OnScoreChangeListener;
import com.cylee.game.util.Config;

public class MainStage extends Stage implements DroppingBrickContactListener{
	private static Random mRandom = new Random(System.currentTimeMillis());
	private static final int DRAG_LOCK_DIRECTION_INVALID = -1;
	private static final int DRAG_LOCK_DIRECTION_X = 0;
	private static final int DRAG_LOCK_DIRECTION_Y = 1;
	private static final int DEFAULT_ANIMATION_DURATION = 300;
	private static final int INVALID_VIEWPORT_POSTION = -1;
	private static final float MIN_DRAG_SLOP = 0.15f * Config.SCREEN_TO_WORLD_STAGE;
	public static GroundActor mGround;
	private final Vector2 mStageVector;
	private boolean mDraged;
	private int mDragDownY;
	private int mDragDownX;
	private float mRealPositionY = INVALID_VIEWPORT_POSTION;
	private long mDoubleChargeTime;
	private boolean mPositionChangedByHand;
	private StageScroler mScroler;
	private StageGestureListener mListener;
	private int mDragLockDirection = DRAG_LOCK_DIRECTION_INVALID;
	private OnScoreChangeListener mOnScoreChangeListener;
	private boolean mDragStarted;
	private WorldManager mWorldManager;
	private GameProgressListener mGameProgressListener;
	
	public MainStage(WorldManager worldManager) {
		mWorldManager = worldManager;
		worldManager.setDroppingBrickContactListener(this);
		mStageVector = new Vector2();
		mScroler = new StageScroler();
		initBodys();
	}
	
	private void initBodys() {
		mGround = new GroundActor(mWorldManager, 5, 1, Config.VIEWPORT_WIDTH * 2, 2);
		addActor(mGround);
		addActor(new BrickActor(mWorldManager, 5, 3, 4, 1));
		int w = 4;
		float random = mRandom.nextFloat();
		w = random > 0.7f ? 2 : w;
		w = random > 0.9f ? 1 : w;
		addActor(new BrickActor(mWorldManager, 5, 5, w, 1));
	}
	
	public void setOnScoreChangeListener(OnScoreChangeListener listener) {
		mOnScoreChangeListener = listener;
	}
	
	public void setGameProgressListener(GameProgressListener listener) {
		mGameProgressListener = listener;
	}
	
	public boolean isGameOver() {
		return mGameProgressListener != null && mGameProgressListener.isGameOver();
	}
	
	public WorldManager getWorldManager() {
		return mWorldManager;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		mScroler.act(delta);
	}
	
	public void smoothScrollViewPort(float dx, float dy, float millisecond) {
		mScroler.mComplete = false;
		mScroler.finish();
		mScroler.setAmount(dx, dy);
		mScroler.setDuration(millisecond / 1000f);
		mScroler.restart();
	}
	
	public void setStageGestureListener(StageGestureListener listener) {
		mListener = listener;
	}
	
	/**
	 * 用户是否手动改变过视野
	 * @return
	 */
	public boolean isViewPortChangedByHand() {
		return mPositionChangedByHand;
	}
	
	public boolean isViewPortInScroll() {
		return !mScroler.mComplete;
	}
	
	/**
	 * 获取最高Actor的中心点位置y坐标, 不包括正在下落并且没有参加过碰撞的actor
	 * @return
	 */
	private float getActorMaxPositonHeight() {
		float result = -1;
		Array<Actor> actors = getActors();
		if (actors.size > 0) {
			for (Actor actor : actors) {
				if (actor instanceof BrickActor
						&& ((BrickActor) (actor)).getUserData() != null
						&& ((BrickActor) (actor)).getUserData().mDropping) {
					continue;
				}
				if (actor.getY() > result) {
					result = actor.getY();
				}
			}
		}
		return result;
	}
	
	/**
	 * 根据落下砖块矫正相机位置
	 */
	public void adjustViewPort() {
		float cameraY = getViewport().getCamera().position.y;
		float maxActorPositionY = getActorMaxPositonHeight();
		float dy = cameraY - maxActorPositionY;
		if (dy < Config.MIN_BRICK_VISIABLE_HEIGHT);
		smoothScrollViewPort(0, dy, 300);
	}
	
	/**
	 * 消除用户手动改变的视野
	 * @param millisecond
	 */
	public void resetViewPort(float millisecond) {
		if (!MathUtils.isEqual(mRealPositionY, INVALID_VIEWPORT_POSTION)) {
			float oldY = getViewport().getCamera().position.y;
			float deltY = mRealPositionY - oldY;
			mScroler.finish();
			mScroler.setDuration(millisecond / 1000f);
			mScroler.setAmountY(deltY);
			mScroler.restart();
			mRealPositionY = INVALID_VIEWPORT_POSTION;
			mPositionChangedByHand = false;
		}
	}
	
	public void resetViewPort() {
		resetViewPort(DEFAULT_ANIMATION_DURATION);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (isGameOver()) {
			return false;
		}
		screenToStageCoordinates(mStageVector.set(screenX, screenY));
		Actor target = hit(mStageVector.x, mStageVector.y, true);
		// 没有点击
		if (target == null) {
			mDragLockDirection = DRAG_LOCK_DIRECTION_INVALID;
			long current = System.currentTimeMillis();
			long splash = current - mDoubleChargeTime;
			if (splash > 0 && splash < Config.DOUBLIC_MAX_TIME) {
				if (mListener != null) {
					mListener.onDoubleClick(this);
				}
				mDraged = false;
				resetViewPort();
				return true;
			}
			mDoubleChargeTime = current;
			mDraged = true;
			mDragDownY = screenY;
			mDragDownX = screenX;
			return true;
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (mDraged) {
			int deltX = Math.abs(screenX - mDragDownX);
			int deltY = Math.abs(screenY - mDragDownY);
			if (mDragStarted || deltX > MIN_DRAG_SLOP || deltY > MIN_DRAG_SLOP) {
				if (!mDragStarted) {
					mDragStarted = true;
					mDragDownX = screenX;
					mDragDownY = screenY;
					return true;
				}
				if ((deltY > deltX && mDragLockDirection == DRAG_LOCK_DIRECTION_INVALID)
						|| mDragLockDirection == DRAG_LOCK_DIRECTION_Y) {
					mDragLockDirection = DRAG_LOCK_DIRECTION_Y;
					if (MathUtils.isEqual(mRealPositionY, INVALID_VIEWPORT_POSTION)) {
						mRealPositionY = getViewport().getCamera().position.y;
						mPositionChangedByHand = true;
					}
					float deltaY = (screenY- mDragDownY) * Config.STAGE_TO_SCREEN_RATIO;
					float currentY = getViewport().getCamera().position.y;
					float resultY = currentY + deltaY;
					if (resultY < Config.MAX_CAMERA_POSITION_Y && resultY > Config.MIN_CAMERA_POSITION_Y) {
						getViewport().getCamera().position.add(0,  deltaY, 0);
					}
					mDragDownY = screenY;
				} else {
					mDragLockDirection = DRAG_LOCK_DIRECTION_X;
					if (mListener != null) {
						mListener.onHorMove(this, (screenX - mDragDownX) * Config.STAGE_TO_SCREEN_RATIO);
					}
					mDragDownX = screenX;
				}
				mDragDownX = screenX;
				mDragDownY = screenY;
			}
			return true;
		}
		return super.touchDragged(screenX, screenY, pointer);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (mDraged) {
			long current = System.currentTimeMillis();
			if (current - mDoubleChargeTime < Config.TAB_MAX_TIME) {
				if (mListener != null) {
					mListener.onStageTab(this);
				}
			}
			mDraged = false;
			mDragStarted = false;
			return true;
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	class StageScroler extends MoveByAction {
		boolean mComplete;
		protected void updateRelative (float percentDelta) {
			getViewport().getCamera().position.add(getAmountX() * percentDelta, getAmountY() * percentDelta, 0);
		}
		
		@Override
		public boolean act(float delta) {
			mComplete =  super.act(delta);
			return mComplete;
		}
		
		@Override
		public void reset() {
			mComplete = false;
			super.reset();
		}
		
		@Override
		public void restart() {
			mComplete = false;
			super.restart();
		}
	}

	@Override
	public void contactBrick(Body droppingBrick, Body other) {
		if (!mScroler.mComplete) {
			return;
		}
		float viewPortY = getViewport().getCamera().position.y;
		float brickY = getActorMaxPositonHeight();
		if (mOnScoreChangeListener != null) {
			mOnScoreChangeListener.onScoreChanged(Math.max(Math.round(brickY) - 1, 0));
		}
		float dy = viewPortY - brickY;
		if (Math.abs(dy) < Config.MIN_BRICK_VISIABLE_HEIGHT) {
			float ddy = Config.MIN_BRICK_VISIABLE_HEIGHT - dy;
			smoothScrollViewPort(0, ddy, 300);
		}
		if (Math.abs(dy) > Config.MAX_BRICK_VISIABLE_HEIGHT) {
			float ddy = Config.MAX_BRICK_VISIABLE_HEIGHT - dy;
			smoothScrollViewPort(0, ddy, 300);
		}
	}

	@Override
	public void contactGround(Body droppingBrick, Body ground) {
		if (mOnScoreChangeListener != null) {
			mOnScoreChangeListener.onScoreChanged(Config.CONTACT_GROUND);
		}
	}
	
	public interface StageGestureListener {
		void onStageTab(MainStage stage);
		void onDoubleClick(MainStage stage);
		void onHorMove(MainStage stage, float dx);
	}
}
