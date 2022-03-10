// Abigail McIntyre
// Project 4 - Trojan Software
// Due 03/16/2022

// ---------------------------------------------------------------------------------------------------------------------------
// The part of the program that creates the thread and receives the messages from the client
// ---------------------------------------------------------------------------------------------------------------------------

import java.net.Socket;

public class ConnectionToClient implements Runnable
{
    Talker talker;
    Boolean keepRunning;
    String str;

    // ======================================================================================

    ConnectionToClient(Socket socket)
    {
        keepRunning = true;
        talker = new Talker(socket);                // create the talker to receive the messages
        new Thread(this).start();                   // start the thread
    }

    // ======================================================================================

    @Override
    public void run() 
    {
        while(keepRunning)                          // while the client hasn't sent the string DONE
        {
            str = talker.receiveMessage();          // receive the file names
            System.out.println(str);                // and print them to the console
            
            if(str.equals("DONE"))
            {
                keepRunning = false;
            }
        }
    }

    // ======================================================================================
    
}
