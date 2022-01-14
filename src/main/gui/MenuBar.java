package main.gui;

// Project Imports
import main.gameMechanics.Game;
import main.networking.NetworkUtils;

// AWT Imports
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Swing Imports
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
		String ipAddress = NetworkUtils.getIPAddressString();
		
		// Create JMenu labelled 'Game'
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('g');
		menuBar.add(gameMenu);
		
		// Single-Player sub-menu
		JMenuItem singlePlayerItem = new JMenuItem("Single-Player");
		singlePlayerItem.setMnemonic('s');
		gameMenu.add(singlePlayerItem);
		
		// Multi-Player sub-menu
		JMenu multiPlayerMenu = new JMenu("Multi-Player");
		multiPlayerMenu.setMnemonic('m');
		gameMenu.add(multiPlayerMenu);
		
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
		gameMenu.add(exitItem);
		
		
		
		// Start a single player game
		singlePlayerItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Single player game has been selected by the user
				Game.game.startSingleplayerGame();
			}
		});
		
		
		
		// Add action listener to host menu
		hostItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				// Create a dialog box for hosting options
				JDialog hostDialog = new JDialog(frame, "Host a game!", Dialog.ModalityType.DOCUMENT_MODAL);
				
				// Create panel for host dialog box
				JPanel hostDialogPanel = new JPanel();
				hostDialogPanel.setLayout(new GridLayout(3, 2));
				hostDialogPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				
				// Create and add labels to the panel
				JLabel lblIPTitle = new JLabel("Your IP Address: \n", SwingConstants.RIGHT);
				JLabel lblPortTitle = new JLabel("Your Port: \n", SwingConstants.RIGHT);
				JLabel lblIPAddress = new JLabel(ipAddress);
				JTextField lblPort = new JTextField(String.valueOf(port));
				JButton btnHost = new JButton("Host Game");
				JButton btnCancel = new JButton("Cancel");
				
				// Add action listener to the cancel button
				btnHost.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Host game over TCP connection
						
					}
				});
				
				
				// Add action listener to the cancel button
				btnCancel.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Dispose of the join dialog window
						hostDialog.dispose();
					}
				});
				
	
				// Add labels to the dialog panel
				lblIPTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				hostDialogPanel.add(lblIPTitle);
				lblIPAddress.setText(ipAddress);
				hostDialogPanel.add(lblIPAddress);
				hostDialogPanel.add(lblPortTitle);
				hostDialogPanel.add(lblPort);
				hostDialogPanel.add(btnHost);
				hostDialogPanel.add(btnCancel);
				
				// Configure the host dialog panel
				hostDialog.add(hostDialogPanel);
				hostDialog.pack();
				
				// Disable ability to resize
				hostDialog.setResizable(false);
				
				// Centre the window
				hostDialog.setLocationRelativeTo(null);
				
				// Set dialog box's visibility
				hostDialog.setVisible(true);
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
				JPanel joinDialogPanel = new JPanel();
				joinDialogPanel.setLayout(new GridLayout(3, 2));
				joinDialogPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				
				// Create widgets for join dialog panel
				JTextField txtIpAddress = new JTextField(16);
				JTextField txtPort = new JTextField(8);
				JLabel lblIPAddress = new JLabel("Please enter an IP Address: \n", SwingConstants.RIGHT);
				JLabel lblPort = new JLabel("Please enter a Port: \n", SwingConstants.RIGHT);
				JButton btnConnect = new JButton("Connect");
				JButton btnCancel = new JButton("Cancel");
				
				// Add widgets to the join dialog panel
				joinDialogPanel.add(lblIPAddress);
				joinDialogPanel.add(txtIpAddress);
				joinDialogPanel.add(lblPort);
				joinDialogPanel.add(txtPort);
				joinDialogPanel.add(btnConnect);
				joinDialogPanel.add(btnCancel);
				
				// Add action listener to the connect button
				btnConnect.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Connect to a client
						
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
