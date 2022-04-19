package main.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client 
{
	private Socket socket = null;
	
	public Client(String ipAddress, String port) 
	{
		try
		{
			socket = new Socket(ipAddress, Integer.parseInt(port));
			
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF("Finally, it's happened to me!");
			outputStream.flush();
			
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			String str = inputStream.readUTF();
			System.out.println(str);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	
	public String receiveFromHost()
	{
		String jsonGameState = "";
		
		try
		{
			// Receieve data from the host via connected socket
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			jsonGameState = inputStream.readUTF();
			System.out.println(jsonGameState);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return jsonGameState;
	}
	
	
	public void shutdown()
	{
		// Shutdown the connected socket
		try
		{
			socket.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
