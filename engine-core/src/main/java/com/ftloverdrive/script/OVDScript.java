package com.ftloverdrive.script;

import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.model.GameModel;
import com.ftloverdrive.ui.screen.OVDStageManager;


public interface OVDScript {

	/** Returns a unique identifier for this script. */
	public String getScriptId();


	/** Returns a single-line description of this script. */
	public String getShortDescription();

	/** Returns a verbose description of this script. */
	public String getDescription();


	/** Calls load() on the game's AssetManager as a screen is created. (TODO) */
	public void screenPreloadAssets( String screenId );

	/** Sets up Models for a Screen. */
	public void screenInitModels( String screenId, GameModel gameModel );

	/** Sets up UI elements for a Screen. */
	public void screenInitUI( String screenId, OVDStageManager stageManager, GameModel gameModel );

	/** Registers this script with a Screen's EventManager to handle Overdrive events. */
	public void screenInitEventHandlers( String screenId, OVDEventManager eventManager, GameModel gameModel );

	/** Calls unload() on the game's AssetManager as a screen is disposed. (TODO) */
	public void screenUnpreloadAssets( String screenId );
}
