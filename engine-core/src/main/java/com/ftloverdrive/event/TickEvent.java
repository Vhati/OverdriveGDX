package com.ftloverdrive.event;

import com.badlogic.gdx.utils.Pool.Poolable;

import com.ftloverdrive.event.OVDEvent;


public class TickEvent extends AbstractOVDEvent implements Poolable {
	protected int tickCount = 0;


	/**
	 * This constructor must take no args, and only set up variables.
	 * Call init() afterward, as if that were the normal constructor.
	 *
	 * @see com.badlogic.gdx.utils.ReflectionPool
	 */
	public TickEvent() {
		super();
	}

	public void incrementTickCount( int n ) {
		tickCount += n;
	}

	public void setTickCount( int n ) {
		tickCount = n;
	}

	public int getTickCount() {
		return tickCount;
	}


	/**
	 * Resets this object for Pool reuse, setting all variables to defaults.
	 * This is automatically called by the Pool's free() method.
	 *
	 * @see com.badlogic.gdx.utils.Pool
	 * @see com.badlogic.gdx.utils.Pool.Poolable
	 */
	@Override
	public void reset() {
		tickCount = 0;
	}

	/**
	 * Pseudo constructor for new and reused objects.
	 * This is capable of accepting args to set initial values.
	 *
	 * Always call this after obtaining a reused object from a Pool.
	 * Subclasses overriding this must call super.init();
	 */
	public void init() {
	}
}
