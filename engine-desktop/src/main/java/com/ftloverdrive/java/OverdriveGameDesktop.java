package com.ftloverdrive.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.ftloverdrive.core.OverdriveGame;

public class OverdriveGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Overdrive";
		config.useGL20 = false;
		config.width = 1024;
		config.height = 650;
		config.fullscreen = false;
		config.vSyncEnabled = true;  // See also Gdx.graphics.setVSync( true );
		config.foregroundFPS = 41;   // If CPU can't keep up, the render thread won't sleep, pegging the CPU.
		config.backgroundFPS = 31;
		new LwjglApplication( new OverdriveGame(), config );
	}
}
