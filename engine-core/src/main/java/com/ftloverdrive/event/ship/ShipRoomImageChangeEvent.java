package com.ftloverdrive.event.ship;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.AbstractOVDEvent;
import com.ftloverdrive.io.ImageSpec;


public class ShipRoomImageChangeEvent extends AbstractOVDEvent implements Poolable {
	public static final int DECOR = 0;

	protected int eventType = DECOR;
	protected int roomRefId = -1;
	protected ImageSpec imageSpec = null;


	public ShipRoomImageChangeEvent() {
	}

	/**
	 * Pseudo-constructor.
	 *
	 * @param eventType  DECOR to set the room's background
	 * @param shipRefId  a reserved reference id for the new room
	 * @param imageSpec  a new image to use, or null
	 */
	public void init( int eventType, int roomRefId, ImageSpec imageSpec ) {
		this.eventType = eventType;
		this.roomRefId = roomRefId;
		this.imageSpec = imageSpec;
	}


	public int getEventType() {
		return eventType;
	}

	public int getRoomRefId() {
		return roomRefId;
	}

	public ImageSpec getImageSpec() {
		return imageSpec;
	}


	@Override
	public void reset() {
		eventType = DECOR;
		roomRefId = -1;
		imageSpec = null;
	}
}
