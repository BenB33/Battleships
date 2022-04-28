package main.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import main.gameMechanics.Board;

public class Client 
{
	private Socket socket = null;
	
	public Client(String ipAddress, String port) {
		try{
			socket = new Socket(ipAddress, Integer.parseInt(port));
			
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF("Finally, it's happened to me!");
			outputStream.flush();
			
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			String str = inputStream.readUTF();
			System.out.println(str);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	
	// Receives a JSON game state from the host and returns it
	// as a string
	//
	public String receiveFromHost(){
		String jsonGameState = "";
		
		try{
			// Receieve data from the host via connected socket
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			jsonGameState = inputStream.readUTF();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return jsonGameState;
	}
	
	
	// Sends the game state containing both the enemy and player boards
	// to the host, so the host can load that data to ensure both
	// the host and client's game states are synced up
	//
	public void sendToHost(Board[] boards){
		// Every time the host player makes a move, the host is updated
		
		// JSON game state
		JSONObject jsonGameState = new JSONObject();
		
		// Serialize each board
		jsonGameState.put("Host Board", boards[0].toString());
		jsonGameState.put("Client Board", boards[1].toString());
		
		try{
			// Send the game state to the host via connected socket
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(jsonGameState.toString());
			outputStream.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	
	public void sendHostRematchResult(String result)
	{
		try {
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(result);
			outputStream.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	// Shuts down the connected socket
	//
	public void shutdown(){
		try{
			socket.close();
			socket = null;
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
