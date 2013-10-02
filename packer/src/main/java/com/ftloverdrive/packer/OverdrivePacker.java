package com.ftloverdrive.packer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

import net.vhati.ftldat.FTLDat;

import com.ftloverdrive.packer.FTLUtilities;


public class OverdrivePacker {

	private static final String ENV_APP_PATH = "OVERDRIVE_APP_PATH";


	public static void main( String args[] ) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiInit();
			}
		});
	}


	private static void guiInit() {

		List<Pattern> skipPtns = new ArrayList<Pattern>();
		// Irrelevant dirs.
		skipPtns.add( Pattern.compile( "^(?!img/.*[.]png$).*" ) );

		// Needlessly big. It's an empty window with a tab (TODO: Crop the tab).
		skipPtns.add( Pattern.compile( "img/box_options_configure[.]png" ) );

		// Redundant. Pieces exist to reconstruct this.
		skipPtns.add( Pattern.compile( "img/scoreUI/score_main[.]png" ) );
		skipPtns.add( Pattern.compile( "img/screenshot[.]png" ) );  // Junk.

		// Stripping whitespace breaks TextureRegion.split().
		// http://code.google.com/p/libgdx/issues/detail?id=1192
		// Not that stripping whitespace helps much for FTL anyway.


		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

		File appDir = new File( "." );
		System.out.println( "CWD: "+ appDir.getAbsolutePath() );

		String envAppPath = System.getenv( ENV_APP_PATH );
		if ( envAppPath != null && envAppPath.length() > 0 ) {
			File envAppDir = new File( envAppPath );
			if ( envAppDir.exists() ) {
				System.out.println( String.format( "Environment var (%s) changed app path: %s", ENV_APP_PATH, envAppPath ) );
				appDir = envAppDir;
			} else {
				System.out.println( String.format( "Environment var (%s) set a non-existent app path: %s", ENV_APP_PATH, envAppPath ) );
			}
		}


		File datsDir = null;

		datsDir = FTLUtilities.findDatsDir();
		if ( datsDir != null ) {
			int response = JOptionPane.showConfirmDialog( null, "FTL resources were found in:\n"+ datsDir.getPath() +"\nIs this correct?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
			if ( response == JOptionPane.NO_OPTION ) datsDir = null;
		}
		if ( datsDir == null ) {
			datsDir = FTLUtilities.promptForDatsDir( null );
		}
		if ( datsDir == null ) {
			showErrorDialog( "FTL resources were not found.\nThe packer will now exit." );
			System.exit( 1 );
		}


		JOptionPane.showMessageDialog( null, "Now choose a dir (probably this one)\nin which to create Overdrive's 'resources' folder.\nAny existing 'resources' folder will be deleted.", "Destination", JOptionPane.INFORMATION_MESSAGE );

		JFileChooser packChooser = new JFileChooser();
		packChooser.setDialogTitle( "Choose a dir to contain resources folder" );
		packChooser.setCurrentDirectory( appDir );
		packChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		packChooser.setMultiSelectionEnabled(false);

		if ( packChooser.showSaveDialog( null ) != JFileChooser.APPROVE_OPTION )
			System.exit( 0 );

		File chosenDir = packChooser.getSelectedFile();
		File outputDir = new File( chosenDir, "resources" );
		if ( outputDir.exists() ) deleteRecursively( outputDir );
		outputDir.mkdirs();

		File resDatFile = new File( datsDir, "resource.dat" );

		FTLDat.FTLPack resP = null;
		try {
			resP = new FTLDat.FTLPack( resDatFile, "r" );

			TexturePacker2.Settings normalSettings = new TexturePacker2.Settings();

			TexturePacker2.Settings bgSettings = new TexturePacker2.Settings();
			bgSettings.paddingX = 0;
			bgSettings.paddingY = 0;

			List<String> allInnerPaths = new ArrayList<String>( resP.list() );
			Collections.sort( allInnerPaths );

			// Sort innerPaths into separate dir lists.
			Map<String,List<String>> dirInnerPathsMap = new LinkedHashMap<String,List<String>>();
			for ( String innerPath : allInnerPaths ) {
				String dirPath = innerPath.replaceAll( "/[^/]*$", "/" );
				if ( !dirInnerPathsMap.containsKey( dirPath ) ) {
					dirInnerPathsMap.put( dirPath, new LinkedList<String>() );
				}
				dirInnerPathsMap.get( dirPath ).add( innerPath );
			}

			for ( Map.Entry<String,List<String>> entry : dirInnerPathsMap.entrySet() ) {
				String dirPath = entry.getKey();
				List<String> dirPaths = entry.getValue();

				if ( dirPaths.isEmpty() || isSkippedDir( dirPaths, skipPtns ) ) continue;
				File nestedOutputDir = new File( outputDir, dirPath );

				TexturePacker2.Settings dirSettings = new TexturePacker2.Settings( normalSettings );
				if ( dirPath.equals( "img/map/" ) ) {
					dirSettings.paddingX = 0;  // Some images are exactly 1024, padding puts them over the limit.
					dirSettings.paddingY = 0;
				}
				else if ( dirPath.equals( "img/stars/" ) ) {
					dirSettings.paddingX = 0;  // Some images are exactly 512.
					dirSettings.paddingY = 0;
				}

				TexturePacker2 dirPacker = new TexturePacker2( dirSettings );

				for ( String innerPath : dirPaths ) {
					if ( isSkippedInnerPath( innerPath, skipPtns ) ) continue;
					if ( !nestedOutputDir.exists() ) nestedOutputDir.mkdirs();

					InputStream imgStream = null;
					try {
						String baseName = innerPath.replaceAll( ".*/", "" );
						baseName = baseName.replaceAll( "[.]9([.][^.]+)$", "-9$1" );  // .9.ext is special in GDX.
						baseName = baseName.replaceAll( "_", "-" );  // Underscores are special in GDX.
						baseName = baseName.replaceAll( "[.][^.]+$", "" );  // Strip extension.

						imgStream = resP.getInputStream( innerPath );
						BufferedImage rawImage = ImageIO.read( imgStream );

						int rawW = rawImage.getWidth();
						int rawH = rawImage.getHeight();
						if ( rawW > 1024 || rawH > 1024 ) {
							// Pack large images in their own atlas of fragments.

							TexturePacker2 bgPacker = new TexturePacker2( bgSettings );
							int regionCount = 0;
							String regionName;
							BufferedImage regionImage;
							int regionW; int regionH;
							int regionSize = 256;
							for ( int regionY=0; regionY < rawH; regionY += regionSize ) {
								for ( int regionX=0; regionX < rawW; regionX += regionSize ) {
									regionName = baseName +"_"+ ++regionCount;  // GDX treats "_123" as a frame index.
									regionW = Math.min( regionSize, rawW - regionX );
									regionH = Math.min( regionSize, rawH - regionY );
									regionImage = rawImage.getSubimage( regionX, regionY, regionW, regionH );
									bgPacker.addImage( regionImage, regionName );
								}
							}

							bgPacker.pack( nestedOutputDir, baseName +"-bigcols"+ ((rawW+regionSize-1)/regionSize) +".atlas" );
							bgPacker = null;
							System.gc();
						}
						else {
							// Add small images to the directory's shared packer.
							dirPacker.addImage( rawImage, baseName );
						}
					}
					finally {
						try {if ( imgStream != null ) imgStream.close();}
						catch ( IOException e ) {}
					}
				}
				dirPacker.pack( nestedOutputDir, "pack.atlas" );
				dirPacker = null;
				System.gc();
			}
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		finally {
			try {if ( resP != null ) resP.close();}
			catch ( IOException e ) {}
		}

		System.out.println( "\nPacking finished!" );
	}


	public static boolean isSkippedDir( List<String> dirPaths, List<Pattern> skipPtns ) {
		boolean allSkipped = true;
		for ( String innerPath : dirPaths ) {
			if ( !isSkippedInnerPath( innerPath, skipPtns ) ) {
				allSkipped = false;
				break;
			}
		}
		return allSkipped;
	}

	public static boolean isSkippedInnerPath( String innerPath, List<Pattern> skipPtns ) {
		for ( Pattern p : skipPtns ) {
			if ( p.matcher( innerPath ).matches() ) {
				return true;
			}
		}
		return false;
	}


	public static boolean deleteRecursively( File f ) {
		if( f.exists() && f.isDirectory() ) {
			File[] files = f.listFiles();
			if ( files != null ) {
				for ( int i=0; i < files.length; i++ ) {
					deleteRecursively( files[i] );
				}
			}
		}
		return( f.delete() );
	}


	private static void showErrorDialog( String message ) {
		JOptionPane.showMessageDialog( null, message, "Error", JOptionPane.ERROR_MESSAGE );
	}
}
