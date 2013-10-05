package com.ftloverdrive.ui.screen;

import com.badlogic.gdx.Screen;

import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.script.OVDScriptManager;
import com.ftloverdrive.ui.screen.OVDStageManager;


public interface OVDScreen extends Screen {

	public OVDStageManager getStageManager();

	public OVDEventManager getEventManager();

	public OVDScriptManager getScriptManager();
}
