/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel which initially displays choice of colours to players
//then the colours chosen by players and is finally reordered
//in the order in whoch players turns are
//Consists of 6 PlayerColorIndicatorPanels for each player

package view;

import java.awt.Color;
import java.util.Map;

import javax.swing.JPanel;

import control.RiskController;
import model.Player;
import util.HashMapUtilities;
import util.ModelConstants;
import util.ViewConstants;

public class PlayerPanel extends JPanel {

	private RiskController viewObserver;
	private PlayerColorIndicatorPanel[] playerColorPanels;		//Array of the 6 panels making up this panel
	private String[] playerNames;								//6 player names are passed into this class
	private Color[] playerColors;								//6 player colours are passed into this class
	
	public PlayerPanel() {
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		initialiseComponents();
		positionComponents();
		installComponents();
		setVisible(false);
	}
	
	public void setViewObserver(RiskController viewObserver) {
		this.viewObserver = viewObserver;
	}
	
	private RiskController getViewObserver() {
		return viewObserver;
	}
	
	//Initialises panel with 6 colours and labels to allow users to choose their colours
	private void initialiseComponents() {
		this.playerColorPanels = new PlayerColorIndicatorPanel[ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS];
		this.playerNames = new String[ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS];
		this.playerColors = new Color[ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS];
		for (int i = 0; i < ModelConstants.NUM_PLAYERS_PLUS_NEUTRALS; i++) {
			playerColors[i] = ViewConstants.PLAYER_COLORS[i];
			playerNames[i] = ViewConstants.PLAYER_COLOR_NAMES[i];
			playerColorPanels[i] = new PlayerColorIndicatorPanel(playerNames[i], playerColors[i]);
		}
	}
	
	private void positionComponents() {
		playerColorPanels[0].setBounds(ViewConstants.PLAYER_COLOR_PANEL_LEFT_X_COORD, ViewConstants.PLAYER_COLOR_PANEL_TOP_Y_COORD, ViewConstants.PLAYER_COLOR_PANEL_WIDTH, ViewConstants.PLAYER_COLOR_PANEL_HEIGHT);
		playerColorPanels[1].setBounds(ViewConstants.PLAYER_COLOR_PANEL_RIGHT_X_COORD, ViewConstants.PLAYER_COLOR_PANEL_TOP_Y_COORD, ViewConstants.PLAYER_COLOR_PANEL_WIDTH, ViewConstants.PLAYER_COLOR_PANEL_HEIGHT);
		playerColorPanels[2].setBounds(ViewConstants.PLAYER_COLOR_PANEL_LEFT_X_COORD, ViewConstants.PLAYER_COLOR_PANEL_MIDDLE_Y_COORD, ViewConstants.PLAYER_COLOR_PANEL_WIDTH, ViewConstants.PLAYER_COLOR_PANEL_HEIGHT);
		playerColorPanels[3].setBounds(ViewConstants.PLAYER_COLOR_PANEL_RIGHT_X_COORD, ViewConstants.PLAYER_COLOR_PANEL_MIDDLE_Y_COORD, ViewConstants.PLAYER_COLOR_PANEL_WIDTH, ViewConstants.PLAYER_COLOR_PANEL_HEIGHT);
		playerColorPanels[4].setBounds(ViewConstants.PLAYER_COLOR_PANEL_LEFT_X_COORD, ViewConstants.PLAYER_COLOR_PANEL_BOTTOM_Y_COORD, ViewConstants.PLAYER_COLOR_PANEL_WIDTH, ViewConstants.PLAYER_COLOR_PANEL_HEIGHT);
		playerColorPanels[5].setBounds(ViewConstants.PLAYER_COLOR_PANEL_RIGHT_X_COORD, ViewConstants.PLAYER_COLOR_PANEL_BOTTOM_Y_COORD, ViewConstants.PLAYER_COLOR_PANEL_WIDTH, ViewConstants.PLAYER_COLOR_PANEL_HEIGHT);
	}
	
	//Loop to install each of the 6 individual panels
	private void installComponents() {
		for (int i = 0; i < playerColorPanels.length; i++) {
			add(playerColorPanels[i]);
		}
	}
	
	//Removes colour names and puts player names in when they have 
	//all picked colours
	public void setPlayerColors() {
		for(PlayerColorIndicatorPanel playerColorPanel : playerColorPanels) {
			Color panelColor = playerColorPanel.getPlayerColor();
			for(Map.Entry<String, Player> player : ModelConstants.PLAYERS.entrySet()) {
				if (player.getValue().getPlayerColor().equals(panelColor)) {
					playerColorPanel.setPlayerColorLabel(player.getKey());
				}
			}
		}
	}
	
	//Reorders panels when player turns order is determined after dice rolls
	public void updatePlayerPanelsOrder(Player[] playerOrder) {
		for (int i = 0; i < playerColorPanels.length; i++) {
			String playerName = HashMapUtilities.getKeyFromValue(ModelConstants.PLAYERS, playerOrder[i]);
			playerColorPanels[i].setPlayerColorLabel(playerName);
			playerColorPanels[i].setCircleColor(ModelConstants.PLAYERS.get(playerName).getPlayerColor());
		}
	}

	//Updates active player indicator when players change turns
	public void updateActivePlayerInPlayerPanel(Player player, Color color) {
		String activePlayerName = HashMapUtilities.getKeyFromValue(ModelConstants.PLAYERS, player);
		for (int i = 0; i < playerColorPanels.length; i++) {
			if (playerColorPanels[i].getPlayerColorLabel().equals(activePlayerName)) {
				playerColorPanels[i].setActivePlayerCircle(color);
			}
			playerColorPanels[i].refreshPanel();
		}
	}
	
	public void removeActivePlayerInPlayerPanel(Player player) {
		String activePlayerName = HashMapUtilities.getKeyFromValue(ModelConstants.PLAYERS, player);
		for (int i = 0; i < playerColorPanels.length; i++) {
			if (playerColorPanels[i].getPlayerColorLabel().equals(activePlayerName)) {
				playerColorPanels[i].removeActivePlayerCircle();;
			}
			playerColorPanels[i].refreshPanel();
		}
	}

	//Adds dice number / number of units available to place by each player
	public void addNumberToPlayerPanel(int number, Player player) {
		for (int i = 0; i < playerColorPanels.length; i++) {
			if(number == -1) {
				playerColorPanels[i].hideDsplayNumber();
			}
			String playerName = HashMapUtilities.getKeyFromValue(ModelConstants.PLAYERS, player);
			if (playerColorPanels[i].getPlayerColorLabel().equals(playerName)) {
				playerColorPanels[i].setDisplayNumber(number);
			}
			playerColorPanels[i].refreshPanel();
		}
	}
}
