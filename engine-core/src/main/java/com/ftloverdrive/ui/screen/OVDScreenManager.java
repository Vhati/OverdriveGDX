package com.ftloverdrive.ui.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;

import com.ftloverdrive.core.OverdriveGame;
import com.ftloverdrive.ui.screen.TestScreen;


public class OVDScreenManager {
	public static final String TEST_SCREEN = "Test";

	public static final String LOADING_SCREEN = "Loading";
	public static final String MAINMENU_SCREEN = "MainMenu";
	public static final String HANGAR_SCREEN = "Hangar";
	public static final String CAMPAIGN_SCREEN = "Campaign";
	public static final String CREDITS_SCREEN = "Credits";

	private Logger log;
	protected Map<String,Screen> screenMap = new HashMap<String,Screen>();
	protected String currentScreenKey = null;

	protected OverdriveGame game;


	public OVDScreenManager( OverdriveGame game ) {
		log = new Logger( OVDScreenManager.class.getCanonicalName(), Logger.INFO );
		this.game = game;
	}

	/**
	 * Returns the key for the first screen that should ever appear.
	 */
	public String getInitScreenKey() {
		return LOADING_SCREEN;
	}


	/**
	 * Hides the current screen and shows another.
	 * The next screen will be created if necessary.
	 */
	public void showScreen( String key ) {
		hideCurrentScreen();
		Screen currentScreen = getOrCreateScreen( key );
		if ( currentScreen != null) {
			currentScreenKey = key;
			game.setScreen( currentScreen );
		}
	}


	/**
	 * Returns an existing or newly constructed screen, or null if not recognized.
	 */
	public Screen getOrCreateScreen( String key ) {
		Screen screen = screenMap.get( key );
		if ( screen == null ) {
			if ( LOADING_SCREEN.equals( key ) ) {
				screen = new LoadingScreen( game );
			}
			else if ( TEST_SCREEN.equals( key ) ) {
				screen = new TestScreen( game );
			}
			if ( screen != null ) {
				screenMap.put( key, screen );
			}
		}
		return screen;
	}


	/**
	 * Disposes the current screen and shows the next (in a fixed order).
	 * The next screen will be created if necessary.
	 */
	public void continueToNextScreen() {
		String nextScreenKey = null;

		if ( currentScreenKey == null ) {
			nextScreenKey = TEST_SCREEN;
		}
		else if ( LOADING_SCREEN.equals( currentScreenKey ) ) {
			nextScreenKey = TEST_SCREEN;
		}

		disposeCurrentScreen();

		if ( nextScreenKey != null ) {
			Screen nextScreen = getOrCreateScreen( nextScreenKey );

			currentScreenKey = nextScreenKey;
			game.setScreen( nextScreen );
		}
	}

	/**
	 * Hides the current screen. You'll still need to dispose() it.
	 */
	public void hideCurrentScreen() {
		Screen currentScreen = screenMap.get( currentScreenKey );
		if ( currentScreen != null) {
			currentScreenKey = null;
			game.setScreen( null );
		}
	}

	public void disposeCurrentScreen() {
		Screen currentScreen = screenMap.get( currentScreenKey );
		if ( currentScreen != null) {
			game.setScreen( null );
			screenMap.remove( currentScreenKey );
			currentScreen.dispose();
			currentScreenKey = null;
		}
	}
}
