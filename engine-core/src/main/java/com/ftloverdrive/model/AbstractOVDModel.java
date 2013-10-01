package com.ftloverdrive.model;

import com.badlogic.gdx.utils.Array;

import com.ftloverdrive.model.OVDModel;


/**
 * A live entity in the game, usually constructed from an OVDBlueprint.
 */
public abstract class AbstractOVDModel implements OVDModel {

	/* A null array to be shared by all models without observers. */
	private final static ModelObserver[] NULL_MODEL_OBSERVER_ARRAY = new ModelObserver[0];

	/** Lazily initialized list of ModelObservers. */
	protected Array<ModelObserver> modelObservers = null;


	@Override
	public void addModelObserver( ModelObserver o ) {
		if ( modelObservers == null ) {
			modelObservers = new Array<ModelObserver>( false, 1 );
		}
		modelObservers.add( o );
	}

	@Override
	public void removeModelObserver( ModelObserver o ) {
		if ( modelObservers == null ) return;
		modelObservers.removeValue( o, true );
	}

	@Override
	public ModelObserver[] getModelObservers() {
		if ( modelObservers == null ) return NULL_MODEL_OBSERVER_ARRAY;
		return modelObservers.toArray( ModelObserver.class );
	}

	/**
	 * Directly notifies observers that something changed, with an optional hint.
	 */
	protected void fireModelChanged( Object hint ) {
		for ( ModelObserver o : getModelObservers() ) {
			o.modelChanged( this, hint );
		}
	}
}
