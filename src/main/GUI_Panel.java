package main;


// Swing Libraries
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// AWT Libraries
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.FontFormatException;

// Other Libraries
import java.io.IOException;

public class GUI_Panel extends JPanel
{
	private static final long serialVersionUID = -5333146759133271395L;
	private static final float TITLE_FONT_SIZE = 24.0f;
	
	// Set main font
	private static Font mainFont = null;
	
	// GUI_Panel Constructor
	public GUI_Panel()
	{
		if(mainFont == null)
		{
			try
			{
				mainFont = Font.createFont(Font.TRUETYPE_FONT, 
						this.getClass().getResource("/KGColdCoffee.ttf").openStream());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(FontFormatException e)
			{
				e.printStackTrace();
			}
		}
		
		
		// Setting up layout of panel
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		// Create panel for the enemy play field
		JPanel enemyPanel = new JPanel();
		this.add(enemyPanel, BorderLayout.WEST);
		enemyPanel.setLayout(new BorderLayout());
		
		// Create enemy play field label
		JLabel enemyBoardLabel = new JLabel("Enemy Playfield");
		enemyBoardLabel.setHorizontalAlignment(JLabel.CENTER);
		enemyBoardLabel.setFont(mainFont.deriveFont(TITLE_FONT_SIZE));
		enemyPanel.add(enemyBoardLabel, BorderLayout.NORTH);
		
		// Create enemy board panel
		JPanel enemyBoardPanelPadding = new JPanel();
		enemyPanel.add(enemyBoardPanelPadding, BorderLayout.CENTER);
		enemyBoardPanelPadding.setLayout(new BorderLayout());
		enemyBoardPanelPadding.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create enemy board
		Game_Board enemyBoard = new Game_Board();		
		enemyBoardPanelPadding.add(enemyBoard, BorderLayout.CENTER);
		
		
		// Create panel for the local player
		JPanel playerPanel = new JPanel();
		this.add(playerPanel, BorderLayout.EAST);
		playerPanel.setLayout(new BorderLayout());
		
		// Create local play field label
		JLabel playerBoardLabel = new JLabel("Your Playfield");
		playerBoardLabel.setHorizontalAlignment(JLabel.CENTER);
		playerBoardLabel.setFont(mainFont.deriveFont(TITLE_FONT_SIZE));
		playerPanel.add(playerBoardLabel, BorderLayout.NORTH);
		
		// Create local player board panel
		JPanel playerBoardPanelPadding = new JPanel();
		playerPanel.add(playerBoardPanelPadding, BorderLayout.CENTER);
		playerBoardPanelPadding.setLayout(new BorderLayout());
		playerBoardPanelPadding.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create local player board
		Game_Board playerBoard = new Game_Board();
		playerBoardPanelPadding.add(playerBoard, BorderLayout.CENTER);
		
		
		/*
		 * 
		 * Local Player Board
		 * 
		 */
		
		// Create player game board


		
		// Information Panel
		JPanel infoPanel = new JPanel();
		playerPanel.add(infoPanel, BorderLayout.SOUTH);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		infoPanel.setLayout(new GridLayout(2, 2));
		
		// Information Panel Buttons
		JLabel lblInfo1 = new JLabel("Info Section 1");
		lblInfo1.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(lblInfo1);
		
		JLabel lblInfo2 = new JLabel("Info Section 2");
		lblInfo2.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(lblInfo2);
		
		JButton button3 = new JButton("Button 3");
		infoPanel.add(button3);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		infoPanel.add(exitButton);
	}
}
