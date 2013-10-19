package com.ftloverdrive.model.ship;

import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.RoomModel;


public class DefaultRoomModel implements RoomModel {

	protected NamedProperties roomProperties;
	protected ImageSpec decorImageSpec = null;


	public DefaultRoomModel() {
		roomProperties = new NamedProperties();
	}


	@Override
	public NamedProperties getProperties() {
		return roomProperties;
	}


	@Override
	public void setRoomDecorImageSpec( ImageSpec imageSpec ) {
		decorImageSpec = imageSpec;
	}

	@Override
	public ImageSpec getRoomDecorImageSpec() {
		return decorImageSpec;
	}
}
