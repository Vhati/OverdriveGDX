package com.ftloverdrive.model.galaxy;

import com.badlogic.gdx.utils.IntMap;

import com.ftloverdrive.model.galaxy.GalaxyCoordinate;


/**
 * Associates SectorModels with locations on a galaxy map.
 */
public class GalaxyLayout {

	protected IntMap<GalaxyCoordinate> sectorRefIdToCoordsMap;
	protected GalaxyCoordinate topRightCoord = null;


	public GalaxyLayout() {
		sectorRefIdToCoordsMap = new IntMap<GalaxyCoordinate>();
	}


	/**
	 * Defines the top-right corner of the sector.
	 *
	 * This must be called after construction, before adding beacons.
	 */
	public void setGalaxySize( GalaxyCoordinate topRightCoord ) {
		this.topRightCoord = topRightCoord;
	}

	public GalaxyCoordinate getSectorSize() {
		return topRightCoord;
	}


	/**
	 * Associates a SectorModel with a GalaxyCoordinate.
	 */
	public void addSector( int sectorModelRefId, GalaxyCoordinate coord ) {
		sectorRefIdToCoordsMap.put( sectorModelRefId, coord );
	}


	/**
	 * Returns the GalaxyCoordinate of a sector.
	 */
	public GalaxyCoordinate getBeaconCoord( int sectorModelRefId ) {
		return sectorRefIdToCoordsMap.get( sectorModelRefId );
	}


	/**
	 * Returns an iterator for the SectorModel reference ids.
	 *
	 * Usage:
	 * for ( IntMap.Keys it = layout.getAllRoomRefIds(); it.hasNext; ) {
	 *   int n = it.next();
	 * }
	 */
	public IntMap.Keys sectorRefIds() {
		return sectorRefIdToCoordsMap.keys();
	}
}
