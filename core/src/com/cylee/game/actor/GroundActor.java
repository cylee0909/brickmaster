package com.cylee.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cylee.game.WorldManager;
import com.cylee.game.entity.BodyUserData;
import com.cylee.game.util.Assets;
import com.cylee.game.util.Config;

public class GroundActor extends Actor{
	private Body mBody;
	private WorldManager mWorldManager;
	private float mWidth;
	private float mHeight;
	private float mX;
	private float mY;
	
	public GroundActor(WorldManager worldManager, float x, float y, float w, float h) {
		mWidth = w;
		mHeight = h;
		mWorldManager = worldManager;
		mBody = mWorldManager.createGround(x, y, w, h);
		mBody.setUserData(new BodyUserData(Config.USER_DATA_GROUND_IDENDITY, false));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		mX = mBody.getPosition().x - mWidth / 2f;
		mY = mBody.getPosition().y - mHeight / 2f;
		float rotation = mBody.getAngle() * MathUtils.radiansToDegrees;
		// 相对于Local坐标系
		setOrigin(mWidth / 2, mHeight / 2);
		// 角度
		setRotation(rotation);
		setX(mX);
		setY(mY);
		Texture img = Assets.getInstance().mGroundBlock;
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		float hr = mHeight / imgHeight;
		float drawWidth = imgWidth * hr;
		int count = (int)(Config.VIEWPORT_WIDTH / drawWidth) + 1;
		for (int i = 0; i < count; i++) {
			batch.draw(img, i * drawWidth, mY, drawWidth, mHeight);
		}
	}
	
	public Body getGroundBody() {
		return mBody;
	}
}
