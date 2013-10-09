package com.ftloverdrive.ui.hud;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.game.GamePlayerShipChangeEvent;
import com.ftloverdrive.event.game.GamePlayerShipChangeListener;
import com.ftloverdrive.event.ship.ShipPropertyEvent;
import com.ftloverdrive.event.ship.ShipPropertyListener;
import com.ftloverdrive.model.ship.ShipModel;


public class PlayerShipHullMonitor extends Actor implements Disposable, GamePlayerShipChangeListener, ShipPropertyListener {
	protected static final String STATUSUI_ATLAS = "img/statusUI/pack.atlas";

	protected AssetManager assetManager;
	protected Sprite bgSprite;
	protected Sprite barSprite;
	protected float barClipWidth = 0;
	protected int shipModelRefId = -1;


	public PlayerShipHullMonitor( OverdriveContext context ) {
		super();
		assetManager = context.getAssetManager();

		assetManager.load( STATUSUI_ATLAS, TextureAtlas.class );
		assetManager.finishLoading();
		TextureAtlas statusUIAtlas = assetManager.get( STATUSUI_ATLAS, TextureAtlas.class );

		bgSprite = statusUIAtlas.createSprite( "top-hull" );
		this.setWidth( bgSprite.getWidth() );
		this.setHeight( bgSprite.getHeight() );

		barSprite = statusUIAtlas.createSprite( "top-hull-bar-mask" );
	}


	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) {
		//Color color = getColor();
		//batch.setColor( color.r, color.g, color.b, color.a * parentAlpha );

		bgSprite.setPosition( this.getX(), this.getY() );
		bgSprite.draw( batch, parentAlpha );

		batch.flush();
		if ( clipBegin( this.getX()+11, this.getY(), barClipWidth, bgSprite.getHeight() ) ) {
			barSprite.setPosition( this.getX()+11, this.getY() + bgSprite.getHeight()/2 - barSprite.getHeight()/2 );
			barSprite.setColor( 0.47f, 1f, 0.47f, 1f );
			barSprite.draw( batch, parentAlpha );
			batch.flush();
			clipEnd();
		}
	}


	public void setShipModel( OverdriveContext context, int shipModelRefId ) {
		this.shipModelRefId = shipModelRefId;
		updateShipInfo( context );
	}


	@Override
	public void gamePlayerShipChanged( OverdriveContext context, GamePlayerShipChangeEvent e ) {
		setShipModel( context, e.getShipRefId() );
	}

	@Override
	public void shipPropertyChanged( OverdriveContext context, ShipPropertyEvent e ) {
		if ( e.getShipRefId() != shipModelRefId ) return;

		if ( e.getPropertyType() == ShipPropertyEvent.INT_TYPE ) {
			if ( "Hull".equals( e.getPropertyKey() ) || "HullMax".equals( e.getPropertyKey() ) ) {
				updateShipInfo( context );
			}
		}
	}


	/**
	 * Updates the bar to match the player ship's Hull/HullMax.
	 */
	private void updateShipInfo( OverdriveContext context ) {
		if ( shipModelRefId == -1 ) {
			barClipWidth = 0;
		}
		else {
			ShipModel shipModel = context.getReferenceManager().getObject( shipModelRefId, ShipModel.class );
			int hullAmt = shipModel.getProperties().getInt( "Hull" );
			int hullMax = shipModel.getProperties().getInt( "HullMax" );
			if ( hullMax != 0 ) {
				barClipWidth = Math.min( ((float)hullAmt / hullMax) * barSprite.getWidth(), barSprite.getWidth() );
			} else {
				barClipWidth = 0;
			}
		}
	}


	// Actors don't normally have a dispose().
	@Override
	public void dispose() {
		assetManager.unload( STATUSUI_ATLAS );
	}
}
