package com.ftloverdrive.ui.ship;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.ship.ShipCoordinate;
import com.ftloverdrive.util.OVDConstants;


/**
 * All the floor tiles on a ship (but no lines).
 *
 * After construction, set the height, then call addTile() for all
 * ShipCoordinates.
 */
public class ShipRoomDecorsActor extends Group implements Disposable {

	protected float tileSize = 35;

	protected AssetManager assetManager;
	protected Array<String> atlasPaths;


	public ShipRoomDecorsActor( OverdriveContext context ) {
		super();
		assetManager = context.getAssetManager();
		atlasPaths = new Array<String>( false, 1 );
	}


	/**
	 * Sets a new tile size (default: 35).
	 *
	 * The clear() method should be called first, if tiles have been added.
	 */
	public void setTileSize( float n ) {
		tileSize = n;
	}


	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) {
		super.draw( batch, parentAlpha );
	}


	protected float calcTileX( ShipCoordinate coord ) {
		return ( coord.x * tileSize );
	}

	protected float calcTileY( ShipCoordinate coord ) {
		return ( this.getHeight() - ( coord.y * tileSize ) );
	}

	/**
	 * Adds a decoration at a rectangle encompassing a room's coordinates.
	 *
	 * TODO: Maybe allow non-rectangular rooms with ScissorsStack clipping.
	 */
	public void addDecor( ImageSpec imageSpec, ShipCoordinate[] coords ) {
		if ( imageSpec == null ) return;

		Rectangle decorBounds = null;
		for ( ShipCoordinate coord : coords ) {
			if ( coord.v != 0 ) continue;

			Rectangle tmpRect = new Rectangle( calcTileX( coord ), calcTileY( coord ), tileSize, tileSize );
			if ( decorBounds == null ) {
				decorBounds = tmpRect;
			} else {
				decorBounds.merge( tmpRect );
			}
		}
		if ( decorBounds == null ) return;

		if ( !atlasPaths.contains( imageSpec.getAtlasPath(), false ) ) {
			assetManager.load( imageSpec.getAtlasPath(), TextureAtlas.class );
			assetManager.finishLoading();
			atlasPaths.add( imageSpec.getAtlasPath() );
		}
		TextureAtlas decorAtlas = assetManager.get( imageSpec.getAtlasPath(), TextureAtlas.class );

		Sprite decorSprite = decorAtlas.createSprite( imageSpec.getRegionName() );
		SpriteDrawable decorDrawable = new SpriteDrawable( decorSprite );
		Image decorImage = new Image( decorDrawable );
		decorImage.setBounds( decorBounds.x, decorBounds.y, decorBounds.width, decorBounds.height );
		this.addActor( decorImage );

		// These are different floats which can cause gaps when mixed.
		// (x * size + size) != ((x+1) * size)
	}


	@Override
	public void dispose() {
		for ( String atlasPath : atlasPaths ) {
			assetManager.unload( atlasPath );
		}
	}
}
