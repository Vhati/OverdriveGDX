package com.ftloverdrive.event;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.event.OVDEvent;


public interface OVDEventHandler {

	/**
	 * Returns event classes that can be handled.
	 */
	public Class[] getEventClasses();

	/**
	 * Returns listener classes that can be notified.
	 */
	public Class[] getListenerClasses();


	/**
	 * Acts on a received event and/or notifies listeners.
	 */
	public void handle( OverdriveContext context, OVDEvent e, Object[] listeners );

	/**
	 * Optionally frees an event, if poolable.
	 */
	public void disposeEvent( OVDEvent e );
}
