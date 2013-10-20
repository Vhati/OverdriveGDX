package com.ftloverdrive.ui.ship;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.game.GamePlayerShipChangeEvent;
import com.ftloverdrive.event.game.GamePlayerShipChangeListener;
import com.ftloverdrive.event.ship.ShipPropertyEvent;
import com.ftloverdrive.event.ship.ShipPropertyListener;
import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.ship.RoomModel;
import com.ftloverdrive.model.ship.ShipCoordinate;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.ui.ship.ShipFloorLinesActor;
import com.ftloverdrive.ui.ship.ShipFloorTilesActor;
import com.ftloverdrive.ui.ship.ShipRoomDecorsActor;
import com.ftloverdrive.ui.ship.ShipWallLinesActor;
import com.ftloverdrive.util.OVDConstants;


public class ShipActor extends Group implements Disposable, GamePlayerShipChangeListener, ShipPropertyListener {
	protected AssetManager assetManager;

	protected Group shipFudgeGroup;
	protected Group shipHullGroup;
	protected Group shipFloorplanGroup;

	protected SpriteDrawable nullDrawable;
	protected Image shieldImage;
	protected Image baseImage;
	protected Image cloakImage;
	protected Image floorImage;

	protected ShipFloorTilesActor floorTiles;
	protected ShipFloorLinesActor floorLines;
	protected ShipRoomDecorsActor roomDecors;
	protected ShipWallLinesActor wallLines;

	protected int shipModelRefId = -1;
	protected ImageSpec shieldImgSpec = null;
	protected ImageSpec baseImgSpec = null;
	protected ImageSpec cloakImgSpec = null;
	protected ImageSpec floorImgSpec = null;


	public ShipActor( OverdriveContext context ) {
		super();
		assetManager = context.getAssetManager();

		assetManager.load( OVDConstants.ROOT_ATLAS, TextureAtlas.class );
		assetManager.load( OVDConstants.SHIP_ATLAS, TextureAtlas.class );
		assetManager.finishLoading();

		shipFudgeGroup = new Group();
		shipFudgeGroup.setName( "ShipFudgeGroup" );
		this.addActor( shipFudgeGroup );

		shipHullGroup = new Group();
		shipHullGroup.setName( "ShipHullGroup" );
		shipFudgeGroup.addActor( shipHullGroup );

		shipFloorplanGroup = new Group();
		shipFloorplanGroup.setName( "ShipFloorplanGroup" );
		shipFudgeGroup.addActor( shipFloorplanGroup );

		TextureAtlas rootAtlas = assetManager.get( OVDConstants.ROOT_ATLAS, TextureAtlas.class );
		Sprite nullSprite = rootAtlas.createSprite( "nullResource" );
		nullDrawable = new SpriteDrawable( nullSprite );

		shieldImage = new Image( nullDrawable );
		shipHullGroup.addActor( shieldImage );

		baseImage = new Image( nullDrawable );
		shipHullGroup.addActor( baseImage );

		cloakImage = new Image( nullDrawable );
		cloakImage.setVisible( false );
		shipHullGroup.addActor( cloakImage );

		floorImage = new Image( nullDrawable );
		shipHullGroup.addActor( floorImage );

		floorTiles = new ShipFloorTilesActor( context );
		shipFloorplanGroup.addActor( floorTiles );

		floorLines = new ShipFloorLinesActor( context );
		shipFloorplanGroup.addActor( floorLines );

		roomDecors = new ShipRoomDecorsActor( context );
		shipFloorplanGroup.addActor( roomDecors );

		wallLines = new ShipWallLinesActor( context );
		shipFloorplanGroup.addActor( wallLines );
	}


	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) {
		if ( shipModelRefId != -1 ) {
			//zzz.setPosition( this.getX(), this.getY() );
			super.draw( batch, parentAlpha );
		}
	}


	public void setShipModel( OverdriveContext context, int shipModelRefId ) {
		this.shipModelRefId = shipModelRefId;
		updateShipInfo( context );
	}


	@Override
	public void gamePlayerShipChanged( OverdriveContext context, GamePlayerShipChangeEvent e ) {
		if ( e.getPlayerRefId() != context.getNetManager().getLocalPlayerRefId() ) {
			return;
		}

		setShipModel( context, e.getShipRefId() );
	}

	@Override
	public void shipPropertyChanged( OverdriveContext context, ShipPropertyEvent e ) {
		if ( e.getShipRefId() != shipModelRefId ) return;

		//if ( e.getPropertyType() == ShipPropertyEvent.INT_TYPE ) {
		//	if ( OVDConstants.HULL.equals( e.getPropertyKey() ) || OVDConstants.HULL_MAX.equals( e.getPropertyKey() ) ) {
		//		updateShipInfo( context );
		//	}
		//}
	}


	/**
	 * Updates the everything to match the current ShipModel.
	 */
	private void updateShipInfo( OverdriveContext context ) {

		if ( shipModelRefId == -1 ) {
			shipFudgeGroup.setPosition( 0, 0 );
			shipHullGroup.setPosition( 0, 0 );

			shieldImgSpec = null;
			shieldImage.setDrawable( nullDrawable );
			shieldImage.setBounds( 0, 0, 0, 0 );
			shieldImage.validate();

			baseImgSpec = null;
			baseImage.setDrawable( nullDrawable );
			baseImage.setBounds( 0, 0, 0, 0 );
			baseImage.validate();

			floorTiles.clear();
			floorTiles.setSize( 0, 0 );

			floorLines.clear();
			floorLines.setSize( 0, 0 );

			this.setSize( 0, 0 );
		}
		else {
			ShipModel shipModel = context.getReferenceManager().getObject( shipModelRefId, ShipModel.class );
			TextureAtlas shipAtlas = assetManager.get( OVDConstants.SHIP_ATLAS, TextureAtlas.class );

			shipFudgeGroup.setPosition( shipModel.getShipOffsetX(), shipModel.getShipOffsetY() );
			shipHullGroup.setPosition( shipModel.getHullOffsetX(), shipModel.getHullOffsetY() );

			ImageSpec currentShieldImgSpec = shipModel.getShieldImageSpec();
			if ( !isEqual( shieldImgSpec, currentShieldImgSpec ) ) {
				shieldImgSpec = currentShieldImgSpec;

				Sprite shieldSprite = shipAtlas.createSprite( shieldImgSpec.getRegionName() );
				if ( shieldSprite != null ) {
					shieldImage.setDrawable( new SpriteDrawable( shieldSprite ) );
				} else {
					shieldImage.setDrawable( nullDrawable );
				}

				shieldImage.setX( shipModel.getShieldEllipseSemiMajorAxis() - shieldImage.getPrefWidth()/2 + shipModel.getShieldEllipseOffsetX() );
				shieldImage.setY( shipModel.getShieldEllipseSemiMinorAxis() - shieldImage.getPrefHeight()/2 + shipModel.getShieldEllipseOffsetY() );
				shieldImage.setSize( shieldImage.getPrefWidth(), shieldImage.getPrefHeight() );
				shieldImage.validate();
			}

			ImageSpec currentBaseImgSpec = shipModel.getBaseImageSpec();
			if ( !isEqual( baseImgSpec, currentBaseImgSpec ) ) {
				baseImgSpec = currentBaseImgSpec;

				Sprite baseSprite = shipAtlas.createSprite( baseImgSpec.getRegionName() );
				if ( baseSprite != null ) {
					baseImage.setDrawable( new SpriteDrawable( baseSprite ) );
				} else {
					baseImage.setDrawable( nullDrawable );
				}

				baseImage.setPosition( 0, 0 );
				baseImage.setSize( shipModel.getHullWidth(), shipModel.getHullHeight() );
				baseImage.validate();
			}

			ImageSpec currentCloakImgSpec = shipModel.getCloakImageSpec();
			if ( !isEqual( cloakImgSpec, currentCloakImgSpec ) ) {
				cloakImgSpec = currentCloakImgSpec;

				Sprite cloakSprite = shipAtlas.createSprite( cloakImgSpec.getRegionName() );
				if ( cloakSprite != null ) {
					cloakImage.setDrawable( new SpriteDrawable( cloakSprite ) );
				} else {
					cloakImage.setDrawable( nullDrawable );
				}

				cloakImage.setPosition( -10, -10 );
				cloakImage.setWidth( shipModel.getHullWidth()+20 );
				cloakImage.setHeight( shipModel.getHullHeight()+20 );
				cloakImage.validate();
			}

			ImageSpec currentFloorImgSpec = shipModel.getFloorImageSpec();
			if ( !isEqual( floorImgSpec, currentFloorImgSpec ) ) {
				floorImgSpec = currentFloorImgSpec;

				Sprite floorSprite = shipAtlas.createSprite( floorImgSpec.getRegionName() );
				if ( floorSprite != null ) {
					floorImage.setDrawable( new SpriteDrawable( floorSprite ) );
				} else {
					floorImage.setDrawable( nullDrawable );
				}

				floorImage.setPosition( 0, 0 );
				floorImage.setSize( shipModel.getHullWidth(), shipModel.getHullHeight() );
				floorImage.validate();
			}

			floorTiles.clear();
			floorTiles.setSize( shipModel.getHullWidth(), shipModel.getHullHeight() );
			floorTiles.setTileSize( 35 );
			for ( ShipCoordinate coord : shipModel.getLayout().getAllShipCoords() ) {
				floorTiles.addTile( coord );
			}

			floorLines.clear();
			floorLines.setSize( shipModel.getHullWidth(), shipModel.getHullHeight() );
			floorLines.setTileSize( 35 );
			for ( ShipCoordinate coord : shipModel.getLayout().getAllShipCoords() ) {
				floorLines.addTile( coord );
			}

			roomDecors.clear();
			roomDecors.setSize( shipModel.getHullWidth(), shipModel.getHullHeight() );
			roomDecors.setTileSize( 35 );
			for ( IntMap.Keys it = shipModel.getLayout().roomRefIds(); it.hasNext; ) {
				int roomRefId = it.next();
				RoomModel roomModel = context.getReferenceManager().getObject( roomRefId, RoomModel.class );
				ImageSpec decorImageSpec = roomModel.getRoomDecorImageSpec();
				if ( decorImageSpec == null ) continue;

				roomDecors.addDecor( decorImageSpec, shipModel.getLayout().getRoomCoords( roomRefId ) );
			}

			wallLines.clear();
			wallLines.setSize( shipModel.getHullWidth(), shipModel.getHullHeight() );
			wallLines.setTileSize( 35 );
			for ( ShipCoordinate coord : shipModel.getLayout().getAllShipCoords() ) {
				wallLines.addTile( coord, shipModel.getLayout().getAllShipCoords() );
			}
		}
	}

	protected boolean isEqual( Object a, Object b ) {
		if ( a != null ) return a.equals( b );
		if ( b != null ) return b.equals( a );
		return true; // Both null.
	}


	// Actors don't normally have a dispose().
	@Override
	public void dispose() {
		floorTiles.dispose();
		roomDecors.dispose();
		assetManager.unload( OVDConstants.SHIP_ATLAS );
		assetManager.unload( OVDConstants.ROOT_ATLAS );
	}
}
