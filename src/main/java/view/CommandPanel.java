/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Panel in sidebar which allows the user to enter commands

package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.RiskController;
import util.ViewConstants;

public class CommandPanel extends JPanel {

	private RiskController viewObserver;
	private JLabel commandLabel;
	private JTextField commandInputField;
	private LinkedList<String> commandBuffer = new LinkedList<String>();
	
	public CommandPanel() {
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		initialiseComponents();
		positionComponents();
		installComponents();
		installListeners();
	}
	
	private void initialiseComponents() {
		this.commandLabel = new JLabel("Enter Command:");
		commandLabel.setForeground(Color.WHITE);
		commandLabel.setFont(ViewConstants.SIDEBAR_FONT);
		this.commandInputField = new JTextField();
		commandInputField.setFont(ViewConstants.SIDEBAR_FONT);
	}
	
	private void positionComponents() {
		commandLabel.setBounds(ViewConstants.COMMAND_LABEL_X_COORD, ViewConstants.COMMAND_LABEL_Y_COORD, ViewConstants.COMMAND_LABEL_WIDTH, ViewConstants.COMMAND_LABEL_HEIGHT);
		commandInputField.setBounds(ViewConstants.COMMAND_INPUT_FIELD_X_COORD, ViewConstants.COMMAND_INPUT_FIELD_Y_COORD, ViewConstants.COMMAND_INPUT_FIELD_WIDTH, ViewConstants.COMMAND_INPUT_FIELD_HEIGHT);
	}

	private void installComponents() {
		add(commandLabel);
		add(commandInputField);
	}

	//When 'enter' is hit, 'getCommand' is called
	private void installListeners() {
		class SubmitCommandListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent commandEntered) {
				synchronized (commandBuffer) {
					commandBuffer.add(commandInputField.getText());
					clearCommandInputField();
					commandBuffer.notify();
				}
				return;
			}
		}
		ActionListener submitCommandListener = new SubmitCommandListener();
		commandInputField.addActionListener(submitCommandListener);
		return;
	}
	
	//Stores user input which can be accessed from Model
	public String getCommand() {
		String command;
		synchronized (commandBuffer) {
			while (commandBuffer.isEmpty()) {
				try {
					commandBuffer.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			command = commandBuffer.pop();
		}
		return command;
	}

	public void clearCommandInputField() {
		commandInputField.setText("");
	}
	
	public void setViewObserver(RiskController viewObserver) {
		this.viewObserver = viewObserver;
	}
	
	private RiskController getViewObserver() {
		return viewObserver;
	}
}
