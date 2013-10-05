package com.ftloverdrive.event;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.Deque;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.event.OVDEvent;
import com.ftloverdrive.event.OVDEventListenerList;
import com.ftloverdrive.event.TickEvent;
import com.ftloverdrive.event.TickListener;


/**
 * Queues and dispatches OVDEvents.
 */
public class OVDEventManager {

	private Deque<OVDEvent> eventQueue = new LinkedBlockingDeque<OVDEvent>();
	OVDEventListenerList listenerList = new OVDEventListenerList();

	private final int tickRate = 1000;  // Milliseconds per tick of game-time.
	private int spareTime = 0;          // Remembers leftover milliseconds between ticks.

	private int elapsedTicks;           // Micro-optimization to avoid redeclaring a variable.
	private final Pool<TickEvent> tickEventPool;


	public OVDEventManager() {
		tickEventPool = Pools.get( TickEvent.class );
	}


	/**
	 * Dispatches any pending events and returns.
	 */
	public void processEvents() {
		OVDEvent event;
		while ( (event = eventQueue.poll()) != null ) {
			if ( event instanceof TickEvent ) {
				processTickEvent( (TickEvent)event );
			}
		}
	}


	/**
	 * Adds an event to the end of the queue. (thread-safe)
	 */
	public void postDelayedEvent( OVDEvent e ) {
		// TODO: Maybe coalesce repeat events, like in java.awt.EventQueue?

		eventQueue.addLast( e );
	}

	/**
	 * Adds an event to the start of the queue. (thread-safe)
	 */
	public void postPreemptiveEvent( OVDEvent e ) {
		// TODO: Maybe coalesce repeat events, like in java.awt.EventQueue?

		eventQueue.addFirst( e );
	}


	/**
	 * Signals that real-world time has elapsed since the last call.
	 *
	 * As enough time accumulates, TickEvents may be generated.
	 */
	public void secondsElapsed( float t ) {
		spareTime += (int)(t * 1000);  // Add as milliseconds.
		elapsedTicks = spareTime / tickRate;
		if ( elapsedTicks > 0 ) {
			spareTime = spareTime % tickRate;
			TickEvent tickEvent = tickEventPool.obtain();
			tickEvent.init();
			tickEvent.setTickCount( elapsedTicks );
			postDelayedEvent( tickEvent );
		}
	}

	public void addTickListener( TickListener l ) {
		listenerList.add( TickListener.class, l );
	}

	public void removeTickListener( TickListener l ) {
		listenerList.remove( TickListener.class, l );
	}

	protected void processTickEvent( TickEvent event ) {
		Object[] listeners = listenerList.getListenerList();
		for ( int i = listeners.length-2; i >= 0; i-=2 ) {
			if ( listeners[i] == TickListener.class ) {
				((TickListener)listeners[i+1]).ticksAccumulated( event );
			}
		}
		tickEventPool.free( event );
	}
}
