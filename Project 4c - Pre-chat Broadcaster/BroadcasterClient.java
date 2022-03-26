\// Abigail McIntyre
// Project 4c - Pre-chat Broadcaster
// Done  03/24/2022

// ---------------------------------------------------------------------------------------------------------------------------
// The client part of the program. 
// ---------------------------------------------------------------------------------------------------------------------------

import java.io.IOException;
import java.net.UnknownHostException;

public class BroadcasterClient 
{
    ConnectionToServer cts;

    // ======================================================================================

    BroadcasterClient(String inputUsername, BroadcasterFrame frame) throws UnknownHostException, IOException
    {
        System.out.println("Client being created");
        System.out.println("Client is ready to receive...");
        cts = new ConnectionToServer(inputUsername, frame);
    }

    // ======================================================================================

}
