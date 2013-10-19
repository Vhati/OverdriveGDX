package com.ftloverdrive.model.ship;

import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.ship.AbstractShipModel;
import com.ftloverdrive.util.OVDConstants;


public class TestShipModel extends AbstractShipModel {

	public TestShipModel() {
		super();

		// TODO: The shield isn't moved enough?
		// Shield ellipse Y+4: Hull height is 4px taller than shield image.
		//   Slides shield up to match their top-left corners instead of
		//   bottom-left.
		// Shield ellipse X-17: ???

		this.setShipOffset( 0, -70 );
		this.setHullOffset( -71, 116 );
		this.setHullSize( 677, 444 );
		this.setShieldEllipse( -30-17, 0+4, 350, 220 );
		this.setShieldImageSpec( new ImageSpec( OVDConstants.SHIP_ATLAS, "kestral-shields1" ) );
		this.setBaseImageSpec( new ImageSpec( OVDConstants.SHIP_ATLAS, "kestral-base" ) );
		this.setCloakImageSpec( new ImageSpec( OVDConstants.SHIP_ATLAS, "kestral-cloak" ) );
		this.setFloorImageSpec( new ImageSpec( OVDConstants.SHIP_ATLAS, "kestral-floor" ) );

		this.getProperties().setInt( OVDConstants.HULL_MAX, 40 );
	}
}
