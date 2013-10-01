package com.ftloverdrive.io;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;


/**
 * A resolver for locations relative to a given dir.
 */
public class RelativeFileHandleResolver implements FileHandleResolver {
	protected File rootDir;


	public RelativeFileHandleResolver( File rootDir ) {
		this.rootDir = rootDir;
	}


	@Override
	public FileHandle resolve( String s ) {
		FileHandle result = null;
		File f = new File( rootDir, s );
		if ( f.exists() ) {
			result = Gdx.files.absolute( f.getAbsolutePath() );
		}
		return result;
	}
}
