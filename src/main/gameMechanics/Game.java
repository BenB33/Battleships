package main.gameMechanics;

import java.util.Random;

// State machine that follows flow chart
public class Game implements Runnable 
{
	// Game loop thread
	private Thread gameThread = new Thread(this);
	
	public static Game game = new Game();
	
	// Initialise the game state
	private GameState state = GameState.WAITING_FOR_PLAY_DECISION;

	private boolean isSinglePlayer = false;
	
	public Game()
	{
		// Set to daemon thread so if the program
		// closes, the thread dies along with it.
		gameThread.setDaemon(true);
		
		// Start the thread
		gameThread.start();
	}

	@Override
	public void run()
	{
		System.out.println("Game Instance Created...\n");
		while(true)
		{
			switch(state)
			{
			case WAITING_FOR_PLAY_DECISION:
				System.out.println("State: Waiting for play decision...\n");
				threadSleep();
				break;
				
			case PLACING_SHIP:
				System.out.println("State: Placing Ships...\n");
				synchronized(gameThread)
				{
					if(isSinglePlayer)
					{
						//Places enemy ships 
						placeShipsAtRandom();
						threadSleep();
					}
				}

				break;
				
			case MAKING_MOVE:
				
				break;
				
			case WAITING_FOR_OPPONENT:
				
				break;
				
			case PLAYER_HAS_WON:
				
				break;
				
			case OPPONENT_HAS_WON:
				
				break;
			}
		}
	}
	
	private synchronized void threadSleep()
	{
		try 
		{
			this.wait();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private synchronized void threadWakeUp()
	{		
		// Notify the thread, waking it up
		synchronized(gameThread)
		{
			gameThread.notify();
		}
	}
	
	public synchronized void startSingleplayerGame()
	{
		synchronized(gameThread)
		{
			isSinglePlayer = true;
		}
		
		state = GameState.PLACING_SHIP;
		threadWakeUp();
	}
	
	public synchronized void playerBoardClicked(int x, int y)
	{
		if (state == GameState.PLACING_SHIP)
		{
			// Place ship on that tile
			
		}
	}
	
	public synchronized void enemyBoardClicked(int x, int y)
	{
		if (state == GameState.MAKING_MOVE)
		{
			// Shoot a bomb
			
		}
	}

	
	private void makeMove()
	{
		// Click somewhere on board
		
		// 
		
		// Determine if the move is legal
	}
	
	// Handles hosting a multi-player game
	public synchronized void hostMultiplayerGame()
	{
		isSinglePlayer = false;
	}
	
	// Handles joining a multi-player game
	public synchronized void joinMultiplayerGame()
	{
		isSinglePlayer = false;
	}
	
	private void placeShipsAtRandom()
	{
		// Array contains number of each ship size
		// [2 x 1] [2 x 2] [1 x 3] [1 x 4] [1 x 5]
		int[] shipLengthQty = {2, 2, 1, 1, 1};
		
		// Total number of ships per board
		int SHIP_COUNT = 7;
		
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
					int shipOrient = rand.nextInt(2);
					
					System.out.println("----------------------------------------------");
					System.out.println("[Ship Coords X: " + xPos + ", Y: " + yPos + "] [Orient: " + shipOrient + "]\n[Ship Length: " + (i+1) + "]\n\n");
					
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
					
					
					// Ship Placement
					
					if(placeShip(i, xPos, yPos, shipOrient, legalTiles))
					{
						shipPlaced = true;
					}
					
					
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
		}	
	}
	
	
	private boolean isShipOverlapDetected(int shipLength, int xPos, int yPos, int shipOrient, boolean[][] legalTiles)
	{
		// Overlap Checking
		if(shipOrient == 0)
		{
			System.out.println("Checking Horizontal Overlapping Ships...\n");
			
			
			boolean illegalMove = false;
			// Horizontal Ship Overlap Checking
			for(int k = 0; k <= shipLength; k++)
			{
				// Looping over each tile in the ship, checking
				// if there is already a ship in that position
				// Using i as it gives the length of the current ship
				System.out.println("Checking Square: " + (xPos+k) + " " + yPos);
				if(legalTiles[xPos+k][yPos] == false)
				{
					// Illegal move detected
					// Ship overlaps another ship
					System.out.println("[SHIP OVERLAP DETECTED]\n");
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
			System.out.println("Checking Vertical Overlapping Ships...\n");
			// Vertical Ship Overlap Checking
			boolean illegalMove = false;
			for(int k = 0; k <= shipLength; k++)
			{
				// Looping over each tile in the ship, checking
				// if there is already a ship in that position
				// Using i as it gives the length of the current ship
				System.out.println("Checking Square: " + xPos + " " + (yPos+k));
				if(legalTiles[xPos][yPos+k] == false)
				{
					// Illegal move detected
					// Ship overlaps another ship
					System.out.println("[SHIP OVERLAP DETECTED]\n");
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
	
	
	private boolean isShipOutOfBounds(int shipLength, int xPos, int yPos, int shipOrient, boolean[][] legalTiles)
	{
		if(shipOrient == 0)
		{
			System.out.println("Checking Bounds...\n");
			// Horizontal Checks (Only check X pos)
			if(xPos + (shipLength+1) > 10)
			{
				System.out.println("[OUT OF BOUNDS DETECTED]\n");
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
				System.out.println("[OUT OF BOUNDS DETECTED]\n");
				// Illegal move detected
				// Ship leaks out of bounds
				return true;
			}
		}
		// No overlap detected
		return false;
	}


	private boolean placeShip(int shipLegnth, int xPos, int yPos, int shipOrient, boolean[][] legalTiles)
	{
		if(shipOrient == 0)
		{
			System.out.println("Placing Ships...\n");
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
			
			// Ship placed successfully
			return true;
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
			
			// Ship placed successfully
			return true;
		}
	}
}
