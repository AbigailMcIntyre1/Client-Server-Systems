// Abigail McIntyre
// Project 4 - Trojan Software
// Due 03/16/2022

// ---------------------------------------------------------------------------------------------------------------------------
//
// ---------------------------------------------------------------------------------------------------------------------------

import java.io.*;
import java.net.*;

public class Talker
{
    private BufferedReader bufferedReader;          // for reading messages from the other side
    private DataOutputStream outputStream;          // for writing messages to the other side
    private String id;                              // identifiers either:
                                                    // i.) for sending - the ID of the recipient
                                                    // ii.) for receiving - the ID of the sender

    // ======================================================================================
    
    Talker(Socket socket)
    {
        try 
        {
            bufferedReader   = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream  = new DataOutputStream(socket.getOutputStream());
            id = new String("Pending");
        } 
        catch (IOException e) 
        {
            System.out.println("IOException in talker on server side");
        }
    }

    // ======================================================================================

    Talker(String domain, int port, String id)
    {
        // construct a Socket using the domain and port, and then get the I/O
        // streams from that instance of Socket. Set the id to the data member id

        try 
        {
            Socket socket = new Socket(domain, port);
            bufferedReader   = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream  = new DataOutputStream(socket.getOutputStream());
            this.id = id;
        } 
        catch (IOException e) 
        {
            System.out.println("IOException in talker on client side");
        }
    }

    // ======================================================================================

    public void sendString(String message)
    {
        try 
        {
            outputStream.writeBytes(message + '\n');
            // System.out.println("ID: " + id + " Message sent: " + message);
            System.out.println(message);
        } 
        catch (IOException e) 
        {
            System.out.println("Error sending String");
        }
    }

    // ======================================================================================

    public String receiveMessage()
    {
        String returnString = null;

        try 
        {
            returnString = bufferedReader.readLine();
            // System.out.println("Sender ID: " + id + " - the message received is: " + returnString);
            System.out.println(returnString);

        } 
        catch (IOException e) 
        {
            System.out.println("Error receiving message");
        }

        return returnString;
    }

    // ======================================================================================
}