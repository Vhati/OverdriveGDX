package com.ftloverdrive.event;


public interface OVDEvent {

	public void setSource( int srcRefId );

	public int getSource();

	public void cancel();

	public boolean isCancelled();
}
