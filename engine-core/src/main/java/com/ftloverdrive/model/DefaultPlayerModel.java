package com.ftloverdrive.model;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.PlayerModel;


public class DefaultPlayerModel implements PlayerModel {

	protected NamedProperties playerProperties;
	protected String playerName = null;


	public DefaultPlayerModel() {
		playerProperties = new NamedProperties();
	}


	@Override
	public NamedProperties getProperties() {
		return playerProperties;
	}


	@Override
	public void setName( String s ) {
		playerName = s;
	}

	@Override
	public String getName() {
		return playerName;
	}
}
