package com.ftloverdrive.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.core.OverdriveGame;
import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.model.GameModel;
import com.ftloverdrive.script.OVDScriptManager;
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

	protected OVDEventManager screenEventManager = null;
	protected OVDScriptManager screenScriptManager = null;
	protected OVDStageManager screenStageManager = null;

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
	 * Sets the OverdriveGame and related values.
	 * Resolver, AssetManager, and ScreenManager.
	 */
	public void init( OverdriveGame game ) {
		this.game = game;
	}

	/**
	 * Copies values from an existing context.
	 * If anything later decides to modify this context, the original won't
	 * be affected.
	 */
	public void init( OverdriveContext context ) {
		this.game = context.getGame();

		this.screenEventManager = null;
		this.screenScriptManager = null;
		this.screenStageManager = null;

		this.gameModel = null;
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


	public void setScreenEventManager( OVDEventManager eventManager ) {
		this.screenEventManager = eventManager;
	}

	public OVDEventManager getScreenEventManager() {
		return screenEventManager;
	}


	public void setScreenScriptManager( OVDScriptManager scriptManager ) {
		this.screenScriptManager = scriptManager;
	}

	public OVDScriptManager getScreenScriptManager() {
		return screenScriptManager;
	}


	public void setScreenStageManager( OVDStageManager stageManager ) {
		this.screenStageManager = stageManager;
	}

	public OVDStageManager getScreenStageManager() {
		return screenStageManager;
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

		this.screenEventManager = null;
		this.screenScriptManager = null;
		this.screenStageManager = null;

		this.gameModel = null;
	}
}
