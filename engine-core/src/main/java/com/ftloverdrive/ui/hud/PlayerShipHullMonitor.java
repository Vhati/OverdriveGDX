package com.ftloverdrive.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.math.Rectangle;

import com.ftloverdrive.core.OverdriveGame;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.PropertiesObserver;
import com.ftloverdrive.model.ship.ShipModel;


public class PlayerShipHullMonitor extends Actor implements PropertiesObserver {
	protected static final String STATUSUI_ATLAS = "img/statusUI/pack.atlas";

	protected OverdriveGame game;
	protected Sprite bgSprite;
	protected Sprite barSprite;
	protected float barClipWidth = 0;
	protected NamedProperties shipProperties = null;


	public PlayerShipHullMonitor() {
		super();
		game = (OverdriveGame)Gdx.app.getApplicationListener();

		game.getAssetManager().load( STATUSUI_ATLAS, TextureAtlas.class );
		game.getAssetManager().finishLoading();
		TextureAtlas statusUIAtlas = game.getAssetManager().get( STATUSUI_ATLAS, TextureAtlas.class );

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


	public void setModel( ShipModel shipModel ) {
		if ( shipProperties != null ) shipProperties.removePropertiesObserver( this );

		if ( shipModel == null ) {
			barClipWidth = 0;
			shipProperties = null;
		} else {
			shipProperties = shipModel.getProperties();
			shipProperties.addPropertiesObserver( this );
			propertyChanged( shipProperties, NamedProperties.INT_TYPE, "Hull" );
		}
	}


	@Override
	public void propertyChanged( NamedProperties source, int type, String key ) {
		if ( source != shipProperties ) return;

		if ( type == NamedProperties.INT_TYPE && ( "Hull".equals( key ) || "HullMax".equals( key ) ) ) {
			int hullAmt = source.getInt( "Hull" );
			int hullMax = source.getInt( "HullMax" );
			if ( hullMax != 0 ) {
				barClipWidth = Math.min( ((float)hullAmt / hullMax) * barSprite.getWidth(), barSprite.getWidth() );
			} else {
				barClipWidth = 0;
			}
		}
	}


	// Actors don't normally have a dispose().
	public void dispose() {
		game.getAssetManager().unload( STATUSUI_ATLAS );
	}
}
