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
import java.awt.Color;
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
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		// Create panel for the enemy play field
		JPanel enemyPanel = new JPanel();
		enemyPanel.setBackground(Color.DARK_GRAY);
		this.add(enemyPanel, BorderLayout.WEST);
		enemyPanel.setLayout(new BorderLayout());
		
		// Create enemy play field label
		JLabel enemyBoardLabel = new JLabel("Enemy Playfield");
		enemyBoardLabel.setHorizontalAlignment(JLabel.CENTER);
		enemyBoardLabel.setFont(mainFont.deriveFont(TITLE_FONT_SIZE));
		enemyPanel.add(enemyBoardLabel, BorderLayout.NORTH);
		
		// Create enemy board panel
		JPanel enemyBoardPanel = new JPanel();
		enemyBoardPanel.setBackground(Color.WHITE);
		enemyPanel.add(enemyBoardPanel, BorderLayout.CENTER);
		enemyBoardPanel.setLayout(new BorderLayout());
		enemyBoardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create enemy board
		JPanel enemyBoard = new JPanel();
		enemyBoard.setBackground(Color.RED);
		enemyBoardPanel.add(enemyBoard, BorderLayout.CENTER);
		
		
		
		// Create panel for the local player
		JPanel playerPanel = new JPanel();
		playerPanel.setBackground(Color.GRAY);
		this.add(playerPanel, BorderLayout.EAST);
		playerPanel.setLayout(new BorderLayout());
		
		// Create local play field label
		JLabel playerBoardLabel = new JLabel("Your Playfield");
		playerBoardLabel.setHorizontalAlignment(JLabel.CENTER);
		playerBoardLabel.setFont(mainFont.deriveFont(TITLE_FONT_SIZE));
		playerPanel.add(playerBoardLabel, BorderLayout.NORTH);
		
		// Create local player board panel
		JPanel playerBoardPanel = new JPanel();
		playerBoardPanel.setBackground(Color.BLUE);
		playerPanel.add(playerBoardPanel, BorderLayout.CENTER);
		playerBoardPanel.setLayout(new BorderLayout());
		playerBoardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create local player board
		JPanel playerBoard = new JPanel();
		playerBoard.setBackground(Color.WHITE);
		playerBoardPanel.add(playerBoard, BorderLayout.CENTER);
		
		
		
		// Information Panel
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.GREEN);
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
