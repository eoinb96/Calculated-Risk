/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This class is the View which is a fram that holds all GUI panels
//and receieves updates from the model and allows the information
//to be passed to the necessary GUI panel

package view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.RiskController;
import model.MessageType;
import model.Player;
import util.ViewConstants;

public class RiskView extends JFrame {

	private RiskController viewObserver;
	private JPanel background;
	private SideBarPanel sideBarPanel;
	private MapPanel mapPanel;
	
	public RiskView() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(ViewConstants.WINDOW_DIMENSION);
		setLocationRelativeTo(null);
		setLayout(null);
		initialiseComponents();
		positionComponents();
		installComponents();
		setUndecorated(true);
		setVisible(true);
	}

	private void initialiseComponents() {
		this.background = new JPanel();
		this.sideBarPanel = new SideBarPanel();
		this.mapPanel = new MapPanel();
	}
	
	private void positionComponents() {
		background.setBounds(0, 0, ViewConstants.WINDOW_WIDTH, ViewConstants.WINDOW_HEIGHT);
		background.setBackground(Color.DARK_GRAY);
		sideBarPanel.setBounds(ViewConstants.SIDEBAR_PANEL_X_COORD, ViewConstants.SIDEBAR_PANEL_Y_COORD, ViewConstants.SIDEBAR_PANEL_WIDTH, ViewConstants.SIDEBAR_PANEL_HEIGHT);
		mapPanel.setBounds(ViewConstants.MAP_X_COORD, ViewConstants.MAP_Y_COORD, ViewConstants.MAP_WIDTH, ViewConstants.MAP_HEIGHT);
	}

	private void installComponents() {
		add(sideBarPanel);
		add(mapPanel);
		add(background);
	}

	public void setViewObserver(RiskController viewObserver) {
		this.viewObserver = viewObserver;
		getMapPanel().setViewObserver(viewObserver);
		getSideBarPanel().setViewObserver(viewObserver);
	}
	
	private RiskController getViewObserver() {
		return viewObserver;
	}
	
	private MapPanel getMapPanel() {
		return mapPanel;
	}
	
	private SideBarPanel getSideBarPanel() {
		return sideBarPanel;
	}
	
	//THESE METHODS ALLOWS THE MODEL TO SEND UPDATES TO THE NECESSARY PANEL

	public void displayMessage(MessageType type, String message, int delay) {
		getSideBarPanel().displayMessage(type, message, delay);
	}

	public String getCommand() {
		return getSideBarPanel().getCommand();
	}

	public void setPlayerPanelVisible() {
		getSideBarPanel().setPlayerPanelVisible();
	}
	
	public void setPlayerColors() {
		getSideBarPanel().setPlayerColors();
	}

	public void refreshGame() {
		getMapPanel().refreshMap();
	}

	public void addPlayerOrderDicePanel(int numberRolled, int rotation) {
		getMapPanel().addPlayerOrderDicePanel(numberRolled, rotation);
	}

	public void updatePlayerPanelOrder(Player[] playerOrder) {
		getSideBarPanel().updatePlayerPanelOrder(playerOrder);
	}

	public void updateActivePlayerInPlayerPanel(Player player, Color color) {
		getSideBarPanel().updateActivePlayerInPlayerPanel(player, color);
	}
	
	public void removeActivePlayerInPlayerPanel(Player player) {
		getSideBarPanel().removeActivePlayerInPlayerPanel(player);
	}

	public void addNumberToPlayerPanel(int number, Player player) {
		getSideBarPanel().addNumberToPlayerPanel(number, player);		
	}

	public void addBattleDice(String diceType, int diceNumber, int numberRolled, int rotation) {
		getMapPanel().addBattleDice(diceType, diceNumber, numberRolled, rotation);
	}

	public void removeDice() {
		getMapPanel().removeDice();
	}
}
