package com.ftloverdrive.ui.ship;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.ship.ShipCoordinate;
import com.ftloverdrive.util.OVDConstants;


/**
 * All the lines between floor tiles on a ship.
 *
 * After construction, set the height, then call addTile() for all
 * ShipCoordinates.
 */
public class ShipFloorLinesActor extends Actor implements Disposable {

	protected float tileSize = 35;

	protected AssetManager assetManager;
	protected Array<Image> tiles;

	private int tileIndex = 0;  // Micro-optimization to reuse a loop var.


	public ShipFloorLinesActor( OverdriveContext context ) {
		super();
		assetManager = context.getAssetManager();

		assetManager.load( OVDConstants.FLOORPLAN_ATLAS, TextureAtlas.class );
		assetManager.finishLoading();

		tiles = new Array<Image>();
	}


	/**
	 * Sets a new tile size (default: 35).
	 *
	 * The clear() method should be called first, if tiles have been added.
	 */
	public void setTileSize( float n ) {
		tileSize = n;
	}


	/**
	 * Removes all floor tiles.
	 */
	public void clear() {
		tiles.clear();
	}


	@Override
	public void draw( SpriteBatch batch, float parentAlpha ) {
		super.draw( batch, parentAlpha );

		for ( tileIndex = tiles.size-1; tileIndex >= 0; tileIndex-- ) {
			tiles.get( tileIndex ).draw( batch, parentAlpha );
		}
	}


	protected float calcTileX( ShipCoordinate coord ) {
		return ( coord.x * tileSize );
	}

	protected float calcTileY( ShipCoordinate coord ) {
		return ( this.getHeight() - ( coord.y * tileSize ) );
	}

	/**
	 * Adds a tile to represent a ShipCoordinate.
	 */
	public void addTile( ShipCoordinate coord ) {
		if ( coord.v != 0 ) return;

		TextureAtlas floorAtlas = assetManager.get( OVDConstants.FLOORPLAN_ATLAS, TextureAtlas.class );

		TextureRegion tileRegion = floorAtlas.findRegion( "floor-line" );
		NinePatchDrawable tileDrawable = new NinePatchDrawable( new NinePatch( tileRegion, 1, 1, 1, 1 ) );
		Image tileImage = new Image( tileDrawable );
		tileImage.setPosition( calcTileX( coord ), calcTileY( coord ) );
		tileImage.setSize( tileSize, tileSize );
		tiles.add( tileImage );

		// These are different floats which can cause gaps when mixed.
		// (x * size + size) != ((x+1) * size)
	}


	@Override
	public void dispose() {
		assetManager.unload( OVDConstants.FLOORPLAN_ATLAS );
	}
}
