package com.ftloverdrive.event;

import com.ftloverdrive.event.OVDEvent;


public abstract class AbstractOVDEvent implements OVDEvent {
	protected int sourceRefId = -1;
	protected boolean cancelled = false;


	public void setSource( int srcRefId ) {
		this.sourceRefId = srcRefId;
	}

	public int getSource() {
		return sourceRefId;
	}

	public void cancel() {
		cancelled = true;
	}

	public boolean isCancelled() {
		return cancelled;
	}
}
