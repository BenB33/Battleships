package main.gui;

// Project Imports
import main.gameMechanics.Game;
import main.networking.NetworkUtils;

// AWT Imports
import java.awt.Dialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

// Other
import java.util.ArrayList;

// Swing Imports
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class MenuBar {

	private static int port = 53200;
	
	public static void populateMenuBar(JMenuBar menuBar, JFrame frame)
	{
		ArrayList<String> ipAddressList = NetworkUtils.getIPv4List();
		
		// Create Single-Player JMenu
		JMenu singlePlayerMenu = new JMenu("Single-Player");
		singlePlayerMenu.setMnemonic('s');
		menuBar.add(singlePlayerMenu);
		
			// Start Single-Player game item
			JMenuItem startSinglePlayerItem = new JMenuItem("Start Game");
			startSinglePlayerItem.setMnemonic('s');
			singlePlayerMenu.add(startSinglePlayerItem);
			
			// End Single-Player game item
			JMenuItem endSinglePlayerItem = new JMenuItem("End Game");
			startSinglePlayerItem.setMnemonic('e');
			singlePlayerMenu.add(endSinglePlayerItem);
		
		// Create Multi-Player JMenu
		JMenu multiPlayerMenu = new JMenu("Multi-Player");
		multiPlayerMenu.setMnemonic('m');
		menuBar.add(multiPlayerMenu);
		
			// Host a multi-player game menu item
			JMenuItem hostItem = new JMenuItem("Host");
			hostItem.setMnemonic('h');
			multiPlayerMenu.add(hostItem);
			
			// Join a multi-player game menu item
			JMenuItem joinItem = new JMenuItem("Join");
			hostItem.setMnemonic('j');
			multiPlayerMenu.add(joinItem);
		
		// Exit game menu item
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('e');
		menuBar.add(exitItem);

		
		// Start a single player game
		startSinglePlayerItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Single player game has been selected by the user
				Game.game.startSingleplayerGame();
			}
		});
		
		// End a single player game
		endSinglePlayerItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Game.game.endSinglePlayerGame();
			}
		});
		
		// Add action listener to host menu
		hostItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Create a dialog box for hosting options
				JDialog hostModal = new JDialog(frame, "Host a game!", Dialog.ModalityType.DOCUMENT_MODAL);
				
				// Create panel for host dialog box
				JPanel hostModalPanel = new JPanel(new GridBagLayout());
				GridBagConstraints constraints = new GridBagConstraints();
				hostModalPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				
				// Create and add labels to the panel
				JLabel lblIPTitle = new JLabel("Your IP Address: \n", SwingConstants.RIGHT);
				JLabel lblPortTitle = new JLabel("Your Port: \n", SwingConstants.RIGHT);
				JComboBox<String> comboIPAddress = new JComboBox<>();
				JTextField txtPort = new JTextField(String.valueOf(port));
				JButton btnHost = new JButton("Host Game");
				JButton btnCancel = new JButton("Cancel");
				
				// Loading Spinner
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				
				ImageIcon originalLoadingSpinner = new ImageIcon(classLoader.getResource("spinner.gif"));
				ImageIcon originalPlaceholder = new ImageIcon(classLoader.getResource("placeholder.png"));
				
				ImageIcon imgLoadingSpinner = new ImageIcon(originalLoadingSpinner.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
				ImageIcon imgPlaceholder = new ImageIcon(originalPlaceholder.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
				JLabel lblPlaceholder = new JLabel(imgPlaceholder);
				JLabel lblLoadingSpinner = new JLabel(imgLoadingSpinner);
				
				
				// Add action listener to the cancel button
				btnHost.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						lblLoadingSpinner.setVisible(true);
						lblLoadingSpinner.repaint();

						// Host game over TCP connection
						Game.game.hostMultiplayerGame(comboIPAddress.getSelectedItem().toString(), txtPort.getText());
						hostModal.dispose();
					}
				});
				
				// Add action listener to the cancel button
				btnCancel.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Dispose of the join dialog window
						hostModal.dispose();
					}
				});
				
				// Add ip address list to combo box
				for(String address : ipAddressList)
				{
					comboIPAddress.addItem(address);
				}
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 0;
				hostModalPanel.add(lblIPTitle, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 1;
				constraints.gridy = 0;
				hostModalPanel.add(comboIPAddress, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 1;
				hostModalPanel.add(lblPortTitle, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 1;
				constraints.gridy = 1;
				hostModalPanel.add(txtPort, constraints);

				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 2;
				constraints.gridwidth = 2;
				hostModalPanel.add(lblPlaceholder, constraints);
				lblLoadingSpinner.setVisible(true);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 2;
				constraints.gridwidth = 2;
				hostModalPanel.add(lblLoadingSpinner, constraints);
				lblLoadingSpinner.setVisible(true);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 3;
				constraints.gridwidth = 1;
				hostModalPanel.add(btnHost, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 1;
				constraints.gridy = 3;
				hostModalPanel.add(btnCancel, constraints);
				
				// Configure the host dialog panel
				hostModal.add(hostModalPanel);
				hostModal.pack();
				
				// Disable ability to resize
				hostModal.setResizable(false);
				
				// Centre the window
				hostModal.setLocationRelativeTo(null);
				
				// Set dialog box's visibility
				hostModal.setVisible(true);
			}
		});
		
		// Add action listener to join menu
		joinItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Create a dialog box for joining options
				JDialog joinDialog = new JDialog(frame, "Join a player!", Dialog.ModalityType.DOCUMENT_MODAL);
				
				// Create panel for join dialog box
				JPanel joinDialogPanel = new JPanel(new GridBagLayout());
				GridBagConstraints constraints = new GridBagConstraints();
				joinDialogPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				
				// Create widgets for join dialog panel
				JTextField txtIpAddress = new JTextField(16);
				JTextField txtPort = new JTextField(8);
				JLabel lblIPAddress = new JLabel("Please enter an IP Address: \n", SwingConstants.RIGHT);
				JLabel lblPort = new JLabel("Please enter a Port: \n", SwingConstants.RIGHT);
				JButton btnConnect = new JButton("Connect");
				JButton btnCancel = new JButton("Cancel");
				
				// Add widgets to the join dialog panel
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 0;
				joinDialogPanel.add(lblIPAddress, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 1;
				constraints.gridy = 0;
				joinDialogPanel.add(txtIpAddress, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 1;
				joinDialogPanel.add(lblPort, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 1;
				constraints.gridy = 1;
				joinDialogPanel.add(txtPort, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 2;
				joinDialogPanel.add(btnConnect, constraints);
				
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 1;
				constraints.gridy = 2;
				joinDialogPanel.add(btnCancel, constraints);
				
				// Add action listener to the connect button
				btnConnect.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Ensure that the IP and port being entered is valid, if they are valid
						// call joinMultiplayerGame and pass the valid IP and port. Then dispose
						// of the join pop-up modal.
						if(NetworkUtils.ipPortValidation(txtIpAddress.getText(), txtPort.getText(), joinDialog))
						{
							Game.game.joinMultiplayerGame(txtIpAddress.getText(), txtPort.getText());
							joinDialog.dispose();
						}
					}
				});
				
				// Add action listener to the cancel button
				btnCancel.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Dispose of the join dialog window
						joinDialog.dispose();
					}
				});
				
				joinDialog.add(joinDialogPanel);
				joinDialog.pack();
				joinDialog.setResizable(false);
				joinDialog.setLocationRelativeTo(null);
				joinDialog.setVisible(true);
					
			}});
		
		// Add action listener to the exit menu bar option
		exitItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Exit program
				System.exit(0);
			}
		});
	}

}
