package com.ftloverdrive.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.core.OverdriveGame;
import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.model.GameModel;
import com.ftloverdrive.script.OVDScriptManager;
import com.ftloverdrive.ui.screen.OVDScreen;
import com.ftloverdrive.ui.screen.OVDScreenManager;
import com.ftloverdrive.ui.screen.OVDStageManager;
import com.ftloverdrive.util.OVDReferenceManager;


/**
 * A means to pass the numerous managers around, especially to callbacks.
 *
 * This should be preferred over globals.
 * It's not necessary to set every value.
 *
 * It might make sense for an object of limited scope to take context as an
 * arg in its constructor and remember it. But if possible, individual
 * methods should never cache an old context passed to them.
 * 
 * Context objects can be created by a Pool for reuse, but it will not be
 * safe to free() them until they're definitely forgotten.
 *
 * OverdriveContext context = Pools.get( OverdriveContext.class ).obtain();
 * context.init(...);
 *
 * @see com.badlogic.gdx.utils.Pools
 * @see com.badlogic.gdx.utils.Pool
 * @see com.badlogic.gdx.utils.Pool.Poolable
 */
public class OverdriveContext implements Poolable {

	protected OverdriveGame game = null;
	protected OVDScreen screen = null;
	protected GameModel gameModel = null;


	/**
	 * This constructor must take no args, and only set up variables.
	 * Call init() afterward, as if that were the normal constructor.
	 *
	 * @see com.badlogic.gdx.utils.ReflectionPool
	 */
	public OverdriveContext() {
	}

	/**
	 * Pseudo constructor for new and reused objects.
	 * This is capable of accepting args to set initial values.
	 *
	 * Always call this after obtaining a reused object from a Pool.
	 * Subclasses overriding this must call super.init();
	 */
	public void init() {
	}

	/**
	 * Pseudo constructor.
	 */
	public void init( OverdriveGame game, OVDScreen screen, GameModel gameModel ) {
		this.game = game;
		this.screen = screen;
		this.gameModel = gameModel;
	}

	/**
	 * Copies values from an existing context.
	 * If anything later decides to modify this context, the original won't
	 * be affected.
	 */
	public void init( OverdriveContext context ) {
		this.game = context.getGame();
		this.screen = context.getScreen();
		this.gameModel = context.getGameModel();
	}


	public void setGame( OverdriveGame game ) {
		this.game = game;
	}

	public OverdriveGame getGame() {
		return game;
	}


	public FileHandleResolver getFileHandleResolver() {
		if ( game != null ) return game.getFileHandleResolver();
		else return null;
	}

	public AssetManager getAssetManager() {
		if ( game != null ) return game.getAssetManager();
		else return null;
	}

	public OVDScreenManager getScreenManager() {
		if ( game != null ) return game.getScreenManager();
		else return null;
	}

	public OVDReferenceManager getReferenceManager() {
		if ( game != null ) return game.getReferenceManager();
		else return null;
	}


	public void setScreen( OVDScreen screen ) {
		this.screen = screen;
	}

	public OVDScreen getScreen() {
		return screen;
	}


	public OVDEventManager getScreenEventManager() {
		if ( screen != null ) return screen.getEventManager();
		else return null;
	}

	public OVDScriptManager getScreenScriptManager() {
		if ( screen != null ) return screen.getScriptManager();
		else return null;
	}

	public OVDStageManager getScreenStageManager() {
		if ( screen != null ) return screen.getStageManager();
		else return null;
	}


	public void setGameModel( GameModel gameModel ) {
		this.gameModel = gameModel;
	}

	public GameModel getGameModel() {
		return gameModel;
	}


	/**
	 * Resets this object for Pool reuse, setting all variables to defaults.
	 * This is automatically called by the Pool's free() method.
	 *
	 * @see com.badlogic.gdx.utils.Pool
	 * @see com.badlogic.gdx.utils.Pool.Poolable
	 */
	@Override
	public void reset() {
		this.game = null;
		this.screen = null;
		this.gameModel = null;
	}
}
