package com.ftloverdrive.script;

import java.io.InputStream;
import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

import bsh.EvalError;
import bsh.Interpreter;

import com.ftloverdrive.util.TextUtilities;


/**
 * Interprets scripts.
 *
 * TODO: Stub
 *
 * @see bsh.Interpreter
 */
public class OVDScriptManager {
	private Logger log;
	private Interpreter bsh;


	public OVDScriptManager() {
		log = new Logger( OVDScriptManager.class.getCanonicalName(), Logger.INFO );

		bsh = new Interpreter();
		try {
			bsh.eval( "print( \"Hello World\" );" );
		}
		catch( EvalError e ) {
			log.error( "Error evaluating script.", e );
		}
	}


	/**
	 * Evaluates a script file in the global namespace.
	 *
	 * @see TextUtilities.decodeText(InputStream srcStream, String srcDescription)
	 */
	public void eval( FileHandle f ) throws IOException, EvalError {
		InputStream is = null;
		try {
			is = f.read();
			bsh.eval( TextUtilities.decodeText( is, f.name() ).text );
		}
		finally {
			try {if ( is != null ) is.close();}
			catch ( IOException e ) {}
		}
	}
}
