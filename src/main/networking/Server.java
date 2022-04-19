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

	public Server(String ipAddress, String port) 
	{
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public void Start()
	{
		try
		{
			serverSocket = new ServerSocket(Integer.parseInt(port));
			socket = serverSocket.accept();
			
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			String str = (String) inputStream.readUTF();
			System.out.println("[Message] " + str);
			
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF("[LOG] Connected to the server");
			outputStream.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void sendToClient(Board[] boards)
	{
		// Every time the host player makes a move, the client is updated
		
		// JSON game state
		JSONObject jsonGameState = new JSONObject();
		
		// Serialize each board
		jsonGameState.put("Host Board", boards[0].toString());
		jsonGameState.put("Client Board", boards[1].toString());
		
		try
		{
			// Send the game state to the client via connected socket
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(jsonGameState.toString());
			outputStream.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public String receieveFromClient()
	{
		String jsonGameState = "";
		
		try
		{
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			jsonGameState = inputStream.readUTF();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return jsonGameState;
	}
}





