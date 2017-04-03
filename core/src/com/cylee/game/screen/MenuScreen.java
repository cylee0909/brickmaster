package com.cylee.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cylee.game.actor.CloudActor;
import com.cylee.game.manager.CloudManager;
import com.cylee.game.util.Assets;

public class MenuScreen implements Screen{
	public static final int BN_IDENTIFY_CLASSIFY = 1;
	public static final int BN_IDENTIFY_CHANLENGE = 2;
	public static final int BN_IDENTIFY_EXIT = 3;
	private Stage mFixedStage;
	private SpriteBatch mSpriteBatch;
	private OnMenuClickListener mMenuClickListener;
	private float mStickX;
	private float mStickDrawWidth;
	public MenuScreen() {
		mSpriteBatch = new SpriteBatch();
		mFixedStage = new Stage(){
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
					return true;
				}
				return super.keyUp(keyCode);
			}
		};
		Viewport fixedPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mFixedStage.setViewport(fixedPort);
		initCloud();
		initButtons();
	}
	
	public void reset() {
		Gdx.input.setInputProcessor(mFixedStage);
	}
	
	private void initButtons() {
		int bnHeight = (int)(mFixedStage.getHeight() * 0.06f);
		int bnWidth = bnHeight * 4;
		float rightMargin = bnHeight * 0.5f;
		float bnX = mFixedStage.getWidth() - bnWidth - rightMargin;
		mStickX = bnX + bnWidth / 2;
		mStickDrawWidth = bnHeight / 2;
		
		float bottomMargin = mFixedStage.getHeight() * 0.24f;
		float gap = bnHeight * 0.1f;
		
		Drawable drawableClassify = new TextureRegionDrawable(Assets.getInstance().mMenuClassifyRegion);
		Drawable drawableClassifyF = new TextureRegionDrawable(Assets.getInstance().mMenuClassifyFRegion);
		TextButton.TextButtonStyle styleClassify = new TextButton.TextButtonStyle(drawableClassify, drawableClassifyF, drawableClassify,Assets.getInstance().mFont);
		styleClassify.downFontColor = Color.RED;
		TextButton bn1 = new TextButton("", styleClassify);
		bn1.setBounds(bnX, bottomMargin + (bnHeight + gap) * 2, bnWidth, bnHeight);
		mFixedStage.addActor(bn1);
		
		Drawable drawableChalenge = new TextureRegionDrawable(Assets.getInstance().mMenuChalengeRegion);
		Drawable drawableChalengeF = new TextureRegionDrawable(Assets.getInstance().mMenuChalengeFRegion);
		TextButton.TextButtonStyle styleChalenge = new TextButton.TextButtonStyle(drawableChalenge, drawableChalengeF, drawableChalenge,Assets.getInstance().mFont);
		styleChalenge.downFontColor = Color.RED;
		TextButton bn2 = new TextButton("", styleChalenge);
		bn2.setBounds(bnX, bottomMargin + bnHeight + gap, bnWidth, bnHeight);
		mFixedStage.addActor(bn2);
		

		Drawable drawableExit = new TextureRegionDrawable(Assets.getInstance().mMenuExitRegion);
		Drawable drawableExitF = new TextureRegionDrawable(Assets.getInstance().mMenuExitFRegion);
		TextButton.TextButtonStyle styleExit = new TextButton.TextButtonStyle(drawableExit, drawableExitF, drawableExit,Assets.getInstance().mFont);
		styleExit.downFontColor = Color.RED;
		TextButton bn3 = new TextButton("", styleExit);
		bn3.setBounds(bnX, bottomMargin, bnWidth, bnHeight);
		mFixedStage.addActor(bn3);
		
		bn1.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (mMenuClickListener != null) {
					mMenuClickListener.onMenuCLick(BN_IDENTIFY_CLASSIFY);
				}
			}
		});
		
		bn2.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (mMenuClickListener != null) {
					mMenuClickListener.onMenuCLick(BN_IDENTIFY_CHANLENGE);
				}
			}
		});
		
		bn3.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (mMenuClickListener != null) {
					mMenuClickListener.onMenuCLick(BN_IDENTIFY_EXIT);
				}
			}
		});
	}
	
	public void setMenuClickListener(OnMenuClickListener listener) {
		mMenuClickListener = listener;
	}
	
	@Override
	public void dispose() {
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
		
		mSpriteBatch.setProjectionMatrix(mFixedStage.getViewport().getCamera().combined);
		mSpriteBatch.begin();
		renderDecorate();
		mSpriteBatch.end();
		
		mFixedStage.act(delta);
		mFixedStage.draw();
	}

	@Override
	public void resize(int w, int h) {
		mFixedStage.getViewport().update(w, h, true);
	}

	@Override
	public void resume() {
		show();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(mFixedStage);
	}
	
	private void renderDecorate() {
		mSpriteBatch.disableBlending();
		mSpriteBatch.draw(Assets.getInstance().mBg, 0, 0, mFixedStage.getWidth(), mFixedStage.getHeight());
		mSpriteBatch.enableBlending();
		
		TextureRegion ground = Assets.getInstance().mGroundBlockRegion;
		int width = ground.getRegionWidth();
		int height = ground.getRegionHeight();
		float groundDrawHeight = Gdx.graphics.getHeight() * 0.12f;
		float drawWidth = height / groundDrawHeight * width;
		int count = (int)(Gdx.graphics.getWidth() / drawWidth) + 1;
		for (int i = 0; i < count; i++){
			mSpriteBatch.draw(ground, i*drawWidth, 0, drawWidth, groundDrawHeight);
		}
		
		// 绘制路牌stick
		TextureRegion stickRegion = Assets.getInstance().mStickRegion;
		float stickWidth = stickRegion.getRegionWidth();
		float stickHeight = stickRegion.getRegionHeight();
		float stickDrawHeigth = mStickDrawWidth / stickWidth * stickHeight;
		float x = mStickX - mStickDrawWidth / 2;
		mSpriteBatch.draw(stickRegion, x, groundDrawHeight, mStickDrawWidth, stickDrawHeigth);
		
		// 绘制Guide
		TextureRegion guideRegion = Assets.getInstance().mGuideBoardRegion;
		float guideWidth = guideRegion.getRegionWidth();
		float guideHeight = guideRegion.getRegionHeight();
		float guideDrawHeight = stickDrawHeigth * 0.7f;
		float guideDrawWidth = guideDrawHeight / guideHeight * guideWidth;
		float guideX = -guideDrawWidth * 0.5f;
		mSpriteBatch.draw(guideRegion, guideX, groundDrawHeight, guideDrawWidth, guideDrawHeight);
	}
	
	private void initCloud() {
		CloudManager manager = new CloudManager(mFixedStage.getWidth(), mFixedStage.getHeight());
		float worldWidth = mFixedStage.getWidth();
		float worldHeight = mFixedStage.getHeight();
		float x1 = worldWidth * 0.3f;
		float y1 = worldHeight * (1-0.1f);
		float cw1 = worldHeight * 0.09f;
		mFixedStage.addActor(manager.newCloud(Assets.getInstance().mCloudRegion1,x1, y1, cw1));
		
		float x2 = worldWidth * 0.1f;
		float y2 = worldHeight * (1-0.33f);
		float cw2 = worldHeight * 0.1f;
		mFixedStage.addActor(manager.newCloud(Assets.getInstance().mCloudRegion2,x2, y2, cw2));
		
		float x3 = worldWidth * 0.8f;
		float y3 = worldHeight * (1-0.45f);
		float cw3 = worldHeight * 0.12f;
		mFixedStage.addActor(manager.newCloud(Assets.getInstance().mCloudRegion1,x3, y3, cw3));
		
		float x4 = worldWidth * 0.4f;
		float y4 = worldHeight * (1-0.2f);
		float cw4 = worldHeight * 0.08f;
		CloudActor actor = manager.newCloud(Assets.getInstance().mCloudRegion2,x4, y4, cw4);
		Action s1 = Actions.scaleBy(0.1f, 0.1f, 400);
		Action s2 = Actions.scaleBy(-0.1f, -0.1f, 400);
		SequenceAction sequenceAction = Actions.sequence(s1, s2);
		RepeatAction repeatAction = Actions.repeat(RepeatAction.FOREVER,  
                sequenceAction);  
		actor.addAction(repeatAction);
		actor.setSpeedX(0);
		mFixedStage.addActor(actor);
	}
	
	public interface OnMenuClickListener {
		void onMenuCLick(int identify);
	}
}
