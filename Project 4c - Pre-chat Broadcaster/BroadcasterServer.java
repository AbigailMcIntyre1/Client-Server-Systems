// Abigail McIntyre
// Project 4c - Pre-chat Broadcaster
// Done  03/24/2022

// ---------------------------------------------------------------------------------------------------------------------------
// The server side of the program, Its primary jobs will be to accept connections from clients, keep a list of the
// logged on clients, and then forward a message sent by one client to all of the other logged on clients (thus
// broadcasting the message).
// ---------------------------------------------------------------------------------------------------------------------------

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.net.*;

public class BroadcasterServer 
{
    // ======================================================================================
    public static void main(String[] args) 
    {
        BroadcasterServer server = new BroadcasterServer();
    }

    // ======================================================================================

    Vector<ConnectionToClient> ctcList;
    Boolean keepLooking;
    ServerSocket serverSocket;
    Socket normalSocket;
    ConnectionToClient ctc;

    BroadcasterServer()
    {
        keepLooking = true;

        ctcList = new Vector<ConnectionToClient>();

        System.out.println("Server is starting...");
        try 
        {
            serverSocket = new ServerSocket(3737);                                  // create a server socket
        } 
        catch (IOException e1) 
        {
            System.out.println("Error constructing socket");
        }                              
        
        while(keepLooking)
        {
            try 
            {
                System.out.println("Server is waiting to accept a request for a connection...");
                normalSocket = serverSocket.accept();                               // use a regular socket to accept a connection from a client
                System.out.println("Server accepted a request.");
                ctc = new ConnectionToClient(normalSocket, this);                   // construct a CTC using a socket

                int n = 0;
                boolean found = false;
                while(!found && n < ctcList.size())
                {
                    if(ctcList.elementAt(n).id.equals(ctc.id))
                        found = true;
                    else
                        n++;
                }
                if(!found)
                    ctcList.add(ctc);
            } 
            catch (IOException e) 
            {
                System.out.println("Error constructing sockets or connecting - server");
            }
        }
    }

    // ======================================================================================
    // Takes the message from the sender's CTC and broadcasts it to the other CTC's

    public void broadcastMessage(String message, String id) throws IOException
    {
        System.out.println("broadcastMessage has been called" + ctcList.size());         // to check that the method is actually being called
        for(int i = 0; i < ctcList.size(); i++)                         // for all the CTC's in the list
        {
            System.out.println("Looking at ID: " + id);
            if(!ctcList.elementAt(i).id.equals(id))                     // if it's not the sender's
            {
                System.out.println("Message being broadcast from server " + id + ": " + message);
                ctcList.elementAt(i).sendMessage(id + ": " + message);
            }
        }
    }

    // ======================================================================================
}
