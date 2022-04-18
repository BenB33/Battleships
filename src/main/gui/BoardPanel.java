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
	static BufferedImage[] waterTiles = new BufferedImage[10];
	static BufferedImage[] shipTiles = new BufferedImage[7];
	static BufferedImage[] miscTiles = new BufferedImage[2];
	static
	{
		try
		{
			// Add all tile images
			
			/*
			 * Water Tiles
			 */
			waterTiles[0] = ImageIO.read(new File("res/water1.png"));
			waterTiles[1] = ImageIO.read(new File("res/water2.png"));
			waterTiles[2] = ImageIO.read(new File("res/water3.png"));
			waterTiles[3] = ImageIO.read(new File("res/water4.png"));
			waterTiles[4] = ImageIO.read(new File("res/water5.png"));
			waterTiles[5] = ImageIO.read(new File("res/water6.png"));
			waterTiles[6] = ImageIO.read(new File("res/water7.png"));
			waterTiles[7] = ImageIO.read(new File("res/water8.png"));
			waterTiles[8] = ImageIO.read(new File("res/water9.png"));
			waterTiles[9] = ImageIO.read(new File("res/water10.png"));
			
			/*
			 * Ship Tiles
			 */
			shipTiles[0] = ImageIO.read(new File("res/shipMiddleHor.png"));
			shipTiles[1] = ImageIO.read(new File("res/shipMiddleVer.png"));
			shipTiles[2] = ImageIO.read(new File("res/shipEndBottom.png"));
			shipTiles[3] = ImageIO.read(new File("res/shipEndTop.png"));
			shipTiles[4] = ImageIO.read(new File("res/shipEndLeft.png"));
			shipTiles[5] = ImageIO.read(new File("res/shipEndRight.png"));
			shipTiles[6] = ImageIO.read(new File("res/shipCircle.png"));
			
			/*
			 * Misc Tiles
			 */
			miscTiles[0] = ImageIO.read(new File("res/fire.png"));
			miscTiles[1] = ImageIO.read(new File("res/miss.png"));
			
		}
		catch (IOException e){ e.printStackTrace(); }
		
	}

	public BoardPanel(BoardOwner identify)
	{
		identifier = identify;
		Random rand = new Random();
		int maxNumber = 10;
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
				g.drawImage(waterTiles[waterTileRandom[x+y*10]], topLeftX, topLeftY, tileSize, tileSize, null);
				
				
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
			drawAllShips(Game.game.getPlayerShipArray(), g);
			drawHitIndicators(Game.game.getPlayerShipArray(), g);
			drawMiss(g);
		}
		else if(identifier == BoardOwner.ENEMY)
		{
			drawSunkenShips(Game.game.getEnemyShipArray(), g);
			//drawAllShips(Game.game.getEnemyShipArray(), g);
			drawHitIndicators(Game.game.getEnemyShipArray(), g);
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
	public void mouseMoved(MouseEvent e) 
	{
		// Track mouse coordinates
		mouseX = e.getX();
		mouseY = e.getY();
		
		// Repaint game boards every time mouse is moved
		this.repaint();
	}
	
	@Override
	public void mouseExited(MouseEvent e) 
	{
		mouseX = -1;
		mouseY = -1;
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
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
	
	private int getTileSize()
	{
		int tileSize;
		
		if (this.getWidth() > this.getHeight()) tileSize = this.getHeight()/10;
		else tileSize = this.getWidth()/10;
		
		return tileSize;
	}
	
	private Point getTopLeftPoint(Ship ship, int index)
	{
		int tileSize = getTileSize();
		int boardSize = tileSize*10;
		
		int xOffset = (this.getWidth() - boardSize) /2;
		int yOffset = (this.getHeight() - boardSize) /2;
		
		Point shipCoords = ship.getShipPos();
		int xPos = (int) shipCoords.getX();
		int yPos = (int) shipCoords.getY();
		
		int topLeftX = tileSize*xPos + xOffset;
		int topLeftY = tileSize*yPos + yOffset;
		
		// Calculating the position of each tile of the ship
		if(ship.getShipOrient() == ShipOrientation.HORIZONTAL)
		{
			topLeftX = topLeftX + (index*tileSize);
		}
		else if(ship.getShipOrient() == ShipOrientation.VERTICAL)
		{
			topLeftY = topLeftY + (index*tileSize);
		}
		
		Point topLeft = new Point(topLeftX, topLeftY);
		
		return topLeft;
	}
	
	private void drawShip(Ship ship, Graphics2D g, int shipLength)
	{
		int tileSize = getTileSize();
		Point topLeft = getTopLeftPoint(ship, shipLength);
		int topLeftX = (int) topLeft.getX();
		int topLeftY = (int) topLeft.getY();	
		
		// Drawing images to the board
		if(ship.getShipLength() == 1)
		{
			// Draws the 1x1 ship, ignoring orientation as the ship is the same for both
			g.drawImage(shipTiles[6], topLeftX, topLeftY, tileSize, tileSize, null);
		}
		else if(ship.getShipOrient() == ShipOrientation.HORIZONTAL)
		{
			if(shipLength == 0)
			{
				// Draws the first tile of the ship, which is always the same
				// shipEndLeft
				g.drawImage(shipTiles[4], topLeftX, topLeftY, tileSize, tileSize, null);
			}
			else if(shipLength < ship.getShipLength() - 1)
			{
				// Draws all middle sections of the ship, regardless of the ship length
				// shipMiddleHor
				g.drawImage(shipTiles[0], topLeftX, topLeftY, tileSize, tileSize, null);
			}
			else
			{
				// Draws the final ship tile, which will always be the same
				// shipEndRight
				g.drawImage(shipTiles[5], topLeftX, topLeftY, tileSize, tileSize, null);
			}
		}
		else if(ship.getShipOrient() == ShipOrientation.VERTICAL)
		{
			if(shipLength == 0)
			{
				// Draws the first tile of the ship, which is always the same
				// shipEndTop
				g.drawImage(shipTiles[3], topLeftX, topLeftY, tileSize, tileSize, null);
			}
			else if(shipLength < ship.getShipLength() - 1)
			{
				// Draws all middle sections of the ship, regardless of the ship length
				// shipMiddleVer
				g.drawImage(shipTiles[1], topLeftX, topLeftY, tileSize, tileSize, null);
			}
			else
			{
				// Draws the final ship tile, which will always be the same
				// shipEndBottom
				g.drawImage(shipTiles[2], topLeftX, topLeftY, tileSize, tileSize, null);
			}
		}
	}
	
	private void drawAllShips(List<Ship> ships, Graphics2D g)
	{
		for(int i = 0; i < ships.size(); i++)
		{			
			for(int j = 0; j < ships.get(i).getShipLength(); j++)
			{
				drawShip(ships.get(i), g, j);
			}
		}
	}
	
	private void drawSunkenShips(List<Ship> ships, Graphics2D g)
	{
		for(int i = 0; i < ships.size(); i++)
		{
			if(ships.get(i).isShipSunk())
			{
				for(int j = 0; j < ships.get(i).getShipLength(); j++)
				{
					drawShip(ships.get(i), g, j);
				}
			}
		}
	}
	
	private void drawHitIndicators(List<Ship> ships, Graphics2D g)
	{
		int tileSize = getTileSize();
		
		for(int i = 0; i < ships.size(); i++)
		{
			for(int j = 0; j < ships.get(i).getShipLength(); j++)
			{
				Point topLeft = getTopLeftPoint(ships.get(i), j);
				int topLeftX = (int) topLeft.getX();
				int topLeftY = (int) topLeft.getY();

				if(ships.get(i).hasShipTileBeenHit(j))
				{
					g.drawImage(miscTiles[0], topLeftX, topLeftY, tileSize, tileSize, null);
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
						g.drawImage(miscTiles[1], topLeftX, topLeftY, tileSize, tileSize, null);
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
						g.drawImage(miscTiles[1], topLeftX, topLeftY, tileSize, tileSize, null);
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
	
	
	@Override
	public void mouseDragged(MouseEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
