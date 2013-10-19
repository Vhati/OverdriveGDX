package com.ftloverdrive.model.incident;

import com.ftloverdrive.core.OverdriveContext;


/**
 * Something that happens as an Incident triggers.
 *
 * TODO: Retrofit code to use reference ids and OVDEvents.
 *
 * TODO: Some possible default classes...
 *
 *   EncounterConsequence (ship event)
 *   RandomRewardConsequence
 *   AugmentConsequence
 *   WeaponConsequence
 *   DroneConsequence
 *   CrewConsequence
 *   DamageConsequence
 *   BoardersConsequence
 *   MapConsequence (revealMap)
 *   PursuitConsequence
 *   QuestConsequence
 *   ItemModificationConsequence
 *   SecretSectorConsequence
 *   StatusConsequence
 */
public interface Consequence {

	/**
	 * Returns spoiler text for what will happen.
	 *
	 * In the original game this was a parenthetical appended to choices
	 * when hidden="false".
	 */
	public String getSpoilerText();


	public void execute( OverdriveContext context );
}
