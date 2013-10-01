package com.ftloverdrive.model;

import com.ftloverdrive.model.ModelObserver;
import com.ftloverdrive.model.OVDModel;


/**
 * A live entity in the game, usually constructed from an OVDBlueprint.
 */
public interface OVDModel {

	public void addModelObserver( ModelObserver o );

	public void removeModelObserver( ModelObserver o );

	public ModelObserver[] getModelObservers();
}
