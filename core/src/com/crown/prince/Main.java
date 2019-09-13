package com.crown.prince;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crown.prince.screens.Screen;
import com.crown.prince.screens.ScreenManager;

public class Main extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;

	public CameraManager cameraManager;
	private Viewport viewport;

	public Assets assets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		cameraManager = new CameraManager();

		viewport = new FitViewport(Constants.ScreenW, Constants.ScreenH, cameraManager.getCamera());
		viewport.apply();

		cameraManager.getCamera().position.set(Constants.ScreenW / 2f , Constants.ScreenH / 2f, 0);
		cameraManager.getCamera().update();

		assets = new Assets();
		assets.loadAllAtOnce();

		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(Screen.GAME);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width,height);
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}
}
