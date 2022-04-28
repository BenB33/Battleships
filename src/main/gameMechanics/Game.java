package main.gameMechanics;

import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import main.networking.Client;
import main.networking.Server;

// State machine that follows flow chart
public class Game implements Runnable{
	// Game loop thread
	private Thread gameThread = new Thread(this);
	
	// Instance of game
	public static Game game = new Game();
	
	// Initialise the game state
	private GameState state = GameState.WAITING_FOR_PLAY_DECISION;

	// Setting flags
	private boolean isSinglePlayer = false;
	boolean isHostTurn = true;
	NetworkRole playerRole;
	
	// Instances for each player's board
	Board playerBoard;
	Board enemyBoard;
	
	// Setting server/client to null before hand
	private Server server = null;
	private Client client = null;
	
	// IP and port of connected client
	String connectedIPAddress = "";
	String connectedPort = "";

	
	public Game(){
		// Creating instances of each board
		playerBoard = new Board();
		enemyBoard = new Board();
		
		// Set to daemon thread so if the program
		// closes, the thread dies along with it.
		gameThread.setDaemon(true);
		
		// Start the thread
		gameThread.start();
	}
	

	// The run function handles each game state. The while loop
	// contains a switch case that, depending on the current
	// game state, performs certain actions
	//
	@Override
	public void run(){
		System.out.println("[LOG] Game Instance Created");
		while(true){
			switch(state){
			case WAITING_FOR_PLAY_DECISION:
				// WAITING_FOR_PLAY_DECISION state is where the player
				// is waiting for singleplayer/multiplayer decision
				System.out.println("[State] Waiting for play decision");
				
				threadSleep();
				break;
				
			case PLACING_SHIP:
				// PLACING_SHIPS state is where the game places the
				// ships for both boards at random
				System.out.println("[State] Placing Ships");
				
				// If singleplayer is selected
				if(isSinglePlayer){
					// Create random variations of both the 
					// player and enemy boards
					playerBoard.placeShipsAtRandom();
					enemyBoard.placeShipsAtRandom();

					// Ships have been placed for both players
					// player is ready to make move so state is changed
					state = GameState.MAKING_MOVE;
				}
				break;
				
			case MAKING_MOVE:
				// MAKING_MOVE state is where the local player is making
				// their move. The player is able to click a tile to move
				System.out.println("[State] Making move");
				
				threadSleep();
				break;
				
			case WAITING_FOR_OPPONENT:
				// WAITING_FOR_OPPONENT state is where the opponent player is
				// making their move. The local player must wait while the move
				// is being carried out. For single player games, this state is
				// where the CPU's move will be carried out.
				System.out.println("[State] Opponent Move");

				// For singleplayer games, CPU move is made
				if(isSinglePlayer) computerMakeMove();
				// For multiplayer games, gamestate data will be receieved
				// from the opponent once their move has been made
				else{
					System.out.println("[LOG] Receiving gamestate from opponent");
					
					String stringGameState = "";
					
					// Depending on the player's role, receive data from client or host
					if(playerRole == NetworkRole.HOST) stringGameState = server.receieveFromClient();
					else stringGameState = client.receiveFromHost();
					
					// Deserialize the data received for both boards
					JSONObject jsonGameState = new JSONObject(stringGameState);
					Object hostBoardJson = jsonGameState.get("Host Board");
					enemyBoard.deserializeBoard(hostBoardJson.toString());
					Object clientBoardJson = jsonGameState.get("Client Board");
					playerBoard.deserializeBoard(clientBoardJson.toString());
				}
				
				// If the player has lost the game, change state accordingly, if not
				// continue on with the game.
				if(playerBoard.playerHasLostTheGame()) state = GameState.OPPONENT_HAS_WON;
				else state = GameState.MAKING_MOVE;
				break;
				
			case PLAYER_HAS_WON:
				// PLAYER_HAS_WON state is where if the local player
				// has won the game, the rematch modal is triggered
				System.out.println("[State] PLAYER HAS WON THE GAME!");
				
				if(playerRole == NetworkRole.HOST)
				{
					rematchModalPopup();
				}
				else
				{
					state = GameState.WAITING_FOR_HOST_REMATCH_DECISION;
				}
				
				
				break;
				
			case OPPONENT_HAS_WON:
				// OPPONENT_HAS_WON state is where if the opponent player
				// has won the game, the rematch modal is triggered
				System.out.println("[State] OPPONENT HAS WON THE GAME!");
				
				if(playerRole == NetworkRole.HOST)
				{
					rematchModalPopup();
				}
				else
				{
					state = GameState.WAITING_FOR_HOST_REMATCH_DECISION;
				}
				
				break;
				
			case WAITING_FOR_HOST_REMATCH_DECISION:
				// WAITING_FOR_HOST_REMATCH_DECISION state is where the host
				// is prompted with a rematch modal, they made the decision
				// before the client is prompted with the modal.
				System.out.println("[State] Waiting for host rematch decision");

				if(!isSinglePlayer){
					if(playerRole == NetworkRole.CLIENT){
						System.out.println("[LOG] Receiving rematch result from host");
						
						String rematchResult = client.receiveFromHost();
						
						if(rematchResult.equals("rematch_yes")){
							// Host has chosen to rematch so prompt the client with the rematch modal
							System.out.println("[LOG] Host has chosen to rematch, make your decision.");
							rematchModalPopup();
						}
						else if(rematchResult.equals("rematch_no")){
							// Host has chosen not to rematch
							System.out.println("[LOG] Host has chosen not to rematch.");
							client.shutdown();
							playerBoard.resetBoard();
							enemyBoard.resetBoard();
							state = GameState.WAITING_FOR_PLAY_DECISION;
						}	
					}
					else if(playerRole == NetworkRole.HOST){
						System.out.println("[LOG] Receiving rematch result from client");
						String rematchResult = server.receieveFromClient();
						
						if(rematchResult.equals("rematch_yes")){
							// Initiate rematch by resetting both boards 
							// and generating a new set of random ships.
							playerBoard.resetBoard();
							enemyBoard.resetBoard();
							playerBoard.placeShipsAtRandom();
							enemyBoard.placeShipsAtRandom();
							
							// Send both boards to the client
							Board[] boards = { playerBoard, enemyBoard };
							server.sendToClient(boards);
							
							state = GameState.MAKING_MOVE;
						}
						else if(rematchResult.equals("rematch_no")){
							// Host has chosen not to rematch
							server.shutdown();
							playerBoard.resetBoard();
							enemyBoard.resetBoard();
							state = GameState.WAITING_FOR_PLAY_DECISION;
						}
					}
				}
				else rematchModalPopup();
				break;
			}
		}
	}
	
	
	// The thread is told to sleep by calling
	// wait on it
	//
	private synchronized void threadSleep(){
		try {
			this.wait();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	// In order to wake the thread up, 
	// notifyAll is called
	//
	private synchronized void threadWakeUp(){		
		// Notify the thread, waking it up
		synchronized(gameThread){
			this.notifyAll();
		}
	}
	
	
	// A single player game will start, boards are reset
	// to ensure to overlap happens, then the gamestate is 
	// updated and the thread is awaken to start the game
	//
	public synchronized void startSingleplayerGame(){
		playerBoard.resetBoard();
		enemyBoard.resetBoard();
		
		isSinglePlayer = true;
		state = GameState.PLACING_SHIP;
		System.out.println("[LOG] Starting Singleplayer");
		threadWakeUp();			
	}
	
	
	// The single player game will be ended and the boards are
	// reset ready for another game to start. Gamestate is updated
	//
	public synchronized void endSinglePlayerGame(){

		state = GameState.WAITING_FOR_PLAY_DECISION;
		isSinglePlayer = false;
		// Both the player and enemy boards are
		// reset so a new game can be started
		playerBoard.resetBoard();
		enemyBoard.resetBoard();
		System.out.println("[LOG] Ending Singleplayer");
		// The thread is awaken in order to change states
		threadWakeUp();
	}
	
	
	// Debug function to display the coordinates when user clicks their own board
	//
	public synchronized void playerBoardClicked(int x, int y){
		if (state == GameState.PLACING_SHIP) System.out.println("x: " + x + " y: " + y);

	}
	
	
	// Handles a host hosting a multi-player game. Using the passed IP Address
	// and port, a server object is created and used to send the initial
	// game state data to the client.
	//
	public synchronized void hostMultiplayerGame(String ipAddress, String port){
		// Update isSinglePlayer flag and playerRole as host
		isSinglePlayer = false;
		playerRole = NetworkRole.HOST;
		// Set network variables to create Server object and start the server
		connectedIPAddress = ipAddress;
		connectedPort = port;
		client = null;		
		server = new Server(ipAddress, port);
		server.start();
		
		System.out.println(ipAddress + ":" + port);

		// Generate the ships randomly on both
		// boards to start the game
		playerBoard.placeShipsAtRandom();
		enemyBoard.placeShipsAtRandom();
		
		// Send both boards to the client
		Board[] boards = { playerBoard, enemyBoard };
		server.sendToClient(boards);
		
		state = GameState.MAKING_MOVE;
	}
	
	
	// Handles a client joining a multi-player game. Using the passed IP Address
	// and port, a client object is created and used to receieve the initial
	// game state data from the host.
	//
	public synchronized void joinMultiplayerGame(String ipAddress, String port){
		// Update isSinglePlayer flag and playerRole as client
		isSinglePlayer = false;
		playerRole     = NetworkRole.CLIENT;
		// Set network variables to create Client object
		connectedIPAddress = ipAddress;
		connectedPort      = port;
		server = null;
		client = new Client(ipAddress, port);
		
		System.out.println(ipAddress + ":" + port);

		// Receive the json game state that was 
		// generated by the host
		String stringGameState = client.receiveFromHost();
		JSONObject jsonGameState = new JSONObject(stringGameState);
		
		// Sync the enemy board with the data recieved
		Object hostBoardJson = jsonGameState.get("Host Board");
		enemyBoard.deserializeBoard(hostBoardJson.toString());
		
		// Sync the player board with the json data recieved 
		Object clientBoardJson = jsonGameState.get("Client Board");
		playerBoard.deserializeBoard(clientBoardJson.toString());
		
		// Update the game state and wake up the thread
		state = GameState.WAITING_FOR_OPPONENT;
		threadWakeUp();
	}
	

	// When the enemy board has been clicked, multiple checks
	// are carried out to ensure if a move has been made, it is legal
	// and if needed, can be sent to the opponent.
	//
	public synchronized void enemyBoardClicked(int x, int y){
		if (state == GameState.MAKING_MOVE){			
			// If move is considered illegal, return the function
			if(!enemyBoard.isMoveLegal(x, y)) return;
			
			// Add the current move that has been determined legal
			// to the previous moves list for the enemy board.
			enemyBoard.previousMoves[x][y]=true;
			
			// Apply the move that has been deemed legal
			if(enemyBoard.applyMove(x, y)){
				// Move has been detected as a hit
				
				// TODO: Add hit sound
			}
			
			// If multiplayer
			if(!isSinglePlayer){
				System.out.println("[LOG] Sending updated gamestate to opponent.");
				Board[] boards = { playerBoard, enemyBoard };
				
				// Determine what the player's role is, then send the board data to their opponent
				if(playerRole == NetworkRole.HOST)        server.sendToClient(boards);
				else if(playerRole == NetworkRole.CLIENT) client.sendToHost(boards);
			}

			// If the enemy has lost the game, update the state accordingly, otherwise
			// continue with the game.
			if(enemyBoard.playerHasLostTheGame()) state = GameState.PLAYER_HAS_WON;
			else state = GameState.WAITING_FOR_OPPONENT;	
			threadWakeUp();
		}
	}

	
	// The CPU move will be determined and made. Checks to ensure the randomly
	// generated move is legal.
	//
	public synchronized void computerMakeMove(){
		if(state == GameState.WAITING_FOR_OPPONENT){
			// Generate random move x and y coordinates
			Random rand = new Random();	
			System.out.println("Rolling random coordinates...");
			int x = rand.nextInt(10);
			int y = rand.nextInt(10);
			
			// If the move is determined illegal, generate new coordinates
			// until they are legal
			while(!playerBoard.isMoveLegal(x, y)){
				System.out.println("[LOG] Move was illegal. Rerolling...");
				x = rand.nextInt(10);
				y = rand.nextInt(10);
			}

			// Add CPU's move to the previous moves list
			// after it has been determined legal
			playerBoard.previousMoves[x][y]=true;
			
			// Check if move is a hit
			if(playerBoard.applyMove(x, y)) System.out.println("Move was a hit!");
			
			// Check if the player has lost the game
			if(playerBoard.playerHasLostTheGame()) state = GameState.PLAYER_HAS_WON;
		}
	}
	
	
	// The rematch modal prompts players asking whether they would like
	// a rematch or not. If they choose yes, a new game with the same
	// opponent is setup, if no, the players client's are reset and
	// waiting for a new play decision
	//
	private void rematchModalPopup(){
		// The modal title is determined for the winner and the loser
		String title;
		if(state == GameState.PLAYER_HAS_WON) title = "WINNER!";
		else 								  title = "LOSER!";
		
		// An option pane is built, returning the yes/no result in integer form
		int result = JOptionPane.showConfirmDialog(null, "Would you like a rematch?", title, JOptionPane.YES_NO_OPTION);
		
		// If the user chooses yes to a rematch
		if(result == JOptionPane.YES_OPTION){
			// If singleplayer game, call startSingleplayerGame
			if(isSinglePlayer) startSingleplayerGame();
			// If multiplayer, the player role is determined
			else
			{
				// If the player is host, a confirmation message is sent
				// to the client to let them know the host wants a rematch
				if(playerRole == NetworkRole.HOST){					
					server.sendClientRematchResult("rematch_yes");
					state = GameState.WAITING_FOR_HOST_REMATCH_DECISION;
				}
				// If the player is client, they will only be prompted if the
				// host has already confirmed they want a rematch. If the client
				// also confirms they want a rematch, then they send a confirmation
				// message to the host and wait for new board data to be sent.
				else if (playerRole == NetworkRole.CLIENT){
					
					client.sendHostRematchResult("rematch_yes");
					
					String stringGameState   = client.receiveFromHost();
					JSONObject jsonGameState = new JSONObject(stringGameState);
					
					// Sync the enemy board with the data recieved
					Object hostBoardJson = jsonGameState.get("Host Board");
					enemyBoard.deserializeBoard(hostBoardJson.toString());
					
					// Sync the player board with the json data recieved 
					Object clientBoardJson = jsonGameState.get("Client Board");
					playerBoard.deserializeBoard(clientBoardJson.toString());
					
					state = GameState.WAITING_FOR_OPPONENT;
					threadWakeUp();
				}
			}
			
		}
		// If the user chooses no to a rematch
		else if(result == JOptionPane.NO_OPTION){
			// If no rematch, then the singleplayer game will end
			if(isSinglePlayer) Game.game.endSinglePlayerGame();
			// If multiplayer, the player role is determined
			else{
				// If no rematch, the host will send a confirmation message
				// to the client, and then reset boards and shutdown connection
				if(playerRole == NetworkRole.HOST){
					server.sendClientRematchResult("rematch_no");
					
					server.shutdown();
					playerBoard.resetBoard();
					enemyBoard.resetBoard();
					
					server = null;
					client = null;
					
					connectedIPAddress = "";
					connectedPort = "";
					
					state = GameState.WAITING_FOR_PLAY_DECISION;
				}
				// If no rematch, the client will send a confirmation message
				// to the host, and then reset boards and shutdown connection
				else if (playerRole == NetworkRole.CLIENT){
					client.sendHostRematchResult("rematch_no");
					
					client.shutdown();
					playerBoard.resetBoard();
					enemyBoard.resetBoard();
					
					server = null;
					client = null;
					
					connectedIPAddress = "";
					connectedPort = "";
					
					state = GameState.WAITING_FOR_PLAY_DECISION;
				}
			}
		}
	}
	

	// Deserializes json passed to the function and passes
	//  the deserialized boards to the respective functions
	//  for the boards to then be deserialized seperately
	//
    public void deserializeGame(String json){
        JSONObject jsonObject = new JSONObject(json);

        // Detect which board is the host and which one is the client
        // and deserialize the game boards accordingly
        if(playerRole == NetworkRole.HOST){
        	playerBoard.deserializeBoard(jsonObject.get("Host Board").toString());
        	enemyBoard.deserializeBoard(jsonObject.get("Client Board").toString());
        }
        else if(playerRole == NetworkRole.CLIENT){
        	enemyBoard.deserializeBoard(jsonObject.get("Host Board").toString());
        	playerBoard.deserializeBoard(jsonObject.get("Client Board").toString());
        }
    }
    
    
    // Getters and setters
    //
	public Board getPlayerBoard()          { return playerBoard; }
	public Board getEnemyBoard()           { return enemyBoard; }
	public int getRemainingShipIndicator() { return enemyBoard.getRemainingShipAmount(); }
	public List<Ship> getPlayerShipArray() { return playerBoard.ships; }
	public List<Ship> getEnemyShipArray() { return enemyBoard.ships; }
    
	public String getTurnIndicator(){
    	if(state == GameState.WAITING_FOR_OPPONENT) return "Enemies";
    	else 										return "Yours";
    }
}
