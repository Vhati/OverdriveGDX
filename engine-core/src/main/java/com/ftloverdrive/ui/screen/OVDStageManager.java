package com.ftloverdrive.ui.screen;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Stage;


public class OVDStageManager {

	protected Map<String,Stage> stageMap = new HashMap<String,Stage>();


	public OVDStageManager() {
	}


	public void putStage( String key, Stage stage ) {
		stageMap.put( key, stage );
	}

	public Stage getStage( String key ) {
		return stageMap.get( key );
	}
}
