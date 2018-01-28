/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This Presenter takes updated logic from the model and
//sends it to the View, updating the games GUI and sequence.

package control;

import java.awt.Color;

import model.MessageType;
import model.Player;
import view.RiskView;

public class RiskPresenter {

	private RiskView riskView;
	
	public RiskPresenter(RiskView riskView) {
		this.riskView = riskView;
	}

	//Sets the GUI to visible when game is launched
	public void presentView() {
		getRiskView().setVisible(true);
	}
	
	//Allows methods in the View to be called
	private RiskView getRiskView() {
		return riskView;
	}

	//Allows Model to send messages to be diplayed in View
	public void createMessage(MessageType type, String message, int delay) {
		getRiskView().displayMessage(type, message, delay);
	}
	
	//Allows user input to be taken from View to Model. Needs to be
	//implemented through the Controller instead.
	public String getCommand() {
		return getRiskView().getCommand();
	}

	//Allows Model to tell View when to show the player panel
	public void setPlayerPanelVisible() {
		getRiskView().setPlayerPanelVisible();
	}
	
	//Allows Model to instruct view to put player names beside
	//correct colour when players have chosen their colours.
	public void setPlayerColors() {
		getRiskView().setPlayerColors();
	}

	//Allows Model to instruct View to repaint components
	public void refreshGame() {
		getRiskView().refreshGame();
	}

	//Allows Model to instruct View to show dice image
	public void addPlayerOrderDicePanel(int numberRolled, int rotation) {
		getRiskView().addPlayerOrderDicePanel(numberRolled, rotation);
	}

	//Allows Model to instruct View to reorder player panel
	public void updatePlayerPanelOrder(Player[] playerOrder) {
		getRiskView().updatePlayerPanelOrder(playerOrder);
	}
	
	//Allows Model to instruct view to update active player indicator
	public void updateActivePlayerInPlayerPanel(Player player, Color color) {
		getRiskView().updateActivePlayerInPlayerPanel(player, color);
	}
	
	public void removeActivePlayerInPlayerPanel(Player player) {
		getRiskView().removeActivePlayerInPlayerPanel(player);
	}

	//Allows Model to instruct view to show relevant numbers in player's colour circle
	public void addNumberToPlayerPanel(int number, Player player) {
		getRiskView().addNumberToPlayerPanel(number, player);
	}

	public void addBattleDice(String diceType, int diceNumber, int numberRolled, int rotation) {
		getRiskView().addBattleDice(diceType, diceNumber, numberRolled, rotation);
	}

	public void removeDice() {
		getRiskView().removeDice();		
	}


}
