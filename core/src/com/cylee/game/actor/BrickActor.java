package com.cylee.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.cylee.game.WorldManager;
import com.cylee.game.entity.BodyUserData;
import com.cylee.game.util.Assets;
import com.cylee.game.util.Config;

public class BrickActor extends Actor{
	/**绘制扩大区域尺寸*/
	private static final float EXPAND_DRAW_DIMEN = Config.UNIT_DIMEN;
	private Body mBody;
	private WorldManager mWorldManager;
	private float mWidth;
	private float mHeight;
	private float mX;
	private float mY;
	private float mRotation;
	private MouseJoint mMouseJoint;
	
	/**
	 * 声明砖块，坐标系为Box2d中的坐标
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public BrickActor(WorldManager worldManager, float x, float y, float w, float h) {
		mWidth = w + 2 * EXPAND_DRAW_DIMEN;
		mHeight = h + 2 * EXPAND_DRAW_DIMEN;
		setBounds(x - w / 2 - EXPAND_DRAW_DIMEN, y - h / 2 - EXPAND_DRAW_DIMEN, mWidth, mHeight);
		mWorldManager = worldManager;
		mBody = mWorldManager.createBrick(x, y , w, h);
		mBody.setBullet(true);
		this.addListener(new BrickInputListener());
	}
	
	public void setUserData(BodyUserData data) {
		mBody.setUserData(data);
	}
	
	public BodyUserData getUserData() {
		return (BodyUserData)mBody.getUserData();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		mX = mBody.getPosition().x - mWidth / 2f - EXPAND_DRAW_DIMEN;
		mY = mBody.getPosition().y - mHeight / 2f - EXPAND_DRAW_DIMEN;
		mRotation = mBody.getAngle() * MathUtils.radiansToDegrees;
		// 相对于Local坐标系
		setOrigin(mWidth / 2 + EXPAND_DRAW_DIMEN, mHeight / 2 + EXPAND_DRAW_DIMEN);
		// 角度
		setRotation(mRotation);
		setX(mX);
		setY(mY);
		batch.draw(Assets.getInstance().mBrickRegion, mX, mY, getOriginX(), getOriginY(), mWidth, mHeight, getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (x > 0 && x < mWidth && y > 0 && y < mHeight) {
			return this;
		}
		return null;
	}
	
	class BrickInputListener extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
//			if (mBody.isActive()) {
//				MouseJointDef mjd = new MouseJointDef();
//				mjd.bodyA = MainStage.mGround.getGroundBody();
//				mjd.bodyB = mBody;
//				mjd.maxForce = Float.MAX_VALUE;
//				mjd.collideConnected = true;
//				// 转化为 Stage坐标
//				mjd.target.set(localToParentCoordinates(new Vector2(mWidth / 2, mHeight / 2)));
//				mMouseJoint = (MouseJoint) mWorldManager.createJoint(mjd);
//				return true;
//			}
			return false;
		}
		
		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			super.touchDragged(event, x, y, pointer);
			if (mMouseJoint != null) {
				// 转化为 Stage坐标
				mMouseJoint.setTarget(localToParentCoordinates(new Vector2(x, y)));
			}
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {
			super.touchUp(event, x, y, pointer, button);
			if (mMouseJoint != null) {
				mWorldManager.destroyJoint(mMouseJoint);
				mMouseJoint = null;
			}
		}
	}
}
