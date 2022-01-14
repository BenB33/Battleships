package main.gui;

// AWT Libraries
import java.awt.BorderLayout;
import java.awt.Dimension;

// Swing Libraries
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BattleshipsMain 
{
	// Global Frame
	private static JFrame frame = null;
	
	public static void main(String[] args) 
	{
		// Set look and feel to target device
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("[LOG] Failed to set host look and feel.\nDefaulting to JAVA L&F.");
			e.printStackTrace();
		}
		
		// Initialise Frame
		frame = new JFrame("Battleships - 18142915");
		
		// Set frame parameters 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.setMinimumSize(new Dimension(1280,720));
		
		// Set frame's layout and add GUI_Panel
		frame.setLayout(new BorderLayout());
		frame.add(new GUILayout(), BorderLayout.CENTER);

		// Centre the frame to the monitor
		frame.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		MenuBar.populateMenuBar(menuBar, frame);		
		
		frame.setVisible(true);
	}
	
}
