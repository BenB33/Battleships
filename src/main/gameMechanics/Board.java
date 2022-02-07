package main.gameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.Point;

public class Board {

	public Board() 
	{
		
	}
	
	// Create an array list of ships
	List<Ship> ships = new ArrayList<Ship>();
	boolean[][] previousMoves = new boolean[10][10];
	
	
	
	public void placeShipsAtRandom()
	{
		resetBoard();
		
		// Array contains number of each ship size
		// [2 x 1] [2 x 2] [1 x 3] [1 x 4] [1 x 5]
		int[] shipLengthQty = {2, 2, 1, 1, 1};
		
		// Create random seed
		Random rand = new Random();
		
		// Initialising and setting all ship legal flags to true
		boolean[][] legalTiles = new boolean[10][10];
		for(int i = 0; i<10;i++)
		{
			for(int j = 0; j<10;j++)
			{
				legalTiles[i][j] = true;
			}
		}

		// Looping through each ship
		for(int i = 0; i < shipLengthQty.length; i++)
		{
			// Looping through each tile of the current ship
			for(int j = 0; j < shipLengthQty[i]; j++)
			{
				boolean shipPlaced = false;
				while(!shipPlaced)
				{
					// Random x and y coordinates to attempt to place
					// the ship in a valid location
					int xPos = rand.nextInt(10);
					int yPos = rand.nextInt(10);
					
					ShipOrientation shipOrient = ShipOrientation.values()[rand.nextInt(2)];
					
					// Bound checking the random location of the ship
					if(isShipOutOfBounds(i, xPos, yPos, shipOrient, legalTiles))
					{
						continue;
					}
					
					// Ship Overlap Checking
					if(isShipOverlapDetected(i, xPos, yPos, shipOrient, legalTiles))
					{
						continue;
					}
					
					// Flag what tiles are legal by calculating the tiles
					// surrounding the current tile
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

	// Getter for the ship list
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
	
	
	public boolean isMoveLegal(int x, int y)
	{
		// Looping through the previous moves list of points checking
		// whether the move being passed to the function is already
		// in the list. If it is in the list then return false, if
		// not them return true and add the move to the previous moves list.
		if(previousMoves[x][y])
		{
			System.out.println("Prevous move detected.");
			return false;
		}

		//previousMoves.add(new Point(x, y));
		System.out.println("Move is legal. Added to List");
		return true;
	}
	
	// This function makes the move and detects if that move is a hit
	public boolean applyMove(int x, int y)
	{
		for(int i = 0; i < ships.size(); i++)
		{
			for(int j = 0; j < ships.get(i).getShipLength(); j++)
			{
				if(ships.get(i).getShipOrient() == ShipOrientation.HORIZONTAL)
				{
					if(x == ships.get(i).getShipPos().x+j && y == ships.get(i).getShipPos().y)
					{
						// Move is hit
						System.out.println("[x: " + x + "y: " + y + "] Move is a hit!");
						ships.get(i).setShipHitBoolean(j);
						if(ships.get(i).isShipSunk())
						{
							// TODO: Change sunken ship texture
						}

						return true;
					}
				}
				else if(ships.get(i).getShipOrient() == ShipOrientation.VERTICAL)
				{
					if(x == ships.get(i).getShipPos().x && y == ships.get(i).getShipPos().y+j)
					{
						// Move is hit
						System.out.println("[x: " + x + "y: " + y + "] Move is a hit!");
						ships.get(i).setShipHitBoolean(j);
						if(ships.get(i).isShipSunk())
						{
							// TODO: Change sunken ship texture
						}
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	
	public boolean playerHasLostTheGame()
	{
		for(int i = 0; i < ships.size(); i++)
		{
			if(!ships.get(i).isShipSunk())
			{
				// If the player has at least one
				// functioning ship, the game
				// continues
				return false;
			}
		}
		
		// If all of the player ships are sunk
		// the player has lost the game
		return true;
	}
	
	
	private void resetBoard()
	{
		// Clears the ship list and previous moves
		// list in case previous game
		// contains ships
		ships.clear();
		
		// Setting all booleans in previousMoves
		// to false
		for(int i = 0; i<10; i++)
		{
			for(int j = 0; j<10; j++)
			{
				previousMoves[i][j] = false;
			}
		}
	}
	
	public boolean[][] getPreviousMovesList()
	{
		return previousMoves;
	}
}
