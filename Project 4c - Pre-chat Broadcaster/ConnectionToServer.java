// Abigail McIntyre
// Project 4c - Pre-chat Broadcaster
// Done  03/24/2022

// ---------------------------------------------------------------------------------------------------------------------------
// Waits to receive messages forwarded by the server. When a client receives a message forwarded by the server, 
// it displays that message on the label along with the id string of the client who initiated the message. 
// ---------------------------------------------------------------------------------------------------------------------------

import java.io.IOException;
import javax.swing.SwingUtilities;

public class ConnectionToServer implements Runnable
{
    Talker myTalker;
    String id;
    String str;
    BroadcasterFrame frame;

    // ======================================================================================

    ConnectionToServer(String username, BroadcasterFrame frame) throws IOException
    {
        myTalker = new Talker("127.0.0.1", 3737, "client");
        this.frame = frame;
        id = username;
        new Thread(this).start();                   // start the thread
    }

    // ======================================================================================
    // receives the messages sent by the CTC and displays them on the screen

    @Override
    public void run()
    {
        boolean keepRunning;
        try
        {
            myTalker.sendString(id);                    // send the id to the CTC
            keepRunning = true;
            while(keepRunning)
            {
                System.out.println("About to receive");
                str = myTalker.receiveMessage();
                System.out.println("In the CTS of: " + id + " The message received is: " + str);
                SwingUtilities.invokeLater              // invoke in the Dispatcher thread
                ( new Runnable()                        // inner class that only implements Runnable
                    {
                        public void run()
                        {
                            System.out.println("Setting the displayLabel");
                            frame.displayLabel.setText(str);
                        }
                    } // end of Runnable
                ); // end of invokeLater(...) method
            }
        }
        catch (Exception e) 
        {
            System.out.println("Error sending or receiving message");           
        }
    }

    // ======================================================================================

    // sends the message to the CTC
    public void sendMessage(String message) throws IOException
    {
        // myTalker.sendString(id + ": " + message);
        myTalker.sendString(message);
        System.out.println("Message being sent to the CTC from the CTS: " + id + ": " + message);
    }

    // ======================================================================================
}
