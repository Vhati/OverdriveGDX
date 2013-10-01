package com.ftloverdrive.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.utils.Array;

import com.ftloverdrive.util.MutableInt;


/**
 * A collection of arbitrarily named values.
 */
public class NamedProperties {

	public static final int INT_TYPE = 0;

	/* A null array to be shared by all instances without observers. */
	private final static PropertiesObserver[] NULL_PROPERTIES_OBSERVER_ARRAY = new PropertiesObserver[0];


	/** Lazily initialized map of named integers. */
	protected Map<String,MutableInt> namedIntMap = null;

	/** Lazily initialized map of named integers' keys. */
	protected Set<String> namedIntKeysView = null;

	/** Lazily initialized list of PropertiesObservers. */
	protected Array<PropertiesObserver> propertiesObservers = null;


	/**
	 * Sets a named integer variable.
	 */
	public void setInt( String key, int n ) {
		MutableInt value = null;
		if ( namedIntMap == null ) {
			namedIntMap = new HashMap<String,MutableInt>();
		} else {
			value = namedIntMap.get( key );
		}
		if ( value == null ) {
			value = new MutableInt();
			namedIntMap.put( key, value );
		}

		value.set( n );
		firePropertyChanged( INT_TYPE, key );
	}

	/**
	 * Adds an amount to a named integer variable.
	 */
	public void incrementInt( String key, int n ) {
		MutableInt value = null;
		if ( namedIntMap == null ) {
			namedIntMap = new HashMap<String,MutableInt>();
		} else {
			value = namedIntMap.get( key );
		}
		if ( value == null ) {
			value = new MutableInt();
			namedIntMap.put( key, value );
		}

		value.increment( n );
		firePropertyChanged( INT_TYPE, key );
	}

	/**
	 * Returns the value of a named integer variable, or 0 if not set.
	 */
	public int getInt( String key ) {
		if ( namedIntMap == null ) return 0;
		MutableInt value = namedIntMap.get( key );
		if ( value == null ) return 0;
		return value.get();
	}

	/**
	 * Returns true if a named integer variable has been set.
	 */
	public boolean hasInt( String key ) {
		if ( namedIntMap == null ) return false;
		MutableInt value = namedIntMap.get( key );
		if ( value == null ) return false;
		return true;
	}

	/**
	 * Returns a read-only view of named integer variables' keys.
	 */
	public Set<String> getIntKeys() {
		if ( namedIntKeysView == null ) {
			namedIntKeysView = Collections.unmodifiableSet( namedIntMap.keySet() );
		}
		return namedIntKeysView;
	}


	public void addPropertiesObserver( PropertiesObserver o ) {
		if ( propertiesObservers == null ) {
			propertiesObservers = new Array<PropertiesObserver>( false, 1 );
		}
		propertiesObservers.add( o );
	}

	public void removePropertiesObserver( PropertiesObserver o ) {
		if ( propertiesObservers == null ) return;
		propertiesObservers.removeValue( o, true );
	}

	public PropertiesObserver[] getPropertiesObservers() {
		if ( propertiesObservers == null ) return NULL_PROPERTIES_OBSERVER_ARRAY;
		return propertiesObservers.toArray( PropertiesObserver.class );
	}

	/**
	 * Directly notifies observers that something changed.
	 * 
	 */
	protected void firePropertyChanged( int type, String key ) {
		for ( PropertiesObserver o : getPropertiesObservers() ) {
			o.propertyChanged( this, type, key );
		}
	}
}
