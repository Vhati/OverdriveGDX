package com.ftloverdrive.event.ship;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEventListener;
import com.ftloverdrive.event.ship.ShipPropertyEvent;


public interface ShipPropertyListener extends OVDEventListener {

	public void shipPropertyChanged( OverdriveContext context, ShipPropertyEvent e );
}
