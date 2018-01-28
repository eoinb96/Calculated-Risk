/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import util.ImageUtilities;


public class TerritoryCardPanel extends JPanel {
	
	private Image territoryCardImage;
	
	public TerritoryCardPanel(String territoryName) {
		setOpaque(false);
		getTerritoryCard(territoryName);
	}

	private void getTerritoryCard(String territoryName) {
		switch (territoryName) {
		case "Ontario":
			this.territoryCardImage = ImageUtilities.ONTARIO;
		}
		revalidate();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D territoryCardPainter = (Graphics2D) g;
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        territoryCardPainter.rotate(-Math.PI / 3, width, height);
		super.paintComponent(g);
		//territoryCardPainter.drawImage(territoryCardImage, 30, 15, ViewConstants.DICE_IMAGE_WIDTH, ViewConstants.DICE_IMAGE_WIDTH, this);
	}
	
}
