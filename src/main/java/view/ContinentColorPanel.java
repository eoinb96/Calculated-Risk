/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Creates instances of continent name and colour indicator for each continent which
//are then displayed in ContinentKeyPanel along with the continent name

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.ViewConstants;

public class ContinentColorPanel extends JPanel {
	
	JLabel continentColorLabel;			//Name of this continent
	Color continentColor;				//Color of this continent
	
	public ContinentColorPanel(String continentName, Color continentColor) {
		initialiseComponents(continentName, continentColor);
		positionComponents();
		installComponents();
	}
	
	private void initialiseComponents(String continentName, Color continentColor) {
		this.setLayout(null);
		this.setOpaque(false);
		this.continentColorLabel = new JLabel(continentName);
		this.continentColor = continentColor;
		continentColorLabel.setForeground(Color.WHITE);
		continentColorLabel.setFont(ViewConstants.CONTINENT_LABEL_FONT);

	}
	
	private void positionComponents() {
		continentColorLabel.setBounds(ViewConstants.CONTINENT_LABEL_X_COORD, ViewConstants.CONTINENT_LABEL_Y_COORD, ViewConstants.CONTINENT_LABEL_WIDTH, ViewConstants.CONTINENT_LABEL_HEIGHT);
	}
	
	private void installComponents() {
		add(continentColorLabel);
	}
	
	//Paints coloured circle for continet and a black outline
	public void paintComponent(Graphics g) {
		Graphics2D painter = (Graphics2D) g;
		painter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		painter.setColor(continentColor);
		painter.fillOval(ViewConstants.PLAYER_COLOR_CIRCLE_X_COORD, ViewConstants.PLAYER_COLOR_CIRCLE_Y_COORD, ViewConstants.COUNTRY_CIRCLE_DIAMETER, ViewConstants.COUNTRY_CIRCLE_DIAMETER);

		painter.setColor(Color.BLACK);			
		painter.setStroke(new BasicStroke(2.0f));
		painter.drawOval(ViewConstants.PLAYER_COLOR_CIRCLE_X_COORD, ViewConstants.PLAYER_COLOR_CIRCLE_Y_COORD, ViewConstants.COUNTRY_CIRCLE_DIAMETER, ViewConstants.COUNTRY_CIRCLE_DIAMETER);
	}
}
	
