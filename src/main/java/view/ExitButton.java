/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel which displayes image of exit button which prompts the user
//if they would liek to exit the agme or cancel when clicked

package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import util.ImageUtilities;

public class ExitButton extends JPanel implements MouseListener {

	private Image exit;
	
	public ExitButton() {
		setOpaque(false);
		exit = ImageUtilities.EXIT;
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(exit, 0, 0, getWidth(), getHeight(), this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		displayExitMenu();
	}
	
	private void displayExitMenu() {
		JPopupMenu exitPopupMenu = new JPopupMenu();
		exitPopupMenu.add("Exit Risk").addActionListener(e -> {System.exit(0);});
		exitPopupMenu.add("Cancel");
		exitPopupMenu.show(this, this.getWidth(), this.getHeight());
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
