package com.ftloverdrive.ui.ship;

import java.util.Set;

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
import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.ship.ShipCoordinate;
import com.ftloverdrive.util.OVDConstants;


/**
 * All the lines for room contours on a ship.
 *
 * After construction, set the height, then call addTile() for all
 * ShipCoordinates.
 *
 * The original tiles drew gray lines in the bottom-right.
 * Wall lines were overlaid on any edge as needed.
 *   2px black wall, 32px white, 1px gray.
 *   34px white, 1px gray.
 */
public class ShipWallLinesActor extends Actor implements Disposable {

	protected float tileSize = 35;

	protected AssetManager assetManager;
	protected Array<Image> tiles;

	private int tileIndex = 0;  // Micro-optimization to reuse a loop var.


	public ShipWallLinesActor( OverdriveContext context ) {
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
	public void addTile( ShipCoordinate coord, Set<ShipCoordinate> allCoords ) {
		if ( coord.v != 0 ) return;

		ShipCoordinate tmpCoord = Pools.get( ShipCoordinate.class ).obtain();

		tmpCoord.init( coord.x, coord.y, 1 );
		boolean nWall = allCoords.contains( tmpCoord ) ? true : false;
		tmpCoord.init( coord.x, coord.y+1, 1 );
		boolean sWall = allCoords.contains( tmpCoord ) ? true : false;
		tmpCoord.init( coord.x, coord.y, 2 );
		boolean wWall = allCoords.contains( tmpCoord ) ? true : false;
		tmpCoord.init( coord.x+1, coord.y, 2 );
		boolean eWall = allCoords.contains( tmpCoord ) ? true : false;

		boolean nwCorner = false;
		boolean neCorner = false;
		boolean swCorner = false;
		boolean seCorner = false;

		tmpCoord.init( coord.x, coord.y-1, 2 );
		if ( allCoords.contains( tmpCoord ) ) nwCorner = true;
		tmpCoord.init( coord.x-1, coord.y, 1 );
		if ( allCoords.contains( tmpCoord ) ) nwCorner = true;
/*
		tmpCoord.init( coord.x+1, coord.y-1, 2 );
		if ( allCoords.contains( tmpCoord ) ) neCorner = true;
		tmpCoord.init( coord.x+1, coord.y, 1 );
		if ( allCoords.contains( tmpCoord ) ) neCorner = true;

		tmpCoord.init( coord.x, coord.y+1, 2 );
		if ( allCoords.contains( tmpCoord ) ) swCorner = true;
		tmpCoord.init( coord.x-1, coord.y+1, 1 );
		if ( allCoords.contains( tmpCoord ) ) swCorner = true;

		tmpCoord.init( coord.x+1, coord.y+1, 2 );
		if ( allCoords.contains( tmpCoord ) ) seCorner = true;
		tmpCoord.init( coord.x+1, coord.y+1, 1 );
		if ( allCoords.contains( tmpCoord ) ) seCorner = true;
*/
		String regionName = null;
		if ( nWall && sWall && wWall && eWall ) {
			regionName = "wall-side-n,s,e,w";
		}
		else if ( nwCorner && neCorner && swCorner && seCorner ) {
			regionName = "wall-corner-nw,ne,sw,se";
		}
		else if ( nWall && !sWall && !wWall && !eWall                           && !swCorner && !seCorner ) {
			regionName = "wall-side-n";
		}
		else if ( !nWall && !sWall && !wWall && eWall && !nwCorner              && !swCorner              ) {
			regionName = "wall-side-e";
		}
		else if ( !nWall && sWall && !wWall && !eWall && !nwCorner && !neCorner                           ) {
			regionName = "wall-side-s";
		}
		else if ( !nWall && !sWall && wWall && !eWall              && !neCorner             && !seCorner ) {
			regionName = "wall-side-w";
		}
		else if ( nWall && !sWall && wWall && !eWall                                        && !seCorner ) {
			regionName = "wall-side-n,w";
		}
		else if ( nWall && !sWall && !wWall && eWall                           && !swCorner              ) {
			regionName = "wall-side-n,e";
		}
		else if ( !nWall && sWall && wWall && !eWall              && !neCorner                           ) {
			regionName = "wall-side-s,w";
		}
		else if ( !nWall && sWall && !wWall && eWall && !nwCorner                                        ) {
			regionName = "wall-side-s,e";
		}
		else if ( nWall && !sWall && wWall && eWall                                                     ) {
			regionName = "wall-side-n,e,w";
		}
		else if ( nWall && sWall && !wWall && eWall                                                     ) {
			regionName = "wall-side-n,s,e";
		}
		else if ( !nWall && sWall && wWall && eWall                                                     ) {
			regionName = "wall-side-s,e,w";
		}
		else if ( nWall && sWall && wWall && !eWall                                                     ) {
			regionName = "wall-side-n,s,w";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && nwCorner && !neCorner && !swCorner && !seCorner ) {
			regionName = "wall-corner-nw";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && !nwCorner && neCorner && !swCorner && !seCorner ) {
			regionName = "wall-corner-ne";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && !nwCorner && !neCorner && swCorner && !seCorner ) {
			regionName = "wall-corner-sw";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && !nwCorner && !neCorner && !swCorner && seCorner ) {
			regionName = "wall-corner-se";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && nwCorner && neCorner && !swCorner && !seCorner ) {
			regionName = "wall-corner-nw,ne";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && !nwCorner && neCorner && !swCorner && seCorner ) {
			regionName = "wall-corner-ne,se";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && !nwCorner && !neCorner && swCorner && seCorner ) {
			regionName = "wall-corner-sw,se";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && nwCorner && !neCorner && !swCorner && !seCorner ) {
			regionName = "wall-corner-nw,sw";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && nwCorner && neCorner && swCorner && !seCorner ) {
			regionName = "wall-corner-nw,ne,sw";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && !nwCorner && neCorner && !swCorner && seCorner ) {
			regionName = "wall-corner-ne,se";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && nwCorner && neCorner && !swCorner && seCorner ) {
			regionName = "wall-corner-nw,ne,se";
		}
		else if ( !nWall && !sWall && !wWall && !eWall && nwCorner && !neCorner && swCorner && seCorner ) {
			regionName = "wall-corner-nw,sw,se";
		}
		else if ( nWall && !sWall && !wWall && !eWall                           && swCorner && !seCorner ) {
			regionName = "wall-corner-n,sw";
		}
		else if ( !nWall && !sWall && !wWall && eWall && nwCorner              && !swCorner              ) {
			regionName = "wall-corner-e,nw";
		}
		else if ( !nWall && sWall && !wWall && !eWall && !nwCorner && neCorner                           ) {
			regionName = "wall-corner-s,ne";
		}
		else if ( !nWall && !sWall && wWall && !eWall              && !neCorner              && seCorner ) {
			regionName = "wall-corner-w,se";
		}
		else if ( nWall && !sWall && !wWall && !eWall                           && swCorner && seCorner ) {
			regionName = "wall-corner-n,sw,se";
		}
		else if ( !nWall && !sWall && !wWall && eWall && nwCorner              && swCorner              ) {
			regionName = "wall-corner-e,nw,sw";
		}
		else if ( !nWall && sWall && !wWall && !eWall && nwCorner && neCorner                           ) {
			regionName = "wall-corner-s,nw,ne";
		}
		else if ( !nWall && !sWall && wWall && !eWall              && neCorner              && seCorner ) {
			regionName = "wall-corner-w,ne,se";
		}
		else if ( nWall && !sWall && wWall && !eWall                                        && seCorner ) {
			regionName = "wall-corner-n,w,se";
		}
		else if ( nWall && !sWall && !wWall && eWall                           && swCorner              ) {
			regionName = "wall-corner-n,e,sw";
		}
		else if ( !nWall && sWall && !wWall && eWall && nwCorner                                        ) {
			regionName = "wall-corner-s,e,nw";
		}
		else if ( !nWall && sWall && wWall && !eWall              && neCorner                           ) {
			regionName = "wall-corner-s,w,ne";
		}

		if ( regionName != null ) {
			TextureAtlas floorAtlas = assetManager.get( OVDConstants.FLOORPLAN_ATLAS, TextureAtlas.class );

			TextureRegion tileRegion = floorAtlas.findRegion( regionName );
			NinePatchDrawable tileDrawable = new NinePatchDrawable( new NinePatch( tileRegion, 2, 2, 2, 2 ) );
			Image tileImage = new Image( tileDrawable );
			tileImage.setPosition( calcTileX( coord ), calcTileY( coord ) );
			tileImage.setSize( tileSize, tileSize );
			tiles.add( tileImage );
		}

		// These are different floats which can cause gaps when mixed.
		// (x * size + size) != ((x+1) * size)
	}


	@Override
	public void dispose() {
		assetManager.unload( OVDConstants.FLOORPLAN_ATLAS );
	}
}
