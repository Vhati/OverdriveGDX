package com.ftloverdrive.model.galaxy;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;
import com.ftloverdrive.model.galaxy.GalaxyLayout;


/**
 * A map of all available sectors (aka the sector tree).
 */
public interface GalaxyModel extends OVDModel {

	public NamedProperties getProperties();


	public GalaxyLayout getGalaxyLayout();
}
