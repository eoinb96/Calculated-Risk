/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Displays an image and node graph of the map, the colours of players
//occupying each territory and the number of units at each territory

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import control.RiskController;
import model.Territory;
import util.HashMapUtilities;
import util.ImageUtilities;
import util.ModelConstants;
import util.ViewConstants;

public class MapPanel extends JPanel {

	private RiskController viewObserver;
	private Image background;
	private ContinentKeyPanel continentKeyPanel;
	private DicePanel playerOrderDicePanel;
	private DicePanel[] attackingDicePanels;
	private DicePanel[] defendingDicePanels;
	
	public MapPanel() {
		setLayout(null);
		setOpaque(false);
		initialiseComponents();
		positionComponents();
		installComponents();
	}
	
	private void initialiseComponents() {
		this.background = ImageUtilities.MAP;
		this.continentKeyPanel = new ContinentKeyPanel();
		this.setPreferredSize(ViewConstants.MAP_DIMENSION);
		this.attackingDicePanels = new DicePanel[3];
		this.defendingDicePanels = new DicePanel[2];
	}
	
	private void positionComponents() {
		continentKeyPanel.setBounds(ViewConstants.CONTINENT_KEY_PANEL_X_COORD, ViewConstants.CONTINENT_KEY_PANEL_Y_COORD, ViewConstants.CONTINENT_KEY_PANEL_WIDTH, ViewConstants.CONTINENT_KEY_PANEL_HEIGHT);
	}
	
	private void installComponents() {
		add(continentKeyPanel);
		labelCountries();
	}

	public void setViewObserver(RiskController viewObserver) {
		this.viewObserver = viewObserver;
	}
	
	private RiskController getViewObserver() {
		return viewObserver;
	}
	
	//Updates map when instructed by the Model
	public void refreshMap() {
		revalidate();
		repaint();
	}
	
	//Adds country names above each node
	private void labelCountries() {
		for (int i = 0; i < ModelConstants.NUM_COUNTRIES; i++) {
			int countryLabelXCoord = (int) (ModelConstants.COUNTRY_COORD[i][0] - (ViewConstants.COUNTRY_LABEL_WIDTH / 2) + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2));
			int countryLabelYCoord = (int) (ModelConstants.COUNTRY_COORD[i][1] - (ViewConstants.COUNTRY_CIRCLE_DIAMETER * 0.6));
			JLabel countryLabel = new JLabel(ModelConstants.COUNTRY_NAMES[i], SwingConstants.CENTER);
			countryLabel.setFont(ViewConstants.COUNTRY_LABEL_FONT);
			countryLabel.setForeground(Color.WHITE);
			countryLabel.setBounds(countryLabelXCoord, countryLabelYCoord, ViewConstants.COUNTRY_LABEL_WIDTH, ViewConstants.COUNTRY_LABEL_HEIGHT);
			this.add(countryLabel);
		}
	}
	
	//Adds the dice image panel when instructed by Model
	public void addPlayerOrderDicePanel(int diceNumber, int rotation) {
		if(playerOrderDicePanel != null) {
			remove(playerOrderDicePanel);
		}
		this.playerOrderDicePanel = new DicePanel(ViewConstants.PLAYER_ORDER, diceNumber, rotation);
		playerOrderDicePanel.setBounds(ViewConstants.DICE_FRAME_X_COORD, ViewConstants.DICE_FRAME_Y_COORD, ViewConstants.DICE_FRAME_WIDTH, ViewConstants.DICE_FRAME_WIDTH);
		add(playerOrderDicePanel);
	}
	
	public void addBattleDice(String diceType, int diceNumber, int numberRolled, int rotation) {
		
		if (diceType.equals("attacking")) {
			if (diceNumber == 1) {
				if(attackingDicePanels[0] != null) {
					remove(attackingDicePanels[0]);
				}
				this.attackingDicePanels[0] = new DicePanel(ViewConstants.ATTACKING, numberRolled, rotation);
				attackingDicePanels[0].setBounds(ViewConstants.ATTACKING_DICE_FRAME_X_COORD[0], ViewConstants.ATTACKING_DICE_FRAME_Y_COORD[0], ViewConstants.DICE_FRAME_WIDTH, ViewConstants.DICE_FRAME_WIDTH);
				add(attackingDicePanels[0]);
			}
			else if (diceNumber == 2) {
				if(attackingDicePanels[1] != null) {
					remove(attackingDicePanels[1]);
				}
				this.attackingDicePanels[1] = new DicePanel(ViewConstants.ATTACKING, numberRolled, rotation);
				attackingDicePanels[1].setBounds(ViewConstants.ATTACKING_DICE_FRAME_X_COORD[1], ViewConstants.ATTACKING_DICE_FRAME_X_COORD[1], ViewConstants.DICE_FRAME_WIDTH, ViewConstants.DICE_FRAME_WIDTH);
				add(attackingDicePanels[1]);
			}
			else if (diceNumber == 3) {
				if(attackingDicePanels[2] != null) {
					remove(attackingDicePanels[2]);
				}
				this.attackingDicePanels[2] = new DicePanel(ViewConstants.ATTACKING, numberRolled, rotation);
				attackingDicePanels[2].setBounds(ViewConstants.ATTACKING_DICE_FRAME_X_COORD[2], ViewConstants.ATTACKING_DICE_FRAME_Y_COORD[2], ViewConstants.DICE_FRAME_WIDTH, ViewConstants.DICE_FRAME_WIDTH);
				add(attackingDicePanels[2]);
			}
		}
		else if (diceType.equals("defending")) {
			if (diceNumber == 1) {
				if(defendingDicePanels[0] != null) {
					remove(defendingDicePanels[0]);
				}
				this.defendingDicePanels[0] = new DicePanel(ViewConstants.DEFENDING, numberRolled, rotation);
				defendingDicePanels[0].setBounds(ViewConstants.DEFENDING_DICE_FRAME_X_COORD[0], ViewConstants.DEFENDING_DICE_FRAME_Y_COORD[0], ViewConstants.DICE_FRAME_WIDTH, ViewConstants.DICE_FRAME_WIDTH);
				add(defendingDicePanels[0]);
			}
			else if (diceNumber == 2) {
				if(defendingDicePanels[1] != null) {
					remove(defendingDicePanels[1]);
				}
				this.defendingDicePanels[1] = new DicePanel(ViewConstants.DEFENDING, numberRolled, rotation);
				defendingDicePanels[1].setBounds(ViewConstants.DEFENDING_DICE_FRAME_X_COORD[1], ViewConstants.DEFENDING_DICE_FRAME_Y_COORD[1], ViewConstants.DICE_FRAME_WIDTH, ViewConstants.DICE_FRAME_WIDTH);
				add(defendingDicePanels[1]);
			}
		}
	}
	
	public void removeDice() {
		if (playerOrderDicePanel != null) {
			remove(playerOrderDicePanel);
		}
		for (int i = 0; i < attackingDicePanels.length; i++) {
			if (attackingDicePanels[i] != null) {
				remove(attackingDicePanels[i]);
			}
		}
		for (int i = 0; i < defendingDicePanels.length; i++) {
			if (defendingDicePanels[i] != null) {
				remove(defendingDicePanels[i]);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, ViewConstants.MAP_X_COORD, ViewConstants.MAP_Y_COORD, getWidth(), getHeight(), this);		
		Graphics2D painter = (Graphics2D) g;
		painter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		painter.setColor(Color.BLACK);
		painter.setStroke(new BasicStroke(2.0f));
		
		//Draws line form Alaska to Kamachatka through edges of map instead of middle
		for(int x = 0; x < ModelConstants.NUM_COUNTRIES; x++) {
			for(int y = 0; y < ModelConstants.ADJACENT[x].length; y++) {
				if ((x == 8 && y == 2) || (x == 22 && y == 0)){
					painter.drawLine((int) ModelConstants.COUNTRY_COORD[22][0] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), (int) ModelConstants.COUNTRY_COORD[22][1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), ViewConstants.MAP_WIDTH, (int) ModelConstants.COUNTRY_COORD[22][1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2));
					painter.drawLine((int) ModelConstants.COUNTRY_COORD[8][0] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), (int) ModelConstants.COUNTRY_COORD[8][1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), 0, (int) ModelConstants.COUNTRY_COORD[8][1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2));
					continue;
				}
				//Draws all the other lines
				painter.drawLine((int) ModelConstants.COUNTRY_COORD[x][0] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), (int) ModelConstants.COUNTRY_COORD[x][1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), (int) ModelConstants.COUNTRY_COORD[(ModelConstants.ADJACENT[x][y])][0] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2), (int) ModelConstants.COUNTRY_COORD[(ModelConstants.ADJACENT[x][y])][1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2));
			}
		}
		
		//Draws black outlines of each node
		for (int i = 0; i < ModelConstants.NUM_COUNTRIES; i++) {
			painter.setColor(ViewConstants.CONTINENT_COLORS[ModelConstants.CONTINENT_IDS[i]]);
			painter.fillOval((int) ModelConstants.COUNTRY_COORD[i][0], (int) ModelConstants.COUNTRY_COORD[i][1], ViewConstants.COUNTRY_CIRCLE_DIAMETER, ViewConstants.COUNTRY_CIRCLE_DIAMETER);
			painter.setColor(Color.BLACK);			
			painter.setStroke(new BasicStroke(2.0f));
			painter.drawOval((int) ModelConstants.COUNTRY_COORD[i][0], (int) ModelConstants.COUNTRY_COORD[i][1], ViewConstants.COUNTRY_CIRCLE_DIAMETER, ViewConstants.COUNTRY_CIRCLE_DIAMETER);
		}
		
		//If territory is occupied, paint player colour circle inside node
		//and number of units below node
		for (Map.Entry<String, Territory> territory: ModelConstants.TERRITORIES.entrySet()) {
			if(territory.getValue().isOccupied()) {
				String occupier = HashMapUtilities.getKeyFromValue(ModelConstants.PLAYERS, territory.getValue().getOccupier());
				Color color = ModelConstants.PLAYERS.get(occupier).getPlayerColor();
				painter.setColor(color);
				painter.fillOval((int) territory.getValue().getTerritoryCoordinates()[0] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 4), (int) territory.getValue().getTerritoryCoordinates()[1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 4), ViewConstants.PLAYER_COLOR_CIRCLE_DIAMETER, ViewConstants.PLAYER_COLOR_CIRCLE_DIAMETER);
				painter.setColor(Color.BLACK);			
				painter.setStroke(new BasicStroke(2.0f));
				painter.drawOval((int) territory.getValue().getTerritoryCoordinates()[0] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 4), (int) territory.getValue().getTerritoryCoordinates()[1] + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 4), ViewConstants.PLAYER_COLOR_CIRCLE_DIAMETER, ViewConstants.PLAYER_COLOR_CIRCLE_DIAMETER);
				
				String numberOfUnitsLabel = Integer.toString(territory.getValue().getNumberOfUnits());
				int stringLength = (int) painter.getFontMetrics().getStringBounds(numberOfUnitsLabel, painter).getWidth();
				int xCoord = (int) ((territory.getValue().getTerritoryCoordinates()[0]) + (ViewConstants.COUNTRY_CIRCLE_DIAMETER / 2) - (stringLength / 2));
				painter.setFont(ViewConstants.NUMBER_OF_UNITS_FONT);
				painter.setColor(Color.WHITE);
				painter.drawString(numberOfUnitsLabel, xCoord, (int) ((territory.getValue().getTerritoryCoordinates()[1]) + (ViewConstants.COUNTRY_CIRCLE_DIAMETER * 1.4)) );
			}
		}
	}
	
	private void pauseExecution(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
