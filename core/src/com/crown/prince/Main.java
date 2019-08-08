package com.crown.prince;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public OrthographicCamera camera;
	public Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera();
		viewport = new FitViewport(Constants.ScreenW, Constants.ScreenH, camera);
		viewport.apply();
		camera.position.set(Constants.ScreenW / 2f , Constants.ScreenH / 2f, 0);
		camera.update();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		batch.begin();

		super.render();

		batch.end();
		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width,height);
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}
