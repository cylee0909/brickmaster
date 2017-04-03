package com.cylee.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cylee.game.interfaces.IResourceLoadListener;
import com.cylee.game.util.Assets;

public class WelcomScreen implements Screen{
	private SpriteBatch mBatch;
	private Texture mWelcom;
	private Texture mTitle;
	private Texture mRoll;
	private IResourceLoadListener mLoadListener;
	private float mTitleX;
	private float mTitleY;
	private float mTitleH;
	private float mTitleW;
	
	private float mRollX;
	private float mRollY;
	private float mRollW;
	private int mRollSrcW;
	private float mRollAngle;
	private float mRollAngleSpeed;
	private long    mStartTime;
	public WelcomScreen(IResourceLoadListener listener) {
		mLoadListener = listener;
		mBatch = new SpriteBatch();
		mStartTime = System.currentTimeMillis();
		mWelcom = Assets.getInstance().loadAssetsTexture("bg.png");
		mTitle = Assets.getInstance().loadAssetsTexture("title.png");
		mRoll = Assets.getInstance().loadAssetsTexture("roll.png");
		Assets.getInstance().addAssets();
		
		mTitle.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mRoll.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		float tw = mTitle.getWidth();
		float th = mTitle.getHeight();
		float dw = Gdx.graphics.getWidth() * 0.75f;
		float dx = (Gdx.graphics.getWidth() - dw ) / 2;
		float dh = dw / tw * th;
		float dy = (Gdx.graphics.getHeight() - dh) / 2;
		
		mTitleX = dx;
		mTitleY = dy;
		mTitleW = dw;
		mTitleH = dh;
		
		mRollSrcW = mRoll.getWidth();
		mRollW = Gdx.graphics.getWidth() * 0.3f;
		mRollX = (Gdx.graphics.getWidth() - mRollW) / 2;
		mRollY = dy - mRollW - mRollW * 0.3f; 
	}
	
	@Override
	public void dispose() {
		mBatch.dispose();
		mWelcom.dispose();
		mLoadListener = null;
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0f, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// 资源加载完毕，且时间已经达到预定时间
		if (Assets.getInstance().update() && (System.currentTimeMillis() - mStartTime >= 3000)) {
			Assets.getInstance().load();
			if (mLoadListener != null) {
				mLoadListener.resourceLoadComplete();
				mLoadListener = null;
			}
		} else {
			mRollAngleSpeed += 0.2f;;
			mRollAngle -= mRollAngleSpeed;
			mBatch.disableBlending();
			mBatch.begin();
			mBatch.draw(mWelcom, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			mBatch.enableBlending();
			mBatch.draw(mTitle, mTitleX, mTitleY, mTitleW, mTitleH);
			mBatch.draw(mRoll, mRollX, mRollY, mRollW / 2, mRollW / 2, mRollW, mRollW, 1, 1, mRollAngle, 0, 0, mRollSrcW, mRollSrcW, false, false);
			mBatch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}
}
