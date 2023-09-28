package com.xime.pong.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xime.pong.App;

import sun.security.krb5.Config;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = App.APP_TITLE + " v" + App.APP_VERSION ;
		config.width = App.APP_DESKTOP_WIDTH;
		config.height = App.APP_DESKTOP_HEIGHT;
		config.backgroundFPS = App.APP_FPS;
		config.foregroundFPS = App.APP_FPS;
		new LwjglApplication(new App(), config);
	}
}
