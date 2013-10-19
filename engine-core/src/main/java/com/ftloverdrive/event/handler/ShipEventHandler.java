package com.ftloverdrive.event.handler;

import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEvent;
import com.ftloverdrive.event.OVDEventHandler;
import com.ftloverdrive.event.ship.ShipCreationEvent;
import com.ftloverdrive.event.ship.ShipLayoutRoomAddEvent;
import com.ftloverdrive.event.ship.ShipPropertyEvent;
import com.ftloverdrive.event.ship.ShipPropertyListener;
import com.ftloverdrive.event.ship.ShipRoomCreationEvent;
import com.ftloverdrive.event.ship.ShipRoomImageChangeEvent;
import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.ship.DefaultRoomModel;
import com.ftloverdrive.model.ship.RoomModel;
import com.ftloverdrive.model.ship.ShipCoordinate;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.model.ship.TestShipModel;
import com.ftloverdrive.util.OVDConstants;


public class ShipEventHandler implements OVDEventHandler {
	private Class[] eventClasses;
	private Class[] listenerClasses;


	public ShipEventHandler() {
		eventClasses = new Class[] {
			ShipCreationEvent.class,
			ShipPropertyEvent.class,
			ShipLayoutRoomAddEvent.class,
			ShipRoomCreationEvent.class,
			ShipRoomImageChangeEvent.class
		};
		listenerClasses = new Class[] {
			ShipPropertyListener.class
		};
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

			int shipRefId = event.getShipRefId();
			ShipModel shipModel = new TestShipModel();
			context.getReferenceManager().addObject( shipModel, shipRefId );
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
		else if ( e instanceof ShipRoomCreationEvent ) {
			ShipRoomCreationEvent event = (ShipRoomCreationEvent)e;

			int roomRefId = event.getRoomRefId();
			RoomModel roomModel = new DefaultRoomModel();
			context.getReferenceManager().addObject( roomModel, roomRefId );
		}
		else if ( e instanceof ShipRoomImageChangeEvent ) {
			ShipRoomImageChangeEvent event = (ShipRoomImageChangeEvent)e;

			if ( event.getEventType() == ShipRoomImageChangeEvent.DECOR ) {
				int roomRefId = event.getRoomRefId();
				ImageSpec imageSpec = event.getImageSpec();
				RoomModel roomModel = context.getReferenceManager().getObject( roomRefId, RoomModel.class );

				roomModel.setRoomDecorImageSpec( imageSpec );
			}
		}
		else if ( e instanceof ShipLayoutRoomAddEvent ) {
			ShipLayoutRoomAddEvent event = (ShipLayoutRoomAddEvent)e;

			int shipRefId = event.getShipRefId();
			ShipModel shipModel = context.getReferenceManager().getObject( shipRefId, ShipModel.class );
			int roomRefId = event.getRoomRefId();
			//RoomModel roomModel = context.getReferenceManager().getObject( roomRefId, RoomModel.class );
			ShipCoordinate[] roomCoords = event.getRoomCoords();

			shipModel.getLayout().addRoom( roomRefId, roomCoords );
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
		else if ( e.getClass() == ShipRoomCreationEvent.class ) {
			Pools.get( ShipRoomCreationEvent.class ).free( (ShipRoomCreationEvent)e );
		}
		else if ( e.getClass() == ShipRoomImageChangeEvent.class ) {
			Pools.get( ShipRoomImageChangeEvent.class ).free( (ShipRoomImageChangeEvent)e );
		}
		else if ( e.getClass() == ShipLayoutRoomAddEvent.class ) {
			Pools.get( ShipLayoutRoomAddEvent.class ).free( (ShipLayoutRoomAddEvent)e );
		}
	}
}
