package com.ftloverdrive.event.ship;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.AbstractOVDEvent;
import com.ftloverdrive.model.ship.ShipCoordinate;


public class ShipLayoutRoomAddEvent extends AbstractOVDEvent implements Poolable {
	protected int shipRefId = -1;
	protected int roomRefId = -1;
	protected ShipCoordinate[] roomCoords = null;


	public ShipLayoutRoomAddEvent() {
	}

	/**
	 * Pseudo-constructor.
	 *
	 * @param shipRefId  a reserved reference id for the ShipModel
	 * @param roomRefId  a reserved reference id for the RoomModel
	 * @param roomCoords  ShipCoordinates to associate with the room
	 */
	public void init( int shipRefId, int roomRefId, ShipCoordinate[] roomCoords ) {
		this.shipRefId = shipRefId;
		this.roomRefId = roomRefId;
		this.roomCoords = roomCoords;
	}


	public int getShipRefId() {
		return shipRefId;
	}

	public int getRoomRefId() {
		return roomRefId;
	}

	public ShipCoordinate[] getRoomCoords() {
		return roomCoords;
	}


	@Override
	public void reset() {
		shipRefId = -1;
		roomRefId = -1;
		roomCoords = null;
	}
}
