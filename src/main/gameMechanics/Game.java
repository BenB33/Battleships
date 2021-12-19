package main.gameMechanics;

// State machine that follows flow chart
public class Game implements Runnable 
{
	// Game loop thread
	private Thread gameThread = new Thread(this);	
	
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
		GameState state = GameState.WAITING_FOR_PLAY_DECISION;
		while(true)
		{
			switch(state)
			{
			case WAITING_FOR_PLAY_DECISION:
				System.out.println("Cum");
				threadSleep();
				break;
				
			case PLACING_SHIP:
				
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
	
	public synchronized void threadSleep()
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
	
	public synchronized void threadWakeUp()
	{		
		// Notify the thread, waking it up
		gameThread.notify();
	}
	
	private void placingShips()
	{
		
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
		
	}
	
	// Handles joining a multi-player game
	public synchronized void joinMultiplayerGame()
	{
		
	}



	
	
}
