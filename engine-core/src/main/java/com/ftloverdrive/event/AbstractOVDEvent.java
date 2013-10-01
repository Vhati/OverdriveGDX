package com.ftloverdrive.event;

import com.ftloverdrive.event.OVDEvent;


public abstract class AbstractOVDEvent implements OVDEvent {
	protected Object source = null;
	protected boolean cancelled = false;


	public void setSource( Object source ) {
		this.source = source;
	}

	public Object getSource() {
		return source;
	}

	public void cancel() {
		cancelled = true;
	}

	public boolean isCancelled() {
		return cancelled;
	}
}
