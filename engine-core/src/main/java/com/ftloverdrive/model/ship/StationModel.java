package com.ftloverdrive.model.ship;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;
import com.ftloverdrive.model.ship.Coordinatable;


/**
 * A console to improve a system's performance while manned by crew.
 *
 * For every system, one or more stations are associated with it.
 * Systems that don't involve crew also require a station, but the
 * station can be set to be auto-manned; it won't be visible but it will
 * designate the system's location on the ship.
 *
 * Each station must be assigned to a ShipCoordinate in the ShipLayout.
 * A single room may contain stations for several systems.
 * A system may have multiple stations in any number of rooms.
 *
 * The benefits of having multiple stations manned is for the system to
 * decide.
 *
 * TODO: The original FTL's stations had several different colored glows.
 *   blue = default
 *   yellow = manned by a one-star mastery crewmember
 *   green = manned by a two-star mastery crewmember
 *   none = at least one damaged bar that has not been fully repaired
 */
public interface StationModel extends OVDModel, Coordinatable {

	public NamedProperties getProperties();


	/**
	 * Sets which edge of the floor tile this station should appear at.
	 *
	 * The argument should be OVDConstants.ORIENT_NORTH, etc.
	 * Technically this rotates the graphic at 90 degree intervals.
	 *
	 * @see com.ftloverdrive.util.OVDConstants
	 */
	public void setOrientation( int orientation );
	public int getOrientation();


	/**
	 * Sets the system associated with this station.
	 */
	public void setSystem( int systemModelRefId );
	public int getSystem();


	/**
	 * Toggled whether crew is manning this station.
	 * This will fluctuate as crew come and go.
	 */
	public void setManned( boolean b );

	/**
	 * Returns true if this station is manned by crew, or is auto-manned.
	 */
	public boolean isManned();


	/**
	 * Sets whether this station should be invisible and always considered
	 * manned.
	 */
	public void setAutoManned( boolean b );
	public boolean isAutoManned();
}
