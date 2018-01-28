/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Loads images to be used in View

package util;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtilities {

	public static final Image MAP = getImage("/images/map.jpg");
	public static final Image RISK_LOGO = getImage("/images/logo.png");
	public static final Image EXIT = getImage("/images/exitButton.png");
	
	public static final Image WHITE_DICE_1 = getImage("/images/dice/whiteDice1.png");
	public static final Image WHITE_DICE_2 = getImage("/images/dice/whiteDice2.png");
	public static final Image WHITE_DICE_3 = getImage("/images/dice/whiteDice3.png");
	public static final Image WHITE_DICE_4 = getImage("/images/dice/whiteDice4.png");
	public static final Image WHITE_DICE_5 = getImage("/images/dice/whiteDice5.png");
	public static final Image WHITE_DICE_6 = getImage("/images/dice/whiteDice6.png");
	public static final Image RED_DICE_1 = getImage("/images/dice/redDice1.png");
	public static final Image RED_DICE_2 = getImage("/images/dice/redDice2.png");
	public static final Image RED_DICE_3 = getImage("/images/dice/redDice3.png");
	public static final Image RED_DICE_4 = getImage("/images/dice/redDice4.png");
	public static final Image RED_DICE_5 = getImage("/images/dice/redDice5.png");
	public static final Image RED_DICE_6 = getImage("/images/dice/redDice6.png");
	public static final Image BLUE_DICE_1 = getImage("/images/dice/blueDice1.png");
	public static final Image BLUE_DICE_2 = getImage("/images/dice/blueDice2.png");
	public static final Image BLUE_DICE_3 = getImage("/images/dice/blueDice3.png");
	public static final Image BLUE_DICE_4 = getImage("/images/dice/blueDice4.png");
	public static final Image BLUE_DICE_5 = getImage("/images/dice/blueDice5.png");
	public static final Image BLUE_DICE_6 = getImage("/images/dice/blueDice6.png");
	
	public static final Image ONTARIO = getImage("/images/Ontario.png");

	private static Image getImage(String path) {
		ImageIcon image = new ImageIcon(ImageUtilities.class.getResource(path));
		return image.getImage();
	}
}
