package main.gui;


// Project Imports
import main.gameMechanics.Game;
import main.gameMechanics.Ship;
import main.gameMechanics.ShipOrientation;

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
import java.awt.RenderingHints;

// Other Imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.List;
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
	BoardOwner identifier = null;
	
	// Water Tile Background
	int waterTileRandom[] = new int[100];
	
	// Buffered Images
	static BufferedImage[] imageTiles = new BufferedImage[14];
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
			imageTiles[11] = ImageIO.read(new File("res/shipCircle.png"));
			
			/*
			 * Misc Tiles
			 */
			imageTiles[12] = ImageIO.read(new File("res/hit.png"));
			imageTiles[13] = ImageIO.read(new File("res/miss.png"));
			
		}
		catch (IOException e){ e.printStackTrace(); }
		
	}

	public BoardPanel(BoardOwner identify)
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
					g.setColor(new Color(10f/255f, 131f/255f, 245f/255f, 0.6f));
					g.fillRect(topLeftX, topLeftY, tileSize, tileSize);
				}
				
				// Game Piece boarders
				g.setColor(boarderColor);
				g.drawRect(topLeftX, topLeftY, tileSize, tileSize);
			}
		}
		
		// Drawing ships
		if(identifier == BoardOwner.PLAYER)
		{
			drawShips(Game.game.getPlayerShipArray(), g);
			drawShipHits(Game.game.getPlayerShipArray(), g);
			drawMiss(g);
		}
		else if(identifier == BoardOwner.ENEMY)
		{
			drawShips(Game.game.getEnemyShipArray(), g);
			drawShipHits(Game.game.getEnemyShipArray(), g);
			drawMiss(g);
		}
		
		
		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				// Draw coordinates in top left corner of each tile
				g.setColor(coordinateColor);
				g.drawString(""+ (char)('A'+ x) + (y+1), tileSize * x + 2 + xOffset, tileSize * y + 12 + yOffset);
			}
		}
		
		// Dispose of the graphics object to prevent
		// memory leak
		g.dispose();
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
		Point tilePos = mousePosToTilePos(e.getX(), e.getY());
		
		mouseClickedX = tilePos.x;
		mouseClickedY = tilePos.y;
		
		
		if(identifier == BoardOwner.PLAYER)
		{
			// Player board has been clicked
			Game.game.playerBoardClicked(mouseClickedX, mouseClickedY);
		}
		else if(identifier == BoardOwner.ENEMY)
		{
			// Enemy board has been clicked
			Game.game.enemyBoardClicked(mouseClickedX, mouseClickedY);
		}
		
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	private int getTileSize()
	{
		int tileSize;
		
		if (this.getWidth() > this.getHeight()) tileSize = this.getHeight()/10;
		else tileSize = this.getWidth()/10;
		
		return tileSize;
	}
	
	private Point getTopLeftPoint(List<Ship> ships, int index, int jindex)
	{
		int tileSize = getTileSize();
		int boardSize = tileSize*10;
		
		int xOffset = (this.getWidth() - boardSize) /2;
		int yOffset = (this.getHeight() - boardSize) /2;
		
		Point shipCoords = ships.get(index).getShipPos();
		int xPos = (int) shipCoords.getX();
		int yPos = (int) shipCoords.getY();
		
		int topLeftX = tileSize*xPos + xOffset;
		int topLeftY = tileSize*yPos + yOffset;
		
		// Calculating the position of each tile of the ship
		if(ships.get(index).getShipOrient() == ShipOrientation.HORIZONTAL)
		{
			topLeftX = topLeftX + (jindex*tileSize);
		}
		else if(ships.get(index).getShipOrient() == ShipOrientation.VERTICAL)
		{
			topLeftY = topLeftY + (jindex*tileSize);
		}
		
		Point topLeft = new Point(topLeftX, topLeftY);
		
		return topLeft;
	}
	
	private void drawShips(List<Ship> ships, Graphics2D g)
	{
		int tileSize = getTileSize();

		for(int i = 0; i < ships.size(); i++)
		{			
			for(int j = 0; j < ships.get(i).getShipLength(); j++)
			{
				Point topLeft = getTopLeftPoint(ships, i, j);
				int topLeftX = (int) topLeft.getX();
				int topLeftY = (int) topLeft.getY();				
				
				// Drawing images to the board
				if(ships.get(i).getShipLength() == 1)
				{
					// Draws the 1x1 ship, ignoring orientation as the ship is the same for both
					g.drawImage(imageTiles[11], topLeftX, topLeftY, tileSize, tileSize, null);
				}
				else if(ships.get(i).getShipOrient() == ShipOrientation.HORIZONTAL)
				{
					if(j == 0)
					{
						// Draws the first tile of the ship, which is always the same
						// shipEndLeft
						g.drawImage(imageTiles[9], topLeftX, topLeftY, tileSize, tileSize, null);
					}
					else if(j < ships.get(i).getShipLength() - 1)
					{
						// Draws all middle sections of the ship, regardless of the ship length
						// shipMiddleHor
						g.drawImage(imageTiles[5], topLeftX, topLeftY, tileSize, tileSize, null);
					}
					else
					{
						// Draws the final ship tile, which will always be the same
						// shipEndRight
						g.drawImage(imageTiles[10], topLeftX, topLeftY, tileSize, tileSize, null);
					}
				}
				else if(ships.get(i).getShipOrient() == ShipOrientation.VERTICAL)
				{
					if(j == 0)
					{
						// Draws the first tile of the ship, which is always the same
						// shipEndTop
						g.drawImage(imageTiles[8], topLeftX, topLeftY, tileSize, tileSize, null);
					}
					else if(j < ships.get(i).getShipLength() - 1)
					{
						// Draws all middle sections of the ship, regardless of the ship length
						// shipMiddleVer
						g.drawImage(imageTiles[6], topLeftX, topLeftY, tileSize, tileSize, null);
					}
					else
					{
						// Draws the final ship tile, which will always be the same
						// shipEndBottom
						g.drawImage(imageTiles[7], topLeftX, topLeftY, tileSize, tileSize, null);
					}
				}
			}
		}
	}
	
	private void drawShipHits(List<Ship> ships, Graphics2D g)
	{
		int tileSize = getTileSize();
		
		for(int i = 0; i < ships.size(); i++)
		{
			for(int j = 0; j < ships.get(i).getShipLength(); j++)
			{
				Point topLeft = getTopLeftPoint(ships, i, j);
				int topLeftX = (int) topLeft.getX();
				int topLeftY = (int) topLeft.getY();

				if(ships.get(i).hasShipTileBeenHit(j))
				{
					g.drawImage(imageTiles[12], topLeftX, topLeftY, tileSize, tileSize, null);
				}
			}
		}
	}
	
	
	private void drawMiss(Graphics2D g)
	{
		int tileSize = getTileSize();
		
		int xOffset = (this.getWidth() - (tileSize*10))/2;
		int yOffset = (this.getHeight() - (tileSize*10))/2;

		if(identifier == BoardOwner.ENEMY)
		{
			for(int x = 0; x < 10; x++)
			{
				for(int y = 0; y < 10; y++)
				{
					int topLeftX = tileSize*x+xOffset;
					int topLeftY = tileSize*y+yOffset;
					
					if(Game.game.getEnemyBoard().getPreviousMovesList()[x][y] && !doesTileContainShip(x,y))
					{
						// Render X
						g.drawImage(imageTiles[13], topLeftX, topLeftY, tileSize, tileSize, null);
					}
				}
			}
		}
		else if(identifier == BoardOwner.PLAYER)
		{
			for(int x = 0; x < 10; x++)
			{
				for(int y = 0; y < 10; y++)
				{
					int topLeftX = tileSize*x+xOffset;
					int topLeftY = tileSize*y+yOffset;
					
					if(Game.game.getPlayerBoard().getPreviousMovesList()[x][y] && !doesTileContainShip(x,y))
					{
						// Render X
						g.drawImage(imageTiles[13], topLeftX, topLeftY, tileSize, tileSize, null);
					}
				}
			}
		}
	}
	
	
	private boolean doesTileContainShip(int xPos, int yPos)
	{
		List<Ship> ships = null;
		if(identifier == BoardOwner.ENEMY)
		{
			ships = Game.game.getEnemyBoard().getShipArray();
		}
		else if(identifier == BoardOwner.PLAYER)
		{
			ships = Game.game.getPlayerBoard().getShipArray();
		}
		
		for(int i = 0; i < ships.size(); i++)
		{
			if(ships.get(i).getShipOrient() == ShipOrientation.HORIZONTAL)
			{
				for(int j = 0; j < ships.get(i).getShipLength(); j++)
				{
					if(xPos == (int) ships.get(i).getShipPos().getX()+j && yPos == (int)ships.get(i).getShipPos().getY())
					{
						// A ship does occupy the tile, so return true.
						return true;
					}
				}
			}
			else if(ships.get(i).getShipOrient() == ShipOrientation.VERTICAL)
			{
				for(int j = 0; j < ships.get(i).getShipLength(); j++)
				{
					if(xPos == (int)ships.get(i).getShipPos().getX() && yPos == (int)ships.get(i).getShipPos().getY()+j)
					{
						// A ship does occupy the tile, so return true.
						return true;
					}
				}
			}
		}
		
		// No ships are found in the tiles, return false.
		return false;
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
		
		// Bounds checking the mouse click position and returning
		// null if outside of the playable area
		if(xPos < 0 || xPos > 10 || yPos < 0 || yPos > 10)
		{
			return null;
		}
		
		// If legal click, return the position
		return new Point(xPos, yPos);
	}
}
