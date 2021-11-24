package main;

// AWT Libraries
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Dialog;

// Swing Libraries
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

// Other Libraries
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.DatagramSocket;

public class GUI_Main 
{

	// Global Frame
	private static JFrame frame = null;
	
	private static ServerSocket server;
	private static Socket socket;
	private static Scanner scanner;
	private static int port = 53200;
	
	public static void main(String[] args) 
	{
		// Initialise Frame
		frame = new JFrame("Battleships - 18142915");
		
		// Set frame parameters 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.setMinimumSize(new Dimension(1280,720));
		
		// Set frame's layout and add GUI_Panel
		frame.setLayout(new BorderLayout());
		frame.add(new GUI_Panel(), BorderLayout.CENTER);

		// Centre the frame to the monitor
		frame.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		populateMenuBar(menuBar);
		
		frame.setVisible(true);
	}
	
	private static void populateMenuBar(JMenuBar menuBar)
	{
		String ipAddress = null;
		
		// Create JMenu labelled 'Game'
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('g');
		menuBar.add(gameMenu);
		
		// Create and add items to the menu bar
		JMenuItem hostItem = new JMenuItem("Host");
		hostItem.setMnemonic('h');
		gameMenu.add(hostItem);
		JMenuItem joinItem = new JMenuItem("Join");
		hostItem.setMnemonic('j');
		gameMenu.add(joinItem);
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('e');
		gameMenu.add(exitItem);
		
		
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
				
				// Create and add labels to the panel
				JLabel lblIPTitle = new JLabel("Your IP Address: \n", SwingConstants.RIGHT);
				JLabel lblPortTitle = new JLabel("Your Port: \n", SwingConstants.RIGHT);
				JLabel lblIPAddress = new JLabel();
				JLabel lblPort = new JLabel(String.valueOf(port));
				JButton btnHost = new JButton("Host Game");
				JButton btnCancel = new JButton("Cancel");
				
				// Add action listener to the cancel button
				btnHost.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						// Host TCP
						try 
						{
							server = new ServerSocket(port, 1, InetAddress.getLocalHost());
						} 
						catch (IOException e1) { e1.printStackTrace(); }
						
						Thread listenThread = new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								try {
									listen();
								} catch (Exception e) { e.printStackTrace(); }
							}
						});
						listenThread.start();
						
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
						// Return true if IP and port are valid, false if invalid
						boolean valid = ipPortValidation(txtIpAddress.getText(), txtPort.getText(), joinDialog);
						
						if(valid)
						{
							// IP and Port are valid. Connect to client
							try 
							{
								socket = new Socket(InetAddress.getLocalHost(), port);
								scanner = new Scanner(System.in);
								
								System.out.println("[LOG] Connected to server: " + socket.getInetAddress());
								
							} 
							catch (IOException e1) { e1.printStackTrace(); }
							
							String input;
							while(true)
							{
								input = scanner.nextLine();
							}
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

	// Synchronised creates a semaphore for the function and 
	// invokes it automatically (Thread safe)
	private static synchronized void listen() throws Exception
	{
		String data = null;
		System.out.println("[LOG] Waiting for connection from client...\n");
		Socket client = server.accept();
		String clientAddress = client.getInetAddress().getHostAddress();
		System.out.println("[LOG] New connection from " + clientAddress);
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(client.getInputStream()));
	}
	
	
	private InetAddress getAddress()
	{
		return GUI_Main.server.getInetAddress();
	}
	
	
	private static boolean ipRegXValidator(String ipAddress)
	{
		Pattern pattern = Pattern.compile(
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher ipMatcher = pattern.matcher(ipAddress);
		return ipMatcher.matches();
	}
	
	
	private static boolean ipPortValidation(String ip, String port, JDialog joinDialog)
	{
		// Add connection
		if(ip.equals("") || port.equals(""))
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter correct information.");		
			return false;
		}
		else if (!isNumber(port) == true)
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter a valid Port.");
			return false;
		}
		else if(Integer.valueOf(port) < 0 || Integer.valueOf(port) > 65535)
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter a valid Port.");
			return false;
		}
		else if(!ipRegXValidator(ip))
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter a valid IP Address.");
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private static boolean isNumber(String input)
	{
		try 
		{
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;	
		}
	}
}
