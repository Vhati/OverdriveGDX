package com.ftloverdrive.model;

import com.ftloverdrive.model.GameModelObserver;
import com.ftloverdrive.model.NamedProperties;
import com.ftloverdrive.model.OVDModel;
import com.ftloverdrive.model.ship.ShipModel;


public interface GameModel extends OVDModel {

	public NamedProperties getProperties();


	public void setPlayerShip( ShipModel model );

	public ShipModel getPlayerShip();


	public void addGameModelObserver( GameModelObserver o );

	public void removeGameModelObserver( GameModelObserver o );

	public GameModelObserver[] getGameModelObservers();
}
