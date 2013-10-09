package com.ftloverdrive.event.ship;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.AbstractOVDEvent;


public class ShipPropertyEvent extends AbstractOVDEvent implements Poolable {
	public static final int INT_TYPE = 0;

	public static final int SET_ACTION = 0;
	public static final int INCREMENT_ACTION = 1;

	protected int shipRefId = -1;
	protected int propertyType = -1;
	protected int action = -1;
	protected String propertyKey = null;
	protected int intValue = 0;


	public ShipPropertyEvent() {
	}

	/**
	 * Pseudo-constructor for an integer property change.
	 */
	public void init( int shipRefId, int propertyType, int action, String propertyKey, int intValue ) {
		this.shipRefId = shipRefId;
		this.propertyType = propertyType;
		this.action = action;
		this.propertyKey = propertyKey;
		this.intValue = intValue;
	}


	public int getShipRefId() {
		return shipRefId;
	}

	public int getPropertyType() {
		return propertyType;
	}

	public int getAction() {
		return action;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public int getIntValue() {
		return intValue;
	}


	@Override
	public void reset() {
		shipRefId = -1;
		propertyType = -1;
		action = -1;
		propertyKey = null;
		intValue = 0;
	}
}
