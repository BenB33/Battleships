package main.gui;

// Swing Imports
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// AWT Imports
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.FontFormatException;

// Other Imports
import java.io.IOException;

public class GUILayout extends JPanel
{
	private static final long serialVersionUID = -5333146759133271395L;
	private static final float TITLE_FONT_SIZE = 24.0f;
	
	// Set main font
	private static Font mainFont = null;
	
	BoardPanel playerBoard;
	BoardPanel enemyBoard;
	
	// Labels that will be interacted with
	JLabel lblTurnIndicator = new JLabel();
	JLabel lblEnemyShipsLeft = new JLabel();
	
	
	// GUI_Panel Constructor
	public GUILayout(){
		loadFont();
		createEnemyBoard();
		
		// Seperate player panel needs to be
		// initialised for use by the infopanel
		JPanel playerPanel = createPlayerBoard();
		createInfoPanel(playerPanel);
		
		// Setting up layout of panel
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	
	// Load the font into memory for use throughout
	// the program
	//
	private void loadFont(){
		if(mainFont == null){
			try{
				mainFont = Font.createFont(Font.TRUETYPE_FONT, 
						this.getClass().getResource("/KGColdCoffee.ttf").openStream());
			}
			catch(IOException | FontFormatException e){
				e.printStackTrace();
			}
		}
	}
	
	
	// Creates the enemy's board that resides in the 
	// left side of the window
	//
	private void createEnemyBoard(){
		// Create JPanel to house the enemy board, label and 
		// padding JPanel
		JPanel enemyPanel = new JPanel();
		this.add(enemyPanel, BorderLayout.WEST);
		enemyPanel.setLayout(new BorderLayout());
		
		// Create JLabel to indicate the enemy board
		JLabel enemyBoardLabel = new JLabel("Enemy Playfield");
		enemyBoardLabel.setHorizontalAlignment(JLabel.CENTER);
		enemyBoardLabel.setFont(mainFont.deriveFont(TITLE_FONT_SIZE));
		enemyPanel.add(enemyBoardLabel, BorderLayout.NORTH);
		
		// Create JPanel for padding
		JPanel enemyBoardPanelPadding = new JPanel();
		enemyPanel.add(enemyBoardPanelPadding, BorderLayout.CENTER);
		enemyBoardPanelPadding.setLayout(new BorderLayout());
		enemyBoardPanelPadding.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create JPanel from previously initialised BoardPanel (extends JPanel)
		// with the board owner Enemy
		enemyBoard = new BoardPanel(BoardOwner.ENEMY);		
		enemyBoardPanelPadding.add(enemyBoard, BorderLayout.CENTER);
	}
	
	
	// Creates the local player's board that resides in the top
	// right side of the window
	//
	private JPanel createPlayerBoard(){
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
		playerBoard = new BoardPanel(BoardOwner.PLAYER);
		playerBoardPanelPadding.add(playerBoard, BorderLayout.CENTER);
		
		return playerPanel;
	}
	
	
	// Creates the info panel section that resides in the
	// bottom right of the window
	//
	private void createInfoPanel(JPanel playerPanel){
		// Information Panel
		JPanel infoPanel = new JPanel();
		playerPanel.add(infoPanel, BorderLayout.SOUTH);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		infoPanel.setLayout(new GridLayout(2, 2));
		
		// Information Panel labels and button
		lblTurnIndicator = new JLabel("Turn:");
		lblTurnIndicator.setHorizontalAlignment(JLabel.CENTER);
		lblEnemyShipsLeft = new JLabel("Ships Left:");
		lblEnemyShipsLeft.setHorizontalAlignment(JLabel.CENTER);
		JButton btnExit = new JButton("Exit");
		
		// Exit button action listener. Exits the program
		btnExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// Exit the program
				System.exit(0);
			}
		});
		
		// Add the widgets to the info panel
		infoPanel.add(lblTurnIndicator);
		infoPanel.add(lblEnemyShipsLeft);
		infoPanel.add(new JLabel(""));
		infoPanel.add(btnExit);
	}
	
	
	// Set the info labels in the info section
	//
	public void setInfoLabels(String turn, int shipsLeft){
		lblTurnIndicator.setText("Turn: " + turn);
		lblEnemyShipsLeft.setText("Ships Left: " + shipsLeft);
	}
}
