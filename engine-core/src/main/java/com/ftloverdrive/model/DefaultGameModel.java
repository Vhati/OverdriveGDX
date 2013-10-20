package com.ftloverdrive.model;

import com.badlogic.gdx.utils.IntIntMap;

import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.NamedProperties;


public class DefaultGameModel extends AbstractOVDModel implements GameModel {

	protected NamedProperties gameProperties = new NamedProperties();
	protected IntIntMap playerRefIdToShipRefIdMap;


	public DefaultGameModel() {
		super();
		playerRefIdToShipRefIdMap = new IntIntMap();
	}


	/**
	 * Returns a collection of arbitrarily named values.
	 */
	@Override
	public NamedProperties getProperties() {
		return gameProperties;
	}


	@Override
	public void setPlayerShip( int playerModelRefId, int shipModelRefId ) {
		playerRefIdToShipRefIdMap.put( playerModelRefId, shipModelRefId );
	}

	@Override
	public int getPlayerShip( int playerModelRefId ) {
		return playerRefIdToShipRefIdMap.get( playerModelRefId, -1 );
	}
}
