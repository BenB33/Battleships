package main.gui;


// Project Imports
import main.gameMechanics.Game;

// Swing Imports
import javax.swing.JPanel;

// AWT Imports
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Other Imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;




import java.util.Random;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener
{	
	private static final long serialVersionUID = -630893169387712747L;
	
	// Colour of graphics elements
	final static Color boarderColor = new Color(0, 0, 0, 127);
	final static Color coordinateColor = new Color(0, 0, 0, 150);

	
	// Mouse Coordinates
	int mouseX, mouseY;
	
	// mouse clicked position
	int mouseClickedX, mouseClickedY;
	
	// Board Identifier
	String identifier = null;
	
	// Water Tile Background
	int waterTileRandom[] = new int[100];
	
	// Buffered Images
	static BufferedImage[] imageTiles = new BufferedImage[11];
	static
	{
		try
		{
			// Add all tile images
			
			/*
			 * Water Tiles
			 */
			imageTiles[0] = ImageIO.read(new File("res/water1.png"));
			imageTiles[1] = ImageIO.read(new File("res/water2.png"));
			imageTiles[2] = ImageIO.read(new File("res/water3.png"));
			imageTiles[3] = ImageIO.read(new File("res/water4.png"));
			imageTiles[4] = ImageIO.read(new File("res/water5.png"));
			
			/*
			 * Ship Tiles
			 */
			imageTiles[5] = ImageIO.read(new File("res/shipMiddleHor.png"));
			imageTiles[6] = ImageIO.read(new File("res/shipMiddleVer.png"));
			imageTiles[7] = ImageIO.read(new File("res/shipEndBottom.png"));
			imageTiles[8] = ImageIO.read(new File("res/shipEndTop.png"));
			imageTiles[9] = ImageIO.read(new File("res/shipEndLeft.png"));
			imageTiles[10] = ImageIO.read(new File("res/shipEndRight.png"));
		}
		catch (IOException e){ e.printStackTrace(); }
		
	}

	public BoardPanel(String identify)
	{
		identifier = identify;
		Random rand = new Random();
		int maxNumber = 5;
		for(int i = 0; i < 100; i++)
		{
			waterTileRandom[i] = rand.nextInt(maxNumber);
		}
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	// Runs every time panel draws
	@Override
	public void paintComponent(Graphics graphics)
	{
		// Casting graphics to graphics 2D
		Graphics2D g = (Graphics2D) graphics;
		

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		//g.fillRect(10,10,20,20);
		
		g.setColor(Color.BLACK);
		g.drawRect(0 , 0, this.getWidth()-1, this.getHeight()-1);
		
		int tileSize;
		if (this.getWidth() > this.getHeight()) tileSize = this.getHeight()/10;
		else tileSize = this.getWidth()/10;

		int gameBoardSize = tileSize*10;
		int xOffset = (this.getWidth() - gameBoardSize)/2;
		int yOffset = (this.getHeight() - gameBoardSize)/2;
		
		// Draw game pieces
		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < 10; y++)
			{		
				// Calculate the top left
				int topLeftX = tileSize * x + xOffset;
				int topLeftY = tileSize * y + yOffset;

				// Draw water image to board
				g.drawImage(imageTiles[waterTileRandom[x+y*10]], topLeftX, topLeftY, tileSize, tileSize, null);
				
				
				// Draw rectangle on tile that mouse is in
				if(mouseX >= topLeftX && mouseX < topLeftX + tileSize && mouseY >= topLeftY && mouseY < topLeftY + tileSize)
				{
					g.setColor(Color.RED);
					g.fillRect(topLeftX, topLeftY, tileSize, tileSize);
				}
				
				// Game Piece boarders
				g.setColor(boarderColor);
				g.drawRect(topLeftX, topLeftY, tileSize, tileSize);
				
				// Draw coordinates in top left corner of each tile
				g.setColor(coordinateColor);
				g.drawString(""+ (char)('A'+ x) + (y+1), tileSize * x + 2 + xOffset, tileSize * y + 12 + yOffset);
				

			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		// Track mouse coordinates
		mouseX = e.getX();
		mouseY = e.getY();
		
		// Repaint game boards every time mouse is moved
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		mouseClickedX = e.getX();
		mouseClickedY = e.getY();
		
		
		if(identifier == "Player")
		{
			// Player board has been clicked
			Game.game.playerBoardClicked(mouseClickedX, mouseClickedY);
		}
		else if(identifier == "Enemy")
		{
			// Enemy board has been clicked
			Game.game.enemyBoardClicked(mouseClickedX, mouseClickedY);

		}
		
		
		mousePosToTilePos(e.getX(), e.getY());
		
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		mouseX = -1;
		mouseY = -1;
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	private Point mousePosToTilePos(int mouseX, int mouseY)
	{
		int tileSize;
		
		if (this.getWidth() > this.getHeight()) tileSize = this.getHeight()/10;
		else tileSize = this.getWidth()/10;
		
		int boardSize = tileSize*10;
		int xOffset = (this.getWidth() - boardSize)/2;
		int yOffset = (this.getHeight() - boardSize)/2;

		
		int xPos = (mouseX - xOffset)  / tileSize;
		int yPos = (mouseY - yOffset) / tileSize;
		
		System.out.println(tileSize);
		
		return new Point(xPos, yPos);
	}
}
