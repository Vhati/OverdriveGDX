package com.ftloverdrive.model.incident;

import java.util.ArrayList;
import java.util.List;

import com.ftloverdrive.core.OverdriveContext;
import com.ftloverdrive.model.AbstractOVDModel;
import com.ftloverdrive.model.incident.Consequence;
import com.ftloverdrive.model.incident.IncidentModel;
import com.ftloverdrive.model.incident.PlotBranch;


/**
 * What the original game referred to as an event.
 *
 * TODO: Retrofit code to use reference ids and OVDEvents.
 */
public abstract class AbstractIncidentModel extends AbstractOVDModel implements IncidentModel {

	protected List<Consequence> consequenceList = new ArrayList<Consequence>( 2 );
	protected List<PlotBranch> branchList = new ArrayList<PlotBranch>( 3 );


	public AbstractIncidentModel() {
		super();
	}


	public abstract String getIncidentId();


	@Override
	public void execute( OverdriveContext context ) {
		for ( Consequence cseq : getConsequences() ) {
			cseq.execute( context );
		}

		// TODO: Whatever it takes for the UI to notice and show a window.
	}


	@Override
	public String getText() {
		return null;
	}


	@Override
	public List<Consequence> getConsequences() {
		return consequenceList;
	}

	@Override
	public void addConsequence( Consequence cseq ) {
		consequenceList.add( cseq );
	}

	@Override
	public void removeConsequence( Consequence cseq ) {
		consequenceList.remove( cseq );
	}


	@Override
	public List<PlotBranch> getPlotBranches() {
		return branchList;
	}

	@Override
	public void addPlotBranch( PlotBranch branch ) {
		branchList.add( branch );
	}

	@Override
	public void removePlotBranch( PlotBranch branch ) {
		branchList.remove( branch );
	}
}
