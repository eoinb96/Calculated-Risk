/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel which displays the risk logo

package view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import util.ImageUtilities;

public class LogoPanel extends JPanel {
	
	private Image riskLogo;
	
	public LogoPanel() {
		setOpaque(false);
		riskLogo = ImageUtilities.RISK_LOGO;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(riskLogo, 0, 0, getWidth(), getHeight(), this);
	}
	
}
