package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.util.Random;

public class Game_Board extends JPanel implements MouseListener, MouseMotionListener
{	
	private static final long serialVersionUID = -630893169387712747L;
	
	// Colour of graphics elements
	final static Color boarderColor = new Color(0, 0, 0, 127);
	final static Color coordinateColor = new Color(0, 0, 0, 150);
	int waterTileRandom[] = new int[100];
	int selectedTileX, selectedTileY;
	
	// Mouse Coordinates
	int mouseX, mouseY;
	
	static BufferedImage[] waterTile = new BufferedImage[4];
	static BufferedImage[] shipTile = new BufferedImage[3];
	static
	{
		try
		{
			// Add all tile images
			
			/*
			 * Water Tiles
			 */
			waterTile[0] = ImageIO.read(new File("res/watertile1.jpg"));
			waterTile[1] = ImageIO.read(new File("res/watertile2.jpg"));
			waterTile[2] = ImageIO.read(new File("res/watertile3.jpg"));
			waterTile[3] = ImageIO.read(new File("res/watertile4.jpg"));
			
			/*
			 * Ship Tiles
			 */
			shipTile[0] = ImageIO.read(new File("res/battleshiptop1.jpg"));
			shipTile[1] = ImageIO.read(new File("res/battleshipmiddle1.jpg"));
			shipTile[2] = ImageIO.read(new File("res/battleshipbottom1.jpg"));
		}
		catch (IOException e){ e.printStackTrace(); }
		
	}

	public Game_Board()
	{
		Random rand = new Random();
		int maxNumber = 4;
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
				g.drawImage(waterTile[waterTileRandom[x+y*10]], topLeftX, topLeftY, tileSize, tileSize, null);
				
				// Game Piece boarders
				g.setColor(boarderColor);
				g.drawRect(topLeftX, topLeftY, tileSize, tileSize);
				
				// Draw coordinates in top left corner of each tile
				g.setColor(coordinateColor);
				g.drawString(""+ (char)('A'+ x) + (y+1), tileSize * x + 2 + xOffset, tileSize * y + 12 + yOffset);
				
				// Draw rectangle on tile that mouse is in
				if(mouseX >= topLeftX && mouseX < topLeftX + tileSize && mouseY >= topLeftY && mouseY < topLeftY + tileSize)
				{
					g.setColor(Color.RED);
					g.fillRect(topLeftX, topLeftY, tileSize, tileSize);
				}
				
				if((x+y*10) == 14)
				{
					g.drawImage(shipTile[0], topLeftX, topLeftY, tileSize, tileSize, null);
					System.out.println("32 or 33");
				}
				if((x+y*10) == 24)
				{
					g.drawImage(shipTile[1], topLeftX, topLeftY, tileSize, tileSize, null);
					System.out.println("32 or 33");
				}
				if((x+y*10) == 34)
				{
					g.drawImage(shipTile[2], topLeftX, topLeftY, tileSize, tileSize, null);
					System.out.println("32 or 33");
				}
								
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
}
