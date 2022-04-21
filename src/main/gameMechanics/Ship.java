package main.gameMechanics;

import java.awt.Point;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ship {

	ShipOrientation shipOrientation;
	int shipLength;
	int shipXPosition;
	int shipYPosition;
	boolean[] shipHitTiles;
	
	public Ship(ShipOrientation shipOrient, int length, int xPos, int yPos) {
		shipOrientation = shipOrient;
		shipLength      = length;
		shipXPosition   = xPos;
		shipYPosition   = yPos;
		shipHitTiles    = new boolean[shipLength];
	}


	// Checks if all of the ship tiles have been hit. If they
	// have, the ship is determined sunk and true is returned.
	//
	public boolean isShipSunk(){
		// Loop over the shipHitTiles array, searching for
		// unsunk tiles.
		for(int i = 0; i < shipHitTiles.length; i++){
			if(shipHitTiles[i] == false) return false;
		}
		// No unsunk tiles detected, ship is sunk
		return true;
	}
	

    // Overriding the toString function converts
    // ship's member variables into JSON and
    // returns the JSON as a string
    //
	@Override
	public String toString(){
		JSONObject json = new JSONObject();
		
		if(shipOrientation == ShipOrientation.HORIZONTAL)    json.put("shipOrientation", "HORIZONTAL");
		else if(shipOrientation == ShipOrientation.VERTICAL) json.put("shipOrientation", "VERTICAL");

		json.put("shipLength", shipLength);
		json.put("shipXPosition", shipXPosition);
		json.put("shipYPosition", shipYPosition);
		json.put("shipHitTiles", shipHitTiles);
		
		return json.toString();
	}
	
	
	// Take in a json string and deserialize it in order to
	// assign data to a ship's member variables.
	//
    public void deserializeShip(String json) {
        // Create a JSON Object using the ship json passed to function
        JSONObject jsonObject = new JSONObject(json);
        
        // Determine the orientation of the ship and assign it accordingly
        String shipOrient = jsonObject.getString("shipOrientation");
        if(shipOrient.equals("VERTICAL"))        shipOrientation = ShipOrientation.VERTICAL;
        else if(shipOrient.equals("HORIZONTAL")) shipOrientation = ShipOrientation.HORIZONTAL;
        
        // Assign the rest of the ship member variabled
        shipLength    = jsonObject.getInt("shipLength");
        shipXPosition = jsonObject.getInt("shipXPosition");
        shipYPosition = jsonObject.getInt("shipYPosition");
        shipHitTiles = new boolean[shipLength];

        // Create a JSONArray to assign the shipHitTiles array
        JSONArray hitTiles = new JSONArray(jsonObject.get("shipHitTiles").toString());
        for(int i = 0; i < shipLength; i++){
            shipHitTiles[i] = hitTiles.getBoolean(i);
        }
    }
    
    
    // Setters and getters
    //
	public void setShipHitBoolean(int hitTile) { shipHitTiles[hitTile] = true; }
	public boolean hasShipTileBeenHit(int hitTile) { return shipHitTiles[hitTile]; }
	public Point getShipPos() { return new Point(shipXPosition, shipYPosition); }
	public int getShipLength() { return shipLength; }
	public ShipOrientation getShipOrient() { return shipOrientation; }
}
