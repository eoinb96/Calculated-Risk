/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This class is the model which waits for user input from the command panel
//and then updates the game logic and controls the sequence in which the game progresses,
//constantly sending methods to the view to update the GUI

//This class needs to be divided into subclasses

package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Stack;

import control.RiskPresenter;
import util.HashMapUtilities;
import util.ModelConstants;
import util.ViewConstants;

public class RiskModel {

	private RiskPresenter modelObserver;
	private Player[] playerOrder;
	private Player activePlayer;
	private boolean tiedPlayers;
	private boolean territoryConquered;
	private ArrayList<String> freeCards;
	private int cardExchangeReinforcementNumber;
	
	public RiskModel() {
		this.playerOrder = new Player[ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS];
		this.tiedPlayers = false;
		this.territoryConquered = false;
		this.freeCards = new ArrayList<String>(Arrays.asList(ModelConstants.COUNTRY_NAMES));
		this.cardExchangeReinforcementNumber = 0;
		scaleCoordinates();
	}

	public void setModelObserver(RiskPresenter modelObsever) {
		this.modelObserver = modelObsever;
	}
	
	private RiskPresenter getModelObserver() {
		return modelObserver;
	}
	
	//Sends a message to be displayed in the Log panel with a delay if needed (visual effect)
	private void addMessageToLog(MessageType type, String message, int delay) {
		getModelObserver().createMessage(type, message, delay);
	
	}
	
	//Displays instruction to user, waits for input, displays error if input is not valid
	private String getCommand(String instruction, String blankCommandError, int delay) {
		String command = "";
		while (!isBlank(command)) {
			addMessageToLog(MessageType.INFO, instruction, delay);
			command = getModelObserver().getCommand();
			checkCommand(command, blankCommandError);
		}
		return command;
	}
	
	//If command is blank, displays error message.
	//Otherwise displays command entered
	public void checkCommand(String command, String error) {
		if (isBlank(command)) {
			addMessageToLog(MessageType.COMMAND, command, 0);
		}
		if (command.equals("")) {
			addMessageToLog(MessageType.ERROR, error,0);
		}
	}
	
	//Checks if command is blank
	private boolean isBlank(String command) {
		return !command.equals("");
	}
	
	//Command called from launcher which begins the games sequence
	public void beginGame() {
		addMessageToLog(MessageType.SUCCESS, "Welcome to Risk!", 0);
		gameSequence();
	}
	
	private void gameSequence() {
		createTerritories();
		String[] playerNames = getPlayerNames();
		createPlayers(playerNames[0], playerNames[1]);
		choosePlayerColors();
		setPlayerColors();
		assignArmies();
		playerOrder = getPlayerOrderArray();
		removeDice();
		setPlayerOrder();
		printPlayerOrder();
		allocateTerritories();
		placeUnitOnAllTerritories();
		placeInitialUnits();
		setPlayerOneActive();
		turnSequence();
	}
	


	//Creates Territory instances for each country
	//Adds them to ModelConstants.TERRITORIES HashMap
	public void createTerritories() {
		for (int i = 0; i <ModelConstants.NUM_COUNTRIES; i++) {
			String continent = ModelConstants.CONTINENT_NAMES[ModelConstants.CONTINENT_IDS[i]];
			ModelConstants.TERRITORIES.put(ModelConstants.COUNTRY_NAMES[i], new Territory(ModelConstants.COUNTRY_COORD[i][0], ModelConstants.COUNTRY_COORD[i][1], continent));
		}
	}
	
	//Gets 2 human player names
	private String[] getPlayerNames() {
		String[] playerNames = new String[2];
		String playerOneName = getCommand("Enter Player 1 name:", "No name entered.", 1000);
		String playerTwoName = "";
		boolean validName = false;
		while (!validName) {
			playerTwoName = getCommand("Enter Player 2 name:", "No name entered.", 500);
			if(playerOneName.equalsIgnoreCase(playerTwoName)) {
				addMessageToLog(MessageType.ERROR, "Players cannot use the same name as each other", 0);
			}
			else {
				validName = true;
			}
		}
		addMessageToLog(MessageType.SUCCESS, playerOneName + " and " + playerTwoName + " have joined the game.", 500);
		playerNames[0] = playerOneName;
		playerNames[1] = playerTwoName;
		return playerNames;
	}

	//Creates bots and 'Player' instances for each human and bot
	//Adds them to ModelConstants.PLAYERS HashMap
	private void createPlayers(String playerOneName, String playerTwoName) {
		addMessageToLog(MessageType.INFO, "Creating players...", 1000);
		ModelConstants.PLAYERS.put(playerOneName, new Player(PlayerType.HUMAN));
		ModelConstants.PLAYERS.put(playerTwoName, new Player(PlayerType.HUMAN));
		for(int i = (ModelConstants.NUM_PLAYERS + 1); i < (ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS + 1); i++) {
			String instanceName = "Bot " + (i - ModelConstants.NUM_PLAYERS);
			ModelConstants.PLAYERS.put(instanceName, new Player(PlayerType.BOT));
		}
		addMessageToLog(MessageType.SUCCESS, ModelConstants.NUM_NEUTRALS + " bots have been created.", 1000);
	}
	
	//Allows players to choose their colour and assigns colours to bots
	private void choosePlayerColors() {
		//Stores colours which have been chosen
		ArrayList<String> takenColors = new ArrayList<>();		
		//Iterates through each player
		for(Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {
			if(player.getValue().getPlayerType().equals(PlayerType.HUMAN)) {			//Allows user input if player is human
				boolean validColor = false;			
				while(!validColor) {				//Loops until valid colour is entered
					boolean messageSent = false;	//Prevents two error messages being sent
					pauseExecution(1000);
					setPlayerPanelVisible();		//Shows choice of colours
					
					//Get user input
					String chosenColor = getCommand(player.getKey() + ", choose a colour from above", "No colour choice entered.", 0);
					for (int i = 0; i < ViewConstants.PLAYER_COLOR_NAMES.length; i++) {				//Iterates through colour names
						if (chosenColor.equalsIgnoreCase(ViewConstants.PLAYER_COLOR_NAMES[i])) {	//and checks if user input matches
							if (takenColors.contains(ViewConstants.PLAYER_COLOR_NAMES[i])) {		//Checks if colour is already chosen
								addMessageToLog(MessageType.ERROR, ViewConstants.PLAYER_COLOR_NAMES[i] + " is already taken.", 0);
								messageSent = true;
							}
							else {
								player.getValue().setPlayerColor(ViewConstants.PLAYER_COLORS[i]);	//If colour is not already taken
								takenColors.add(ViewConstants.PLAYER_COLOR_NAMES[i]);				//Add to list of taken colours
								validColor = true;													//exits while loop
								
								addMessageToLog(MessageType.SUCCESS, player.getKey() + " has chosen the colour " + ViewConstants.PLAYER_COLOR_NAMES[i], 500);
							}
						}
					}
					if (!validColor && !messageSent){			//If invalid command is entered
						addMessageToLog(MessageType.ERROR, "Invalid color selection", 0);
					}
				}
			}	
		}
		//Assigns rest of colours to bots
		addMessageToLog(MessageType.INFO, "Assigning colours to bots...", 1000);
		for (int i = 0; i < ViewConstants.PLAYER_COLOR_NAMES.length; i++) {
			if (!(takenColors.contains(ViewConstants.PLAYER_COLOR_NAMES[i]))) {
				for(Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {
					if (player.getValue().getPlayerColor() == null) {
						player.getValue().setPlayerColor(ViewConstants.PLAYER_COLORS[i]);
						takenColors.add(ViewConstants.PLAYER_COLOR_NAMES[i]);
						break;
					}
				}
			}
		}
		addMessageToLog(MessageType.SUCCESS, "All players have been assigned a color", 1000);
	}

	//Assigns 36 units to players and 24 to bots
	private void assignArmies() {
		addMessageToLog(MessageType.INFO, "Assigning armies to players...", 1000);
		String humanPlayers = "";
		for (Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {	//Iterates through players
			if (player.getValue().getPlayerType() == PlayerType.HUMAN) {				//Add correct number to humans
				player.getValue().addFreeUnits(ModelConstants.INIT_UNITS_PLAYER);
				humanPlayers += " and " + player.getKey();								//Adds each human name to string
				
			}
			if (player.getValue().getPlayerType() == PlayerType.BOT) {
				player.getValue().addFreeUnits(ModelConstants.INIT_UNITS_NEUTRAL);			//Adds correct number to bots
			}
		}
		humanPlayers = humanPlayers.substring(5);	//Removes first 'and' from string
		addMessageToLog(MessageType.SUCCESS, humanPlayers + " have received " + ModelConstants.INIT_UNITS_PLAYER + " army units each.", 1000);
		addMessageToLog(MessageType.SUCCESS, "Each Bot has received " + ModelConstants.INIT_UNITS_NEUTRAL + " army units.", 2000);
	}
	
	//Creates an array or players in the order in which they will take turns
	private Player[] getPlayerOrderArray() {
		addMessageToLog(MessageType.INFO, "Each player will roll a dice to determine the order in which they take turns.", 2000);
		//Creates arrays from Player hashmap
		Player[] players = ModelConstants.PLAYERS.values().toArray(new Player[ModelConstants.PLAYERS.size()]);
		String[] playerNames = ModelConstants.PLAYERS.keySet().toArray(new String[ModelConstants.PLAYERS.size()]);
		determinePlayerOrder(playerNames, players,  0, players.length);
		pauseExecution(2000);
		return players;
	}

	private void determinePlayerOrder(String[] playerNames, Player[] players, int start, int end) {
		//Get all players between players[start] (inclusive) and
		//players[end] (exclusive) to re-roll the dice.
		for (int i = start; i < end; i++) {
			activePlayer = players[i];	
			updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);	//Highlights player in player panel
			players[i].setDiceNumberRolled(rollDice(playerNames[i], players[i]));	//Shows dice number rolled in player panel
		}
	
		// Sort this portion of the array according to the number rolled.
		Arrays.sort(players, start, end, new Comparator<Player>() {		
			@Override public int compare(Player a, Player b) {
				return Integer.compare(a.getDiceNumberRolled(), b.getDiceNumberRolled());
			}
		});
		
		//Gets names(keys) of each player(values) from hashmap
		for (int i = 0; i < playerNames.length; i++) {
			playerNames[i] = getPlayerName(players[i]);
		}

		// Look for players who rolled the same number.
		int i = start;
		while (i < end) {
			// Try to find a "run" of players with the same number.
			int runStart = i;
			int diceNumberRolled = players[runStart].getDiceNumberRolled();
			i++;
			while (i < end && players[i].getDiceNumberRolled() == diceNumberRolled) {
				addNumberToPlayerPanel(diceNumberRolled, players[i]);
				i++;
			}

			if (i - runStart > 1) {
				// Found more than one player with the same dice number.
				// Get all of the players with that dice number to roll again.
				addMessageToLog(MessageType.INFO, "There has been a tie." , 2000);
				tiedPlayers = true;
				determinePlayerOrder(playerNames, players, runStart, i);
				tiedPlayers = false;
			}
		}
	}

	//Method to roll the dice when user enters command 
	//and display messages showing who is rolling and what they rolled
	private int rollDice(String playerName, Player player) {
		RandomNumberGenerator dice = new RandomNumberGenerator();
		int numberRolled = 0;
		if (player.getPlayerType().equals(PlayerType.HUMAN)) {			//Human - game waits for input
			boolean diceRolled = false;
			while (!diceRolled) {
				String message = ", roll the dice";
				if (tiedPlayers == true) {
					message += " again.";		//Adds to string if this player is re-rolling 
				}
				else {
					message += ".";
				}
				String userInput = getCommand(playerName +  message, "No command entered. Type \"Roll Dice\" or something similar to roll the dice.", 2000);
				if (userInput.equalsIgnoreCase("Roll Dice") || userInput.equalsIgnoreCase("roll the dice") || userInput.equalsIgnoreCase("Roll")) {
					numberRolled = dice.rollDice();
					diceRolled = true;
				}
				else {
					addMessageToLog(MessageType.ERROR, "Invlaid command. Type \"Roll Dice\" or something similar.", 0);
				}
			}
		}
		else {															//Bot - dice automatically rolled
			String message = " is now rolling the dice";
			if (tiedPlayers == true) {
				message += " again...";			//Adds to string if this player is re-rolling 
			}
			else {
				message += "...";
			}
			addMessageToLog(MessageType.INFO, playerName + message, 2000);
			numberRolled = dice.rollDice();
		}
		player.setDiceNumberRolled(numberRolled);			//Stores number in Player class
		addMessageToLog(MessageType.SUCCESS, playerName + " rolled a " + numberRolled, 1000);
		addPlayerOrderDicePanel(numberRolled, 3);			//Shows dice image 
		addNumberToPlayerPanel(numberRolled, player);		//Shows number rolled in player panel
		return numberRolled;
	}

	//Sets 'nextPlayer' for each Player instance based on sorted playerOrder[] array created when rolling the dice
	private void setPlayerOrder() {
		Collections.reverse(Arrays.asList(playerOrder));		//Reverses array so that the player with the highest roll is first
		for (int i = 0; i < playerOrder.length; i++) {
			if (i == (playerOrder.length - 1)) {				//Links last player to first
				playerOrder[i].setNextPlayer(playerOrder[0]);
			}
			else {
				playerOrder[i].setNextPlayer(playerOrder[i + 1]);
			}
		}
		setPlayerOneActive();
		updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);	//highlights active player
	}
	
	//After player order has been determined, print in log
	private void printPlayerOrder() {
		addMessageToLog(MessageType.INFO, "Determining player order...", 0);
		addNumberToPlayerPanel(-1, null);
		addMessageToLog(MessageType.SUCCESS, "Player order has been created:", 1000);
		String order = "\n";
		for (int i = 0; i < playerOrder.length; i++) {
			order += "Player " + (i + 1) + ": " + getPlayerName(playerOrder[i]);
			if (i != (playerOrder.length - 1)) {
				order += "\n";
			}
		}
		addMessageToLog(MessageType.BLANK, order , 2000);
		updatePlayerPanelOrder(playerOrder);
	}
	
	//Allows players to pick territory cards
	private void allocateTerritories() {
		RandomNumberGenerator randomTerritoryIndex = new RandomNumberGenerator(0, 42);
		ArrayList<String> unoccupiedTerritories = new ArrayList<>();		//Stores cards which haven't been chosen
		for (int i = 0; i < ModelConstants.COUNTRY_NAMES.length; i++) {
			unoccupiedTerritories.add(ModelConstants.COUNTRY_NAMES[i]);		//Initially adds all territories to list
		}
		addMessageToLog(MessageType.INFO, "Each human player will now choose 9 territory cards and each bot will choose 6.", 4000);
		pauseExecution(2000);
		int roundCount = 0;		//tracks number of cards picked by each player
		
		//Loops until 9 have been picked by humans
		while (roundCount < ModelConstants.INIT_COUNTRIES_PLAYER) {
			//stays in while loop until all players have picked a card
			for (int i = 0; i < ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS; i++) {
				String playerName = getPlayerName(activePlayer);
				if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {	//If player is human
					boolean cardPicked = false;
					//Loop until command is valid
					while (!cardPicked) {
						String userInput = getCommand(playerName +  " pick a card.", "No command entered. Type \"Pick Card\" or something similar to pick a territory card from the pile.", 500);
						if (userInput.equalsIgnoreCase("pick a card") || userInput.equalsIgnoreCase("pick card") || userInput.equalsIgnoreCase("card")) {
							String territory = unoccupiedTerritories.get(randomTerritoryIndex.getRandomNumber()); //gets random unoccupied territory
							unoccupiedTerritories.remove(territory);	//Removes from list
							randomTerritoryIndex.reduceRange();			//Reduces random number range
							ModelConstants.TERRITORIES.get(territory).setOccupier(activePlayer, 0);		//Adds player to chosen territory
							addMessageToLog(MessageType.SUCCESS, playerName + " now occupies " + territory, 500);
							cardPicked = true;
						}
						else {
							addMessageToLog(MessageType.ERROR, "Invlaid command. Type \"Pick a card\" or something similar.", 0);
						}
					}
				}
				//if bot - automatically pick a card
				//(until they have chosen 6)
				else if ((activePlayer.getPlayerType().equals(PlayerType.BOT)) && (roundCount < ModelConstants.INIT_COUNTRIES_NEUTRAL)) {
					addMessageToLog(MessageType.INFO, playerName + " is picking a card...", 500);
					String territory = unoccupiedTerritories.get(randomTerritoryIndex.getRandomNumber());
					unoccupiedTerritories.remove(territory);
					randomTerritoryIndex.reduceRange();
					ModelConstants.TERRITORIES.get(territory).setOccupier(activePlayer, 0);
					addMessageToLog(MessageType.SUCCESS, playerName + " now occupies " + territory, 500);
				}
				else {
					pauseExecution(100);
				}
				changePlayer();				//Change to next player and loop
				refreshGame();				//Show newly occupied territory on map
			}
			//loop to next round
			addMessageToLog(MessageType.INFO, "ROUND " + (roundCount + 1) + "/" + ModelConstants.INIT_COUNTRIES_PLAYER + " COMPLETE.", 500);
			if (roundCount == 5) {
				addBlankLineToLog(1000);
				addMessageToLog(MessageType.SUCCESS, "Each bot has receieved its 6 territories.", 0);
			}
			else if (roundCount == 8) {
				addBlankLineToLog(1000);
				addMessageToLog(MessageType.SUCCESS, "All players have received their territories", 0);
			}
			roundCount++;
		}
	}
	//Places 1 unit on each territory
	private void placeUnitOnAllTerritories() {
		addMessageToLog(MessageType.INFO, "The number of units each player has available is now displayed above.", 2000);
		for (Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {
			addNumberToPlayerPanel(player.getValue().getNumberOfFreeUnits(), player.getValue());
		}
		refreshGame();
		addMessageToLog(MessageType.INFO, "Placing 1 unit on each territory...", 3000);
		for (Map.Entry<String, Territory> territory : ModelConstants.TERRITORIES.entrySet()) {	//Iterates through territories
			territory.getValue().addFreeUnits(1);													//Adds unit to territory
			Player occupier = territory.getValue().getOccupier();								//Finds player occupying current territory
			addNumberToPlayerPanel(occupier.getNumberOfFreeUnits(), occupier);					//Displays number of free units for each player
			refreshGame();
			pauseExecution(100);																//Slight delay before placing next unit
		}
		addBlankLineToLog(500);
		addMessageToLog(MessageType.SUCCESS, "Each player has placed 1 unit on each of their territories.", 0);
	}
	
	private void placeInitialUnits() {
		addMessageToLog(MessageType.INFO, "Each player will now take turns to place 3 units (Humans) or 1 unit (Bots) on their own territories until all units have been placed.", 2000);
		pauseExecution(4000);
		boolean allUnitsPlaced = false;
		while (!allUnitsPlaced) {
			int playerFinishedCount = 0;
			for (Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {
				if (player.getValue().getNumberOfFreeUnits() == 0) {
					playerFinishedCount++;
				}
			}
			if (playerFinishedCount == 6) {
				allUnitsPlaced = true;
			}
			else if (activePlayer.getNumberOfFreeUnits() == 0) {
				changePlayer();
			}
			else {
				placeUnitsOnSpecifiedTerritory(false, 3);
				changePlayer();
			}
		}
		addBlankLineToLog(1000);
		addMessageToLog(MessageType.SUCCESS, "All players have placed all of their units", 0);
	}
	
	private void placeUnitsOnSpecifiedTerritory(boolean inputNumberOfUnits, int numberOfUnits) {
		boolean validTerritory = false;
		String territoryName = "";
		if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			while (!validTerritory) {
				String userInput = getCommand(getPlayerName(activePlayer) + ", choose a territory to add units to:", "No territory entered.", 300);
				ArrayList<String> matchedCountries = createListOfMatchedCountries(userInput);
				if (matchedCountries.size() > 1) {							
					displayUnambiguousSelectionMessage(matchedCountries);
				}
				else if (matchedCountries.size() == 0) {									
					addMessageToLog(MessageType.ERROR, "Invalid command. Enter the name of one of your territories.", 0);
				}
				else {		
					territoryName = matchedCountries.get(0);
					Territory territory = ModelConstants.TERRITORIES.get(territoryName);
					if (!(territory.getOccupier().equals(activePlayer))) {				
						addMessageToLog(MessageType.ERROR, "You do not occupy " + territoryName + ". Try again.", 0);
					}
					else {
						validTerritory = true;
						if (inputNumberOfUnits == true) {
							numberOfUnits = -1;
							boolean validNumber = false;
							while (!validNumber) {
								userInput = getCommand("How many units would you like to place on " + territoryName + "?", "No number of units entered.", 300);
								try {
									numberOfUnits = Integer.parseInt(userInput);
								}
								catch (NumberFormatException e) {
									addMessageToLog(MessageType.ERROR, userInput + " is not a valid number.", 0);
								}
								if (numberOfUnits > -1) {
									if (numberOfUnits > activePlayer.getNumberOfFreeUnits()) {
										addMessageToLog(MessageType.ERROR, "You only have " + activePlayer.getNumberOfFreeUnits() + " units available to place.", 0);
									}
									else {
										validNumber = true;
										territory.addFreeUnits(numberOfUnits);
									}
								}
							}
						}
						else {
							territory.addFreeUnits(numberOfUnits);
						}
					}
				}
			}
		}
		else { //bot
			numberOfUnits = 1;
			addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " is now placing a unit.", 300);
			while (!validTerritory) {	//Loop until random territory they occupy is found
				RandomNumberGenerator random = new RandomNumberGenerator(0, 42);			//Choose random number
				int territoryIndex = random.getRandomNumber();								//If they own territory with 
				territoryName = ModelConstants.COUNTRY_NAMES[territoryIndex];				//that index, place unit
				Territory territory = ModelConstants.TERRITORIES.get(territoryName);		//Otherwise loop
				if (territory.getOccupier().equals(activePlayer)) {
					territory.addFreeUnits(numberOfUnits);
					validTerritory = true;
					break;
				}
			}
		}
		if (numberOfUnits == 1) {
			addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has placed 1 unit on " + territoryName + ".", 300);
		}
		else {
			addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has placed " + numberOfUnits + " units on " + territoryName + ".", 300);
		}
		addNumberToPlayerPanel(activePlayer.getNumberOfFreeUnits(), activePlayer);
		updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);
		refreshGame();
	}
	
	private void turnSequence() {
		territoryConquered = false;
		int playersRemaining = ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS;
		while (true) {
			if(activePlayer.gameOver()) {
				for (int i = 0; i < playerOrder.length; i++) {
					if (i == 0 && playerOrder[i].equals(activePlayer)) {
						playerOrder[playerOrder.length - 1].setNextPlayer(playerOrder[1]);
					}
					else if (i == playerOrder.length - 1 && playerOrder[i].equals(activePlayer)) {
						playerOrder[playerOrder.length - 2].setNextPlayer(playerOrder[0]);
					}
					else if (playerOrder[i].equals(activePlayer)) {
						playerOrder[i - 1].setNextPlayer(playerOrder[i + 1]);
					}
				}
				playersRemaining--;
				if (playersRemaining == 1) {
					break;
				}
				changePlayer();
			}
			activePlayer.addFreeUnits(calculateReinforcements());
			while (activePlayer.getNumberOfFreeUnits() > 0) {
				placeUnitsOnSpecifiedTerritory(true, 0);
			}
			
			if (activePlayer.getNumberOfTerritoryCards() >= 3) {
				exchangeTerritoryCards();
			}
			
			if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {
				boolean launchAttack = true;
				boolean firstAttack = true;
				humanAttackChoice(launchAttack, firstAttack);
			}
			else {
				botAttackChoice();
			}
			boolean fortifyAvailable = false;
			for (Map.Entry<String, Territory> territory : ModelConstants.TERRITORIES.entrySet()) {
				if (territory.getValue().getOccupier().equals(activePlayer) && territory.getValue().getNumberOfUnits() > 1) {
					int territoryIndex = getTerritoryIndex(territory.getValue());
					for (int adjacentIndex : ModelConstants.ADJACENT[territoryIndex]) {
						if (ModelConstants.TERRITORIES.get(ModelConstants.COUNTRY_NAMES[adjacentIndex]).getOccupier().equals(activePlayer)) {
							fortifyAvailable = true;
						}
					}
				}
			}
			if (fortifyAvailable) {
				fortifyChoice();
			}
			else {
				addMessageToLog(MessageType.INFO, "As " + getPlayerName(activePlayer) + " has no connected territories with more than 1 unit in one of these territories, territory fortification is not available.", 1000);
			}
			if (territoryConquered) {
				collectTerritoryCard();
			}
			changePlayer();
		}
		for (int i = 0; i < playerOrder.length; i++) {
			if (!playerOrder[1].gameOver()) {
				addMessageToLog(MessageType.SUCCESS, getPlayerName(playerOrder[i]) + " WINS!", 1000);
			}
		}
	}
	
	private void exchangeTerritoryCards() {

		String exchangeChoice;

		if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			boolean validChoice = false;
			while (!validChoice) {
				exchangeChoice = getCommand(getPlayerName(activePlayer) + ", as you have " + activePlayer.getTotalNumberOfUnits() + ", would you like to exchange a set of cards for reinforcements?", "No choice entered. Choose \"Yes\" or \"No\".", 1000);
				if (exchangeChoice.equalsIgnoreCase("y") || exchangeChoice.equalsIgnoreCase("yes")) {
					getNextReinforcementNumber();
					activePlayer.removeTerritoryCardSet();
					activePlayer.addFreeUnits(cardExchangeReinforcementNumber);
					validChoice = true;
				}
				else if (exchangeChoice.equalsIgnoreCase("n") || exchangeChoice.equalsIgnoreCase("no")) {
					validChoice = true;
				}
				else {
					addMessageToLog(MessageType.ERROR, "Enter \"yes\" or \"no\".", 0);
				}
			}
		}
		else {
			getNextReinforcementNumber();
			activePlayer.removeTerritoryCardSet();
			activePlayer.addFreeUnits(cardExchangeReinforcementNumber);
		}
		
		while (activePlayer.getNumberOfFreeUnits() > 0) {
			placeUnitsOnSpecifiedTerritory(true, 0);
		}
	}
	
	private void getNextReinforcementNumber() {
		if (cardExchangeReinforcementNumber == 0) {
			cardExchangeReinforcementNumber = ModelConstants.NUM_REINFORCEMENT_PER_TERRITORY_CARD[0];
		} 		
		else if (cardExchangeReinforcementNumber != 60) {
			for (int i = 0; i < ModelConstants.NUM_REINFORCEMENT_PER_TERRITORY_CARD.length; i++) {
				if (cardExchangeReinforcementNumber == ModelConstants.NUM_REINFORCEMENT_PER_TERRITORY_CARD[i]) {
					cardExchangeReinforcementNumber = ModelConstants.NUM_REINFORCEMENT_PER_TERRITORY_CARD[i + 1];
				}
			}
		}
	}

	private void humanAttackChoice(boolean launchAttack, boolean firstAttack) {
		while (launchAttack) {
			boolean attackAvailable = false;
			for (Map.Entry<String, Territory> territory : ModelConstants.TERRITORIES.entrySet()) {
				if (territory.getValue().getOccupier().equals(activePlayer) && territory.getValue().getNumberOfUnits() > 1) {
					attackAvailable = true;
				}
			}
			if (attackAvailable) {
				boolean validCommand = false;
				while (!validCommand) {
					String attackChoice;
					if (firstAttack) {
						attackChoice = getCommand(getPlayerName(activePlayer) + ", would you like to attack an opponent's territory?", "No choice entered. Choose \"Yes\" or \"No\".", 1000);
					}
					else {
						attackChoice = getCommand(getPlayerName(activePlayer) + ", would you like to launch another attack?", "No choice entered. Choose \"Yes\" or \"No\".", 1000);
					}
					if (attackChoice.equalsIgnoreCase("y") || attackChoice.equalsIgnoreCase("yes")) {
						validCommand = true;
						firstAttack = false;
						getTerritoriesForHumanAttack();
					}
					else if (attackChoice.equalsIgnoreCase("n") || attackChoice.equalsIgnoreCase("no")) {
						validCommand = true;
						launchAttack = false;
					}
					else {
						addMessageToLog(MessageType.ERROR, "Enter \"yes\" or \"no\".", 0);
					}
				}
			}
			else {
				addMessageToLog(MessageType.INFO, "As " + getPlayerName(activePlayer) + " has no territories with more than 1 unit, they cannot launch an attack.", 1000);
			}
		}
	}
	
	private void botAttackChoice() {
		
		addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " is deciding whether to launch an attack or not...", 1000);
		RandomNumberGenerator randomChoice = new RandomNumberGenerator(0, 2);
		int choice = randomChoice.getRandomNumber();
		boolean attackAvailable = false;
		for (Map.Entry<String, Territory> territory : ModelConstants.TERRITORIES.entrySet()) {
			if (territory.getValue().getOccupier().equals(activePlayer) && territory.getValue().getNumberOfUnits() > 1) {
				attackAvailable = true;
			}
		}
		if (choice == 1 && attackAvailable) {
			getTerritoriesForBotAttack();
		}
		else if (choice == 0) {
			addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has decided not to attack an opponent's territory.", 1000);
		}
	}
	
	private void getTerritoriesForHumanAttack() {
		addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has chosen to launch an attack on a neighbouring territory.", 0);
		Territory attackingTerritory = null;
		boolean validAttackingTerritory = false;
		while (!validAttackingTerritory) {
			String userInput = getCommand(getPlayerName(activePlayer) + ", which of your territories would you like to attack from?", "No territory entered.", 2000);
			ArrayList<String> matchedTerritories = createListOfMatchedCountries(userInput);
			if (matchedTerritories.size() > 1) {
				displayUnambiguousSelectionMessage(matchedTerritories);
			}
			else if (matchedTerritories.size() == 0) {									
				addMessageToLog(MessageType.ERROR, "Invalid command. Enter the name of one of your territories.", 0);
			}
			else {
				Territory chosenTerritory = ModelConstants.TERRITORIES.get(matchedTerritories.get(0));
				if (!chosenTerritory.getOccupier().equals(activePlayer)) {
					addMessageToLog(MessageType.ERROR, "You do not occupy " + matchedTerritories.get(0) + ". Try again.", 0);
				}
				else if (chosenTerritory.getNumberOfUnits() < 2) {
					addMessageToLog(MessageType.ERROR, "You must have at least 2 units occupying a territory to launch an attack from that territory.", 0);
				}
				else {
					validAttackingTerritory = true;
					attackingTerritory = chosenTerritory;
					addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has chosen to launch an invasion from " + getTerritoryName(attackingTerritory), 0);
				}
			}
		}
		Territory defendingTerritory = null;
		boolean validDefendingTerritory = false;
		while (!validDefendingTerritory) {
			String userInput = getCommand(getPlayerName(activePlayer) + ", which neighbouring territory would you like to invade?", "No territory entered.", 1000);
			ArrayList<String> matchedTerritories = createListOfMatchedCountries(userInput);
			if (matchedTerritories.size() > 1) {
				displayUnambiguousSelectionMessage(matchedTerritories);
			}
			else if (matchedTerritories.size() == 0) {									
				addMessageToLog(MessageType.ERROR, "Invalid command. Enter the name of one of your opponent's territories.", 0);
			}
			else {
				Territory chosenTerritory = ModelConstants.TERRITORIES.get(matchedTerritories.get(0));
				if (chosenTerritory.getOccupier().equals(activePlayer)) {
					addMessageToLog(MessageType.ERROR, matchedTerritories.get(0) + " is your territory. Try again.", 0);
				}
				else {
					int attackingTerritoryIndex = getTerritoryIndex(attackingTerritory);
					int chosenTerritoryIndex = getTerritoryIndex(chosenTerritory);
					for (int i = 0; i < ModelConstants.ADJACENT[attackingTerritoryIndex].length; i++) {
						if (ModelConstants.ADJACENT[attackingTerritoryIndex][i] == chosenTerritoryIndex) {
							validDefendingTerritory = true;
							defendingTerritory = chosenTerritory;
							addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " is going to attack " + getTerritoryName(defendingTerritory) + " from " + getTerritoryName(attackingTerritory), 0);
							getNumberOfUnitsForHumanAttack(attackingTerritory, defendingTerritory);
							return;
						}
					}
					addMessageToLog(MessageType.ERROR, getTerritoryName(chosenTerritory) + " is not adjacent to " + getTerritoryName(attackingTerritory) + ". Try again.", 0);
				}	
			}
		}
	}
	
	private void getNumberOfUnitsForHumanAttack(Territory attackingTerritory, Territory defendingTerritory) {
		
		updateActivePlayerInPlayerPanel(attackingTerritory.getOccupier(), Color.RED);
		updateActivePlayerInPlayerPanel(defendingTerritory.getOccupier(), Color.BLUE);
		
		Player attackingPlayer = attackingTerritory.getOccupier();
		int numberOfAttackingUnits = -1;
		if (attackingTerritory.getNumberOfUnits() == 2) {
			addMessageToLog(MessageType.INFO, getTerritoryName(attackingTerritory) + " has 2 occupying units, therefore only 1 unit can be used in this attack.", 1000);
			numberOfAttackingUnits = 1;
		}
		else if (attackingTerritory.getNumberOfUnits() > 2) {
			boolean validNumber = false;
			while (!validNumber) {
				String userInput = getCommand(getPlayerName(attackingPlayer) + ", how many units would you like to use in this attack?", "No number of units entered.", 1000);
				try {
					numberOfAttackingUnits = Integer.parseInt(userInput);
				}
				catch (NumberFormatException e){
					addMessageToLog(MessageType.ERROR, userInput + " is not a valid number.", 0);
				}
				if (numberOfAttackingUnits > 0) {
					if (attackingTerritory.getNumberOfUnits() == 3 && numberOfAttackingUnits != 1 && numberOfAttackingUnits != 2) {
							addMessageToLog(MessageType.ERROR, "As " + getTerritoryName(attackingTerritory) + " has 3 occupying units, you can only use 1 or 2 units in this attack.", 0);
					}
					else if (attackingTerritory.getNumberOfUnits() == 4 && numberOfAttackingUnits != 1 && numberOfAttackingUnits != 2 && numberOfAttackingUnits != 3) {
							addMessageToLog(MessageType.ERROR, "As " + getTerritoryName(attackingTerritory) + " has 4 occupying units, you can use at most 3 units in this attack.", 0);
					}
					else if (attackingTerritory.getNumberOfUnits() > 4 && numberOfAttackingUnits != 1 && numberOfAttackingUnits != 2 && numberOfAttackingUnits != 3) {
							addMessageToLog(MessageType.ERROR, "You can use at most 3 units in an attack", 0);
					}
					else {
						validNumber = true;
					}
				}
				else {
					addMessageToLog(MessageType.ERROR, numberOfAttackingUnits + " is not a valid number of units.", 0);
				}
			}
		}
		
		Player defendingPlayer = defendingTerritory.getOccupier();
		int numberOfDefendingUnits = -1;
		if (defendingTerritory.getNumberOfUnits() == 1) {
			addMessageToLog(MessageType.INFO, "As " + getTerritoryName(defendingTerritory) + " only has 1 unit, this unit will be used to defend the invasion.", 1000);
			numberOfDefendingUnits = 1;
		}
		else {
			if (defendingPlayer.getPlayerType().equals(PlayerType.BOT)) {
				RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(1, 2);
				numberOfDefendingUnits = randomNumberGenerator.getRandomNumber();
				String units = "";
				if (numberOfDefendingUnits == 1) {
					units = numberOfDefendingUnits + " unit.";
				}
				else {
					units = numberOfDefendingUnits + " units.";
				}
				addMessageToLog(MessageType.INFO, getPlayerName(defendingPlayer) + " has chosen to defend " + getTerritoryName(defendingTerritory) + " with " + units, 1000);
			}
			else {
				boolean validNumber = false;
				while (!validNumber) {
					boolean messageSent = false;
					String userInput = getCommand(getPlayerName(defendingPlayer) + ", how many units would you like to defend with in this attack on " + getTerritoryName(defendingTerritory) + "?", "No number of units entered.", 1000);
					try {
						numberOfDefendingUnits = Integer.parseInt(userInput);
					}
					catch (NumberFormatException e){
						addMessageToLog(MessageType.ERROR, userInput + " is not a valid number.", 0);
						messageSent = true;
						continue;
					}
					if (numberOfDefendingUnits > 0) {
						if (numberOfDefendingUnits != 1 && numberOfDefendingUnits != 2) {
							addMessageToLog(MessageType.ERROR, "You can only use 1 or 2 units to defend in an attack.", 0);
						}
						else {
							validNumber = true;
						}
					}
					else if (!messageSent){
						addMessageToLog(MessageType.ERROR, numberOfDefendingUnits + " is not a valid number of units.", 0);
					}
				}
			}
		}
		String attackingUnits = "";
		String defendingUnits = "";
		if (numberOfAttackingUnits == 1) {
			attackingUnits = numberOfAttackingUnits + " unit. ";
		}
		else {
			attackingUnits = numberOfAttackingUnits + " units. ";
		}
		if (numberOfDefendingUnits == 1) {
			defendingUnits = numberOfDefendingUnits + " unit.";
		}
		else {
			defendingUnits = numberOfDefendingUnits + " units.";
		}
		addBlankLineToLog(2000);
		addMessageToLog(MessageType.SUCCESS, getPlayerName(attackingPlayer) + " is launching an attack on " + getTerritoryName(defendingTerritory) + " from "  + getTerritoryName(attackingTerritory) + " using " + attackingUnits 
				+ getPlayerName(defendingPlayer) + " will defend " + getTerritoryName(defendingTerritory) + " using " + defendingUnits, 0);
		
		battleSequence(attackingTerritory, numberOfAttackingUnits, defendingTerritory, numberOfDefendingUnits);
	}
	
	private void getTerritoriesForBotAttack() {
		RandomNumberGenerator randomTerritoryIndex = new RandomNumberGenerator(0, 42);
		int index;
		Territory attackingTerritory;
		do {
			index = randomTerritoryIndex.getRandomNumber();
			attackingTerritory = ModelConstants.TERRITORIES.get(ModelConstants.COUNTRY_NAMES[index]);
		} while (!attackingTerritory.getOccupier().equals(activePlayer) || attackingTerritory.getNumberOfUnits() < 2);
		
		RandomNumberGenerator randomAdjacentIndex = new RandomNumberGenerator(0, (ModelConstants.ADJACENT[index].length));
		int adjacentIndex;
		Territory defendingTerritory;
		do {
			adjacentIndex = randomAdjacentIndex.getRandomNumber();
			defendingTerritory = ModelConstants.TERRITORIES.get(ModelConstants.COUNTRY_NAMES[ModelConstants.ADJACENT[index][adjacentIndex]]);
		} while (defendingTerritory.getOccupier().equals(activePlayer));
		addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has chosen to launch an attack on " + getTerritoryName(defendingTerritory) + " from " + getTerritoryName(attackingTerritory) + ".", 1000);
		getNumberOfUnitsForBotAttack(attackingTerritory, defendingTerritory);
	}
	
	private void getNumberOfUnitsForBotAttack(Territory attackingTerritory, Territory defendingTerritory) {
		int numberOfAttackingUnits = 0;
		Player attackingPlayer = attackingTerritory.getOccupier();
		if (attackingTerritory.getNumberOfUnits() == 2) {
			numberOfAttackingUnits = 1;
		}
		else if (attackingTerritory.getNumberOfUnits() == 3) {
			RandomNumberGenerator random = new RandomNumberGenerator(1, 2);
			numberOfAttackingUnits = random.getRandomNumber();
		}
		else if (attackingTerritory.getNumberOfUnits() > 3) {
			RandomNumberGenerator random = new RandomNumberGenerator(1, 3);
			numberOfAttackingUnits = random.getRandomNumber();
		}
		
		String attackingUnits = "";
		if (numberOfAttackingUnits == 1) {
			attackingUnits = numberOfAttackingUnits + " unit";
		}
		else if (numberOfAttackingUnits > 1) {
			attackingUnits = numberOfAttackingUnits + " units";
		}
		addMessageToLog(MessageType.INFO, getPlayerName(attackingPlayer) + " has chosen to use " + attackingUnits + " in this invasion.", 1000);
		int numberOfDefendingUnits = 0;
		Player defendingPlayer = defendingTerritory.getOccupier();
		if (defendingTerritory.getNumberOfUnits() == 1) {
			addMessageToLog(MessageType.INFO, "As only 1 unit occupies " + getTerritoryName(defendingTerritory) + ", it will be used to defend this attack.", 1000);
			numberOfDefendingUnits = 1;
		}
		else if (defendingPlayer.getPlayerType().equals(PlayerType.BOT)) {
			RandomNumberGenerator random = new RandomNumberGenerator(1, 2);
			numberOfDefendingUnits = random.getRandomNumber();
		}
		else if (defendingPlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			boolean validNumber = false;
			while (!validNumber) {
				String userInput = getCommand(getPlayerName(defendingPlayer) + ", how many units would you like to use to defend " + getTerritoryName(defendingTerritory) + "?", "No number of units entered.", 1000);
				try {
					numberOfDefendingUnits = Integer.parseInt(userInput);
				}
				catch (NumberFormatException e){
					addMessageToLog(MessageType.ERROR, userInput + " is not a valid number.", 0);
					continue;
				}
				if (numberOfDefendingUnits > 0) {
					if (numberOfDefendingUnits != 1 && numberOfDefendingUnits != 2) {
						addMessageToLog(MessageType.ERROR, "You can only use 1 or 2 units to defend in an attack.", 0);
					}
					else {
						validNumber = true;
					}
				}
				else {
					addMessageToLog(MessageType.ERROR, numberOfDefendingUnits + " is not a valid number of units.", 0);
				}
			}
		}
		String defendingUnits = "";
		if (numberOfDefendingUnits == 1) {
			defendingUnits = numberOfDefendingUnits + " unit.";
		}
		else if (numberOfDefendingUnits > 1) {
			defendingUnits = numberOfDefendingUnits + " units.";
		}
		addMessageToLog(MessageType.SUCCESS, getPlayerName(attackingPlayer) + " is launching an attack on " + getTerritoryName(defendingTerritory) + " from "  + getTerritoryName(attackingTerritory) + " using " + attackingUnits + ". " 
				+ getPlayerName(defendingPlayer) + " will defend " + getTerritoryName(defendingTerritory) + " using " + defendingUnits, 1000);
	
		battleSequence(attackingTerritory, numberOfAttackingUnits, defendingTerritory, numberOfDefendingUnits);
	}
	
	private void battleSequence(Territory attackingTerritory, int attackingUnits, Territory defendingTerritory, int defendingUnits) {		
		Player attackingPlayer = attackingTerritory.getOccupier();
		Player defendingPlayer = defendingTerritory.getOccupier();
		RandomNumberGenerator dice = new RandomNumberGenerator();
		int[] attackingDiceNumbers = new int[3];
		int[] defendingDiceNumbers = new int[2];
		
		if (attackingPlayer.getPlayerType().equals(PlayerType.HUMAN) || defendingPlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			boolean validCommand = false;
			while (!validCommand) {
				String userInput = getCommand(getPlayerName(attackingPlayer) + " and " + getPlayerName(defendingPlayer) + ", roll a dice for each attacking and defending unit...", "No command entered. Type \"Roll the Dice\" or something similar.", 2000);
				if (userInput.equalsIgnoreCase("roll") || userInput.equalsIgnoreCase("roll the dice") || userInput.equalsIgnoreCase("roll dice")) {
					validCommand = true;
				}
				else {
					addMessageToLog(MessageType.ERROR, "Invalid command. Type \'Roll the dice\' or something similar.", 0);
				}
			}
		}
		else {
			addMessageToLog(MessageType.INFO, getPlayerName(attackingPlayer) + " and " + getPlayerName(defendingPlayer) + " will now roll a dice for each of their attacking and defending units...", 2000);
		}
		for (int i = 0; i < attackingUnits; i++) {
			addMessageToLog(MessageType.INFO, getPlayerName(attackingPlayer) + " is rolling the dice for his attacking unit " + (i + 1) + "...", 1000);
			attackingDiceNumbers[i] = dice.rollDice();
			addMessageToLog(MessageType.SUCCESS, getPlayerName(attackingPlayer) + " rolled a " + attackingDiceNumbers[i], 2000);
			addBattleDice(ViewConstants.ATTACKING, i + 1, attackingDiceNumbers[i], ViewConstants.ATTACKING_DICE_ROTATIONS[i]);
		}
		for (int i = 0; i < defendingUnits; i++) {
			addMessageToLog(MessageType.INFO, getPlayerName(defendingPlayer) + " is rolling the dice for his defending unit " + (i + 1) + "...", 1000);
			defendingDiceNumbers[i] = dice.rollDice();
			addMessageToLog(MessageType.SUCCESS, getPlayerName(defendingPlayer) + " rolled a " + defendingDiceNumbers[i], 2000);
			addBattleDice(ViewConstants.DEFENDING, i + 1, defendingDiceNumbers[i], ViewConstants.DEFENDING_DICE_ROTATIONS[i]);
		}
		
		attackingDiceNumbers = ascendingSort(attackingDiceNumbers);
		defendingDiceNumbers = ascendingSort(defendingDiceNumbers);
		int attackingUnitsLost = 0;
		int defendingUnitsLost = 0;
		int numberOfComparisons = attackingDiceNumbers.length;
		if (defendingDiceNumbers.length < attackingDiceNumbers.length) {
			numberOfComparisons = defendingDiceNumbers.length;
		}
				
		for (int i = 0; i < numberOfComparisons; i++) {
			if (attackingDiceNumbers[i] > 0 && defendingDiceNumbers[i] > 0) {
				if (attackingDiceNumbers[i] > defendingDiceNumbers[i]) {
					addMessageToLog(MessageType.BLANK, "\n" + getPlayerName(attackingPlayer) + "'s unit (" + attackingDiceNumbers[i] + ") defeats " + getPlayerName(defendingPlayer) + "'s unit (" + defendingDiceNumbers[i] + ").", 1000);
					defendingUnitsLost++;;
				}
				else if (defendingDiceNumbers[i] > attackingDiceNumbers[i]) {
					addMessageToLog(MessageType.BLANK, "\n" + getPlayerName(defendingPlayer) + "'s unit (" + defendingDiceNumbers[i] + ") defeats " + getPlayerName(attackingPlayer) + "'s unit (" + attackingDiceNumbers[i] + ").", 1000);
					attackingUnitsLost++;
				}
				else {
					addMessageToLog(MessageType.BLANK, "\nAs a tie results in a victory for the defending player, " + getPlayerName(defendingPlayer) + "'s unit (" + defendingDiceNumbers[i] + ") defeats " + getPlayerName(attackingPlayer) + "'s unit (" + attackingDiceNumbers[i] + ").", 1000);
					attackingUnitsLost++;
				}
			}
		}
		
		addBlankLineToLog(2000);
		addMessageToLog(MessageType.SUCCESS, "Battle complete.", 0);
		addBlankLineToLog(2000);
		if (attackingUnitsLost == 1) {
			addMessageToLog(MessageType.BLANK, getPlayerName(attackingPlayer) + " lost " + attackingUnitsLost + " unit from " + getTerritoryName(attackingTerritory) + ".", 1000);
		}
		else if (attackingUnitsLost > 1) {
			addMessageToLog(MessageType.BLANK, getPlayerName(attackingPlayer) + " lost " + attackingUnitsLost + " units from " + getTerritoryName(attackingTerritory) + ".", 1000);
		}
		if (defendingUnitsLost == 1) {
			addMessageToLog(MessageType.BLANK, getPlayerName(defendingPlayer) + " lost " + defendingUnitsLost + " unit from " + getTerritoryName(defendingTerritory)+ ".", 1000);
		}
		else if (defendingUnitsLost > 1) {
			addMessageToLog(MessageType.BLANK, getPlayerName(defendingPlayer) + " lost " + defendingUnitsLost + " units from " + getTerritoryName(defendingTerritory) + ".", 1000);
		}
		
		attackingUnits -= attackingUnitsLost;
		defendingUnits -= defendingUnitsLost;
		attackingTerritory.removeUnits(attackingUnitsLost);
		defendingTerritory.removeUnits(defendingUnitsLost);
		removeActivePlayerInPlayerPanel(attackingPlayer);
		removeActivePlayerInPlayerPanel(defendingPlayer);
		removeDice();
		updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);
		
		if(defendingTerritory.getNumberOfUnits() == 0) {
			territoryConquered = true;
			addBlankLineToLog(4000);
			addMessageToLog(MessageType.SUCCESS, getPlayerName(attackingPlayer) + " has eliminated all of " + getPlayerName(defendingPlayer) + "'s units occupying " + getTerritoryName(defendingTerritory) +
					". " + getPlayerName(attackingPlayer) + " now takes control of " + getTerritoryName(defendingTerritory) + 
					". The units that " + getPlayerName(attackingPlayer) + " used to do so have been moved into " + getTerritoryName(defendingTerritory) + ".", 0);
			attackingTerritory.removeUnits(attackingUnits - attackingUnitsLost);
			defendingTerritory.setOccupier(attackingPlayer, attackingUnits - attackingUnitsLost);
			attackingPlayer.addUnits(attackingUnits - attackingUnitsLost);
			refreshGame();
			checkPlayerUnits();
			sendTroopsToConqueredTerritory(attackingPlayer, attackingTerritory, attackingTerritory.getNumberOfUnits(), defendingTerritory);
		}
		refreshGame();
		pauseExecution(2000);
	}
	
	private void checkPlayerUnits() {
		for (Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {
			if (player.getValue().getTotalNumberOfUnits() == 0) {
				addMessageToLog(MessageType.INFO, player.getKey() +  " has no more units and so has been eliminated from the game.", 2000);
			}
		}
	}

	private void sendTroopsToConqueredTerritory(Player attackingPlayer, Territory attackingTerritory, int availableUnits, Territory conqueredTerritory) {
		int numberToSend = 0;
		if (attackingTerritory.getNumberOfUnits() == 1) {
			addMessageToLog(MessageType.INFO, getPlayerName(attackingPlayer) + ", as there is only 1 unit in " + getTerritoryName(attackingTerritory) + ", no reinforcements can be sent into " + getTerritoryName(conqueredTerritory), 0);
		}
		else if (attackingPlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			boolean validAnswer = false;
			while (!validAnswer) {
				String userInput = getCommand(getPlayerName(activePlayer) + ", would you like to send any units from " + getTerritoryName(attackingTerritory) +
						" to your newly conquered territory " + getTerritoryName(conqueredTerritory) + "?", "No choice entered. Type \"Yes\" or \"No\".", 2000);
				if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("yes")) {
					validAnswer = true;
					boolean validNumber = false;
					while (!validNumber) {
						userInput = getCommand("How many units would you like to send ?", "Number of units not entered.", 500);
						try {
							numberToSend = Integer.parseInt(userInput);
						}
						catch (NumberFormatException e) {
							addMessageToLog(MessageType.ERROR, userInput + " is not a valid number.", 0);
						}
						if (numberToSend > attackingTerritory.getNumberOfUnits()) {
							addMessageToLog(MessageType.ERROR, "You only have " + availableUnits + " units in " + getTerritoryName(attackingTerritory) + ".", 0);
						}
						else if (numberToSend == attackingTerritory.getNumberOfUnits()) {
							addMessageToLog(MessageType.ERROR, "You must leave at least 1 unit in " + getTerritoryName(attackingTerritory) + ".", 0);
						}
						else if (numberToSend > 0 && numberToSend < availableUnits) {
							validNumber = true;
							attackingTerritory.removeUnits(numberToSend);
							conqueredTerritory.receiveUnits(numberToSend);
							attackingPlayer.addUnits(numberToSend);
							addMessageToLog(MessageType.SUCCESS, getPlayerName(attackingPlayer) + " has reinforced " + getTerritoryName(conqueredTerritory) 
							+ " with " + numberToSend + " units from " + getTerritoryName(attackingTerritory) + "." , 1000);
						}
						else {
							addMessageToLog(MessageType.ERROR, numberToSend + " is not a valid number.", 0);
						}
					}
				}
				else if (userInput.equalsIgnoreCase("n") || userInput.equalsIgnoreCase("no")) {
					validAnswer = true;
				}
				else {
					addMessageToLog(MessageType.ERROR, "Invalid choice. Enter \"yes\" or \"no\".", 0);
				}
			}
		}
		else {
			RandomNumberGenerator randomChoice = new RandomNumberGenerator(0, 2);
			int choice = randomChoice.getRandomNumber();
			if (choice == 0) {
				addMessageToLog(MessageType.INFO, getPlayerName(attackingPlayer) + " has decided not to send any units from " + getTerritoryName(attackingTerritory)
				+ " to their newly conquered territory, " + getTerritoryName(conqueredTerritory) + ".", 1000);
			}
			else {
				RandomNumberGenerator randomUnits = new RandomNumberGenerator(1, attackingTerritory.getNumberOfUnits() - 1);
				numberToSend = randomUnits.getRandomNumber();
				attackingTerritory.removeUnits(numberToSend);
				conqueredTerritory.receiveUnits(numberToSend);
				addMessageToLog(MessageType.INFO, getPlayerName(attackingPlayer) + " has sent " + numberToSend + " units from " 
						+ getTerritoryName(attackingTerritory) + " to their newly conquered territory " + getTerritoryName(conqueredTerritory) + ".", 1000);
			}
		}
	}

	private int[] ascendingSort(int[] intArray) {
	    Integer[] IntegerArray = new Integer[intArray.length];
	    for (int i = 0; i < intArray.length; i++) {
	    	IntegerArray[i] = Integer.valueOf(intArray[i]);
	    }
	    Arrays.sort(IntegerArray, Collections.reverseOrder());
	    for (int i = 0; i < IntegerArray.length; i++) {
	    	intArray[i] = IntegerArray[i];
	    }
	    return intArray;
	}

	private void fortifyChoice() {
		if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			String userInput;
			boolean validCommand = false;
			while (!validCommand) {
				userInput = getCommand("Would you like to fortify one of your territories?", "No choice entered. Type \"Yes\" or \"No\".", 1000);
				if (userInput.equalsIgnoreCase("y") ||userInput.equalsIgnoreCase("yes")) {
					validCommand = true;
					fortifyTerritory();
				}
				else if (userInput.equalsIgnoreCase("n") ||userInput.equalsIgnoreCase("no")) {
					addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has chosen NOT to fortify a territory.", 0);
					validCommand = true;
				}
				else {
					addMessageToLog(MessageType.ERROR, "Invalid command. Enter \"yes\" or \"no\".", 0);
				}
			}
		}
		else {
			addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " is deciding whether or not they would like to fortify a territory...", 1000);
			RandomNumberGenerator random = new RandomNumberGenerator(0, 2);
			int randomChoice = random.getRandomNumber();
			if (randomChoice == 0) {
				addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " has chosen NOT to fortify a territory.", 1000);
			}
			else {
				fortifyTerritory();
			}
		}
	}

	private void fortifyTerritory() {
		Territory sendingTerritory = null;
		Territory receivingTerritory = null;
		int numberOfUnits = -1;
		boolean validTerritory = false;
		String userInput;
		if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			while (!validTerritory) {
				userInput = getCommand(getPlayerName(activePlayer) + ", which of your territories would you like to fortify?", "No territory name entered.", 1000);
				ArrayList<String> matchedTerritories = createListOfMatchedCountries(userInput);
				if (matchedTerritories.size() > 1) {
					displayUnambiguousSelectionMessage(matchedTerritories);
				}
				else if (matchedTerritories.size() == 0) {									
					addMessageToLog(MessageType.ERROR, "Invalid command. Enter the name of one of your territories.", 0);
				}
				else if (!ModelConstants.TERRITORIES.get(matchedTerritories.get(0)).getOccupier().equals(activePlayer)) {
					addMessageToLog(MessageType.ERROR, "You do not occupy " + matchedTerritories.get(0) + ". Try again.", 0);
				}
				else {
					validTerritory = true;
					receivingTerritory = ModelConstants.TERRITORIES.get(matchedTerritories.get(0));
				}
			}
			validTerritory = false;
			while (!validTerritory) {
				userInput = getCommand(getPlayerName(activePlayer) + ", which of your territories would you like to send units to " + getTerritoryName(receivingTerritory) + " from?", "No territory name entered.", 1000);
				ArrayList<String> matchedTerritories = createListOfMatchedCountries(userInput);
				if (matchedTerritories.size() > 1) {
					displayUnambiguousSelectionMessage(matchedTerritories);
				}
				else if (matchedTerritories.size() == 0) {									
					addMessageToLog(MessageType.ERROR, "Invalid command. Enter the name of one of your territories.", 0);
				}
				else if (!ModelConstants.TERRITORIES.get(matchedTerritories.get(0)).getOccupier().equals(activePlayer)) {
					addMessageToLog(MessageType.ERROR, "You do not occupy " + matchedTerritories.get(0) + ". Try again.", 0);
				}
				else if (ModelConstants.TERRITORIES.get(matchedTerritories.get(0)).getNumberOfUnits() == 1) {
					addMessageToLog(MessageType.ERROR, "You need more than 1 unit on " + matchedTerritories.get(0) + " to send units from it." , 0);
					fortifyChoice();
				}
				else {
					sendingTerritory = ModelConstants.TERRITORIES.get(matchedTerritories.get(0));
					if(findPath(sendingTerritory, receivingTerritory)) {
						validTerritory = true;
						boolean validNumber = false;
						while (!validNumber) {
							userInput = getCommand("How many units would you like to send from " + getTerritoryName(sendingTerritory) + " to " + getTerritoryName(receivingTerritory) + "?", "No number of units entered.", 1000);
							try {
								numberOfUnits = Integer.parseInt(userInput);
							}
							catch (NumberFormatException e) {
								addMessageToLog(MessageType.ERROR, userInput + " is not a valid number.", 0);
							}
							if (numberOfUnits < 0) {
								addMessageToLog(MessageType.ERROR, numberOfUnits + " is not a valid number of units to send.", 0);
							}
							else if (numberOfUnits > sendingTerritory.getNumberOfUnits()) {
								addMessageToLog(MessageType.ERROR, getTerritoryName(sendingTerritory) + " ony has " + sendingTerritory.getNumberOfUnits() + " units.", 0);
							}
							else if (numberOfUnits == sendingTerritory.getNumberOfUnits()) {
								addMessageToLog(MessageType.ERROR, "You must leave at least 1 unit at " + getTerritoryName(sendingTerritory), 0);
							}
							else {
								validNumber = true;
							}
						}
					}
					else {
						addMessageToLog(MessageType.ERROR, "There is no path from " +  getTerritoryName(sendingTerritory) + " to " + getTerritoryName(receivingTerritory) + " through your own territories.", 0);
					}
				}
			}
		}
		else { //bot
			RandomNumberGenerator randomTerritoryIndex = new RandomNumberGenerator(0, 42);
			int territoryIndex;
			do {
				territoryIndex = randomTerritoryIndex.getRandomNumber();
				sendingTerritory = ModelConstants.TERRITORIES.get(ModelConstants.COUNTRY_NAMES[territoryIndex]);
			} while (!sendingTerritory.getOccupier().equals(activePlayer) || sendingTerritory.getNumberOfUnits() < 2);
			for (Map.Entry<String, Territory> territory : ModelConstants.TERRITORIES.entrySet()) {
				receivingTerritory = territory.getValue();
				if (!receivingTerritory.equals(sendingTerritory) && receivingTerritory.getOccupier().equals(activePlayer) && findPath(sendingTerritory, receivingTerritory)) {
					validTerritory = true;
					break;
				}
			}
			if (!validTerritory) {
				addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " has chosen NOT to fortify a territory.", 1000);
				return;
			}
			RandomNumberGenerator randomUnitsNumber = new RandomNumberGenerator(1, sendingTerritory.getNumberOfUnits() - 1);
			numberOfUnits = randomUnitsNumber.getRandomNumber();	
		}
		
		String unitsString = " units ";
		if (numberOfUnits == 1) {
			unitsString = " unit ";
		}
		sendingTerritory.removeUnits(numberOfUnits);
		receivingTerritory.receiveUnits(numberOfUnits);
		activePlayer.addUnits(numberOfUnits);
		addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has fortified " + getTerritoryName(receivingTerritory) + " with " + numberOfUnits + unitsString +  "from " + getTerritoryName(sendingTerritory) + ".", 1000);
		refreshGame();
	}
	
	private void collectTerritoryCard() {
		RandomNumberGenerator randomCardIndex = new RandomNumberGenerator(0, freeCards.size());
		if (activePlayer.getPlayerType().equals(PlayerType.HUMAN)) {
			addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + ", as you have conquered a territory, you now get to collect a random territory card.", 1000);
			boolean cardPicked = false;
			while (!cardPicked) {
				String userInput = getCommand("Pick a card from the deck.", "No command entered. Type \"Pick Card\" or something similar to pick a territory card from the pile.", 1000);
				if (userInput.equalsIgnoreCase("pick a card") || userInput.equalsIgnoreCase("pick card") || userInput.equalsIgnoreCase("card")) {
					cardPicked = true;
				}
				else {
					addMessageToLog(MessageType.ERROR, "Invlaid command. Type \"Pick a card\" or something similar.", 0);
				}
			}
		}
		else {
			addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " is now picking a random territory card as they conquered an opponent's territory during their turn.", 1000);
		}
		String cardSelected = freeCards.get(randomCardIndex.getRandomNumber()); 
		freeCards.remove(cardSelected);	
		activePlayer.addTerritoryCard();
		String card = " territory card";
		if (activePlayer.getNumberOfTerritoryCards() > 1) {
			card = " territory cards";
		}
		addMessageToLog(MessageType.INFO, getPlayerName(activePlayer) + " has picked the " + cardSelected + " card and now holds " 
				+ activePlayer.getNumberOfTerritoryCards() + card + ".", 2000);
	}
	
	//Depth-First Search algorithm
	private boolean findPath(Territory startingTerritory, Territory finishingTerritory) {
		Stack<Territory> currentPath = new Stack<>();
		ArrayList<Territory> visitedTerritories = new ArrayList<>();
		currentPath.push(startingTerritory);		
		while (!currentPath.isEmpty()) {
			Territory currentTerritory = currentPath.pop();
			if (!visitedTerritories.contains(currentTerritory)) {
				visitedTerritories.add(currentTerritory);
				int currentTerritoryIndex = getTerritoryIndex(currentTerritory);
				for (int adjacentTerritoryIndex : ModelConstants.ADJACENT[currentTerritoryIndex]) {
					Territory adjacentTerritory = ModelConstants.TERRITORIES.get(ModelConstants.COUNTRY_NAMES[adjacentTerritoryIndex]);
					if (adjacentTerritory.equals(finishingTerritory)) {
						return true;
					}
					else if (!adjacentTerritory.getOccupier().equals(activePlayer)) {
						continue;
					}
					else {
						currentPath.push(adjacentTerritory);
					}
				}
			}
		}
		return false;
	}

	//Show white player order dice image
	private void addPlayerOrderDicePanel(int numberRolled, int rotation) {
		getModelObserver().addPlayerOrderDicePanel(numberRolled, rotation);
		refreshGame();
	}
	
	private void removeDice() {
		getModelObserver().removeDice();
		refreshGame();
	}
	
	private void addBattleDice(String diceType, int diceNumber, int numberRolled, int rotation) {
		getModelObserver().addBattleDice(diceType, diceNumber, numberRolled, rotation);
		refreshGame();
	}
	
	//show number in player panel (for dice number rolled and number of free units available
	private void addNumberToPlayerPanel(int number, Player player) {
		getModelObserver().addNumberToPlayerPanel(number, player);
	}
	
	//Show colour choices to players
	private void setPlayerPanelVisible() {
		getModelObserver().setPlayerPanelVisible();
	}
	
	//Assign correct player name to each player
	private void setPlayerColors() {
		getModelObserver().setPlayerColors();
	}
	
	//Reorder player panel based on player order
	private void updatePlayerPanelOrder(Player[] playerOrder) {
		addMessageToLog(MessageType.INFO, "Reordering the player labels above...", 2000);
		getModelObserver().updatePlayerPanelOrder(playerOrder);
		addMessageToLog(MessageType.SUCCESS, "Player labels are now in the order in which they take turns", 2000);
	}
	
	private String getPlayerName(Player player) {
		return HashMapUtilities.getKeyFromValue(ModelConstants.PLAYERS, player);
	}
	
	private String getTerritoryName(Territory territory) {
		return HashMapUtilities.getKeyFromValue(ModelConstants.TERRITORIES, territory);
	}
	
	private void setPlayerOneActive() {
		activePlayer = playerOrder[0];
		updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);
	}
	
	//Change to next player
	private void changePlayer() {
		activePlayer = activePlayer.getNextPlayer();
		pauseExecution(100);
		updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);
	}
	
	//Highlight active player in player panel
	private void updateActivePlayerInPlayerPanel(Player player, Color color) {
		getModelObserver().updateActivePlayerInPlayerPanel(player, color);
	}
	
	private void removeActivePlayerInPlayerPanel(Player player) {
		getModelObserver().removeActivePlayerInPlayerPanel(player);
	}
	
	//Refresh map
	private void refreshGame() {
		getModelObserver().refreshGame();
	}
	
	//Scale coordinates based on display size when game launches
	private void scaleCoordinates() {
		for (int i = 0; i < ModelConstants.COUNTRY_COORD.length; i++) {
			int countryCircleDiamDiff = ViewConstants.COUNTRY_CIRCLE_DIAMETER - (ViewConstants.MAP_WIDTH / 60);
			ModelConstants.COUNTRY_COORD[i][0] = ((ModelConstants.COUNTRY_COORD[i][0] / 1280) * ViewConstants.WINDOW_WIDTH) - (countryCircleDiamDiff / 2);
			ModelConstants.COUNTRY_COORD[i][1] = ((ModelConstants.COUNTRY_COORD[i][1] / 720) * ViewConstants.WINDOW_HEIGHT) - (countryCircleDiamDiff / 2);
			
		}
	}
	
	private int getTerritoryIndex(Territory territory) {
		String territoryName = HashMapUtilities.getKeyFromValue(ModelConstants.TERRITORIES, territory);
		for (int i = 0; i < ModelConstants.NUM_COUNTRIES; i++) {
			if (ModelConstants.COUNTRY_NAMES[i].equals(territoryName)) {
				return i;
			}
		}
		return -1;
	}
	
	//Searches for matching countries when user enters a shortened country name
	//Also allows user to enter two-worded countries without a space
	private ArrayList<String> createListOfMatchedCountries(String userCountry) {
		ArrayList<String> matches = new ArrayList<String>();
		for (String country : ModelConstants.COUNTRY_NAMES) {
			String countryNoSpaces = country.replaceAll("\\s","");
			String userCountryNoSpaces = userCountry.replaceAll("\\s","");
			if (userCountryNoSpaces.length() > countryNoSpaces.length()) {
				continue;
			}
			if (userCountryNoSpaces.equalsIgnoreCase(countryNoSpaces.substring(0, userCountryNoSpaces.length()))) {
				matches.add(country);
			}
		}
		return matches;
	}
	
	private void displayUnambiguousSelectionMessage(ArrayList<String> matches) {
		addMessageToLog(MessageType.ERROR, "Unambigous selection entered.", 0);
		addMessageToLog(MessageType.BLANK, "\nDid you mean:", 1000);	
		for (String country : matches) {
			addMessageToLog(MessageType.BLANK, country, 0);
		}
		addMessageToLog(MessageType.BLANK, "", 0);
	}
	
	private int calculateReinforcements() {
		addMessageToLog(MessageType.INFO, "Calculating the number of reinforcements " + getPlayerName(activePlayer) + " will receive...", 1500);
		int reinforcements = 0;
		int[] continentCount = new int[ModelConstants.NUM_CONTINENTS];
		for (Map.Entry<String, Territory> territory : ModelConstants.TERRITORIES.entrySet()) {
			if (territory.getValue().getOccupier().equals(activePlayer)) {
				reinforcements++;
				for (int i = 0; i < ModelConstants.NUM_CONTINENTS; i++) {
					if(territory.getValue().getContinent().equals(ModelConstants.CONTINENT_NAMES[i])) {
						continentCount[i]++;
					}
				}
			}
		}
		addMessageToLog(MessageType.BLANK, "\n" + getPlayerName(activePlayer) + " occupies " + reinforcements + " territories...", 1000);
		reinforcements /= 3;
		if (reinforcements < 3) {
			reinforcements = 3;
		}
		boolean continentSecured = false;
		for (int i = 0; i < ModelConstants.NUM_CONTINENTS; i++) {
			if (continentCount[i] == ModelConstants.CONTINENT_COUNTRIES[i]) {
				reinforcements += ModelConstants.CONTINENT_VALUES[i];
				addMessageToLog(MessageType.BLANK, getPlayerName(activePlayer) + " has also secured all of " + ModelConstants.CONTINENT_NAMES[i] + "...", 1000);
				continentSecured = true;
			}
		}
		if (continentSecured == false) {
			addMessageToLog(MessageType.BLANK, getPlayerName(activePlayer) + " has not secured any continents...\n", 1000);
		}
		addNumberToPlayerPanel(reinforcements, activePlayer);
		updateActivePlayerInPlayerPanel(activePlayer, Color.GREEN);
		addMessageToLog(MessageType.SUCCESS, getPlayerName(activePlayer) + " has received " + reinforcements + " units.", 2000);
		pauseExecution(2000);
		return reinforcements;
	}
	
	private void addBlankLineToLog(int delay) {
		addMessageToLog(MessageType.BLANK, "", delay);
	}
	
	//Adds a pause to the game's execution when needed (visual effect)
	private void pauseExecution(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}