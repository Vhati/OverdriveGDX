package com.ftloverdrive.model.ship;

import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.util.OVDConstants;


public class AbstractShipModel extends AbstractOVDModel implements ShipModel {

	protected NamedProperties shipProperties = new NamedProperties();


	public AbstractShipModel() {
		super();
		shipProperties.setInt( OVDConstants.HULL_MAX, 0 );
		shipProperties.setInt( OVDConstants.HULL, 0 );
		shipProperties.setInt( OVDConstants.SCRAP, 0 );
		shipProperties.setInt( OVDConstants.FUEL, 0 );
		shipProperties.setInt( OVDConstants.MISSILES, 0 );
		shipProperties.setInt( OVDConstants.DRONE_PARTS, 0 );
	}


	/**
	 * Returns a collection of arbitrarily named values.
	 */
	@Override
	public NamedProperties getProperties() {
		return shipProperties;
	}
}
