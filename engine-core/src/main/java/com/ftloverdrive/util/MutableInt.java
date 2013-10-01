package com.ftloverdrive.util;


/**
 * An object that wraps an int.
 */
public class MutableInt {
	protected int n = 0;

	public void set( int n ) {
		this.n = n;
	}

	public void increment( int n ) {
		this.n += n;
	}

	public int get() {
		return n;
	}
}
