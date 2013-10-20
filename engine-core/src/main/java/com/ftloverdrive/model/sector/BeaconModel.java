package com.ftloverdrive.model.sector;

import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;


/**
 * A beacon in a sector.
 */
public interface BeaconModel extends OVDModel {

	public NamedProperties getProperties();


	/**
	 * Sets the full-screen background image when at this beacon, or null.
	 */
	public void setStarscapeImageSpec( ImageSpec imageSpec );
	public ImageSpec getStarscapeImageSpec();


	/**
	 * Sets a small image to float in front of the starscape, or null.
	 */
	public void setAccentImageSpec( ImageSpec imageSpec );
	public ImageSpec getAccentImageSpec();

	/**
	 * Sets the accent image's position.
	 *
	 * The image's center will be at (x,y).
	 * x=0.0 is the left edge of the screen.
	 * x=1.0 is the right edge of the screen.
	 * y=0.0 is the bottom edge of the screen.
	 * y=1.0 is the top edge of the screen.
	 */
	public void setAccentImagePosition( float x, float y );
	public float getAccentImageX();
	public float getAccentImageY();

	/**
	 * Sets the accent image's rotation (positive = clockwise).
	 */
	public void setAccentImageRotation( int degrees );
	public int getAccentImageRotation();


	/**
	 * Sets the identifier to create an Incident upon arrival, or null.
	 */
	public void setArrivalIncidentId( String incidentId );
	public String getArrivalIncidentId();
}
