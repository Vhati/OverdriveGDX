package com.ftloverdrive.ui.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.ui.screen.OVDScreen;
import com.ftloverdrive.ui.screen.TestScreen;


public class OVDScreenManager implements Disposable {
	public static final String TEST_SCREEN = "Test";

	public static final String LOADING_SCREEN = "Loading";
	public static final String MAINMENU_SCREEN = "MainMenu";
	public static final String HANGAR_SCREEN = "Hangar";
	public static final String CAMPAIGN_SCREEN = "Campaign";
	public static final String CREDITS_SCREEN = "Credits";

	private Logger log;
	protected Map<String,OVDScreen> screenMap = new HashMap<String,OVDScreen>();
	protected String currentScreenKey = null;

	protected OverdriveContext context;


	public OVDScreenManager( OverdriveContext srcContext ) {
		this.context = Pools.get( OverdriveContext.class ).obtain();
		this.context.init( srcContext );

		log = new Logger( OVDScreenManager.class.getCanonicalName(), Logger.INFO );
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
		OVDScreen currentScreen = getOrCreateScreen( key );
		if ( currentScreen != null) {
			currentScreenKey = key;
			context.getGame().setScreen( currentScreen );
		}
	}


	/**
	 * Returns an existing or newly constructed screen, or null if not recognized.
	 */
	public OVDScreen getOrCreateScreen( String key ) {
		OVDScreen screen = screenMap.get( key );
		if ( screen == null ) {
			if ( LOADING_SCREEN.equals( key ) ) {
				screen = new LoadingScreen( context );
			}
			else if ( TEST_SCREEN.equals( key ) ) {
				screen = new TestScreen( context );
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
			OVDScreen nextScreen = getOrCreateScreen( nextScreenKey );

			currentScreenKey = nextScreenKey;
			context.getGame().setScreen( nextScreen );
		}
	}

	/**
	 * Hides the current screen. You'll still need to dispose() it.
	 */
	public void hideCurrentScreen() {
		OVDScreen currentScreen = screenMap.get( currentScreenKey );
		if ( currentScreen != null) {
			currentScreenKey = null;
			context.getGame().setScreen( null );
		}
	}

	public void disposeCurrentScreen() {
		OVDScreen currentScreen = screenMap.get( currentScreenKey );
		if ( currentScreen != null) {
			context.getGame().setScreen( null );
			screenMap.remove( currentScreenKey );
			currentScreen.dispose();
			currentScreenKey = null;
		}
	}

	@Override
	public void dispose() {
		Pools.get( OverdriveContext.class ).free( context );
	}
}
