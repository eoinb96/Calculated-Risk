/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This enumeration allows error/info/success/user command
//messages to be easily created

package model;

import java.awt.Color;

public enum MessageType {

	//Messages of each type which are created are prefixed by
	//these initiialisation string and displayer in these colours
	INFO("\nINFO: ", Color.BLACK),
	COMMAND("> ", new Color(0, 116, 255)),
	ERROR("\nERROR: ", new Color(217, 0, 0)),
	SUCCESS("SUCCESS: ", new Color(0, 178, 44)),
	BLANK("", Color.BLACK);
	
	private MessageType(String initialisation, Color messageColor) {
		this.initialisation = initialisation;
		this.messageColor = messageColor;
	}
		
	private String initialisation;
	private Color messageColor;
	
	public String getInitialisation() {
		return initialisation;
	}
	
	public Color getMessageColor() {
		return messageColor;
	}
}
