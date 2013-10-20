package com.ftloverdrive.model.sector;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;
import com.ftloverdrive.model.sector.SectorLayout;


public interface SectorModel extends OVDModel {

	public NamedProperties getProperties();


	/**
	 * Sets the identifier to create an Incident upon arrival, or null.
	 */
	public void setArrivalIncidentId( String incidentId );
	public String getArrivalIncidentId();


	/**
	 * Sets the identifier to create an Incident upon exiting, or null.
	 */
	public void setExitIncidentId( String incidentId );
	public String getExitIncidentId();


	public SectorLayout getSectorLayout();
}
