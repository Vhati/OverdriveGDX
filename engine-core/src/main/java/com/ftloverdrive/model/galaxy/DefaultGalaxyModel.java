package com.ftloverdrive.model.galaxy;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;
import com.ftloverdrive.model.galaxy.GalaxyLayout;


public class DefaultGalaxyModel implements GalaxyModel {

	protected NamedProperties galaxyProperties;
	protected GalaxyLayout galaxyLayout;


	public DefaultGalaxyModel() {
		galaxyProperties = new NamedProperties();
		galaxyLayout = new GalaxyLayout();
	}


	@Override
	public NamedProperties getProperties() {
		return galaxyProperties;
	}


	@Override
	public GalaxyLayout getGalaxyLayout() {
		return galaxyLayout;
	}
}
