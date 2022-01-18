package main.gameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

	public Board() 
	{
		
	}
	
	// Create an array list of ships
	List<Ship> ships = new ArrayList<Ship>();
	
	
	public void placeShipsAtRandom()
	{
		// Clears the ship list in case previous game
		// contains ships
		ships.clear();
		
		
		// Array contains number of each ship size
		// [2 x 1] [2 x 2] [1 x 3] [1 x 4] [1 x 5]
		int[] shipLengthQty = {2, 2, 1, 1, 1};
		
		// Create random seed
		Random rand = new Random();
		
		// Legal tiles for ships to be, all initially set to true
		boolean[][] legalTiles = new boolean[10][10];
		for(int i = 0; i<10;i++)
		{
			for(int j = 0; j<10;j++)
			{
				legalTiles[i][j] = true;
			}
		}

		
		for(int i = 0; i < shipLengthQty.length; i++)
		{
			for(int j = 0; j < shipLengthQty[i]; j++)
			{
				boolean shipPlaced = false;
				while(!shipPlaced)
				{
					int xPos = rand.nextInt(10);
					int yPos = rand.nextInt(10);
					// 0 = Horizontal  -  1 = Vertical 
					ShipOrientation shipOrient = ShipOrientation.values()[rand.nextInt(2)];
					

					
					// Bound checking
					if(isShipOutOfBounds(i, xPos, yPos, shipOrient, legalTiles))
					{
						continue;
					}
					
					// Ship Overlap Checking
					if(isShipOverlapDetected(i, xPos, yPos, shipOrient, legalTiles))
					{
						continue;
					}
					
					// Flag what tiles are legal
					flagLegalTiles(i, xPos, yPos, shipOrient, legalTiles);

					// Add new ship with variables that have been checked
					// for validity. 
					System.out.println("Adding Ship of length: " + i + " xPos: " + xPos + " yPos: " + yPos);
					ships.add(new Ship(shipOrient, i+1, xPos, yPos));
					shipPlaced = true;
					//printShipDebug(legalTiles);
				}
			}
		}	
	}
	
	
	private boolean isShipOverlapDetected(int shipLength, int xPos, int yPos, ShipOrientation shipOrient, boolean[][] legalTiles)
	{
		// Overlap Checking
		if(shipOrient == ShipOrientation.HORIZONTAL)
		{
			//System.out.println("Checking Horizontal Overlapping Ships...\n");
			
			
			boolean illegalMove = false;
			// Horizontal Ship Overlap Checking
			for(int k = 0; k <= shipLength; k++)
			{
				// Looping over each tile in the ship, checking
				// if there is already a ship in that position
				// Using i as it gives the length of the current ship
				//System.out.println("Checking Square: " + (xPos+k) + " " + yPos);
				if(legalTiles[xPos+k][yPos] == false)
				{
					// Illegal move detected
					// Ship overlaps another ship
					//System.out.println("[SHIP OVERLAP DETECTED]\n");
					illegalMove = true;
					break;
				}
			}
			if(illegalMove == true)
			{
				return true;
			}
		}
		else
		{
			//System.out.println("Checking Vertical Overlapping Ships...\n");
			// Vertical Ship Overlap Checking
			boolean illegalMove = false;
			for(int k = 0; k <= shipLength; k++)
			{
				// Looping over each tile in the ship, checking
				// if there is already a ship in that position
				// Using i as it gives the length of the current ship
				//System.out.println("Checking Square: " + xPos + " " + (yPos+k));
				if(legalTiles[xPos][yPos+k] == false)
				{
					// Illegal move detected
					// Ship overlaps another ship
					//System.out.println("[SHIP OVERLAP DETECTED]\n");
					illegalMove = true;
					break;
				}
			}
			if(illegalMove == true)
			{
				return true;
			}
		}
		
		// No Ship Overlap Detected
		return false;
	}
	
	
	private boolean isShipOutOfBounds(int shipLength, int xPos, int yPos, ShipOrientation shipOrient, boolean[][] legalTiles)
	{
		if(shipOrient == ShipOrientation.HORIZONTAL)
		{
			//System.out.println("Checking Bounds...\n");
			// Horizontal Checks (Only check X pos)
			if(xPos + (shipLength+1) > 10)
			{
				//System.out.println("[OUT OF BOUNDS DETECTED]\n");
				// Illegal move detected
				// Ship leaks out of bounds
				return true;
			}
		}
		else
		{
			// Vertical Checks (Only check Y pos)
			if(yPos + (shipLength+1) > 10)
			{
				//System.out.println("[OUT OF BOUNDS DETECTED]\n");
				// Illegal move detected
				// Ship leaks out of bounds
				return true;
			}
		}
		// No overlap detected
		return false;
	}


	private void flagLegalTiles(int shipLegnth, int xPos, int yPos, ShipOrientation shipOrient, boolean[][] legalTiles)
	{
		if(shipOrient == ShipOrientation.HORIZONTAL)
		{
			//System.out.println("Placing Ships...\n");
			// Setting booleans in ship location to false
			// To let other ships know there is one in this position
			for(int k = 0; k <= shipLegnth; k++)
			{
				legalTiles[xPos+k][yPos] = false;
				
				if((xPos+k)+1 <= 9)
				{
					legalTiles[(xPos+k)+1][yPos] = false;
				}
				if((xPos+k)-1 >= 0)
				{
					legalTiles[(xPos+k)-1][yPos] = false;
				}
				if((yPos)+1 <=9)
				{
					legalTiles[xPos+k][yPos+1] = false;
				}
				if((yPos)-1 >=0)
				{
					legalTiles[xPos+k][yPos-1] = false;
				}
			}
		}
		else
		{
			// Placing a Vertical Ship (Setting booleans to false)
			// Setting booleans in ship location to false
			// To let other ships know there is one in this position
			for(int k = 0; k <= shipLegnth; k++)
			{
				legalTiles[xPos][yPos+k] = false;
				
				// Marking all orthogonal tiles as invalid
				// as ships cannot be placed orthogonally
				if((xPos)+1 <= 9)
				{
					legalTiles[xPos+1][yPos+k] = false;
				}
				if((xPos)-1 >= 0)
				{
					legalTiles[xPos-1][yPos+k] = false;
				}
				if((yPos+k)+1 <=9)
				{
					legalTiles[xPos][(yPos+k)+1] = false;
				}
				if((yPos+k)-1 >=0)
				{
					legalTiles[xPos][(yPos+k)-1] = false;
				}
			}
		}
	}

	
	public List<Ship> getShipArray()
	{
		return ships;
	}
	
	@SuppressWarnings("unused")
	private void printShipDebug(boolean[][] legalTiles)
	{
		// *Debug Code*
		for(int q = 0; q<10;q++)
		{
			for(int p = 0; p<10;p++)
			{
				if(legalTiles[p][q] == true)
				{
					System.out.print(" . ");
				}
				else
				{
					System.out.print(" X ");
				}
			}
			System.out.print("\n");
		}
		System.out.print("\n\n\n");
	}
}
