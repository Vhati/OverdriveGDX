package com.ftloverdrive.model;

import com.badlogic.gdx.utils.Array;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.ShipModel;


public class DefaultGameModel extends AbstractOVDModel implements GameModel {

	/* A null array to be shared by all models without observers. */
	private final static GameModelObserver[] NULL_GAME_MODEL_OBSERVER_ARRAY = new GameModelObserver[0];

	/** Lazily initialized list of GameModelObservers. */
	protected Array<GameModelObserver> gameModelObservers = null;

	protected NamedProperties gameProperties = new NamedProperties();
	protected int playerShipModelRefId = -1;


	public DefaultGameModel() {
		super();
	}


	/**
	 * Returns a collection of arbitrarily named values.
	 */
	@Override
	public NamedProperties getProperties() {
		return gameProperties;
	}


	@Override
	public void setPlayerShip( int shipModelRefId ) {
		playerShipModelRefId = shipModelRefId;
		fireGamePlayerShipReplaced();
	}

	@Override
	public int getPlayerShip() {
		return playerShipModelRefId;
	}


	@Override
	public void addGameModelObserver( GameModelObserver o ) {
		if ( gameModelObservers == null ) {
			gameModelObservers = new Array<GameModelObserver>( false, 1 );
		}
		gameModelObservers.add( o );
	}

	@Override
	public void removeGameModelObserver( GameModelObserver o ) {
		if ( gameModelObservers == null ) return;
		gameModelObservers.removeValue( o, true );
	}

	@Override
	public GameModelObserver[] getGameModelObservers() {
		if ( gameModelObservers == null ) return NULL_GAME_MODEL_OBSERVER_ARRAY;
		return gameModelObservers.toArray( GameModelObserver.class );
	}

	protected void fireGamePlayerShipReplaced() {
		for ( GameModelObserver o : getGameModelObservers() ) {
			o.gamePlayerShipReplaced( this );
		}
	}
}
