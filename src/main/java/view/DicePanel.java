/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel which displays image of one of the 6 dice sides
//when the dice is rolled

package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import util.ImageUtilities;
import util.ViewConstants;

public class DicePanel extends JPanel {
	
	private Image diceImage;
	private int rotation;
	
	public DicePanel(String diceType, int diceNumber, int rotation) {
		this.rotation = rotation;
		setOpaque(false);
		if (diceType.equalsIgnoreCase("player order")) {
			getPlayerOrderDice(diceNumber);
		}
		else if (diceType.equalsIgnoreCase("attacking")) {
			getAttackingDice(diceNumber);
		}
		else if (diceType.equalsIgnoreCase("defending")) {
			getDefendingDice(diceNumber);
		}
	}

	//Gets image based on number rolled
	private void getPlayerOrderDice(int number) {
		switch(number) {
		case 1:
			this.diceImage = ImageUtilities.WHITE_DICE_1;
			break;
		case 2:
			this.diceImage = ImageUtilities.WHITE_DICE_2;
			break;
		case 3:
			this.diceImage = ImageUtilities.WHITE_DICE_3;
			break;
		case 4:
			this.diceImage = ImageUtilities.WHITE_DICE_4;
			break;
		case 5:
			this.diceImage = ImageUtilities.WHITE_DICE_5;
			break;
		case 6:
			this.diceImage = ImageUtilities.WHITE_DICE_6;
			break;
		}
		revalidate();
		repaint();
	}
	
	private void getAttackingDice(int number) {
		switch(number) {
		case 1:
			this.diceImage = ImageUtilities.RED_DICE_1;
			break;
		case 2:
			this.diceImage = ImageUtilities.RED_DICE_2;
			break;
		case 3:
			this.diceImage = ImageUtilities.RED_DICE_3;
			break;
		case 4:
			this.diceImage = ImageUtilities.RED_DICE_4;
			break;
		case 5:
			this.diceImage = ImageUtilities.RED_DICE_5;
			break;
		case 6:
			this.diceImage = ImageUtilities.RED_DICE_6;
			break;
		}
		revalidate();
		repaint();
	}
	
	private void getDefendingDice(int number) {
		switch(number) {
		case 1:
			this.diceImage = ImageUtilities.BLUE_DICE_1;
			break;
		case 2:
			this.diceImage = ImageUtilities.BLUE_DICE_2;
			break;
		case 3:
			this.diceImage = ImageUtilities.BLUE_DICE_3;
			break;
		case 4:
			this.diceImage = ImageUtilities.BLUE_DICE_4;
			break;
		case 5:
			this.diceImage = ImageUtilities.BLUE_DICE_5;
			break;
		case 6:
			this.diceImage = ImageUtilities.BLUE_DICE_6;
			break;
		}
		revalidate();
		repaint();
	}
	
	//Paints rotated image of dice
	public void paintComponent(Graphics g) {
        Graphics2D dicePainter = (Graphics2D) g;
		dicePainter.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        dicePainter.rotate(-Math.PI / rotation, width, height);
		super.paintComponent(g);
		dicePainter.drawImage(diceImage, ViewConstants.DICE_DIMENSIONS[0], ViewConstants.DICE_DIMENSIONS[1], ViewConstants.DICE_DIMENSIONS[2], ViewConstants.DICE_DIMENSIONS[3], this);
	}
}
