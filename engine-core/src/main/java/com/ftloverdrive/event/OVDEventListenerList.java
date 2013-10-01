package com.ftloverdrive.event;

import java.lang.reflect.Array;

import com.ftloverdrive.event.OVDEventListener;


/**
 * A class that holds a list of OVDEventListeners.
 *
 * Copied from javax.swing.event.EventListenerList.
 */
public class OVDEventListenerList {
	/* A null array to be shared by all empty listener lists. */
	private final static Object[] NULL_ARRAY = new Object[0];


	/* The list of ListenerType - Listener pairs. */
	protected transient Object[] listenerList = NULL_ARRAY;

	public Object[] getListenerList() {
		return listenerList;
	}

	@SuppressWarnings("unchecked")
	public <T extends OVDEventListener> T[] getListeners( Class<T> t ) {
		Object[] lList = listenerList;
		int n = getListenerCount( lList, t );
		T[] result = (T[])Array.newInstance( t, n );
		int j = 0;
		for ( int i = lList.length-2; i >= 0; i-=2 ) {
			if ( lList[i] == t ) {
				result[j++] = (T)lList[i+1];
			}
		}
		return result;
	}

	public int getListenerCount() {
		return listenerList.length/2;
	}

	public int getListenerCount( Class<?> t ) {
		Object[] lList = listenerList;
		return getListenerCount( lList, t );
	}

	private int getListenerCount( Object[] list, Class t ) {
		int count = 0;
		for ( int i=0; i < list.length; i+=2 ) {
			if ( t == (Class)list[i] )
				count++;
		}
		return count;
	}

	public synchronized <T extends OVDEventListener> void add( Class<T> t, T l ) {
		if ( l == null ) {
			// In an ideal world, we would do an assertion here
			// to help developers know they are probably doing
			// something wrong.
			return;
		}
		if ( !t.isInstance(l) ) {
			throw new IllegalArgumentException( "Listener "+ l +" is not of type "+ t );
		}
		if ( listenerList == NULL_ARRAY ) {
			// If this is the first listener added,
			// initialize the lists.
			listenerList = new Object[] { t, l };
		} else {
			// Otherwise copy the array and add the new listener.
			int i = listenerList.length;
			Object[] tmp = new Object[i+2];
			System.arraycopy( listenerList, 0, tmp, 0, i );

			tmp[i] = t;
			tmp[i+1] = l;

			listenerList = tmp;
		}
	}

	public synchronized <T extends OVDEventListener> void remove( Class<T> t, T l ) {
		if ( l ==null ) {
			// In an ideal world, we would do an assertion here
			// to help developers know they are probably doing
			// something wrong.
			return;
		}
		if ( !t.isInstance(l) ) {
			throw new IllegalArgumentException( "Listener "+ l +" is not of type "+ t );
		}
		// Is l on the list?
		int index = -1;
		for ( int i = listenerList.length-2; i >= 0; i-=2 ) {
			if ( (listenerList[i]==t) && (listenerList[i+1].equals(l) == true) ) {
				index = i;
				break;
				}
			}
	
			// If so,  remove it
			if ( index != -1 ) {
			Object[] tmp = new Object[listenerList.length-2];
			// Copy the list up to index.
			System.arraycopy( listenerList, 0, tmp, 0, index );
			// Copy from two past the index, up to
			// the end of tmp (which is two elements
			// shorter than the old list).
			if ( index < tmp.length )
				System.arraycopy( listenerList, index+2, tmp, index, tmp.length - index );
			// set the listener array to the new array or null.
			listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
		}
	}
}
