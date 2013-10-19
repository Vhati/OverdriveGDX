package com.ftloverdrive.blueprint.ship;

import com.ftloverdrive.blueprint.OVDBlueprint;
import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.ship.ShipModel;


/**
 * Instructions and defaults to construct ShipModels.
 *
 * For ship image positioning in FTL, see:
 * http://www.ftlgame.com/forum/viewtopic.php?f=12&t=17413
 *
 * TODO: Stub.
 */
public interface ShipBlueprint extends OVDBlueprint {

	/**
	 * Enqueues events to create and populate an instance of the ship.
	 */
	public int createShip( OverdriveContext context );
}
