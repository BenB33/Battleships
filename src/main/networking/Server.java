package main.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

import main.gameMechanics.Board;

public class Server 
{
	ServerSocket serverSocket = null;
	Socket socket = null;
	
	String ipAddress = "";
	String port = "";

	public Server(String ipAddress, String port){
		this.ipAddress = ipAddress;
		this.port = port;
	}

	
	// The start function takes the port class member and uses
	// it to create a new server socket, then listens for a new
	// connection. A message is then sent to the client to show
	// connection is established.
	//
	public void start(){
		try{
			serverSocket = new ServerSocket(Integer.parseInt(port));
			socket = serverSocket.accept();
			
			// Received a message from the client to show connection is established
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			String str = (String) inputStream.readUTF();
			System.out.println("[Message] " + str);
			
			// Sends a message to the client to show connection is established
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF("[LOG] Connected to the server");
			outputStream.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	// Sends the game state containing both the enemy and player boards
	// to the client, so the client can load that data to ensure both
	// the host and client's game states are synced up
	//
	public void sendToClient(Board[] boards){
		// Every time the host player makes a move, the client is updated
		
		// JSON game state
		JSONObject jsonGameState = new JSONObject();
		
		// Serialize each board
		jsonGameState.put("Host Board", boards[0].toString());
		jsonGameState.put("Client Board", boards[1].toString());
		
		System.out.println("JSON: " + jsonGameState);
		
		try{
			// Send the game state to the client via connected socket
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(jsonGameState.toString());
			outputStream.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	// Receives a JSON game state from the client and returns it
	// as a string
	//
	public String receieveFromClient(){
		String jsonGameState = "";
		
		try{
			// Receieve data from the client via connected socket
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			jsonGameState = inputStream.readUTF();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return jsonGameState;
	}
	
	
	public void sendClientRematchResult(String result)
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
	
	
	// Shuts down the socket
	//
	public void shutdown(){
		try{
			socket.close();
			serverSocket.close();
			serverSocket = null;
			socket = null;
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}