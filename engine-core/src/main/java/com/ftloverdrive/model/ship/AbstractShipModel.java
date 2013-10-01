package com.ftloverdrive.model.ship;

import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.ShipModel;


public class AbstractShipModel extends AbstractOVDModel implements ShipModel {

	protected NamedProperties shipProperties = new NamedProperties();


	public AbstractShipModel() {
		super();
		shipProperties.setInt( "HullMax", 0 );
		shipProperties.setInt( "Hull", 0 );
		shipProperties.setInt( "Scrap", 0 );
		shipProperties.setInt( "Fuel", 0 );
		shipProperties.setInt( "Missiles", 0 );
		shipProperties.setInt( "DroneParts", 0 );
	}


	/**
	 * Returns a collection of arbitrarily named values.
	 */
	@Override
	public NamedProperties getProperties() {
		return shipProperties;
	}
}
