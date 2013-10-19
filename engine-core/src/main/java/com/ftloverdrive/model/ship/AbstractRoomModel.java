package com.ftloverdrive.model.ship;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.ShipCoordinate;


public class AbstractRoomModel {

	protected NamedProperties roomProperties = new NamedProperties();
	protected ShipCoordinate[] squareCoords = null;


	public AbstractRoomModel( ShipCoordinate[] squareCoords ) {
		this.squareCoords = squareCoords;
	}

	private AbstractRoomModel() {
	}


	public ShipCoordinate[] getSquareCoords() {
		return squareCoords;
	}
}
