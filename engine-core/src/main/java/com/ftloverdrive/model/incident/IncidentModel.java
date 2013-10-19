package com.ftloverdrive.model.incident;

import java.util.List;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.OVDModel;
import com.ftloverdrive.model.incident.Consequence;
import com.ftloverdrive.model.incident.PlotBranch;


/**
 * What the original game referred to as an event.
 *
 * TODO: Retrofit code to use reference ids and OVDEvents.
 *
 * TODO: A DeferredIncidentModel class, which constructs a named Incident
 * at the last minute, probably from an IncidentBlueprint. That would make
 * event loops possible.
 */
public interface IncidentModel extends OVDModel {

	/** Returns a unique identifier for this Incident.
	public String getIncidentId();


	/** Triggers Consequences and notifies the UI to represent this model, with a Window. */
	public void execute( OverdriveContext context );


	/**
	 * Returns text to show when triggered, or null.
	 *
	 * TODO: Allow for context-aware text (e.g., inserting crew names).
	 */
	public String getText();


	/** Returns a list of Consequences, all of which trigger at the start of this Incident. */
	public List<Consequence> getConsequences();

	public void addConsequence( Consequence cseq );

	public void removeConsequence( Consequence cseq );


	/** Returns choices which may lead to further Incidents. */
	public List<PlotBranch> getPlotBranches();

	public void addPlotBranch( PlotBranch branch );

	public void removePlotBranch( PlotBranch branch );
}
