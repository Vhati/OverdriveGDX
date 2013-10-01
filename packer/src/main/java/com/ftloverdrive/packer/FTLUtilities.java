// Copied from Slipstream Mod Manager 1.4.
// (Excerpts)

package com.ftloverdrive.packer;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


public class FTLUtilities {

	/**
	 * Confirms the FTL resources dir exists and contains the dat files.
	 */
	public static boolean isDatsDirValid( File d ) {
		if ( !d.exists() || !d.isDirectory() ) return false;
		if ( !new File(d, "data.dat").exists() ) return false;
		if ( !new File(d, "resource.dat").exists() ) return false;
		return true;
	}

	/**
	 * Returns the FTL resources dir, or null.
	 *
	 * TODO: Add cross-platform GDX FileHandles, such as the local dir.
	 */
	public static File findDatsDir() {
		String steamPath = "Steam/steamapps/common/FTL Faster Than Light/resources";
		String gogPath = "GOG.com/Faster Than Light/resources";
		String humblePath = "FTL/resources";

		String xdgDataHome = System.getenv("XDG_DATA_HOME");
		if (xdgDataHome == null)
			xdgDataHome = System.getProperty("user.home") +"/.local/share";

		File[] candidates = new File[] {
			// Windows - Steam
			new File( new File(""+System.getenv("ProgramFiles(x86)")), steamPath ),
			new File( new File(""+System.getenv("ProgramFiles")), steamPath ),
			// Windows - GOG
			new File( new File(""+System.getenv("ProgramFiles(x86)")), gogPath ),
			new File( new File(""+System.getenv("ProgramFiles")), gogPath ),
			// Windows - Humble Bundle
			new File( new File(""+System.getenv("ProgramFiles(x86)")), humblePath ),
			new File( new File(""+System.getenv("ProgramFiles")), humblePath ),
			// Linux - Steam
			new File( xdgDataHome +"/Steam/SteamApps/common/FTL Faster Than Light/data/resources" ),
			// OSX - Steam
			new File( System.getProperty("user.home") +"/Library/Application Support/Steam/SteamApps/common/FTL Faster Than Light/FTL.app/Contents/Resources" ),
			// OSX
			new File( "/Applications/FTL.app/Contents/Resources" ),
		};

		File result = null;

		for ( File candidate : candidates ) {
			if ( isDatsDirValid( candidate ) ) {
				result = candidate;
				break;
			}
		}

		return result;
	}

	/**
	 * Modally prompts the user for the FTL resources dir.
	 *
	 * Reminder: GUI dialogs need to be in the event dispatch thread.
	 *
	 * @param parentComponent a parent for Swing dialogs, or null
	 */
	public static File promptForDatsDir( Component parentComponent ) {
		File result = null;

		String message = "";
		message += "You will now be prompted to locate FTL manually.\n";
		message += "Select '(FTL dir)/resources/data.dat'.\n";
		message += "Or 'FTL.app', if you're on OSX.";
		JOptionPane.showMessageDialog( parentComponent,  message, "Find FTL", JOptionPane.INFORMATION_MESSAGE );

		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle( "Find data.dat or FTL.app" );
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "FTL Data File - (FTL dir)/resources/data.dat";
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().equals("data.dat") || f.getName().equals("FTL.app");
			}
		});
		fc.setMultiSelectionEnabled(false);

		if ( fc.showOpenDialog( parentComponent ) == JFileChooser.APPROVE_OPTION ) {
			File f = fc.getSelectedFile();
			if ( f.getName().equals("data.dat") ) {
				result = f.getParentFile();
			}
			else if ( f.getName().endsWith(".app") && f.isDirectory() ) {
				File contentsPath = new File(f, "Contents");
				if( contentsPath.exists() && contentsPath.isDirectory() && new File(contentsPath, "Resources").exists() )
					result = new File(contentsPath, "Resources");
			}
		}

		if ( result != null && isDatsDirValid( result ) ) {
			return result;
		}

		return null;
	}
}
