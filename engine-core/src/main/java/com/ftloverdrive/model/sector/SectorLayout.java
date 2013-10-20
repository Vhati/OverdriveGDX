package com.ftloverdrive.model.sector;

import com.badlogic.gdx.utils.IntMap;

import com.ftloverdrive.model.sector.SectorCoordinate;


/**
 * Associates BeaconModels with locations on a sector map.
 */
public class SectorLayout {

	protected IntMap<SectorCoordinate> beaconRefIdToCoordsMap;
	protected SectorCoordinate topRightCoord = null;
	protected int arrivalBeaconRefId = -1;
	protected int exitBeaconRefId = -1;


	public SectorLayout() {
		beaconRefIdToCoordsMap = new IntMap<SectorCoordinate>();
	}


	/**
	 * Defines the top-right corner of the sector.
	 *
	 * This must be called after construction, before adding beacons.
	 */
	public void setSectorSize( SectorCoordinate topRightCoord ) {
		this.topRightCoord = topRightCoord;
	}

	public SectorCoordinate getSectorSize() {
		return topRightCoord;
	}


	/**
	 * Associates a BeaconModel with a SectorCoordinate.
	 */
	public void addBeacon( int beaconModelRefId, SectorCoordinate coord ) {
		beaconRefIdToCoordsMap.put( beaconModelRefId, coord );
	}


	/**
	 * Returns the SectorCoordinate of a beacon.
	 */
	public SectorCoordinate getBeaconCoord( int beaconModelRefId ) {
		return beaconRefIdToCoordsMap.get( beaconModelRefId );
	}


	/**
	 * Returns an iterator for the BeaconModel reference ids.
	 *
	 * Usage:
	 * for ( IntMap.Keys it = layout.getAllRoomRefIds(); it.hasNext; ) {
	 *   int n = it.next();
	 * }
	 */
	public IntMap.Keys beaconRefIds() {
		return beaconRefIdToCoordsMap.keys();
	}


	/**
	 * Sets the reference id of a BeaconModel for ships arriving in this
	 * sector.
	 *
	 * This must be called after construction.
	 */
	public void setArrivalBeaconRefId( int beaconModelRefId ) {
		arrivalBeaconRefId = beaconModelRefId;
	}

	public int getArrivalBeaconRefId() {
		return arrivalBeaconRefId;
	}


	/**
	 * Sets the reference id of a BeaconModel for ships exiting this sector.
	 *
	 * This must be called after construction.
	 */
	public void setExitBeaconRefId( int beaconModelRefId ) {
		exitBeaconRefId = beaconModelRefId;
	}

	public int getExitBeaconRefId() {
		return exitBeaconRefId;
	}
}
