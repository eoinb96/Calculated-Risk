/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Insatnces of this class display color indicator circle, player name,
//dice number rolled and green highlight if this player is currently 
//taking their turn

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.ViewConstants;

public class PlayerColorIndicatorPanel extends JPanel {
	
	private JLabel playerColorLabel;
	private Color playerColor;
	private boolean activePlayer = false;          //True if it is this players turn
	private Color activePlayerColor;
	private int displayNumber;
	private boolean showDisplayNumber = false;
	
	public PlayerColorIndicatorPanel(String playerName, Color playerColor) {
		initialiseComponents(playerName, playerColor);
		positionComponents();
		installComponents();
	}
	
	private void initialiseComponents(String playerName, Color playerColor) {
		this.setLayout(null);
		this.setOpaque(false);
		this.playerColorLabel = new JLabel(playerName);
		this.playerColor = playerColor;
		playerColorLabel.setForeground(Color.WHITE);
		playerColorLabel.setFont(ViewConstants.SIDEBAR_FONT);
	}
	
	private void positionComponents() {
		playerColorLabel.setBounds(ViewConstants.PLAYER_COLOR_LABEL_X_COORD, ViewConstants.PLAYER_COLOR_LABEL_Y_COORD, ViewConstants.PLAYER_COLOR_LABEL_WIDTH, ViewConstants.PLAYER_COLOR_LABEL_HEIGHT);
	}
	
	private void installComponents() {
		add(playerColorLabel);
	}
	
	public Color getPlayerColor() {
		return this.playerColor;
	}
	
	public void setPlayerColorLabel(String playerName) {
		this.playerColorLabel.setText(playerName);
	}
	
	public String getPlayerColorLabel() {
		return this.playerColorLabel.getText();
	}
	
	public void setCircleColor(Color color) {
		this.playerColor = color;
		revalidate();
		repaint();
	}
	
	public void setActivePlayerCircle(Color color) {
		this.activePlayerColor = color;
		activePlayer = true;
		refreshPanel();
	}
	
	public void removeActivePlayerCircle() {
		activePlayer = false;
		refreshPanel();
	}
	
	public void setDisplayNumber(int number) {
		displayNumber = number;
		showDisplayNumber = true;
	}
	
	public void hideDsplayNumber() {
		showDisplayNumber = false;
	}
	
	public void refreshPanel() {
		revalidate();
		repaint();
	}
	
	//Paints circle outline, and player color circle
	//as well as active player highlight and dice number or free units number if necessary
	public void paintComponent(Graphics g) {
		Graphics2D painter = (Graphics2D) g;
		painter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		painter.setColor(Color.DARK_GRAY);
		if (activePlayer) {
			painter.setColor(activePlayerColor);
			activePlayer = false;
		}
		painter.fillOval(ViewConstants.ACTIVE_PLAYER_CIRCLE_X_COORD, ViewConstants.ACTIVE_PLAYER_CIRCLE_Y_COORD, ViewConstants.ACTIVE_PLAYER_CIRCLE_WIDTH, ViewConstants.ACTIVE_PLAYER_CIRCLE_HEIGHT);
		painter.setColor(playerColor);
		painter.fillOval(ViewConstants.PLAYER_COLOR_CIRCLE_X_COORD, ViewConstants.PLAYER_COLOR_CIRCLE_Y_COORD, ViewConstants.COUNTRY_CIRCLE_DIAMETER, ViewConstants.COUNTRY_CIRCLE_DIAMETER);

		painter.setColor(Color.BLACK);			
		painter.setStroke(new BasicStroke(2.0f));
		painter.drawOval(ViewConstants.PLAYER_COLOR_CIRCLE_X_COORD, ViewConstants.PLAYER_COLOR_CIRCLE_Y_COORD, ViewConstants.COUNTRY_CIRCLE_DIAMETER, ViewConstants.COUNTRY_CIRCLE_DIAMETER);
		
		if (showDisplayNumber) {
			String number = Integer.toString(displayNumber);
			int stringLength = (int) painter.getFontMetrics().getStringBounds(number, painter).getWidth();
			int xCoord = (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2) - (stringLength / 2) + (ViewConstants.WINDOW_HEIGHT / 128);
			painter.setFont(ViewConstants.NUMBER_OF_UNITS_FONT);
			painter.drawString(number, xCoord, ViewConstants.PLAYER_PANEL_DISPLAY_NUMBER_Y_COORD);
		}
	}
}
