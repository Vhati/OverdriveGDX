package com.ftloverdrive.event;

import com.ftloverdrive.event.TickEvent;


public interface TickListener extends OVDEventListener {

	/**
	 * A number of units of game-time have elapsed.
	 */
	public void ticksAccumulated( TickEvent e );
}
