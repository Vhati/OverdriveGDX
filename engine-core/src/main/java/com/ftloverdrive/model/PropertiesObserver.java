package com.ftloverdrive.model;

import com.ftloverdrive.model.NamedProperties;


/**
 * An interface for objects that passively observe NamedProperties.
 */
public interface PropertiesObserver {

	public void propertyChanged( NamedProperties source, int type, String key );
}
