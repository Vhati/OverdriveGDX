package com.ftloverdrive.event.game;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEventListener;
import com.ftloverdrive.event.game.GamePlayerShipChangeEvent;


public interface GamePlayerShipChangeListener extends OVDEventListener {

	/**
	 * The GameModel's player ShipModel has changed.
	 */
	public void gamePlayerShipChanged( OverdriveContext context, GamePlayerShipChangeEvent e );
}
