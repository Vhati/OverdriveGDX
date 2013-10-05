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
 *
 * TODO: Maybe coalesce repeat events when posting, like in
 * java.awt.EventQueue?
 */
public class OVDEventManager {

	private Deque<OVDEvent> outQueue = new LinkedBlockingDeque<OVDEvent>();
	private Deque<OVDEvent> inQueue = new LinkedBlockingDeque<OVDEvent>();
	OVDEventListenerList outListenerList = new OVDEventListenerList();
	OVDEventListenerList inListenerList = new OVDEventListenerList();

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
		while ( (event = inQueue.poll()) != null ) {
			if ( event instanceof TickEvent ) {
				processTickEvent( (TickEvent)event );
			}
		}
		while ( (event = outQueue.poll()) != null ) {
			// TODO: A registry of handlers to dispatch various event types.
			// Otherwise this will just be a hardcoded if/else instance check.

			if ( !event.isCancelled() ) {
				// Pretend it went to the server and back.
				postDelayedInboundEvent( event );
			}
		}
	}


	/**
	 * Adds an event to the end of the inbound queue. (thread-safe)
	 * This should not normally be used.
	 */
	public void postDelayedInboundEvent( OVDEvent e ) {
		inQueue.addLast( e );
	}

	/**
	 * Adds an event to the end of the outbound queue. (thread-safe)
	 */
	public void postDelayedEvent( OVDEvent e ) {
		outQueue.addLast( e );
	}

	/**
	 * Adds an event to the start of the outbound queue. (thread-safe)
	 */
	public void postPreemptiveEvent( OVDEvent e ) {
		outQueue.addFirst( e );
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

			// Pretend the server issued this event.
			postDelayedInboundEvent( tickEvent );
		}
	}

	public void addTickListener( TickListener l ) {
		inListenerList.add( TickListener.class, l );
	}

	public void removeTickListener( TickListener l ) {
		inListenerList.remove( TickListener.class, l );
	}

	protected void processTickEvent( TickEvent event ) {
		Object[] listeners = inListenerList.getListenerList();
		for ( int i = listeners.length-2; i >= 0; i-=2 ) {
			if ( listeners[i] == TickListener.class ) {
				((TickListener)listeners[i+1]).ticksAccumulated( event );
			}
		}
		tickEventPool.free( event );
	}
}
