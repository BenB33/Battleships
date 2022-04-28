package main.gameMechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Board {

	// Array contains number of each ship size
	// [2 x 1] [2 x 2] [1 x 3] [1 x 4] [1 x 5]
	final static private int[] SHIP_LENGTH_QTY = {2, 2, 1, 1, 1};
	
	final static private int BOARD_MIN = 0;
	final static private int BOARD_MAX = 10;
	
	// Create an array list of ships
	List<Ship> ships = null;
	boolean[][] previousMoves = new boolean[10][10];
	
	public Board(){
		ships = new ArrayList<Ship>();
		
		// Loop will run for the sum of values in SHIP_LENGTH_QTY
		// and initialise a ship for each loop.
		for(int i = 0; i < IntStream.of(SHIP_LENGTH_QTY).sum(); i++){
			ships.add(new Ship(ShipOrientation.HORIZONTAL, 0, 0, 0));
		}
	}

	
	// Populate the ships ArrayList with a set of ships placed randomly
	// on the board. Checks are made to ensure the ships are placed
	// at legal positions, and new legal flags are updated.
	//
	public void placeShipsAtRandom(){
		// Reset the board before generating 
		// another set of random ships
		resetBoard();

		// Create random seed
		Random rand = new Random();
		
		// Initialising and setting all ship legal flags to true
		boolean[][] legalTiles = new boolean[10][10];
		for(int i = BOARD_MIN; i < BOARD_MAX; i++){
			for(int j = BOARD_MIN; j < BOARD_MAX; j++){
				legalTiles[i][j] = true;
			}
		}
		
		// Reset the array list to place ships
		ships = new ArrayList<Ship>();
		
		// Looping through each ship
		for(int i = 0; i < SHIP_LENGTH_QTY.length; i++){
			// Looping through each tile of the current ship
			for(int j = 0; j < SHIP_LENGTH_QTY[i]; j++){
				boolean shipPlaced = false;
				while(!shipPlaced){
					// Random x and y coordinates to attempt 
					// to place the ship in a valid location
					int xPos = rand.nextInt(10);
					int yPos = rand.nextInt(10);
					
					// Randomise the ship orientation
					ShipOrientation shipOrient = ShipOrientation.values()[rand.nextInt(2)];
					
					// Bound checking the random location of the ship
					if(isShipOutOfBounds(i, xPos, yPos, shipOrient, legalTiles)) continue;
					
					// Ship Overlap Checking
					if(isShipOverlapDetected(i, xPos, yPos, shipOrient, legalTiles)) continue;
					
					// Flag what tiles are legal by calculating the tiles
					// surrounding the current tile
					flagLegalTiles(i, xPos, yPos, shipOrient, legalTiles);

					// Add new ship with variables that have been checked for validity.
					System.out.println("Adding Ship of length: " + i + " xPos: " + xPos + " yPos: " + yPos);
					ships.add(new Ship(shipOrient, i+1, xPos, yPos));
					shipPlaced = true;
					//printShipDebug(legalTiles);
				}
			}
		}	
	}
	
	
	// Detect whether a ship being placed will overlap a ship already placed on the
	// board. If a ship will detect, return false to indicate illgal ship placement.
	// If the ship doesn't detect, return true so ship can be placed.
	// 
	private boolean isShipOverlapDetected(int shipLength, int xPos, int yPos, ShipOrientation shipOrient, boolean[][] legalTiles){
		// Check ships of horizontal orientation
		if(shipOrient == ShipOrientation.HORIZONTAL){			
			// Checking each tile of the ship
			for(int k = 0; k <= shipLength; k++){
				// If the tile in question is illegal, then  
				// an overlap is detected so return true
				if(legalTiles[xPos+k][yPos] == false) return true;
			}
		}
		// Check ships of vertical orientation
		else if(shipOrient == ShipOrientation.VERTICAL){
			// Checking each tile of the ship
			for(int k = 0; k <= shipLength; k++){
				// If the tile in question is illegal, then  
				// an overlap is detected so return true
				if(legalTiles[xPos][yPos+k] == false) return true;
			}
		}
		
		// No Ship Overlap Detected
		return false;
	}
	
	
	// Check if any ship being placed is out of bounds, if out of
	// bounds detected, the function returns fasle. If not, return true
	//
	private boolean isShipOutOfBounds(int shipLength, int xPos, int yPos, ShipOrientation shipOrient, boolean[][] legalTiles){
		// Bounds checking for horizontal ship
		if(shipOrient == ShipOrientation.HORIZONTAL){
			// Out of bounds detected, returning false
			if(xPos + (shipLength+1) > 10) return true;
		}
		// Bounds checking for vertical ship
		else if (shipOrient == ShipOrientation.VERTICAL){
			// Out of bounds detected, returning false
			if(yPos + (shipLength+1) > 10) return true;
		}
		// No out of bounds detected, returning false
		return false;
	}
	
	
	// Flag each tile that can legally contain a ship, determined by the position of other
	// ships. Ships cannot placed orthogonally to other ships, so all orthogonal tiles
	// are flaged as illegal.
	// 
	private void flagLegalTiles(int shipLength, int xPos, int yPos, ShipOrientation shipOrient, boolean[][] legalTiles){
		if(shipOrient == ShipOrientation.HORIZONTAL){
			// Set all ship tiles and orthogonal tiles to the ship 
			// as illegal so another ship cannot be placed there.
			for(int k = 0; k <= shipLength; k++){
				legalTiles[xPos+k][yPos] = false;
				
				if((xPos+k)+1 <= 9) legalTiles[(xPos+k)+1][yPos] = false;
				if((xPos+k)-1 >= 0) legalTiles[(xPos+k)-1][yPos] = false;
				if((yPos)+1 <=9)    legalTiles[xPos+k][yPos+1]   = false;
				if((yPos)-1 >=0)    legalTiles[xPos+k][yPos-1]   = false;
			}
		}
		else{
			// Set all ship tiles and orthogonal tiles to the ship 
			// as illegal so another ship cannot be placed there.
			for(int k = 0; k <= shipLength; k++){
				legalTiles[xPos][yPos+k] = false;

				if((xPos)+1 <= 9)  legalTiles[xPos+1][yPos+k]   = false;
				if((xPos)-1 >= 0)  legalTiles[xPos-1][yPos+k]   = false;
				if((yPos+k)+1 <=9) legalTiles[xPos][(yPos+k)+1] = false;
				if((yPos+k)-1 >=0) legalTiles[xPos][(yPos+k)-1] = false;
			}
		}
	}
	
	
	// Debug function used to print out virtual board to visualise
	// the ship positions. Will be unused in production release
	//
	@SuppressWarnings("unused")
	private void printShipDebug(boolean[][] legalTiles)
	{
		// *Debug Code*
		for(int q = BOARD_MIN; q < BOARD_MAX; q++)
		{
			for(int p = BOARD_MIN; p < BOARD_MAX; p++)
			{
				if(legalTiles[p][q] == true) System.out.print(" . ");
				else System.out.print(" X ");
			}
			System.out.print("\n");
		}
		System.out.print("\n\n\n");
	}
	
	// Replace if statement with min and max variables
	
	// Carrying out multiple checks to ensure the move being
	// passed to the function is legal and can be made
	//
	public boolean isMoveLegal(int x, int y){
		// If a move is outside of the board range, it is illegal
		if(x >= BOARD_MAX || x < BOARD_MIN || y >= BOARD_MAX || y < BOARD_MIN){
			System.out.println("Move is off the board.");
			return false;
		}
		// If the move has already been made this game, it is illegal
		if(previousMoves[x][y]){
			System.out.println("Prevous move detected.");
			return false;
		}
		
		// No illegal move detected, move is legal and can be made
		System.out.println("Move is legal. Added to List");
		return true;
	}
	
	
	// Apply the move and return true of false, indicating
	// whether the move was a hit or not
	//
	public boolean applyMove(int x, int y){
		// Loops for each ship
		for(int i = 0; i < ships.size(); i++){
			// Loops for each tile of the ship
			for(int j = 0; j < ships.get(i).getShipLength(); j++){
				// If the ship is of horizontal orientation
				if(ships.get(i).getShipOrient() == ShipOrientation.HORIZONTAL){
					// If the x and y coordinates of the move match the x and y coordinates of
					// a ship tile, the the move is a hit. x ship index offset taken into account
					if(x == ships.get(i).getShipPos().x+j && y == ships.get(i).getShipPos().y){
						System.out.println("[x: " + x + "y: " + y + "] Move is a hit!");
						// Set the ship tile hit flat to true
						ships.get(i).setShipHitBoolean(j);
						if(ships.get(i).isShipSunk()) System.out.println("[LOG] Ship " + i + " has been sunk.");

						// Since the move was a hit, return true
						return true;
					}
				}
				// If the ship is of vertical orientation
				else if(ships.get(i).getShipOrient() == ShipOrientation.VERTICAL){
					// If the x and y coordinates of the move match the x and y coordinates of
					// a ship tile, the the move is a hit. y ship index offset taken into account
					if(x == ships.get(i).getShipPos().x && y == ships.get(i).getShipPos().y+j){
						System.out.println("[x: " + x + "y: " + y + "] Move is a hit!");
						// Set the ship tile hit flat to true
						ships.get(i).setShipHitBoolean(j);
						if(ships.get(i).isShipSunk()) System.out.println("[LOG] Ship " + i + " has been sunk.");
						
						// Return true as the move was a hit
						return true;
					}
				}
			}
		}
		// Return false as the move was a miss
		return false;
	}
	
	
	// Loops through a player's ship list, checking if the
	// ships are sunk. If a ship is not sunk, then the
	// player has not yet lost the game, so return false.
	// If the player has no unsunk ships, they lose, return true
	//
	public boolean playerHasLostTheGame(){
		for(int i = 0; i < ships.size(); i++){
			if(!ships.get(i).isShipSunk()) return false;
		} 
		return true;
	}
	
	
	// Clears the ship list and previous moves
	// list ready for another game to occur
	//
	public void resetBoard(){

		ships.clear();
		
		// Setting all booleans in previousMoves
		// to false
		for(int i = BOARD_MIN; i < BOARD_MAX; i++){
			for(int j = BOARD_MIN; j < BOARD_MAX; j++){
				previousMoves[i][j] = false;
			}
		}
	}
	
	
	// Deserializes json passed to the function and
	// sets member variables to received data
	//
	// The json data passed to this function must be that
	// of the board, not the whole game.
	//
    public void deserializeBoard(String json){
        // Create a JSON Object using the string passed to function
        JSONObject jsonObject = new JSONObject(json);

        // Extract the previous moves from the board json
        Object previousMovesString = jsonObject.get("previousMoves");

        // Create a JSON array using the previous moves json
        JSONArray jsonArray = new JSONArray(previousMovesString.toString());

        // For loop loops through the json array, grabbing each
        // individual boolean from the json array and storing it
        // in the previousMoves boolean array.
        for(int i = 0; i < jsonArray.length(); i++){
            jsonArray.get(i);

            JSONArray innerJsonArray = new JSONArray(jsonArray.get(i).toString());
            for(int j = 0; j < innerJsonArray.length(); j++){
                innerJsonArray.getBoolean(j);

                previousMoves[i][j] = innerJsonArray.getBoolean(j);
            }
        }

        // JSON containing all of the ships
        JSONObject shipMap = new JSONObject(jsonObject.get("ships").toString());
        // Setting new ArrayList for ships about to be deserialized
        ships = new ArrayList<Ship>();
        
        // Looping through each ship in the json ship man and
        // creating a new ship object with the data passed.
        for(int i = 0; i < shipMap.length(); i++){
        	Ship ship = new Ship(null, 0, 0, 0);
        	ship.deserializeShip(shipMap.get("Ship " + (i)).toString());
        	ships.add(ship);
        }
    }
    
    
    // Overriding the toString function converts
    // board's member variables into JSON and
    // returns the JSON as a string
    //
	@Override
	public String toString(){
		JSONObject json = new JSONObject();
		json.put("previousMoves", previousMoves);
		
		JSONObject shipJson = new JSONObject();
		
		for(int i = 0; i < ships.size(); i++){
			shipJson.put("Ship " + i, ships.get(i));
		}
		json.put("ships", shipJson);
		
		return json.toString();
	}
	
	
	// This function returns the amount of ships
	// that remain un-sunk on a given board.
	//
	public int getRemainingShipAmount(){
		int remainingAmount = 7;
		
		for(int i = 0; i < ships.size(); i++){
			if(ships.get(i).isShipSunk()){
				remainingAmount--;
			}
		}

		return remainingAmount;
	}
	
	
	// Previous move list getter
	//
	public boolean[][] getPreviousMovesList(){ return previousMoves; }

	
	// Ship array list getter
	//
	public List<Ship> getShipArray(){ return ships; }

}
