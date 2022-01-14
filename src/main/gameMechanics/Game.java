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
					
					// Bound checking
					if(shipOrient == 0)
					{
						// Horizontal Checks (Only check X pos)
						if(xPos + (i+1) >= 10)
						{
							// Illegal move detected
							// Ship leaks out of bounds
							continue;
						}
					}
					else
					{
						// Vertical Checks (Only check Y pos)
						if(yPos + (i+1) >= 10)
						{
							// Illegal move detected
							// Ship leaks out of bounds
							continue;
						}
					}
					
					// Overlap Checking
					if(shipOrient == 0)
					{
						// Horizontal Ship Overlap Checking
						for(int k = 0; k < i; k++)
						{
							// Looping over each tile in the ship, checking
							// if there is already a ship in that position
							// Using i as it gives the length of the current ship
							if(legalTiles[xPos+i][yPos] == false)
							{
								// Illegal move detected
								// Ship overlaps another ship
								continue;
							}
						}
					}
					else
					{
						// Vertical Ship Overlap Checking
						for(int k = 0; k < i; k++)
						{
							// Looping over each tile in the ship, checking
							// if there is already a ship in that position
							// Using i as it gives the length of the current ship
							if(legalTiles[xPos][yPos+i] == false)
							{
								// Illegal move detected
								// Ship overlaps another ship
								continue;
							}
						}
					}
					
					// Ship Placement
					if(shipOrient == 0)
					{
						// Setting booleans in ship location to false
						// To let other ships know there is one in this position
						for(int k = 0; k < i; k++)
						{
							legalTiles[xPos+i][yPos] = false;
						}
					}
					else
					{
						// Placing a Vertical Ship (Setting booleans to false)
						// Setting booleans in ship location to false
						// To let other ships know there is one in this position
						for(int k = 0; k < i; k++)
						{
							legalTiles[xPos][yPos+i] = false;
						}
					}
				}
			}
		}
		
		for(int i = 0; i<10;i++)
		{
			for(int j = 0; j<10;j++)
			{
				if(legalTiles[i][j] == true)
				{
					System.out.print(" O ");
				}
				else
				{
					System.out.print(" X ");
				}
			}
			System.out.print("\n");
		}
		
	}
}
