package com.ftloverdrive.script;

import com.badlogic.gdx.utils.Logger;

import bsh.EvalError;
import bsh.Interpreter;


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
}
