package com.xime.pong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.xime.pong.managers.GameScreenManager;



public class App extends Game {

	//Variables de la aplicacion
	public static String APP_TITLE = "Ping Pong";
	public static double APP_VERSION = 0.1;
	public static int APP_DESKTOP_WIDTH = 720;
	public static int APP_DESKTOP_HEIGHT = 420;
	public static int APP_FPS = 60;

	//Variables del juego
	public static int V_WIDTH = 720;
	public static int V_HEIGHT = 420;

	//Managers
	public GameScreenManager gsm;
	public AssetManager assets;

	//Batches
	public SpriteBatch batch;
	public ShapeRenderer shapeBatch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();

		//Setup Managers
		assets = new AssetManager();
		gsm = new GameScreenManager(this);
	}

	@Override
	public void render () {
		super.render();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeBatch.dispose();
		assets.dispose();
		gsm.dispose();
	}
}
