package main.networking;

// Swing Imports
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// Regex Imports
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.Inet4Address;
// Other Imports
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkUtils {
	
	// Gets the IP address in string form
	public static ArrayList<String> getIPv4List()
	{
		ArrayList<String> ipAddressList = new ArrayList<>();
		
		try 
		{
			// Grabbing all of the network interfaces associated with the machine
			var networkInterfaces = NetworkInterface.getNetworkInterfaces();
			
			// Looping through each interface
			for(var interfaces : Collections.list(networkInterfaces))
			{
				// Extracts all of the InetAddresses from the interfaces
				var inetAddresses = interfaces.getInetAddresses();
				
				// Looping through each InetAddress
				for(var address : Collections.list(inetAddresses))
				{
					// If the InetAddress is a loopback address, ignore and continue
					if(address.isLoopbackAddress()) continue;
					
					// If the InetAddress is an ipv4, add it to the IP Address list (trim the / from the start)
					if(address instanceof Inet4Address)
					{
						ipAddressList.add(address.toString().replaceAll("/", ""));
					}
				}
			}
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		return ipAddressList;
	}
	
	// Validates the IP Address using a RegEx pattern
	public static boolean ipRegXValidator(String ipAddress)
	{
		Pattern pattern = Pattern.compile(
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher ipMatcher = pattern.matcher(ipAddress);
		return ipMatcher.matches();
	}
	
	// Ensures the port is valid by running multiple checks on it
	public static boolean ipPortValidation(String ip, String port, JDialog joinDialog)
	{
		// Add connection
		if(ip.equals("") || port.equals(""))
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter correct information.");		
			return false;
		}
		else if (!isNumber(port) == true)
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter a valid Port.");
			return false;
		}
		else if(Integer.valueOf(port) < 1024 || Integer.valueOf(port) > 65535)
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter a valid Port.");
			return false;
		}
		else if(!ipRegXValidator(ip))
		{
			JOptionPane.showMessageDialog(joinDialog, "ERROR: Please enter a valid IP Address.");
			return false;
		}
		else
		{
			return true;
		}
	}
	
	// Checks if the input string is a number
	public static boolean isNumber(String input)
	{
		try 
		{
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;	
		}
	}
}
