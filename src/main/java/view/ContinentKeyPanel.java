/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel which displays a ContinentColorPanel(name & colour indicator)
//for each of the 6 continents

package view;

import javax.swing.JPanel;

import util.ModelConstants;
import util.ViewConstants;

public class ContinentKeyPanel extends JPanel {
	
	private ContinentColorPanel continentOneColorPanel;
	private ContinentColorPanel continentTwoColorPanel;
	private ContinentColorPanel continentThreeColorPanel;
	private ContinentColorPanel continentFourColorPanel;
	private ContinentColorPanel continentFiveColorPanel;
	private ContinentColorPanel continentSixColorPanel;

	public ContinentKeyPanel() {
		setLayout(null);
		setOpaque(false);
		initialiseComponents();
		positionComponents();
		installComponents();
	}

	private void initialiseComponents() {
		continentOneColorPanel = new ContinentColorPanel(ModelConstants.CONTINENT_NAMES[0], ViewConstants.CONTINENT_COLORS[0]);
		continentTwoColorPanel = new ContinentColorPanel(ModelConstants.CONTINENT_NAMES[1], ViewConstants.CONTINENT_COLORS[1]);
		continentThreeColorPanel = new ContinentColorPanel(ModelConstants.CONTINENT_NAMES[2], ViewConstants.CONTINENT_COLORS[2]);
		continentFourColorPanel = new ContinentColorPanel(ModelConstants.CONTINENT_NAMES[3], ViewConstants.CONTINENT_COLORS[3]);
		continentFiveColorPanel = new ContinentColorPanel(ModelConstants.CONTINENT_NAMES[4], ViewConstants.CONTINENT_COLORS[4]);
		continentSixColorPanel = new ContinentColorPanel(ModelConstants.CONTINENT_NAMES[5], ViewConstants.CONTINENT_COLORS[5]);
	}
	
	public void positionComponents() {
		continentOneColorPanel.setBounds(ViewConstants.CONTINENT_COLOR_PANEL_X_COORD, (ViewConstants.CONTINENT_COLOR_PANEL_Y_COORD * 0), ViewConstants.CONTINENT_COLOR_PANEL_WIDTH, ViewConstants.CONTINENT_COLOR_PANEL_HEIGHT);
		continentTwoColorPanel.setBounds(ViewConstants.CONTINENT_COLOR_PANEL_X_COORD, (ViewConstants.CONTINENT_COLOR_PANEL_Y_COORD * 1), ViewConstants.CONTINENT_COLOR_PANEL_WIDTH, ViewConstants.CONTINENT_COLOR_PANEL_HEIGHT);
		continentThreeColorPanel.setBounds(ViewConstants.CONTINENT_COLOR_PANEL_X_COORD, (ViewConstants.CONTINENT_COLOR_PANEL_Y_COORD * 2), ViewConstants.CONTINENT_COLOR_PANEL_WIDTH, ViewConstants.CONTINENT_COLOR_PANEL_HEIGHT);
		continentFourColorPanel.setBounds(ViewConstants.CONTINENT_COLOR_PANEL_X_COORD, (ViewConstants.CONTINENT_COLOR_PANEL_Y_COORD * 3), ViewConstants.CONTINENT_COLOR_PANEL_WIDTH, ViewConstants.CONTINENT_COLOR_PANEL_HEIGHT);
		continentFiveColorPanel.setBounds(ViewConstants.CONTINENT_COLOR_PANEL_X_COORD, (ViewConstants.CONTINENT_COLOR_PANEL_Y_COORD * 4), ViewConstants.CONTINENT_COLOR_PANEL_WIDTH, ViewConstants.CONTINENT_COLOR_PANEL_HEIGHT);
		continentSixColorPanel.setBounds(ViewConstants.CONTINENT_COLOR_PANEL_X_COORD, (ViewConstants.CONTINENT_COLOR_PANEL_Y_COORD * 5), ViewConstants.CONTINENT_COLOR_PANEL_WIDTH, ViewConstants.CONTINENT_COLOR_PANEL_HEIGHT);

	}

	private void installComponents() {
		add(continentOneColorPanel);
		add(continentTwoColorPanel);
		add(continentThreeColorPanel);
		add(continentFourColorPanel);
		add(continentFiveColorPanel);
		add(continentSixColorPanel);

	}
}
