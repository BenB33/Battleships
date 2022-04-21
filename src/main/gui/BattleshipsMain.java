package main.gui;

// AWT Libraries
import java.awt.BorderLayout;
import java.awt.Dimension;

// Swing Libraries
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.gameMechanics.Game;

public class BattleshipsMain{
	// Global Frame
	private static JFrame frame = null;
	
	public static void main(String[] args){
		// Initialize a GUILayout to later add to the main frame
		GUILayout gui = new GUILayout();
		// Set look and feel to target device
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
			System.err.println("[LOG] Failed to set host look and feel.\nDefaulting to JAVA L&F.");
			e.printStackTrace();
		}
		
		// Initialize Frame
		frame = new JFrame("Battleships - 18142915");
		
		// Set frame parameters 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.setMinimumSize(new Dimension(1280,720));
		
		// Set frame's layout and add GUI_Panel
		frame.setLayout(new BorderLayout());
		frame.add(gui, BorderLayout.CENTER);

		// Centre the frame to the monitor
		frame.setLocationRelativeTo(null);
		
		// Initialize the menu bar and populate it
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		MenuBar.populateMenuBar(menuBar, frame);		
		
		// Finally set the frame to be visible
		frame.setVisible(true);
		
		
		// Main game loop that repaints the screen
		// 30 times per second to ensure the GUI
		// is as up-to-date as the game.
		//
		// Measure how much time has passed since
		// the last frame was repainted. If that time
		// is less than FRAME_TIME, the thread will
		// sleep until enough time has passed.
		long lastTime = System.currentTimeMillis();
		final long FRAME_TIME = 1000 / 30;
		
		while(frame.isVisible()){
			// Setting the info labels (Who's turn it is/remaining unsunk ships) and repaint the frame
			gui.setInfoLabels(Game.game.getTurnIndicator(), Game.game.getRemainingShipIndicator());
			frame.repaint();
			
			long currentTime = System.currentTimeMillis();
			long deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			
			// If deltaTime is less than the time set for
			// a frame, the thread will sleep.
			try{
				if(deltaTime < FRAME_TIME) Thread.sleep(FRAME_TIME - deltaTime);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		
	}
}
