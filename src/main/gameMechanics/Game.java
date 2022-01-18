package main.gameMechanics;

import java.util.List;

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
					playerBoard.placeShipsAtRandom();
					enemyBoard.placeShipsAtRandom();

					threadSleep();
				}
				else
				{
					// Multi-Player
					
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

	
	@SuppressWarnings("unused")
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
	
	// Ship Array Getters for each board
	public List<Ship> getPlayerShipArray()
	{
		return playerBoard.ships;
	}
	public List<Ship> getEnemyShipArray()
	{
		return enemyBoard.ships;
	}
}
