/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Instances of this class are used to store attributes for each player
//These instances are stored in the HashMap:
//ModelConstants.PLAYERS<String playerName, Player player>

package model;

import java.awt.Color;

public class Player {

	private PlayerType playerType;		//Human or Bot
	private Player nextPlayer;			//Link to next player in order of turns
	private int totalNumberOfUnits;     //Total number of units the player has (maybe unnecessary)
	private int numberOfFreeUnits;      //Number of units unassigned to a territory
	private int numberOfTerritoryCards;
	private Color playerColor;          //Color of player indicator
	private int diceNumberRolled;       //Number player has rolled
	private boolean eliminated;
	
	public Player(PlayerType playerType) {
		this.playerType = playerType;
		this.nextPlayer = null;
		this.totalNumberOfUnits = 0;
		this.numberOfFreeUnits = 0;
		this.numberOfTerritoryCards = 0;
		this.playerColor = null;
		this.diceNumberRolled = 0;
		this.eliminated = false;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}
	
	public Player getNextPlayer() {
		return nextPlayer;
	}
	
	//Adds units to player which can then be assigned to a territory
	public void addFreeUnits(int numberToAdd) {
		this.totalNumberOfUnits += numberToAdd;
		this.numberOfFreeUnits += numberToAdd;
	}
	
	public void addUnits(int numberToAdd) {
		this.totalNumberOfUnits += numberToAdd;
	}
	
	public void removeUnits(int numberToRemove) {
		this.totalNumberOfUnits -= numberToRemove;
		if (this.getTotalNumberOfUnits() == 0) {
			this.eliminated = true;
		}
	}
	
	public int getTotalNumberOfUnits() {
		return totalNumberOfUnits;
	}
	
	public int getNumberOfFreeUnits() {
		return numberOfFreeUnits;
	}
	
	//Decreases number of unassigned units when they are sent to a territory
	public void addFreeUnitsToTerritory(int numberOfUnits) {
		this.numberOfFreeUnits -= numberOfUnits;
	}
	
	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}
	
	public void addTerritoryCard() {
		this.numberOfTerritoryCards++;
	}
	
	public void removeTerritoryCardSet() {
		this.numberOfTerritoryCards = this.numberOfTerritoryCards - 3;
	}
	
	public int getNumberOfTerritoryCards() {
		return this.numberOfTerritoryCards;
	}
	
	public Color getPlayerColor() {
		return playerColor;
	}
	
	public void setDiceNumberRolled(int number) {
		this.diceNumberRolled = number;
	}
	
	public int getDiceNumberRolled() {
		return diceNumberRolled;
	}
	
	public boolean gameOver() {
		return this.eliminated;
	}
}
