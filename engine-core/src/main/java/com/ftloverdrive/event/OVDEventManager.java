package com.ftloverdrive.event;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import com.ftloverdrive.core.OverdriveContext;
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
	Map<Class,OVDEventHandler> handlerMap = new HashMap<Class,OVDEventHandler>();

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
	public void processEvents( OverdriveContext context ) {
		OVDEvent event;
		while ( (event = inQueue.poll()) != null ) {
			OVDEventHandler h = handlerMap.get( event.getClass() );
			if ( h != null ) {
				h.handle( context, event, inListenerList.getListenerList() );
				h.disposeEvent( event );
			}
			else {
				//System.out.println( "Unhandled event: "+ event );
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


	/**
	 * Sets the handler for a specific event class.
	 */
	public void setEventHandler( Class eventClass, OVDEventHandler h ) {
		handlerMap.put( eventClass, h );
	}

	/**
	 * Adds a listener for incoming events.
	 *
	 * @param l  a listener to be notified
	 * @param listenerClass  a class a handler expects that the listener can be cast as
	 */
	public <T extends OVDEventListener> void addEventListener( T l, Class<T> listenerClass ) {
		inListenerList.add( listenerClass, l );
	}
}
