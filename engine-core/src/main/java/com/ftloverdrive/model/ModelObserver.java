package com.ftloverdrive.model;

import com.ftloverdrive.model.OVDModel;


/**
 * An interface for objects that passively observe OVDModels.
 */
public interface ModelObserver {

	public void modelChanged( OVDModel source, Object hint );

	public void nestedModelAdded( OVDModel source, OVDModel child, Object hint );

	public void nestedModelRemoved( OVDModel source, OVDModel child, Object hint );
}
