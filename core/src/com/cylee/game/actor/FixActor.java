package com.cylee.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cylee.game.util.Assets;

public class FixActor extends Actor{
	private TextureRegion mRegion;
	public FixActor(float x, float y, float w, float h) {
		setBounds(x - w / 2, y - h / 2, w, h);
		mRegion = Assets.getInstance().mBrickRegion;
	}
	
	public void setTextureRegion(TextureRegion region) {
		mRegion = region;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(mRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && x >0 && x < getWidth() && y > 0 && y < getHeight()) {
			return this;
		}
		return null;
	}
}
