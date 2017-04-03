package com.cylee.game.util;

import com.badlogic.gdx.Gdx;

public class Config {
	/**
	 * 当前屏幕展示的世界宽度
	 */
	public static final float VIEWPORT_WIDTH = 10; 
	
	/**
	 * stage尺寸转换为屏幕上尺寸的缩放比
	 */
	public static final float STAGE_TO_SCREEN_RATIO = VIEWPORT_WIDTH / (float) Gdx.graphics.getWidth();
	
	/**
	 * 屏幕尺寸转换为stage上尺寸的缩放比
	 */
	public static final float SCREEN_TO_WORLD_STAGE = (float) Gdx.graphics.getWidth() / VIEWPORT_WIDTH;
	
	/**
	 * 世界的高度与宽度比值
	 */
	public static final float SCREEN_RATIO = Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
	
	/**
	 * 当前屏幕展示的世界高度
	 */
	public static final float VIEWPORT_HEIGHT = VIEWPORT_WIDTH * SCREEN_RATIO;
	
	/**
	 * 世界中元素的最大高度
	 */
	public static final float MAX_WORLD_HEIGHT = VIEWPORT_WIDTH * SCREEN_RATIO * 10;
	
	/**
	 * 相机的最低位置
	 */
	public static final float MIN_CAMERA_POSITION_Y = VIEWPORT_HEIGHT / 2f;
	
	/**
	 * 相机的最高位置
	 */
	public static final float MAX_CAMERA_POSITION_Y = MAX_WORLD_HEIGHT - VIEWPORT_HEIGHT / 2f;
	
	/**
	 * 砖块最大高度
	 */
	public static final int MAX_BRICK_HEIGHT = 1;
	
	/**
	 * 砖块最大显示高度距中心显示区域的最小比例
	 */
	public static final float MIN_BRICK_VISIABLE_HEIGHT_RATIO = 0.3f;
	
	/**
	 * 砖块最大显示高度距中心显示区域的最小比例
	 */
	public static final float MAX_BRICK_VISIABLE_HEIGHT_RATIO = 0.4f;
	
	/**
	 * 砖块最大显示高度距中心显示区域的最小距离
	 */
	public static final float MIN_BRICK_VISIABLE_HEIGHT = MIN_BRICK_VISIABLE_HEIGHT_RATIO * VIEWPORT_HEIGHT;
	
	/**
	 * 砖块最大显示高度距中心显示区域的最小距离
	 */
	public static final float MAX_BRICK_VISIABLE_HEIGHT = MAX_BRICK_VISIABLE_HEIGHT_RATIO * VIEWPORT_HEIGHT;
	
	public static final String USER_DATA_GROUND_IDENDITY = "ground_idendity";
	
	public static final String USER_DATA_DROPPING_BRICK_IDENDITY = "dropping_brick_idendity";
	
	public static final float UNIT_DIMEN = (float)VIEWPORT_WIDTH / Gdx.graphics.getWidth();
	
	public static final int TAB_MAX_TIME = 200;  // 200ms
	
	public static final int DOUBLIC_MAX_TIME = 400;  // 400ms
	
	public static final int CONTACT_GROUND = -1;
}
