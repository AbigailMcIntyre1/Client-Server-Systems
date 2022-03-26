// Abigail McIntyre
// Project 4c - Pre-chat Broadcaster
// Done  03/24/2022

// ---------------------------------------------------------------------------------------------------------------------------
// The part of the program that creates the thread and receives the messages from the client
// ---------------------------------------------------------------------------------------------------------------------------

import java.io.IOException;
import java.net.Socket;

public class ConnectionToClient implements Runnable
{
    Talker talker;
    Boolean keepRunning;
    String id;
    BroadcasterServer server;

    // ======================================================================================

    ConnectionToClient(Socket socket, BroadcasterServer server) throws IOException
    {
        keepRunning = true;
        this.server = server;
        id = " ";
        talker = new Talker(socket);                // construct a talker
        new Thread(this).start();                   // start the thread
    }

    // ======================================================================================

    // receive the message sent by the CTS and send it to all the other CTC's in the list
    @Override
    public void run()
    {
        try 
        {
            id = talker.receiveMessage();           // receive the ID string
            while(keepRunning)
            {
                String message = talker.receiveMessage();
                System.out.println("Message received in CTC: " + message + "---- about to be broadcast");
                server.broadcastMessage(message, this.id);
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Exception in the run method in the CTC");
        }
    }

    // ======================================================================================

    public void sendMessage(String message) throws IOException
    {
        talker.sendString(message);
    }

    // ======================================================================================
    
}
