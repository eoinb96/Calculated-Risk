/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This class creates the MVC structure for the game and commences the game

package launcher;

import control.RiskController;
import control.RiskPresenter;
import model.RiskModel;
import view.RiskView;

public class RiskLauncher {

	public void launchGame() {
		RiskModel riskModel = new RiskModel();
		RiskView riskView = new RiskView();
		RiskController riskController = new RiskController(riskModel);
		RiskPresenter riskPresenter = new RiskPresenter(riskView);
		riskView.setViewObserver(riskController);
		riskModel.setModelObserver(riskPresenter);
		riskPresenter.presentView();
		riskModel.beginGame();
	}
}