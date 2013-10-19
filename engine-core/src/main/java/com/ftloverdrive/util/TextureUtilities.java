package com.ftloverdrive.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;


/**
 * Not used.
 */
public class TextureUtilities {

	// These methods were early experiments. PixmapPacker is recommended.

	/**
	 * Loads an image file as a region of a square texture.
	 * The texture's dimensions will be the nearest power of two.
	 *
	 * Warning, textures created from Pixmaps (such as via this method)
	 * will be unmanaged, and at risk of going blank after a pause
	 * causes the GL context to be lost.
	 *
	 * Loading non-square Textures directly will cause an error. The
	 * line below can suppress that error, but some hardware may just
	 * leave surfaces white.
	 * Texture.setEnforcePotImages(false);
	 */
	public static TextureRegion loadSquareRegion( FileHandle imageFile ) {
		Pixmap origPixels = new Pixmap( imageFile );
		return loadSquareRegion( origPixels );
	}

	public static TextureRegion loadSquareRegion( Pixmap origPixels ) {
		int origW = origPixels.getWidth();
		int origH = origPixels.getHeight();

		Pixmap paddedPixels = new Pixmap( MathUtils.nextPowerOfTwo( origW ), MathUtils.nextPowerOfTwo( origH ), Pixmap.Format.RGBA8888 );
		// Paint the unused area bright green.
		paddedPixels.setColor( 0f, 1f, 0f, 1f );
		paddedPixels.fillRectangle( origW, 0, paddedPixels.getWidth()-origW, paddedPixels.getHeight() );
		paddedPixels.fillRectangle( 0, origH, paddedPixels.getWidth(), paddedPixels.getHeight()-origH );
		paddedPixels.drawPixmap( origPixels, 0, 0, 0, 0, origW, origH );
		origPixels.dispose();

		// Load the image into GL.
		Texture texture = new Texture( paddedPixels );
		paddedPixels.dispose();

		TextureRegion region = new TextureRegion( texture, 0, 0, origW, origH );
		return region;
	}
}
