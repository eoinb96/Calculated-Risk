/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This class is used to link the view and the model.
//Actions are sent from the view to the model and the game
//logic is then updated and sent back to the view through
//the presenter class.

//As of now, our game model only receives updates through its
//'getCommand' method which goes through the presenter - this
//is not the correct 'MVC' structure and needs to be revised.

package control;

import model.RiskModel;

public class RiskController {

	private RiskModel riskModel;

	public RiskController(RiskModel riskModel) {
		this.riskModel = riskModel;
	}
	
	//Allows methods in the model to be called from this class.
	private RiskModel getRiskModel() {
		return riskModel;
	}
}
