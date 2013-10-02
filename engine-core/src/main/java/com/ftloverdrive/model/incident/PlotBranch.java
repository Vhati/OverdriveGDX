package com.ftloverdrive.model.incident;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.incident.IncidentModel;


/**
 * What the original game referred to as an event choice.
 *
 * TODO: A PlotBranchCriteria class to decide whether this branch is a
 * normal choice, a blue option, or unavailable (if the Overdrive context
 * doesn't meet arbitrary reqs).
 */
public interface PlotBranch {

	/** Returns the clickable text for this branch. */
	public String getText();


	/**
	 * Returns spoiler text for what will happen.
	 * Scans the nested Incident's Consequences and returns a concatenation
	 * of all their spoilers... or null. */
	public String getSpoilerText();

	/** Toggles desired visibility of the spoiler text.
	public void setSpoilerVisible( boolean b );

	/** Returns true if this branch wants spoilers to be shown, false otherwise. */
	public boolean isSpoilerVisible();


	/** Returns an Incident to trigger if this branch is chosen, or null. */
	public IncidentModel getIncident();
}
