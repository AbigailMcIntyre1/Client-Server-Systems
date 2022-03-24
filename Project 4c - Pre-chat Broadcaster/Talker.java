// Abigail McIntyre
// Project 4c - Pre-chat Broadcaster
// Done  03/24/2022

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
    
    Talker(Socket socket) throws IOException
    {
        bufferedReader   = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream  = new DataOutputStream(socket.getOutputStream());
        id = new String("Pending");
    }

    // ======================================================================================

    Talker(String domain, int port, String id) throws UnknownHostException, IOException
    {
        // construct a Socket using the domain and port, and then get the I/O
        // streams from that instance of Socket. Set the id to the data member id

        Socket socket = new Socket(domain, port);
        bufferedReader   = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream  = new DataOutputStream(socket.getOutputStream());
        this.id = id;
    }

    // ======================================================================================

    public void sendString(String message) throws IOException
    {
        outputStream.writeBytes(message + '\n');
        // System.out.println("ID: " + id + " Message sent: " + message);
    }

    // ======================================================================================

    public String receiveMessage() throws IOException
    {
        String returnString = null;
        returnString = bufferedReader.readLine();
        // System.out.println("Sender ID: " + id + " - the message received is: " + returnString);
        return returnString;
    }

    // ======================================================================================
}