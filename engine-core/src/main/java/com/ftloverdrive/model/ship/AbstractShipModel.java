package com.ftloverdrive.model.ship;

import com.ftloverdrive.io.ImageSpec;
import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.ShipLayout;
import com.ftloverdrive.model.ship.ShipModel;
import com.ftloverdrive.util.OVDConstants;


public class AbstractShipModel extends AbstractOVDModel implements ShipModel {

	protected NamedProperties shipProperties = new NamedProperties();

	protected ShipLayout shipLayout;

	protected float shipOffsetX = 0f;
	protected float shipOffsetY = 0f;
	protected float hullOffsetX = 0f;
	protected float hullOffsetY = 0f;
	protected float hullWidth = 0f;
	protected float hullHeight = 0f;

	protected float shieldEllipseOffsetX = 0f;
	protected float shieldEllipseOffsetY = 0f;
	protected float shieldEllipseSemiMajorAxis = 0f;
	protected float shieldEllipseSemiMinorAxis = 0f;

	protected ImageSpec baseImageSpec = null;
	protected ImageSpec cloakImageSpec = null;
	protected ImageSpec floorImageSpec = null;
	protected ImageSpec shieldImageSpec = null;


	public AbstractShipModel() {
		super();
		shipProperties.setInt( OVDConstants.HULL_MAX, 0 );
		shipProperties.setInt( OVDConstants.HULL, 0 );
		shipProperties.setInt( OVDConstants.SCRAP, 0 );
		shipProperties.setInt( OVDConstants.FUEL, 0 );
		shipProperties.setInt( OVDConstants.MISSILES, 0 );
		shipProperties.setInt( OVDConstants.DRONE_PARTS, 0 );
		shipLayout = new ShipLayout();
	}


	/**
	 * Returns a collection of arbitrarily named values.
	 */
	@Override
	public NamedProperties getProperties() {
		return shipProperties;
	}


	@Override
	public ShipLayout getLayout() {
		return shipLayout;
	}


	@Override
	public void setShipOffset( float x, float y ) {
		shipOffsetX = x;
		shipOffsetY = y;
	}

	@Override
	public float getShipOffsetX() {
		return shipOffsetX;
	}

	@Override
	public float getShipOffsetY() {
		return shipOffsetY;
	}


	@Override
	public void setHullOffset( float x, float y ) {
		hullOffsetX = x;
		hullOffsetY = y;
	}

	@Override
	public float getHullOffsetX() {
		return hullOffsetX;
	}

	@Override
	public float getHullOffsetY() {
		return hullOffsetY;
	}

	@Override
	public void setHullSize( float width, float height ) {
		hullWidth = width;
		hullHeight = height;
	}

	@Override
	public float getHullWidth() {
		return hullWidth;
	}

	@Override
	public float getHullHeight() {
		return hullHeight;
	}


	@Override
	public void setShieldEllipse( float x, float y, float semiMajorAxis, float semiMinorAxis ) {
		shieldEllipseOffsetX = x;
		shieldEllipseOffsetY = y;
		shieldEllipseSemiMajorAxis = semiMajorAxis;
		shieldEllipseSemiMinorAxis = semiMinorAxis;
	}

	@Override
	public float getShieldEllipseOffsetX() {
		return shieldEllipseOffsetX;
	}

	@Override
	public float getShieldEllipseOffsetY() {
		return shieldEllipseOffsetY;
	}

	@Override
	public float getShieldEllipseSemiMajorAxis() {
		return shieldEllipseSemiMajorAxis;
	}

	@Override
	public float getShieldEllipseSemiMinorAxis() {
		return shieldEllipseSemiMinorAxis;
	}


	@Override
	public void setBaseImageSpec( ImageSpec imageSpec ) {
		baseImageSpec = imageSpec;
	}

	@Override
	public ImageSpec getBaseImageSpec() {
		return baseImageSpec;
	}


	@Override
	public void setCloakImageSpec( ImageSpec imageSpec ) {
		cloakImageSpec = imageSpec;
	}

	@Override
	public ImageSpec getCloakImageSpec() {
		return cloakImageSpec;
	}


	@Override
	public void setFloorImageSpec( ImageSpec imageSpec ) {
		floorImageSpec = imageSpec;
	}

	@Override
	public ImageSpec getFloorImageSpec() {
		return floorImageSpec;
	}


	@Override
	public void setShieldImageSpec( ImageSpec imageSpec ) {
		shieldImageSpec = imageSpec;
	}

	@Override
	public ImageSpec getShieldImageSpec() {
		return shieldImageSpec;
	}
}
