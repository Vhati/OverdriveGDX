package com.ftloverdrive.event.game;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.AbstractOVDEvent;


public class GamePlayerShipChangeEvent extends AbstractOVDEvent implements Poolable {
	protected int gameRefId = -1;
	protected int shipRefId = -1;


	public GamePlayerShipChangeEvent() {
	}

	/**
	 * Pseudo-constructor.
	 *
	 * @param gameRefId  a reserved reference id for the GameModel
	 * @param shipRefId  a reserved reference id for the ShipModel
	 */
	public void init( int gameRefId, int shipRefId ) {
		this.gameRefId = gameRefId;
		this.shipRefId = shipRefId;
	}


	public int getGameRefId() {
		return gameRefId;
	}

	public int getShipRefId() {
		return shipRefId;
	}


	@Override
	public void reset() {
		gameRefId = -1;
		shipRefId = -1;
	}
}
