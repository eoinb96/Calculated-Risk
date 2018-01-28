/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel to show a scrollable text pane with current and previous 
//instructions/errors/success messages/user commands in their correct colours.

package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import control.RiskController;
import model.MessageType;
import util.ViewConstants;

public class LogPanel extends JPanel {

	private RiskController viewObserver;
	private JTextPane riskLog;
	private JScrollPane scrollingLogPane;
	private StyledDocument document;			//Allows each message to be coloured and prefixed accordingly
		
	public LogPanel() {
		initialiseComponents();
		positionComponents();
		installComponents();
	}
	
	private void initialiseComponents() {
		this.riskLog = new JTextPane();
		this.scrollingLogPane = new JScrollPane(riskLog);
		this.document = riskLog.getStyledDocument();
		riskLog.setFont(ViewConstants.LOG_PANEL_FONT);
		riskLog.setEditable(false);
	}
	
	private void positionComponents() {
		setLayout(new BorderLayout());
	}

	private void installComponents() {
		add(scrollingLogPane, BorderLayout.CENTER);
	}
	
	public void setViewObserver(RiskController viewObserver) {
		this.viewObserver = viewObserver;
	}
	
	private RiskController getViewObserver() {
		return viewObserver;
	}
	
	//Displays the (delayed) message passed from the Model with it's correct
	//prefix and colour
	public void displayMessage(MessageType type, String message, int delay) {
		delay = 0;
		String outputMessage = type.getInitialisation() + message + "\n";
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(simpleAttributeSet, type.getMessageColor());
		try {
			Thread.sleep(delay);	//pauses thread for given time before message is displayed	
			document.insertString(document.getLength(), outputMessage, simpleAttributeSet);
			riskLog.setCaretPosition(riskLog.getDocument().getLength());
		} catch (BadLocationException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
