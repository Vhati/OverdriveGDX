package com.ftloverdrive.model;

import com.badlogic.gdx.utils.Array;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.ShipModel;


public class DefaultGameModel extends AbstractOVDModel implements GameModel {

	protected NamedProperties gameProperties = new NamedProperties();
	protected int playerShipModelRefId = -1;


	public DefaultGameModel() {
		super();
	}


	/**
	 * Returns a collection of arbitrarily named values.
	 */
	@Override
	public NamedProperties getProperties() {
		return gameProperties;
	}


	@Override
	public void setPlayerShip( int shipModelRefId ) {
		playerShipModelRefId = shipModelRefId;
	}

	@Override
	public int getPlayerShip() {
		return playerShipModelRefId;
	}
}
