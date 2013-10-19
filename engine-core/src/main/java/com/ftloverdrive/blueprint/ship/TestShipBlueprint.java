package com.ftloverdrive.blueprint.ship;

import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.ship.ShipCreationEvent;
import com.ftloverdrive.event.ship.ShipLayoutRoomAddEvent;
import com.ftloverdrive.event.ship.ShipRoomCreationEvent;
import com.ftloverdrive.event.ship.ShipRoomImageChangeEvent;
import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.ship.ShipCoordinate;
import com.ftloverdrive.model.ship.ShipLayout;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.model.ship.TestShipModel;
import com.ftloverdrive.util.IntRectangle;
import com.ftloverdrive.util.OVDConstants;


public class TestShipBlueprint implements ShipBlueprint {

	@Override
	public int createShip( OverdriveContext context ) {
		int shipRefId = context.getNetManager().requestNewRefId();

		ShipCreationEvent shipCreateEvent = Pools.get( ShipCreationEvent.class ).obtain();
		shipCreateEvent.init( shipRefId, "Test" );
		context.getScreenEventManager().postDelayedEvent( shipCreateEvent );

		int roomRefId = -1;
		ShipCoordinate[] roomCoords = null;

		// The Y-offset is 1 higher than the original FTL's layout.txt.
		int[][] roomsXYWH = new int[][] {
			new int[] { 14, 3, 1, 2 },
			new int[] { 12, 3, 2, 2 },
			new int[] { 10, 3, 2, 1 },
			new int[] { 10, 4, 2, 1 },
			new int[] { 8, 2, 2, 2 },
			new int[] { 8, 4, 2, 2 },
			new int[] { 6, 1, 2, 1 },
			new int[] { 6, 2, 2, 2 },
			new int[] { 6, 4, 2, 2 },
			new int[] { 6, 6, 2, 1 },
			new int[] { 4, 3, 2, 2 },
			new int[] { 3, 2, 2, 1 },
			new int[] { 3, 5, 2, 1 },
			new int[] { 1, 2, 2, 1 },
			new int[] { 1, 3, 2, 2 },
			new int[] { 1, 5, 2, 1 },
			new int[] { 0, 3, 1, 2 }
		};
		ImageSpec[] roomsDecor = new ImageSpec[ roomsXYWH.length ];
		roomsDecor[0] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-pilot" );
		roomsDecor[2] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-doors" );
		roomsDecor[3] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-sensors" );
		roomsDecor[4] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-medbay" );
		roomsDecor[13] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-oxygen" );
		roomsDecor[5] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-shields" );
		roomsDecor[14] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-engines" );
		roomsDecor[10] = new ImageSpec( OVDConstants.SHIP_INTERIOR_ATLAS, "room-weapons" );

		for ( int i=0; i < roomsXYWH.length; i++ ) {
			int[] xywh = roomsXYWH[i];
			roomRefId = context.getNetManager().requestNewRefId();
			roomCoords = ShipLayout.createRoomCoords( xywh[0], xywh[1], xywh[2], xywh[3] );

			ShipRoomCreationEvent roomCreateEvent = Pools.get( ShipRoomCreationEvent.class ).obtain();
			roomCreateEvent.init( roomRefId );
			context.getScreenEventManager().postDelayedEvent( roomCreateEvent );

			ImageSpec decorImageSpec = roomsDecor[i];
			if ( decorImageSpec != null ) {
				ShipRoomImageChangeEvent roomDecorEvent = Pools.get( ShipRoomImageChangeEvent.class ).obtain();
				roomDecorEvent.init( ShipRoomImageChangeEvent.DECOR, roomRefId, decorImageSpec );
				context.getScreenEventManager().postDelayedEvent( roomDecorEvent );
			}

			ShipLayoutRoomAddEvent roomAddEvent = Pools.get( ShipLayoutRoomAddEvent.class ).obtain();
			roomAddEvent.init( shipRefId, roomRefId, roomCoords );
			context.getScreenEventManager().postDelayedEvent( roomAddEvent );
		}

		return shipRefId;
	}
}
