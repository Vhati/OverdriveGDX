package com.ftloverdrive.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.utils.Array;


/**
 * AssetLoader for BitmapFont instances generated from ttf files via FreeType.
 * The instance is loaded synchronously.
 *
 * The font size can be specified by appending to the fileName: "...?size=10".
 */
public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontParameter> {
	protected Pattern argsPtn;


	public FreeTypeFontLoader( FileHandleResolver resolver ) {
		super( resolver );
		argsPtn = Pattern.compile( "&?([A-Za-z0-9]+)=([A-Za-z0-9]+)(#?)" );
	}


	@Override
	public FileHandle resolve( String fileName ) {
		String scrubbedFileName = fileName;

		int qMark = fileName.lastIndexOf( "?" );
		if ( qMark != -1 ) {
			scrubbedFileName = scrubbedFileName.substring( 0, qMark );
		}
		return super.resolve( scrubbedFileName );
	}


	@Override
	public BitmapFont loadSync( AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter ) {
		int fontSize = 10;
		String scrubbedFileName = fileName;

		int qMark = fileName.lastIndexOf( "?" );
		if ( qMark != -1 ) {
			scrubbedFileName = scrubbedFileName.substring( 0, qMark );

			Matcher m = argsPtn.matcher( fileName );
			m.region( qMark+1, fileName.length() );
			while ( m.lookingAt() ) {
				if ( m.group( 1 ).equals( "size" ) ) {
					fontSize = Integer.parseInt( m.group( 2 ) );
				}
				if ( m.group( 3 ).length() > 0 ) break;  // Hit the "#" separator.
				m.region( m.end(), fileName.length() );
			}
		}

		boolean flip = parameter != null ? parameter.flip : false;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator( file );

		FreeTypeBitmapFontData data = generator.generateData( fontSize, FreeTypeFontGenerator.DEFAULT_CHARS, flip );
		generator.dispose();

		return new BitmapFont( data, data.getTextureRegions(), true );
	}
	
	@Override
	public void loadAsync( AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter ) {
		// No-op.
	}

	@Override
	public Array<AssetDescriptor> getDependencies( String fileName, FileHandle file, BitmapFontParameter parameter ) {
		return new Array<AssetDescriptor>();
	}
}
