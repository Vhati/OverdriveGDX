package com.ftloverdrive.model.ship;

import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;


public interface ShipModel extends OVDModel {

	public NamedProperties getProperties();


	/**
	 * Returns this ship's layout.
	 */
	public ShipLayout getLayout();


	/**
	 * Shifts *all* graphics (hull images and rooms).
	 *
	 * In Overdrive: +X is right, +Y is up.
	 *
	 * The original FTL set this in shipname.txt.
	 * X_OFFSET*35 + HORIZONTAL
	 * Y_OFFSET*35 + VERTICAL
	 * There, +X was right, +Y was down.
	 */
	public void setShipOffset( float x, float y );
	public float getShipOffsetX();
	public float getShipOffsetY();

	/**
	 * Shifts the hull images only, not rooms.
	 *
	 * In Overdrive: +X is right, +Y is up.
	 *
	 * The cloak image will be shifted to (X-10)x(Y-10) due to scaling.
	 *
	 * The original FTL set this in snipname.xml.
	 * The img tag's x and y attributes behaved similarly.
	 * There, +X was right, +Y was down.
	 */
	public void setHullOffset( float x, float y );
	public float getHullOffsetX();
	public float getHullOffsetY();

	/**
	 * Sets the size of hull images.
	 *
	 * The base image will be scaled to WxH.
	 * The floor image will be scaled to WxH.
	 * The cloak image will be scaled to (W+20)x(H+20).
	 * The shield image will not be scaled.
	 *
	 * The original FTL set this in snipname.xml.
	 * The img tag's w and h attributes behaved similarly.
	 */
	public void setHullSize( float width, float height );
	public float getHullWidth();
	public float getHullHeight();


	/**
	 * Sets a number of things about shields.
	 *
	 * Sets an elliptical region that absorbs incoming projectiles.
	 * Sets an elliptical path for orbiting satellites.
	 * The shield image will be centered on the ellipse's center.
	 * The shield image will not be scaled.
	 *
	 * The x and y args are offsets relative to the hull offset.
	 * When x=0 and y=0, the lower-left corner of the ellipse bounds is at
	 * the hull offset.
	 *
	 * The axes are half the total width/height of the ellipse.
	 *
	 * In Overdrive: +X is right, +Y is up.
	 *
	 * The original FTL set this in shipname.txt.
	 * ELLIPSE
	 * There, +X was right, +Y was down.
	 */
	public void setShieldEllipse( float x, float y, float semiMajorAxis, float semiMinorAxis );
	public float getShieldEllipseOffsetX();
	public float getShieldEllipseOffsetY();
	public float getShieldEllipseSemiMajorAxis();
	public float getShieldEllipseSemiMinorAxis();


	/**
	 * Sets the ship's base image.
	 */
	public void setBaseImageSpec( ImageSpec imageSpec );
	public ImageSpec getBaseImageSpec();

	/**
	 * Sets the ship's cloak image.
	 */
	public void setCloakImageSpec( ImageSpec imageSpec );
	public ImageSpec getCloakImageSpec();

	/**
	 * Sets the ship's floor image.
	 */
	public void setFloorImageSpec( ImageSpec imageSpec );
	public ImageSpec getFloorImageSpec();

	/**
	 * Sets the ship's shield image.
	 */
	public void setShieldImageSpec( ImageSpec imageSpec );
	public ImageSpec getShieldImageSpec();
}
