package main.gameMechanics;

import java.util.List;
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
	
	Board playerBoard;
	Board enemyBoard;
	
	public Game()
	{
		// Creating instances of each board
		playerBoard = new Board();
		enemyBoard = new Board();
		
		// Set to daemon thread so if the program
		// closes, the thread dies along with it.
		gameThread.setDaemon(true);
		
		// Start the thread
		gameThread.start();
	}
	
	// Ship Array Getters for each board
	public List<Ship> getPlayerShipArray()
	{
		return playerBoard.ships;
	}
	public List<Ship> getEnemyShipArray()
	{
		return enemyBoard.ships;
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
				System.out.println("State: Waiting for play decision...\n");
				
				if(isSinglePlayer)
				{
					//Single Player
					
					// Create random variations of both the 
					// player and enemy boards
					playerBoard.placeShipsAtRandom();
					enemyBoard.placeShipsAtRandom();

					threadSleep();
				}
				else
				{
					// Multi-Player
					
					// Create random variation of the
					// player board
					//playerBoard.placeShipsAtRandom();
					
					// Receive board from enemy
					
				}

				break;
				
			case MAKING_MOVE:
				
				threadSleep();
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
			this.notifyAll();
		}
	}
	
	public synchronized void startSingleplayerGame()
	{
		isSinglePlayer = true;			

		state = GameState.PLACING_SHIP;
		System.out.println("Waking up thread...\n");
		threadWakeUp();
		System.out.println("Thread has been woken up...\n");
	}
	
	public synchronized void finishedShipPlacement()
	{
		state = GameState.MAKING_MOVE;
		System.out.println("Waking up thread...\n");
		threadWakeUp();
		System.out.println("Thread has been woken up...\n");
	}
	
	public synchronized void playerBoardClicked(int x, int y)
	{
		if (state == GameState.PLACING_SHIP)
		{
			// Place ship on that tile
			System.out.println("x: " + x + " y: " + y);
		}
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
	

	
	
	// -----------
	// Making Move
	// ------------
	
	public synchronized void enemyBoardClicked(int x, int y)
	{
		if (state == GameState.PLACING_SHIP)
		{
			// Shoot a bomb
			
			// Check if move is legal
			if(!enemyBoard.isMoveLegal(x, y))
			{
				// Move is not legal, player is required
				// to click again.
				
				// Maybe print error message
				return;
			}
			
			// Add the current move that has been determined legal
			// to the previous moves list for the enemy board.
			enemyBoard.previousMoves[x][y]=true;
			
			// Check if move is hit
			if(enemyBoard.isMoveHit(x, y))
			{
				// Move has been detected as a hit
				
				// Maybe print out a message or
				// start animation. Will decide later
			}
			
			// If legal, thread is awaken and 
			// state changed to waiting for opponent
			

			
			// Make move then state change
			
			
			
			// Check if the game is over and the opponent has won
			if(enemyBoard.playerHasLostTheGame())
			{
				// Game is over
				state = GameState.OPPONENT_HAS_WON;
			}
			
		}
	}

	
	public synchronized void computerMakeMove()
	{
		if(state == GameState.WAITING_FOR_OPPONENT)
		{
			// Create random x and y coordinates
			// to work out the move position
			Random rand = new Random();		
			int x = rand.nextInt(10);
			int y = rand.nextInt(10);
			
			// If the move isn't legal, return the function
			while(!playerBoard.isMoveLegal(x, y))
			{
				x = rand.nextInt(10);
				y = rand.nextInt(10);
			}
			
			// Add NPC move to the previous moves list
			// after it has been determined legal
			playerBoard.previousMoves[x][y]=true;
			
			// Check if move is a hit
			if(playerBoard.isMoveHit(x, y))
			{
				// Move has been detected as a hit
				
				// Maybe print out a message or
				// start animation. Will decide later
			}

			
			
			if(playerBoard.playerHasLostTheGame())
			{
				// Game is over
				state = GameState.PLAYER_HAS_WON;
			}	
		}
	}
	
	public Board getPlayerBoard()
	{
		return playerBoard;
	}
	public Board getEnemyBoard()
	{
		return enemyBoard;
	}
}
