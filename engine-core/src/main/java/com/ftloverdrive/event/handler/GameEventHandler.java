package com.ftloverdrive.event.handler;

import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEvent;
import com.ftloverdrive.event.OVDEventHandler;
import com.ftloverdrive.event.game.GamePlayerShipChangeEvent;
import com.ftloverdrive.event.game.GamePlayerShipChangeListener;
import com.ftloverdrive.model.GameModel;
import com.ftloverdrive.model.ship.ShipModel;


public class GameEventHandler implements OVDEventHandler {
	private Class[] eventClasses;
	private Class[] listenerClasses;


	public GameEventHandler() {
		eventClasses = new Class[] { GamePlayerShipChangeEvent.class };
		listenerClasses = new Class[] { GamePlayerShipChangeListener.class };
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
		if ( e instanceof GamePlayerShipChangeEvent ) {
			GamePlayerShipChangeEvent event = (GamePlayerShipChangeEvent)e;

			int gameRefId = event.getGameRefId();
			int shipRefId = event.getShipRefId();
			GameModel gameModel = context.getReferenceManager().getObject( gameRefId, GameModel.class );
			ShipModel shipModel = context.getReferenceManager().getObject( shipRefId, ShipModel.class );
			if ( shipModel != null ) {
				gameModel.setPlayerShip( shipRefId );
			}

			for ( int i = listeners.length-2; i >= 0; i-=2 ) {
				if ( listeners[i] == GamePlayerShipChangeListener.class ) {
					((GamePlayerShipChangeListener)listeners[i+1]).gamePlayerShipChanged( context, event );
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public void disposeEvent( OVDEvent e ) {
		if ( e.getClass() == GamePlayerShipChangeEvent.class ) {
			Pools.get( GamePlayerShipChangeEvent.class ).free( (GamePlayerShipChangeEvent)e );
		}
	}
}
