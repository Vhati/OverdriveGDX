package com.ftloverdrive.event.ship;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEventListener;
import com.ftloverdrive.event.ship.ShipLayoutRoomAddEvent;


public interface ShipLayoutListener extends OVDEventListener {

	public void shipLayoutRoomAdded( OverdriveContext context, ShipLayoutRoomAddEvent e );
}
