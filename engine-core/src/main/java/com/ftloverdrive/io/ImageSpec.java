package com.ftloverdrive.io;


/**
 * A pointer to an atlas and region.
 */
public class ImageSpec {
	protected String atlasPath = null;
	protected String regionName = null;


	public ImageSpec( String atlasPath, String regionName ) {
		this.atlasPath = atlasPath;
		this.regionName = regionName;
	}

	public ImageSpec() {
	}


	public String getAtlasPath() {
		return atlasPath;
	}

	public String getRegionName() {
		return regionName;
	}

	@Override
	public boolean equals( Object o ) {
		if ( o instanceof ImageSpec == false ) return false;
		ImageSpec other = (ImageSpec)o;

		if ( atlasPath == null ) {
			if ( other.getAtlasPath() != null ) return false;
		} else {
			if ( !atlasPath.equals( other.getAtlasPath() ) ) return false;
		}

		if ( regionName == null ) {
			if ( other.getRegionName() != null ) return false;
		} else {
			if ( !regionName.equals( other.getRegionName() ) ) return false;
		}

		return true;
	}
}
