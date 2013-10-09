package com.ftloverdrive.core;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;

import com.ftloverdrive.io.FreeTypeFontLoader;
import com.ftloverdrive.io.RelativeFileHandleResolver;
import com.ftloverdrive.io.URIFileHandleResolver;
import com.ftloverdrive.net.OVDNetManager;
import com.ftloverdrive.ui.screen.OVDScreenManager;
import com.ftloverdrive.util.FTLUtilities;
import com.ftloverdrive.util.OVDReferenceManager;


public class OverdriveGame implements ApplicationListener {

	public static final String APP_NAME = "Overdrive";
	public static final String APP_VERSION = "0.1";

	private static final String ENV_APP_PATH = "OVERDRIVE_APP_PATH";


	// Don't instantiate early, or the classloader might complain?
	private Logger log;
	private File appDir;
	private File resourcesDir;

	private URIFileHandleResolver fileHandleResolver;

	private AssetManager assetManager;
	private OVDScreenManager screenManager;
	private OVDReferenceManager refManager;
	private OVDNetManager netManager;
	private Screen currentScreen = null;


	@Override
	public void create () {
		Gdx.app.setLogLevel( Application.LOG_DEBUG );
		log = new Logger( OverdriveGame.class.getCanonicalName(), Logger.DEBUG );
		log.info( String.format( "%s v%s", APP_NAME, APP_VERSION ) );
		log.info( String.format( "%s %s", System.getProperty("os.name"), System.getProperty("os.version") ) );
		log.info( String.format( "%s, %s, %s", System.getProperty("java.vm.name"), System.getProperty("java.version"), System.getProperty("os.arch") ) );

		appDir = new File( "." );
		log.info( "CWD: "+ appDir.getAbsolutePath() );

		String envAppPath = System.getenv( ENV_APP_PATH );
		if ( envAppPath != null && envAppPath.length() > 0 ) {
			File envAppDir = new File( envAppPath );
			if ( envAppDir.exists() ) {
				log.info( String.format( "Environment var (%s) changed app path: %s", ENV_APP_PATH, envAppPath ) );
				appDir = envAppDir;
			} else {
				log.error( String.format( "Environment var (%s) set a non-existent app path: %s", ENV_APP_PATH, envAppPath ) );
			}
		}

		resourcesDir = new File( appDir, "resources" );

		java.nio.IntBuffer buf = com.badlogic.gdx.utils.BufferUtils.newIntBuffer(16);
		Gdx.gl.glGetIntegerv( GL10.GL_MAX_TEXTURE_SIZE, buf );
		int maxTextureSize = buf.get();
		log.debug( "Device Estimated Max Texture Size: "+ maxTextureSize );

		log.debug( "Device GL11: "+ Gdx.graphics.isGL11Available() );
		log.debug( "Device GL20: "+ Gdx.graphics.isGL20Available() );

		fileHandleResolver = new URIFileHandleResolver();
		fileHandleResolver.setResolver( "internal:", new InternalFileHandleResolver(), true );
		fileHandleResolver.setResolver( "external:", new ExternalFileHandleResolver(), true );
		fileHandleResolver.addDefaultResolver( new RelativeFileHandleResolver( resourcesDir ) );
		fileHandleResolver.addDefaultResolver( new RelativeFileHandleResolver( appDir ) );
		fileHandleResolver.addDefaultResolver( new InternalFileHandleResolver() );

		refManager = new OVDReferenceManager();
		netManager = new OVDNetManager();

		assetManager = new AssetManager( fileHandleResolver );
		assetManager.setLoader( BitmapFont.class, new FreeTypeFontLoader( fileHandleResolver ) );

		OverdriveContext context = new OverdriveContext();
		context.init( this, null, -1 );
		screenManager = new OVDScreenManager( context );

		screenManager.showScreen( screenManager.getInitScreenKey() );
	}


	/**
	 * Returns the main FileHandleResolver.
	 *
	 * Usage:
	 *   FileHandle fh = getFileHandleResolver().resolve( pathString );
	 *   // If you need a File object...
	 *   File f = fh.file();
	 *
	 * Given a string, this resolver will look in several locations.
	 *   {current_dir|APP_PATH}/resources/...
	 *   {current_dir|APP_PATH}/...
	 *   {internal}/...
	 *
	 * If a URI prefix is found, that will be stripped, and a specific
	 * location will be checked instead.
	 *   internal://... - Root of the jar, and current dir on desktop.
	 *   external://... - Android SD card, or desktop user's home dir.
	 */
	public FileHandleResolver getFileHandleResolver() {
		return fileHandleResolver;
	}

	/**
	 * Returns a manager to load assets.
	 *
	 * This one can load ttf fonts:
	 *   String assetString = ".../myfont.ttf?size=10";
	 *   assetManager.load( assetString, BitmapFont.class );
	 *   assetManager.finishLoading();
	 *   BitmapFont font = assetManager.get( assetString, BitmapFont.class );
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Returns a manager to change screens.
	 */
	public OVDScreenManager getScreenManager() {
		return screenManager;
	}

	/**
	 * Returns a manager to link objects together by reference ids.
	 */
	public OVDReferenceManager getReferenceManager() {
		return refManager;
	}

	/**
	 * Returns a manager to request new reference ids.
	 */
	public OVDNetManager getNetManager() {
		return netManager;
	}


	/**
	 * Hides the current screen and shows another.
	 *
	 * This shouldn't be called directly.
	 * Use getScreenManager() instead.
	 */
	public void setScreen( Screen screen ) {
		if ( currentScreen != null ) currentScreen.hide();

		currentScreen = screen;
		if ( currentScreen != null ) currentScreen.show();
	}

	public Screen getScreen() {
		return currentScreen;
	}


	@Override
	public void resize( int width, int height ) {
		log.info( String.format( "Screen resized: %dx%d", width, height ) );
		if ( currentScreen != null ) currentScreen.resize( width, height );
	}

	@Override
	public void render() {
		if ( currentScreen != null ) currentScreen.render( Gdx.graphics.getDeltaTime() );
	}

	@Override
	public void pause() {
		if ( currentScreen != null ) currentScreen.pause();
	}

	@Override
	public void resume() {
		if ( currentScreen != null ) currentScreen.resume();
	}

	@Override
	public void dispose () {
		if ( currentScreen != null ) currentScreen.dispose();
		screenManager.dispose();
		assetManager.dispose();
	}
}
