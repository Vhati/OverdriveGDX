package com.ftloverdrive.model;

import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;


public interface PlayerModel extends OVDModel {

	public NamedProperties getProperties();


	/**
	 * Sets the player's name.
	 */
	public void setName( String s );
	public String getName();
}
