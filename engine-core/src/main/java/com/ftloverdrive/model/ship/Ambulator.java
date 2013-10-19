package com.ftloverdrive.model.ship;

import com.ftloverdrive.model.ship.Coordinatable;
import com.ftloverdrive.model.ship.ShipCoordinate;


/**
 * Something that can navigate a ship's floorplan.
 */
public interface Ambulator extends Coordinatable {

	/**
	 * Returns the ideal movement speed.
	 *
	 * Depending on the angle of movement, this will be broken up into
	 * X and Y components. And those will be capped when near enough to the
	 * next waypoint that a full speed interval would overshoot it.
	 *
	 * TODO: Write travel calculations so the server and client's UI can
	 * compute progress, so they'll agree on arrival when the server finally
	 * sets the waypoint as the current position.
	 */
	public float getAmbulationSpeed();

	/**
	 * Sets an ultimate goal coordinate to travel toward, or null.
	 *
	 * Pathfinding algorithms will incrementally determine each waypoint and
	 * begin moving.
	 */
	public void setAmbulationGoal( ShipCoordinate coord );
	public ShipCoordinate getAmbulationGoal();


	/** Returns true if this is currently moving toward a destination. */
	public boolean isAmbulating();


	/**
	 * Returns the pending coordinate if moving, null otherwise.
	 *
	 * The UI should show an animation of travel between the current
	 * coordinate and the next.
	 */
	public ShipCoordinate getNextWaypoint();
}
