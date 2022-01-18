package main.gameMechanics;

import java.awt.Point;

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


	@SuppressWarnings("unused")
	private boolean isShipSunk()
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
}
