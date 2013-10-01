package com.ftloverdrive.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;


/**
 * A resolver for multiple locations, delegating to resolvers based on URI.
 *
 * When resolving a string that is not a URI, a list of default resolvers
 * are tried until one works.
 *
 * An AssetManager constructed without an explicit resolver will only have
 * an InternalFileHandleResolver.
 *
 * Internal: Path relative to the asset directory on Android and to the
 * application's root directory on the desktop. On the desktop, if the file
 * is not found, then the classpath is checked. Internal files are always
 * readonly.
 *
 * External: Path relative to the root of the SD card on Android and to the
 * home directory of the current user on the desktop.
 *
 * @see com.badlogic.gdx.assets.AssetManager
 * @see com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
 * @see com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver
 * @see com.badlogic.gdx.Files.FileType#External
 * @see com.badlogic.gdx.Files.FileType#Internal
 */
public class URIFileHandleResolver implements FileHandleResolver {
	protected List<FileHandleResolver> defaultMap = new ArrayList<FileHandleResolver>(2);

	protected Map<String,FileHandleResolver> handlerMap = new HashMap<String,FileHandleResolver>(3);
	protected Map<String,Boolean> stripURIMap = new HashMap<String,Boolean>(3);

	protected Pattern uriPtn;


	public URIFileHandleResolver() {
		this.uriPtn = Pattern.compile( "^([a-zA-z_-]+:)//" );
	}


	/**
	 * Sets a resolver to handle a given scheme.
	 *
	 * @param scheme example: "http:"
	 * @param r the resolver, or null to skip
	 * @param stripURIPrefix removes "abc://" if r won't understand URIs
	 */
	public void setResolver( String scheme, FileHandleResolver r, boolean stripURIPrefix ) {
		handlerMap.put( scheme, r );
		stripURIMap.put( scheme, new Boolean(stripURIPrefix) );
	}

	/**
	 * Adds a resolver to use when the string to resolve isn't a URI.
	 * These will be tried, in the order added, until one returns a
	 * non-null FileHandle that exists.
	 */
	public void addDefaultResolver( FileHandleResolver r ) {
		defaultMap.add( r );
	}


	@Override
	public FileHandle resolve( String s ) {
		Matcher m = uriPtn.matcher( s );
		if ( m.find() ) {
			String scheme = m.group(1);
			FileHandleResolver r = handlerMap.get( scheme );
			if ( r != null ) {
				if ( Boolean.TRUE.equals( stripURIMap.get( scheme ) ) ) {
					s = m.replaceAll( "" );
				}
				return r.resolve( s );
			}
			// It was a URI, but no resolvers were relevant.
			return null;
		}
		else {
			// The string was not a URI.
			FileHandle result;
			for ( FileHandleResolver r : defaultMap ) {
				result = r.resolve( s );
				if ( result != null && result.exists() ) {
					return result;
				}
			}
			// None of those worked either.
			return null;
		}
	}
}
