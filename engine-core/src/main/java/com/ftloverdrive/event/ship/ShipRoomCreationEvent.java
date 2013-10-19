package com.ftloverdrive.event.ship;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.AbstractOVDEvent;


public class ShipRoomCreationEvent extends AbstractOVDEvent implements Poolable {
	protected int roomRefId = -1;


	public ShipRoomCreationEvent() {
	}

	/**
	 * Pseudo-constructor.
	 *
	 * @param shipRefId  a reserved reference id for the new room
	 */
	public void init( int roomRefId ) {
		this.roomRefId = roomRefId;
	}


	public int getRoomRefId() {
		return roomRefId;
	}


	@Override
	public void reset() {
		roomRefId = -1;
	}
}
