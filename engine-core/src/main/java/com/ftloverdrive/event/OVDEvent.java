package com.ftloverdrive.event;


public interface OVDEvent {

	public void setSource( Object source );

	public Object getSource();

	public void cancel();

	public boolean isCancelled();
}
