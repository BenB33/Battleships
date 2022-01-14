package main.networking;

// Swing Imports
import javax.swing.JDialog;
import javax.swing.JOptionPane;

// Regex Imports
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Other Imports
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
	
	// Gets the IP address in string form
	public static String getIPAddressString()
	{
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipAddress;
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
		else if(Integer.valueOf(port) < 0 || Integer.valueOf(port) > 65535)
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
