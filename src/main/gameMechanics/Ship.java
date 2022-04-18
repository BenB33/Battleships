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
	
	public Ship(ShipOrientation shipOrient, int length, int xPos, int yPos) 
	{
		shipOrientation = shipOrient;
		shipLength = length;
		shipXPosition = xPos;
		shipYPosition = yPos;
		
		shipHitTiles = new boolean[shipLength];
	}


	public boolean isShipSunk()
	{
		// Loop over the shipHitTiles array, searching for
		// unsunk tiles.
		for(int i = 0; i < shipHitTiles.length; i++)
		{
			if(shipHitTiles[i] == false)
			{
				// An unsunk tile has been detected,
				// therefore the ship isn't sunk.
				return false;
			}
		}
		// No unsunk tiles detected, ship is sunk
		return true;
	}
	
	public void setShipHitBoolean(int hitTile)
	{
		shipHitTiles[hitTile] = true; 
	}
	
	public boolean hasShipTileBeenHit(int hitTile)
	{
		return shipHitTiles[hitTile];
	}
	
	public Point getShipPos()
	{
		return new Point(shipXPosition, shipYPosition);
	}
	
	public int getShipLength()
	{
		return shipLength;
	}
	
	public ShipOrientation getShipOrient()
	{
		return shipOrientation;
	}
	
	// Convert the ship's member variables into
	// JSONObject format and return as a string.
	@Override
	public String toString()
	{
		JSONObject json = new JSONObject();
		
		if(shipOrientation == ShipOrientation.HORIZONTAL)
		{
			json.put("shipOrientation", "HORIZONTAL");
		}
		else
		{
			json.put("shipOrientation", "VERTICAL");
		}
		
		json.put("shipLength", shipLength);
		json.put("shipXPosition", shipXPosition);
		json.put("shipYPosition", shipYPosition);
		json.put("shipHitTiles", shipHitTiles);
		
		return json.toString();
	}
	
	
    public void deserializeShip(String json)
    {
        // Create a JSON Object using the ship json passed to function
        JSONObject jsonObject = new JSONObject(json);

        // Extract the ship from the json
        shipLength = jsonObject.getInt("shipLength");
        shipXPosition = jsonObject.getInt("shipXPosition");
        shipYPosition = jsonObject.getInt("shipYPosition");

        boolean[] shipHitTiles = new boolean[shipLength];

        JSONArray hitTiles = new JSONArray(jsonObject.get("shipHitTiles").toString());
        for(int i = 0; i < shipLength; i++)
        {
            shipHitTiles[i] = hitTiles.getBoolean(i);
        }
    }
}
