package com.ftloverdrive.model;

import com.ftloverdrive.model.GameModel;


/**
 * An interface for objects that passively observe GameModel.
 */
public interface GameModelObserver {

	public void gamePlayerShipReplaced( GameModel source );
}
