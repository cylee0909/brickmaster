package com.cylee.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CloudActor extends Actor{
	private float mSpeedX;
	private float mDirection;
	private TextureRegion mTexture;
	private int mMinBoundX;
	private int mMaxBoundX;
	public CloudActor(TextureRegion texture, float x, float y, float w, float h) {
		mTexture = texture;
		setBounds(x - w / 2, y - h / 2, w, h);
	}
	
	public void setSpeedX(float speedX) {
		mSpeedX = speedX;
	}
	
	public void setDirection(int direction) {
		mDirection = direction;
	}
	
	public void setMinBoundX(int minBoundX) {
		this.mMinBoundX = minBoundX;
	}
	
	public void setMaxBoundX(int maxBoundX) {
		this.mMaxBoundX = maxBoundX;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		setX(getX() + mSpeedX * mDirection);
		if (getX() > mMaxBoundX || getX() < mMinBoundX) {
			mSpeedX = -mSpeedX;
		}
		batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && x >0 && x < getWidth() && y > 0 && y < getHeight()) {
			return this;
		}
		return null;
	}
}
