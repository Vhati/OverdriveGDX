package com.ftloverdrive.ui.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Logger;

import com.ftloverdrive.event.OVDEventManager;
import com.ftloverdrive.event.TickEvent;
import com.ftloverdrive.event.TickListener;
import com.ftloverdrive.model.DefaultGameModel;
import com.ftloverdrive.model.GameModel;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.model.ship.TestShipModel;
import com.ftloverdrive.script.OVDScriptManager;
import com.ftloverdrive.ui.hud.PlayerShipHullMonitor;
import com.ftloverdrive.ui.screen.OVDStageManager;

import com.ftloverdrive.core.OverdriveGame;


public class TestScreen implements Screen {
	protected static final String MISC_ATLAS = "img/misc/pack.atlas";
	protected static final String PEOPLE_ATLAS = "img/people/pack.atlas";

	private Logger log;
	private TextureAtlas miscAtlas;
	private TextureAtlas peopleAtlas;
	private Map<String,TextureRegion> texturesMap;
	private PixmapPacker packer;
	private Animation walkAnim;
	private SpriteBatch batch;

	private boolean renderedPreviousFrame = false;
	private float elapsed = 0;

	private InputMultiplexer inputMultiplexer;
	private Stage hudStage;

	private OVDStageManager stageManager;
	private OVDEventManager eventManager;
	private OVDScriptManager scriptManager;

	private Sprite driftSprite;
	private PlayerShipHullMonitor playerShipHullMonitor;

	private OverdriveGame game;


	public TestScreen( OverdriveGame game ) {
		this.game = game;
		log = new Logger( TestScreen.class.getCanonicalName(), Logger.DEBUG );

		stageManager = new OVDStageManager();
		eventManager = new OVDEventManager();
		scriptManager = new OVDScriptManager();

		hudStage = new Stage();
		stageManager.putStage( "HUD", hudStage );

		game.getAssetManager().load( MISC_ATLAS, TextureAtlas.class );
		game.getAssetManager().load( PEOPLE_ATLAS, TextureAtlas.class );
		game.getAssetManager().finishLoading();
		miscAtlas = game.getAssetManager().get( MISC_ATLAS, TextureAtlas.class );
		driftSprite = miscAtlas.createSprite( "crosshairs-placed" );

		peopleAtlas = game.getAssetManager().get( PEOPLE_ATLAS, TextureAtlas.class );
		TextureRegion crewRegion = peopleAtlas.findRegion( "human-player-yellow" );

		// FTL's animations.xml counts 0-based rows from the bottom.
		TextureRegion[][] tmpFrames = crewRegion.split( 35, 35 );
		TextureRegion[] walkFrames = new TextureRegion[ 4 ];
		int walkStart = 4;
		for ( int i=0; i < walkFrames.length; i++ ) {
			walkFrames[i] = tmpFrames[0][ walkStart + i ];
		}
		walkAnim = new Animation( .3f, walkFrames );

		batch = new SpriteBatch();

		GameModel gameModel = new DefaultGameModel();
		final ShipModel playerShipModel = new TestShipModel();
		playerShipModel.getProperties().setInt( "HullMax", 40 );
		gameModel.setPlayerShip( playerShipModel );

		playerShipHullMonitor = new PlayerShipHullMonitor();
		playerShipHullMonitor.setPosition( 0, hudStage.getHeight()-playerShipHullMonitor.getHeight() );
		playerShipHullMonitor.setModel( playerShipModel );
		hudStage.addActor( playerShipHullMonitor );

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor( hudStage );

		eventManager.addTickListener(new TickListener() {
			@Override
			public void ticksAccumulated( TickEvent e ) {
				//System.out.println( "Tick ("+ e.getTickCount() +")" );

				int hull = playerShipModel.getProperties().getInt( "Hull" );
				int hullMax = playerShipModel.getProperties().getInt( "HullMax" );
				if ( hull < hullMax ) {
					playerShipModel.getProperties().incrementInt( "Hull", 1 );
				}

				// Eventually a PropertyEvent would have an INC_IF_LESS_THAN flag
				// or something. Or maybe a sentinel event listener - set to watch
				// certain property names - vetoing attempts to increment beyond
				// the names' associated maxes?
			}
		});
	}


	@Override
	public void resize( int width, int height ) {
		hudStage.setViewport( width, height, true );
		// TODO: Re-layout Stages.
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
		eventManager.processEvents();

		Gdx.gl.glClearColor( 0, 0, 0, 0 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		if ( renderedPreviousFrame )
			elapsed += delta;

		batch.begin();
		driftSprite.setPosition( 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed) );
		driftSprite.draw( batch );

		TextureRegion walkFrame = walkAnim.getKeyFrame( elapsed, true );
		batch.draw( walkFrame, 50, 50 );
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
		game.getAssetManager().unload( MISC_ATLAS );
		game.getAssetManager().unload( PEOPLE_ATLAS );
	}
}
