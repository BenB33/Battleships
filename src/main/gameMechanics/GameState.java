package main.gameMechanics;

public enum GameState{
	WAITING_FOR_PLAY_DECISION, PLACING_SHIP, MAKING_MOVE, WAITING_FOR_OPPONENT, PLAYER_HAS_WON, OPPONENT_HAS_WON, WAITING_FOR_HOST_REMATCH_DECISION
	
	// Waiting for play decision
	
	// Placing ships
	// When placing ships, a ghost ship follows the cursor to indicate
	// the type of ship being placed. Info panel will display ships
	// left to place, and also instructions on how to place.
	// (Left click to place, Right click to rotate)
	
	// Making move
	// When making a move, a ghost bomb follows the cursor to indicate
	// what tile is currently selected. The info panel shows instructions
	// on how to place (Left click to drop bomb). When a bomb is dropped,
	// if a hit is detected, the player goes again. If no hit is detected,
	// the player's turn ends.
	
	// Waiting for opponent
	// The player idles as the opposing player makes their turn.
	
	// Player has won
	// A player has won when all of their enemy ships have been sunk.
	
	// Opponent has won
	// The opponent has won the game when all of the player's ships have been sunk.
	
	// Waiting for host rematch decision
	// The host will decide whether they want a rematch or not.
}
