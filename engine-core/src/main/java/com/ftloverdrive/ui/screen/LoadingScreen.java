// Based on Matsemann's demo.
//   https://github.com/Matsemann/libgdx-loading-screen

package com.ftloverdrive.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.script.OVDScriptManager;
import com.ftloverdrive.ui.screen.OVDScreen;
import com.ftloverdrive.ui.screen.OVDStageManager;


public class LoadingScreen implements Disposable, OVDScreen {
	private Logger log;

	private AssetManager loadingAssetManager;
	private TextureAtlas atlas;

	private OVDStageManager stageManager = null;
	private OVDEventManager eventManager = null;
	private OVDScriptManager scriptManager = null;

	private Stage mainStage;

	private Image logo;
	private Image loadingFrame;
	private Image loadingBarHidden;;
	private Image screenBg;
	private Image loadingBg;
	private Actor loadingBar;

	private float startX, endX;
	private float progress, percent;
	private boolean renderedPreviousFrame = false;

	private OverdriveContext context;


	public LoadingScreen( OverdriveContext srcContext ) {
		this.context = Pools.get( OverdriveContext.class ).obtain();
		this.context.init( srcContext );
		this.context.setScreen( this );

		log = new Logger( LoadingScreen.class.getCanonicalName(), Logger.INFO );

		stageManager = new OVDStageManager();
		eventManager = new OVDEventManager();
		scriptManager = new OVDScriptManager();

		mainStage = new Stage();
		stageManager.putStage( "Main", mainStage );

		loadingAssetManager = new AssetManager();
		loadingAssetManager.load( "overdrive-assets/images/loading.pack", TextureAtlas.class );
		loadingAssetManager.finishLoading();  // Block until loaded completely.

		atlas = loadingAssetManager.get( "overdrive-assets/images/loading.pack", TextureAtlas.class );
		logo = new Image( atlas.findRegion("libgdx-logo") );
		loadingFrame = new Image( atlas.findRegion("loading-frame") );
		loadingBarHidden = new Image( atlas.findRegion("loading-bar-hidden") );
		screenBg = new Image( atlas.findRegion("screen-bg") );
		loadingBg = new Image( atlas.findRegion("loading-frame-bg") );

		// Animated bar.
		//Animation anim = new Animation( 0.05f, atlas.findRegions("loading-bar-anim") );
		//anim.setPlayMode(Animation.LOOP_REVERSED);
		//loadingBar = new LoadingBar(anim);

		// Static bar.
		loadingBar = new Image( atlas.findRegion("loading-bar1") );

		mainStage.addActor( screenBg );
		mainStage.addActor( loadingBar );
		mainStage.addActor( loadingBg );
		mainStage.addActor( loadingBarHidden );
		mainStage.addActor( loadingFrame );
		//mainStage.addActor( logo );

		// Uncomment to preload textures.
		//context.getAssetManager().load( "img/buttons/FTL/pack.atlas", TextureAtlas.class );
		//context.getAssetManager().load( "img/combatUI/pack.atlas", TextureAtlas.class );
		context.getAssetManager().load( "img/effects/pack.atlas", TextureAtlas.class );
		context.getAssetManager().load( "img/icons/pack.atlas", TextureAtlas.class );
		context.getAssetManager().load( "img/people/pack.atlas", TextureAtlas.class );
		//context.getAssetManager().load( "img/ship/pack.atlas", TextureAtlas.class );
		context.getAssetManager().load( "img/statusUI/pack.atlas", TextureAtlas.class );
		context.getAssetManager().load( "img/systemUI/pack.atlas", TextureAtlas.class );
		//context.getAssetManager().load( "img/weapons/pack.atlas", TextureAtlas.class );
	}


	@Override
	public void resize( int width, int height ) {
		width = 480 * width / height;
		height = 480;
		mainStage.setViewport( width, height, false );

		// Make the background fill the screen.
		screenBg.setSize(width, height);

		// Place the logo in the middle of the screen and 100 px up.
		logo.setX((width - logo.getWidth()) / 2);
		logo.setY((height - logo.getHeight()) / 2 + 100);

		// Place the loading frame in the middle of the screen.
		loadingFrame.setX((mainStage.getWidth() - loadingFrame.getWidth()) / 2);
		loadingFrame.setY((mainStage.getHeight() - loadingFrame.getHeight()) / 2);

		// Place the loading bar at the same spot as the frame, adjusted a few px.
		loadingBar.setX(loadingFrame.getX() + 15);
		loadingBar.setY(loadingFrame.getY() + 5);

		// Place the image that will hide the bar on top of the bar, adjusted a few px.
		loadingBarHidden.setX(loadingBar.getX() + 35);
		loadingBarHidden.setY(loadingBar.getY() - 3);
		// The start position and how far to move the hidden loading bar.
		startX = loadingBarHidden.getX();
		endX = 440;

		// The rest of the hidden bar.
		loadingBg.setSize(450, 50);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setY(loadingBarHidden.getY() + 3);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor( mainStage );
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor( null );
		renderedPreviousFrame = false;
	}


	@Override
	public void render( float delta ) {
		Gdx.gl.glClearColor( 0, 0, 0, 0 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		// Incrementally load assets until completely done.
		if ( context.getAssetManager().update() ) {
			//if ( Gdx.input.isTouched() ) {
				context.getScreenManager().continueToNextScreen();
			//}
		}

		// Interpolate the percentage to make it more smooth.
		progress = context.getAssetManager().getProgress();
		percent = Interpolation.linear.apply( percent, progress, 0.1f );

		loadingBarHidden.setX( startX + endX * percent );
		loadingBg.setX( loadingBarHidden.getX() + 30 );
		loadingBg.setWidth( 450 - 450 * percent );
		loadingBg.invalidate();

		if ( renderedPreviousFrame )
			mainStage.act( delta );

		mainStage.draw();

		renderedPreviousFrame = true;
	}


	@Override
	public void pause() {
		renderedPreviousFrame = false;
	}

	@Override
	public void resume() {
	}


	@Override
	public void dispose() {
		mainStage.dispose();
		loadingAssetManager.dispose();
		Pools.get( OverdriveContext.class ).free( context );
	}


	@Override
	public OVDStageManager getStageManager() {
		return stageManager;
	}

	@Override
	public OVDEventManager getEventManager() {
		return eventManager;
	}

	@Override
	public OVDScriptManager getScriptManager() {
		return scriptManager;
	}
}
