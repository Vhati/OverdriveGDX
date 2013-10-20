package com.ftloverdrive.net;

import com.badlogic.gdx.utils.Array;


public class OVDNetManager {

	protected Array<Range> idRanges;
	protected int nextId = 0;
	protected int localPlayerModelRefId = -1;


	public OVDNetManager() {
		idRanges = new Array<Range>( true, 1 );
		idRanges.add( new Range( 0, Integer.MAX_VALUE ) );  // Default range.
	}


	/**
	 * Sets a range of assignable reference ids, clearing all others.
	 * The next id will be the start of this range.
	 *
	 * @param start  the start of the range, inclusive
	 * @param start  the end of the range, exclusive
	 */
	public void setRefIdRange( int start, int end ) {
		idRanges.clear();
		idRanges.add( new Range( start, end ) );
		nextId = start;
	}

	/**
	 * Adds an additional range of assignable reference ids.
	 *
	 * @param start  the start of the range, inclusive
	 * @param start  the end of the range, exclusive
	 */
	public void addRefIdRange( int start, int end ) {
		idRanges.add( new Range( start, end ) );
	}

	/**
	 * Reserves and returns the next available reference id.
	 *
	 * TODO: Have the server pre-assign each player a different large range
	 * of ids. When this is called, get the next id from the range; if that's
	 * been exhausted, make a synchronous RMI call to fetch a new range of
	 * ids.
	 */
	public int requestNewRefId() {
		while ( idRanges.size > 0 && nextId >= idRanges.get( 0 ).end ) {
			idRanges.removeIndex( 0 );
		}
		if ( idRanges.size == 0 ) {
			// TODO: Make a synchronous RMI call to fetch a new range.
		}
		return nextId++;
	}


	/**
	 * Sets the reference id for the local PlayerModel.
	 */
	public void setLocalPlayerRefId( int playerModelRefId ) {
		localPlayerModelRefId = playerModelRefId;
	}

	public int getLocalPlayerRefId() {
		return localPlayerModelRefId;
	}



	public static class Range {
		public int start = 0;
		public int end = 0;

		public Range( int start, int end ) {
			this.start = start;
			this.end = end;
		}

		public Range() {
		}
	}
}
