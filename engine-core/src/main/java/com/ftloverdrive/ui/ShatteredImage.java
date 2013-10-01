package com.ftloverdrive.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;


/**
 * A sprite that draws an array of TextureRegions as a row of tiles, wrapping after N columns.
 *
 * Usage:
 *   Array<TextureRegion> tiles = atlas.findRegions( "main-base2" );
 *   ShatteredImage bigImage = new ShatteredImage( tiles, 5 );
 */
public class ShatteredImage extends Table {
	protected Array<Image> tileImages;

	protected Array<? extends TextureRegion> tileRegions;
	protected int tileColumns;


	public ShatteredImage( Array<? extends TextureRegion> tileRegions, int tileColumns ) {
		super();
		this.tileRegions = tileRegions;
		this.tileColumns = tileColumns;

		for ( int n=0; n < tileRegions.size; n++ ) {
			Image tileImage = new Image( tileRegions.get(n) );
			this.add( tileImage );
			if ( n % tileColumns == tileColumns-1 ) {
				// This was the last tile in a row.
				this.row();
			}
		}
	}
}
