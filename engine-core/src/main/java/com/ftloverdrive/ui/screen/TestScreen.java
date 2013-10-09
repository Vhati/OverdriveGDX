package com.ftloverdrive.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.event.TickEvent;
import com.ftloverdrive.event.TickListener;
import com.ftloverdrive.event.game.GamePlayerShipChangeEvent;
import com.ftloverdrive.event.game.GamePlayerShipChangeListener;
import com.ftloverdrive.event.handler.GameEventHandler;
import com.ftloverdrive.event.handler.ShipEventHandler;
import com.ftloverdrive.event.handler.TickEventHandler;
import com.ftloverdrive.event.ship.ShipCreationEvent;
import com.ftloverdrive.event.ship.ShipPropertyEvent;
import com.ftloverdrive.event.ship.ShipPropertyListener;
import com.ftloverdrive.model.DefaultGameModel;
import com.ftloverdrive.model.GameModel;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.model.ship.TestShipModel;
import com.ftloverdrive.script.OVDScriptManager;
import com.ftloverdrive.ui.ShatteredImage;
import com.ftloverdrive.ui.hud.PlayerShipHullMonitor;
import com.ftloverdrive.ui.screen.OVDScreen;
import com.ftloverdrive.ui.screen.OVDStageManager;


public class TestScreen implements Disposable, OVDScreen {
	protected static final String BKG_ATLAS = "img/stars/bg-dullstars-bigcols5.atlas";
	protected static final String ROOT_ATLAS = "img/pack.atlas";
	protected static final String MISC_ATLAS = "img/misc/pack.atlas";
	protected static final String PEOPLE_ATLAS = "img/people/pack.atlas";
	protected static final String PLOT_FONT = "fonts/JustinFont12Bold.ttf?size=13";

	private Logger log;
	private TextureAtlas bgAtlas;
	private TextureAtlas rootAtlas;
	private TextureAtlas miscAtlas;
	private TextureAtlas peopleAtlas;
	private SpriteBatch batch;

	private boolean renderedPreviousFrame = false;
	private float elapsed = 0;

	private OVDStageManager stageManager = null;
	private OVDEventManager eventManager = null;
	private OVDScriptManager scriptManager = null;

	private InputMultiplexer inputMultiplexer;
	private Stage mainStage;
	private Stage hudStage;

	private Sprite driftSprite = null;
	private Animation walkAnim = null;
	private PlayerShipHullMonitor playerShipHullMonitor;

	private OverdriveContext context;


	public TestScreen( OverdriveContext srcContext ) {
		this.context = Pools.get( OverdriveContext.class ).obtain();
		this.context.init( srcContext );
		this.context.setScreen( this );

		log = new Logger( TestScreen.class.getCanonicalName(), Logger.DEBUG );

		stageManager = new OVDStageManager();
		eventManager = new OVDEventManager();
		scriptManager = new OVDScriptManager();

		mainStage = new Stage();
		stageManager.putStage( "Main", mainStage );
		hudStage = new Stage();
		stageManager.putStage( "HUD", hudStage );

		context.getAssetManager().load( BKG_ATLAS, TextureAtlas.class );
		context.getAssetManager().load( ROOT_ATLAS, TextureAtlas.class );
		context.getAssetManager().load( MISC_ATLAS, TextureAtlas.class );
		context.getAssetManager().load( PEOPLE_ATLAS, TextureAtlas.class );
		context.getAssetManager().load( PLOT_FONT, BitmapFont.class );
		context.getAssetManager().finishLoading();

		bgAtlas = context.getAssetManager().get( BKG_ATLAS, TextureAtlas.class );
		ShatteredImage bgImage = new ShatteredImage( bgAtlas.findRegions( "bg-dullstars" ), 5 );
		bgImage.setFillParent( true );
		bgImage.setPosition( 0, 0 );
		mainStage.addActor( bgImage );

		textWindowDemo();

		movingSpriteDemo();

		walkAnimDemo();

		batch = new SpriteBatch();

		GameModel gameModel = new DefaultGameModel();
		int gameRefId = context.getNetManager().requestNewRefId();
		context.getReferenceManager().addObject( gameModel, 0 );
		context.setGameModelRefId( gameRefId );

		playerShipHullMonitor = new PlayerShipHullMonitor( context );
		playerShipHullMonitor.setPosition( 0, hudStage.getHeight()-playerShipHullMonitor.getHeight() );
		hudStage.addActor( playerShipHullMonitor );

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor( hudStage );
		inputMultiplexer.addProcessor( mainStage );


		// Wire up the event manager...
		TickEventHandler tickHandler = new TickEventHandler();
		for ( Class c : tickHandler.getEventClasses() )
			eventManager.setEventHandler( c, tickHandler );

		GameEventHandler gameHandler = new GameEventHandler();
		for ( Class c : gameHandler.getEventClasses() )
			eventManager.setEventHandler( c, gameHandler );

		ShipEventHandler shipHandler = new ShipEventHandler();
		for ( Class c : shipHandler.getEventClasses() )
			eventManager.setEventHandler( c, shipHandler );

		eventManager.addEventListener( playerShipHullMonitor, GamePlayerShipChangeListener.class );
		eventManager.addEventListener( playerShipHullMonitor, ShipPropertyListener.class );

		// When there's a ship, increment its hull after every tick.
		eventManager.addEventListener(new TickListener() {
			@Override
			public void ticksAccumulated( TickEvent e ) {
				//System.out.println( "Tick ("+ e.getTickCount() +")" );

				GameModel gameModel = context.getReferenceManager().getObject( context.getGameModelRefId(), GameModel.class );
				int shipRefId = gameModel.getPlayerShip();
				if ( shipRefId != -1 ) {
					ShipModel shipModel = context.getReferenceManager().getObject( shipRefId, ShipModel.class );
					int hull = shipModel.getProperties().getInt( "Hull" );
					int hullMax = shipModel.getProperties().getInt( "HullMax" );
					if ( hull < hullMax ) {
						ShipPropertyEvent event = Pools.get( ShipPropertyEvent.class ).obtain();
						event.init( shipRefId, ShipPropertyEvent.INT_TYPE, ShipPropertyEvent.INCREMENT_ACTION, "Hull", 1 );
						context.getScreenEventManager().postDelayedEvent( event );
					}
				}

				// Eventually a PropertyEvent would have an INC_IF_LESS_THAN flag
				// or something. Or maybe a sentinel event listener - set to watch
				// certain property names - vetoing attempts to increment beyond
				// the names' associated maxes?
			}
		}, TickListener.class);


		// Create a test ship.
		int shipRefId = context.getNetManager().requestNewRefId();
		ShipCreationEvent shipCreateEvent = Pools.get( ShipCreationEvent.class ).obtain();
		shipCreateEvent.init( shipRefId, "Test" );
		eventManager.postDelayedEvent( shipCreateEvent );

		// Set it as the player's ship.
		GamePlayerShipChangeEvent shipChangeEvent = Pools.get( GamePlayerShipChangeEvent.class ).obtain();
		shipChangeEvent.init( gameRefId, shipRefId );
		eventManager.postDelayedEvent( shipChangeEvent );


		try {
			FileHandleResolver resolver = context.getFileHandleResolver();
			//scriptManager.eval( resolver.resolve( "script.java" ) );
		}
		catch ( Exception e ) {
			log.error( "Error evaluating script.", e );
		}
	}


	private void textWindowDemo() {
		BitmapFont plotFont = context.getAssetManager().get( PLOT_FONT, BitmapFont.class );

		String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, ";
		loremIpsum += "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
		loremIpsum += "\n\nThis window is draggable.";

		rootAtlas = context.getAssetManager().get( ROOT_ATLAS, TextureAtlas.class );
		TextureRegion plotDlgRegion = rootAtlas.findRegion( "box-text1" );
		NinePatchDrawable plotDlgBgDrawable = new NinePatchDrawable( new NinePatch( plotDlgRegion, 20, 20, 35, 20 ) );

		Window plotDlg = new Window( "", new Window.WindowStyle( plotFont, new Color( 1f, 1f, 1f, 1f ), plotDlgBgDrawable ) );
		plotDlg.setKeepWithinStage( true );
		plotDlg.setMovable( true );
		plotDlg.setSize( 200, 250 );
		plotDlg.setPosition( 300, 100 );

		plotDlg.row().top().expand().fill();
		Label plotLbl = new Label( loremIpsum, new Label.LabelStyle( plotFont, new Color( 1f, 1f, 1f, 1f ) ) );
		plotLbl.setAlignment( Align.top|Align.left, Align.center|Align.left );
		plotLbl.setWrap( true );
		plotDlg.add( plotLbl );

		hudStage.addActor( plotDlg );
	}

	private void movingSpriteDemo() {
		miscAtlas = context.getAssetManager().get( MISC_ATLAS, TextureAtlas.class );
		driftSprite = miscAtlas.createSprite( "crosshairs-placed" );
	}

	private void walkAnimDemo() {
		peopleAtlas = context.getAssetManager().get( PEOPLE_ATLAS, TextureAtlas.class );
		TextureRegion crewRegion = peopleAtlas.findRegion( "human-player-yellow" );

		// FTL's animations.xml counts 0-based rows from the bottom.
		TextureRegion[][] tmpFrames = crewRegion.split( 35, 35 );
		TextureRegion[] walkFrames = new TextureRegion[ 4 ];
		int walkStart = 4;
		for ( int i=0; i < walkFrames.length; i++ ) {
			walkFrames[i] = tmpFrames[0][ walkStart + i ];
		}
		walkAnim = new Animation( .3f, walkFrames );
	}


	@Override
	public void resize( int width, int height ) {
		hudStage.setViewport( width, height, true );
		// TODO: Re-layout Stages.

		// SpriteBatches get resized to match the new aspect ratio,
		// need to counteract this.
		// http://stackoverflow.com/questions/14085212/libgdx-framebuffer-scaling

		playerShipHullMonitor.setPosition( 0, hudStage.getHeight()-playerShipHullMonitor.getHeight() );
		Matrix4 matrix = new Matrix4();
		matrix.setToOrtho2D( 0, 0, width, height );
		batch.setProjectionMatrix( matrix );
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor( inputMultiplexer );
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor( null );
		renderedPreviousFrame = false;
	}


	@Override
	public void render( float delta ) {
		if ( renderedPreviousFrame )
			eventManager.secondsElapsed( delta );
		eventManager.processEvents( context );

		Gdx.gl.glClearColor( 0, 0, 0, 0 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		if ( renderedPreviousFrame )
			elapsed += delta;

		mainStage.act( Gdx.graphics.getDeltaTime() );
		mainStage.draw();


		batch.begin();
		if ( driftSprite != null ) {
			driftSprite.setPosition( 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed) );
			driftSprite.draw( batch );
		}

		if ( walkAnim != null ) {
			TextureRegion walkFrame = walkAnim.getKeyFrame( elapsed, true );
			batch.draw( walkFrame, 50, 50 );
		}
		batch.end();


		hudStage.act( Gdx.graphics.getDeltaTime() );
		hudStage.draw();

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
		hudStage.dispose();
		playerShipHullMonitor.dispose();
		context.getAssetManager().unload( BKG_ATLAS );
		context.getAssetManager().unload( ROOT_ATLAS );
		context.getAssetManager().unload( MISC_ATLAS );
		context.getAssetManager().unload( PEOPLE_ATLAS );
		context.getAssetManager().unload( PLOT_FONT );
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
