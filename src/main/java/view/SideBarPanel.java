/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This panel holds the logo, exit button, player panel,
//log panel and command input panel and allows the methods
//to be passed from the View to these panels from the model

package view;

import java.awt.Color;

import javax.swing.JPanel;

import control.RiskController;
import model.MessageType;
import model.Player;
import util.ViewConstants;

public class SideBarPanel extends JPanel {

	private RiskController viewObserver;
	private PlayerPanel playerPanel;
	private CommandPanel commandPanel;
	private LogPanel logPanel;
	private ExitButton exitButton;
	private LogoPanel logoPanel;
	
	public SideBarPanel() {
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		initialiseComponents();
		positionComponents();
		installComponents();
	}
	
	private void initialiseComponents() {
		this.playerPanel = new PlayerPanel();
		this.logPanel = new LogPanel();
		this.exitButton = new ExitButton();
		this.logoPanel = new LogoPanel();
		this.commandPanel = new CommandPanel();

	}
	
	private void positionComponents() {
		exitButton.setBounds(ViewConstants.EXIT_BUTTON_X_COORD, ViewConstants.EXIT_BUTTON_Y_COORD, ViewConstants.EXIT_BUTTON_WIDTH, ViewConstants.EXIT_BUTTON_HEIGHT);
		logoPanel.setBounds(ViewConstants.LOGO_PANEL_X_COORD, ViewConstants.LOGO_PANEL_Y_COORD, ViewConstants.LOGO_PANEL_WIDTH, ViewConstants.LOGO_PANEL_HEIGHT);
		playerPanel.setBounds(ViewConstants.PLAYER_PANEL_X_COORD, ViewConstants.PLAYER_PANEL_Y_COORD, ViewConstants.PLAYER_PANEL_WIDTH, ViewConstants.PLAYER_PANEL_HEIGHT);
		logPanel.setBounds(ViewConstants.LOG_PANEL_X_COORD, ViewConstants.LOG_PANEL_Y_COORD, ViewConstants.LOG_PANEL_WIDTH, ViewConstants.LOG_PANEL_HEIGHT);
		commandPanel.setBounds(ViewConstants.COMMAND_PANEL_X_COORD, ViewConstants.COMMAND_PANEL_Y_COORD, ViewConstants.COMMAND_PANEL_WIDTH, ViewConstants.COMMAND_PANEL_HEIGHT);
	}

	private void installComponents() {
		add(exitButton);
		add(logoPanel);
		add(playerPanel);
		add(logPanel);
		add(commandPanel);
	}
	
	public void setViewObserver(final RiskController viewObserver) {
		this.viewObserver = viewObserver;
		getPlayerPanel().setViewObserver(viewObserver);
		getCommandPanel().setViewObserver(viewObserver);
		getLogPanel().setViewObserver(viewObserver);
	}
	
	private RiskController getViewObserver() {
		return viewObserver;
	}
	
	private PlayerPanel getPlayerPanel() {
		return playerPanel;
	}
	
	private CommandPanel getCommandPanel() {
		return commandPanel;
	}
	
	private LogPanel getLogPanel() {
		return logPanel;
	}

	public void displayMessage(MessageType type, String message, int delay) {
		getLogPanel().displayMessage(type, message, delay);
	}
	
	public String getCommand() {
		return getCommandPanel().getCommand();
	}

	public void setPlayerPanelVisible() {
		getPlayerPanel().setVisible(true);
	}
	
	public void setPlayerColors() {
		getPlayerPanel().setPlayerColors();
	}

	public void updatePlayerPanelOrder(Player[] playerOrder) {
		getPlayerPanel().updatePlayerPanelsOrder(playerOrder);
	}

	public void updateActivePlayerInPlayerPanel(Player player, Color color) {
		getPlayerPanel().updateActivePlayerInPlayerPanel(player, color);
	}
	
	public void removeActivePlayerInPlayerPanel(Player player) {
		getPlayerPanel().removeActivePlayerInPlayerPanel(player);
	}

	public void addNumberToPlayerPanel(int number, Player player) {
		getPlayerPanel().addNumberToPlayerPanel(number, player);
	}
}
