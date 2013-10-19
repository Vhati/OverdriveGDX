package com.ftloverdrive.model.ship;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.ship.StationModel;
import com.ftloverdrive.util.OVDConstants;


public class DefaultStationModel implements StationModel {

	protected NamedProperties stationProperties;
	protected int orientation = OVDConstants.ORIENT_NORTH;
	protected int systemModelRefId = -1;
	protected boolean manned = false;
	protected boolean autoManned = false;


	public DefaultStationModel() {
		stationProperties = new NamedProperties();
	}


	@Override
	public NamedProperties getProperties() {
		return stationProperties;
	}


	@Override
	public void setOrientation( int orientation ) {
		this.orientation = orientation;
	}

	@Override
	public int getOrientation() {
		return orientation;
	}


	@Override
	public void setSystem( int systemModelRefId ) {
		this.systemModelRefId = systemModelRefId;
	}

	@Override
	public int getSystem() {
		return systemModelRefId;
	}


	@Override
	public void setManned( boolean b ) {
		manned = b;
	}

	@Override
	public boolean isManned() {
		return ( manned || autoManned );
	}


	@Override
	public void setAutoManned( boolean b ) {
		autoManned = b;
	}

	@Override
	public boolean isAutoManned() {
		return autoManned;
	}
}
