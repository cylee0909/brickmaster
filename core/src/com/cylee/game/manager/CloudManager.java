package com.cylee.game.manager;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cylee.game.actor.CloudActor;


public class CloudManager {
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private float mWorldWidth;
	private float mWorldHeight;
	public CloudManager(float worldWidth, float worldHeight) {
		mWorldHeight = worldHeight;
		mWorldWidth = worldWidth;
	}
	
	public CloudActor newCloud(TextureRegion region) {
		return newCloud(region, (int)(mWorldWidth * RANDOM.nextFloat()), mWorldHeight * (0.4f + RANDOM.nextInt(60) / 100f), mWorldHeight * 0.12f);
	}
	
	public CloudActor newCloud(TextureRegion region, float x, float y, float cloudHeight) {
		float baseSpeed = mWorldWidth / 60 / 100;
		float cloud1SpeedX = baseSpeed * (RANDOM.nextFloat() * 2 + 0.5f);
		int cw1 = region.getRegionWidth();
		int ch1 = region.getRegionHeight();
		float rw1 = cloudHeight / ch1 * cw1;
		CloudActor actor = new CloudActor(region ,x, y, rw1, cloudHeight);
		actor.setSpeedX(cloud1SpeedX);
		actor.setDirection(RANDOM.nextFloat() > 0.5 ? -1 : 1);
		actor.setMinBoundX((int)(-rw1*2));
		actor.setMaxBoundX((int)(mWorldWidth + rw1));
		return actor;
	}
}
