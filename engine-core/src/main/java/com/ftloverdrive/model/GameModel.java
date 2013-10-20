package com.ftloverdrive.model;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;


public interface GameModel extends OVDModel {

	public NamedProperties getProperties();


	public void setPlayerShip( int playerModelRefId, int shipModelRefId );

	public int getPlayerShip( int playerModelRefId );
}
