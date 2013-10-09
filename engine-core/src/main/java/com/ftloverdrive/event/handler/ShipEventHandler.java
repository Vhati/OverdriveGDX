package com.ftloverdrive.event.handler;

import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEvent;
import com.ftloverdrive.event.OVDEventHandler;
import com.ftloverdrive.event.ship.ShipCreationEvent;
import com.ftloverdrive.event.ship.ShipPropertyEvent;
import com.ftloverdrive.event.ship.ShipPropertyListener;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.model.ship.TestShipModel;
import com.ftloverdrive.util.OVDConstants;


public class ShipEventHandler implements OVDEventHandler {
	private Class[] eventClasses;
	private Class[] listenerClasses;


	public ShipEventHandler() {
		eventClasses = new Class[] {
			ShipCreationEvent.class,
			ShipPropertyEvent.class
		};
		listenerClasses = new Class[] { ShipPropertyListener.class };
	}


	@Override
	public Class[] getEventClasses() {
		return eventClasses;
	}

	@Override
	public Class[] getListenerClasses() {
		return listenerClasses;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void handle( OverdriveContext context, OVDEvent e, Object[] listeners ) {
		if ( e instanceof ShipCreationEvent ) {
			ShipCreationEvent event = (ShipCreationEvent)e;

			TestShipModel shipModel = new TestShipModel();
			int shipRefId = event.getShipRefId();
			context.getReferenceManager().addObject( shipModel, shipRefId );
			shipModel.getProperties().setInt( OVDConstants.HULL_MAX, 40 );
		}
		else if ( e instanceof ShipPropertyEvent ) {
			ShipPropertyEvent event = (ShipPropertyEvent)e;

			int shipRefId = event.getShipRefId();
			ShipModel shipModel = context.getReferenceManager().getObject( shipRefId, ShipModel.class );
			if ( event.getPropertyType() == ShipPropertyEvent.INT_TYPE ) {
				if ( event.getAction() == ShipPropertyEvent.SET_ACTION ) {
					int value = event.getIntValue();
					String key = event.getPropertyKey();
					shipModel.getProperties().setInt( key, value );
				}
				else if ( event.getAction() == ShipPropertyEvent.INCREMENT_ACTION ) {
					int value = event.getIntValue();
					String key = event.getPropertyKey();
					shipModel.getProperties().incrementInt( key, value );
				}
			}

			for ( int i = listeners.length-2; i >= 0; i-=2 ) {
				if ( listeners[i] == ShipPropertyListener.class ) {
					((ShipPropertyListener)listeners[i+1]).shipPropertyChanged( context, event );
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public void disposeEvent( OVDEvent e ) {
		if ( e.getClass() == ShipCreationEvent.class ) {
			Pools.get( ShipCreationEvent.class ).free( (ShipCreationEvent)e );
		}
		else if ( e.getClass() == ShipPropertyEvent.class ) {
			Pools.get( ShipPropertyEvent.class ).free( (ShipPropertyEvent)e );
		}
	}
}
