package main.gameMechanics;

import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import main.networking.Client;
import main.networking.Server;

// State machine that follows flow chart
public class Game implements Runnable 
{
	// Game loop thread
	private Thread gameThread = new Thread(this);
	
	public static Game game = new Game();
	
	// Initialise the game state
	private GameState state = GameState.WAITING_FOR_PLAY_DECISION;

	private boolean isSinglePlayer = false;
	NetworkRole playerRole;
	
	Board playerBoard;
	Board enemyBoard;
	
	private Server server = null;
	private Client client = null;
	
	
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
				System.out.println("[State] Waiting for play decision...\n");
				threadSleep();
				
				break;
				
			case PLACING_SHIP:
				System.out.println("[State] Placing Ships...\n");
				
				if(isSinglePlayer)
				{
					//Single Player
					
					// Create random variations of both the 
					// player and enemy boards
					playerBoard.placeShipsAtRandom();
					enemyBoard.placeShipsAtRandom();

					// Ships have been placed for both players
					// player is ready to make move so state is changed
					state = GameState.MAKING_MOVE;
				}
				else
				{
					// Multi-Player

					// The host randomises both the player
					// and enemy boards and then sends those
					// boards to the client connected.					
				}

				break;
				
			case MAKING_MOVE:
				// Local player makes move
				// While in the MAKING_MOVE state, the player
				// is able to click tiles to make moves.
				System.out.println("[State] Making move...\n");

				threadSleep();
				break;
				
			case WAITING_FOR_OPPONENT:
				// CPU/multiplayer opponent makes move
				System.out.println("[State] Opponent Move...\n");
				
				if(isSinglePlayer)
				{
					computerMakeMove();
				}
				else
				{
					// Multiplayer
					System.out.println("[LOG] Receiving gamestate from opponent");
					
					String stringGameState = "";
					
					if(playerRole == NetworkRole.HOST)
					{
						stringGameState = server.receieveFromClient();
					}
					else
					{
						stringGameState = client.receiveFromHost();
					}
					
					JSONObject jsonGameState = new JSONObject(stringGameState);
					var hostBoardJson = jsonGameState.get("Host Board");
					enemyBoard.deserializeBoard(hostBoardJson.toString());
					
					var clientBoardJson = jsonGameState.get("Client Board");
					playerBoard.deserializeBoard(clientBoardJson.toString());
				}

				
				if(playerBoard.playerHasLostTheGame())
				{
					state = GameState.OPPONENT_HAS_WON;
				}
				else
				{
					state = GameState.MAKING_MOVE;
				}
				break;
				
			case PLAYER_HAS_WON:
				System.out.println("[State] PLAYER HAS WON THE GAME!");
				rematchModalPopup();
				break;
				
			case OPPONENT_HAS_WON:
				System.out.println("[State] OPPONENT HAS WON THE GAME!");
				rematchModalPopup();
				// TODO: Add modal prompting for game replay
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
		playerBoard.resetBoard();
		enemyBoard.resetBoard();
		
		isSinglePlayer = true;
		state = GameState.PLACING_SHIP;
		System.out.println("end of start singleplayer game func");
		threadWakeUp();			
	}
	
	public synchronized void endSinglePlayerGame()
	{
		// The single player game will be ended so the 
		// state will change back to waiting for play decision
		state = GameState.WAITING_FOR_PLAY_DECISION;
		isSinglePlayer = false;
		// Both the player and enemy boards are
		// reset so a new game can be started
		playerBoard.resetBoard();
		enemyBoard.resetBoard();
		// The thread is awaken in order to change states
		threadWakeUp();
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
	public synchronized void hostMultiplayerGame(String ipAddress, String port)
	{
		System.out.println(ipAddress + ":" + port);
		isSinglePlayer = false;
		playerRole = NetworkRole.HOST;
		
		// TODO: Add host game functionality
		client = null;		
		server = new Server(ipAddress, port);
		server.Start();
		
		// Generate the ships randomly on both boards
		// to start the game
		playerBoard.placeShipsAtRandom();
		enemyBoard.placeShipsAtRandom();
		
		// Send both boards to the client
		Board[] boards = { playerBoard, enemyBoard };
		server.sendToClient(boards);
		
		state = GameState.MAKING_MOVE;
	}
	
	// Handles joining a multi-player game
	public synchronized void joinMultiplayerGame(String ipAddress, String port)
	{
		System.out.println(ipAddress + ":" + port);
		isSinglePlayer = false;
		playerRole = NetworkRole.CLIENT;
		
		// TODO: Add join game functionality
		server = null;
		client = new Client(ipAddress, port);
		
		// Receive the json game state that was 
		// generated by the host
		String stringGameState = client.receiveFromHost();
		JSONObject jsonGameState = new JSONObject(stringGameState);
		
		// Sync the enemy board with the data recieved
		var hostBoardJson = jsonGameState.get("Host Board");
		enemyBoard.deserializeBoard(hostBoardJson.toString());
		
		// Sync the player board with the json data recieved 
		var clientBoardJson = jsonGameState.get("Client Board");
		playerBoard.deserializeBoard(clientBoardJson.toString());
		
		
		state = GameState.WAITING_FOR_OPPONENT;
		threadWakeUp();
	}
	
	
	// -----------
	// Making Move
	// ------------
	
	public synchronized void enemyBoardClicked(int x, int y)
	{
		if (state == GameState.MAKING_MOVE)
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
			
			// Apply the move that has been deemed legal
			if(enemyBoard.applyMove(x, y))
			{
				// Move has been detected as a hit
				
				// Maybe print out a message or
				// start animation. Will decide later
			}
			
			// If legal, thread is awaken and 
			// state changed to waiting for opponent
			
			if(!isSinglePlayer)
			{
				System.out.println("[LOG] Sending updated gamestate to opponent.");
				
				Board[] boards = { playerBoard, enemyBoard };
				
				if(playerRole == NetworkRole.HOST)
				{
					server.sendToClient(boards);
				}
				else
				{
					client.sendToHost(boards);
				}
			}
			
			// Make move then state change
			
			// TODO: Look at game winning synchonization point
			// Check if the game is over and the opponent has won
			if(enemyBoard.playerHasLostTheGame())
			{
				// Game is over
				state = GameState.PLAYER_HAS_WON;
			}
			else
			{
				state = GameState.WAITING_FOR_OPPONENT;				
			}
			threadWakeUp();
		}
	}

	
	public synchronized void computerMakeMove()
	{
		if(state == GameState.WAITING_FOR_OPPONENT)
		{
			// Create random x and y coordinates
			// to work out the move position
			Random rand = new Random();	
			System.out.println("Rolling random coordinates...");
			int x = rand.nextInt(10);
			int y = rand.nextInt(10);
			
			// If the move isn't legal, return the function
			while(!playerBoard.isMoveLegal(x, y))
			{
				System.out.println("Move was illegal. Rerolling...");
				x = rand.nextInt(10);
				y = rand.nextInt(10);
			}

			// Add NPC move to the previous moves list
			// after it has been determined legal
			playerBoard.previousMoves[x][y]=true;
			
			// Check if move is a hit
			if(playerBoard.applyMove(x, y))
			{
				// Move has been detected as a hit
				System.out.println("Move was a hit!");
			}
			
			if(playerBoard.playerHasLostTheGame())
			{
				// Game is over
				state = GameState.PLAYER_HAS_WON;
			}	
		}
	}
	
	// TODO: Move to GUI class maybe
	private void rematchModalPopup()
	{
		String title;
		if(state == GameState.PLAYER_HAS_WON)
		{
			title = "WINNER!";
		}
		else
		{
			title = "LOSER!";
		}
		int result = JOptionPane.showConfirmDialog(null, "Would you like a rematch?", title, JOptionPane.YES_NO_OPTION);
		
		if(result == JOptionPane.YES_OPTION)
		{
			// Rematch
			System.out.println("YES OPTION CHOSEN");
			startSingleplayerGame();
		}
		else if(result == JOptionPane.NO_OPTION)
		{
			// End game
			System.out.println("NO OPTION CHOSEN");
			Game.game.endSinglePlayerGame();
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
	
    public void deserializeGame(String json)
    {
        JSONObject jsonObject = new JSONObject(json);

        // Detect which board is the host and which one is the client
        // and deserialize the game boards accordingly
        if(playerRole == NetworkRole.HOST)
        {
        	playerBoard.deserializeBoard(jsonObject.get("Host Board").toString());
        	enemyBoard.deserializeBoard(jsonObject.get("Client Board").toString());
        }
        else if(playerRole == NetworkRole.CLIENT)
        {
        	enemyBoard.deserializeBoard(jsonObject.get("Host Board").toString());
        	playerBoard.deserializeBoard(jsonObject.get("Client Board").toString());
        }
    }
}
