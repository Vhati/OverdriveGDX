package com.ftloverdrive.event.ship;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.AbstractOVDEvent;


public class ShipCreationEvent extends AbstractOVDEvent implements Poolable {
	protected int shipRefId = -1;
	protected String shipType = null;


	public ShipCreationEvent() {
	}

	/**
	 * Pseudo-constructor.
	 *
	 * TODO: Decide on a convention for shipType values.
	 * Have blueprints create the model without events?
	 *
	 * @param shipRefId  a reserved reference id for the new ship
	 * @param shipType  a string identifying what ship to make
	 */
	public void init( int shipRefId, String shipType ) {
		this.shipRefId = shipRefId;
		this.shipType = shipType;
	}


	public int getShipRefId() {
		return shipRefId;
	}

	public String getShipType() {
		return shipType;
	}


	@Override
	public void reset() {
		shipRefId = -1;
		shipType = null;
	}
}
