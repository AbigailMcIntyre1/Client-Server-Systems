// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// Connects the Client to the Server. Executes commands sent to it.
// ----------------------------------------------------------------------------------------------------------------

package Client;

import java.io.IOException;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class ConnectionToServer implements Runnable
{
    Talker myTalker;
    String tmpUsername;
    ChatFrame frame;
    ChatClient client;
    Vector<String> buddies;
    String senderUsername;

    // ======================================================================================

    ConnectionToServer(String username, ChatFrame frame, ChatClient client) throws IOException
    {
        myTalker = new Talker("127.0.0.1", 3737, "client");
        this.frame = frame;
        this.client = client;
        buddies = new Vector<String>();
        tmpUsername = username;
        new Thread(this).start();                   // start the thread
    }

    // ======================================================================================
    // receives the messages sent by the CTC and displays them on the screen

    @Override
    public void run()
    {
        boolean keepRunning;
        String message;
        String parsedMessage[];
        String buddy;
        String sentMessage;
        int port;

        try
        {
            keepRunning = true;
            while(keepRunning)
            {
                System.out.println("-------------------------");
                System.out.println("Waiting to receive in CTS...");

                message = myTalker.receiveMessage();                                                // receive the sent message

                System.out.println("Received in CTS: " + message);

                // ------------------ Login/Register Stuff ----------------------------------------------------------
                // if registering and the username is already taken, then the program will send that message and they can try again
                // if logging in and the username or password is incorrect, then the program will send that message and they can try again
                // if logging in was successful then it'll send the list of buddies to the frame to be displayed and buttons will be enabled

                if(message.startsWith("ALREADY_TAKEN"))                     // username already taken
                {
                    System.out.println("Username already taken");
                    frame.usernameTakenDialog();                                    // throw up a dialog box telling the user
                    frame.setTitle("Please try logging in or registering again");
                }

                else if(message.startsWith("LOGIN/REGISTER_SUCCESSFUL"))    // successfully logged in
                {
                    System.out.println("Register/Login successful");
                    frame.setTitle(tmpUsername);                                    // set the title bar to the user's username
                    frame.addBuddyButton.setEnabled(true);                       // enable the user to add buddies
                    frame.logoutButton.setEnabled(true);                         // enable the user to logout
                    frame.loginButton.setEnabled(false);                         // don't let the user login again
                    frame.registerButton.setEnabled(false);                      // don't let the user register again

                    // then have the ctc send the list of buddy names and add to this vector so that the client can display it on the frame
                    parsedMessage = message.split(" ");

                    for(int i = 1; i < parsedMessage.length; i++)
                    {
                        buddy = parsedMessage[i];
                        buddies.add(buddy);
                    }

                    client.updateBuddiesOnLogin();                                   // fill the default list model with buddy names
                }

                else if(message.startsWith("INVALID_USERNAME_OR_PASSWORD"))   // either the username doesn't exist or it's the wrong password
                {
                    System.out.println("Invalid username or password");
                    frame.invalidUsernameOrPassword();                               // send up a dialog box saying that it's invalid
                    frame.setTitle("Please try logging in or registering again");
                }

                // -----------------------------------------------------------------------------------------------------------------


                // ---------------------- Buddy Stuff --------------------------------------------------------------------
                // how the buddy stuff works:
                // 1.) frame will use the cts to send a BUDDY_REQUEST to the ctc
                // 2.) the ctc will send that PENDING_BUDDY_REQUEST from the requested buddy's ctc to their cts
                // 3.) their cts will display in a popup on the frame if they want to accept, if they accept, it sends
                //     a ADD_BUDDY to their ctc
                // 4.) from there, the ctc sends an ACCEPTED_BUDDY_REQUEST to their cts
                // 5.) it adds the new buddy to the listModel and then sends ACCEPTED_BUDDY_REQUEST back to the ctc
                // 6.) from there it uses the original sender's ctc to send the accepted friend's username to their cts (ADD_BUDDY)
                // 7.) from the cts it adds the accepted friend to the listModel

                else if(message.startsWith("ADD_BUDDY"))
                {
                    System.out.println("Adding buddy");
                    parsedMessage = message.split(" ");                                      // parse the message
                    frame.buddiesListModel.addElement(parsedMessage[1]);                           // add the buddy to the defaultlistmodel in the frame
                }
                
                else if(message.startsWith("PENDING_BUDDY_REQUEST"))
                {
                    System.out.println("Pending buddy request");
                    parsedMessage = message.split(" ");                                     // parse the message
                    frame.youHaveABuddyRequest(parsedMessage[1]);                                 // send that the user has a buddy request in the frame
                }

                else if(message.startsWith("ACCEPTED_BUDDY_REQUEST"))
                {
                    System.out.println("Accepted buddy request");
                    parsedMessage = message.split(" ");                                     // parse the message
                    frame.buddiesListModel.addElement(parsedMessage[1]);                          // add the buddy to the defaultlistmodel in the frame
                    myTalker.sendString("ACCEPTED_BUDDY_REQUEST " + parsedMessage[1]);            // send that the user accepted the buddy request to the ctc
                }

                else if(message.startsWith("BUDDY_REQUESTED_OFFLINE"))
                {
                    System.out.println("Requested buddy offline");
                    parsedMessage = message.split(" ");                                     // parse the message
                    frame.requestedBuddyOffline(parsedMessage[1]);                                // send that the requested buddy is offline in the frame
                }

                else if(message.startsWith("ALREADY_BUDDIES"))
                {
                    System.out.println("Already buddies");
                    parsedMessage = message.split(" ");                                     // parse the message
                    frame.alreadyBuddies(parsedMessage[1]);                                       // send that the user is already buddies with the requested buddy in the frame
                }

                // -----------------------------------------------------------------------------------------------------------------


                // -------------------- Chatting stuff ----------------------------------------------------------------------------
                // when a message has been forwarded from the sender's ChatBox to their CTC to the receiver's CTS (here),
                // parse the message to separate the command, sender name, and message. And then have the frame display
                // the sent message in the receiver's ChatBox.

                else if(message.startsWith("MSG_FORWARDED"))
                {
                    sentMessage = new String("");                            // create a new string to hold the sent message. Empty cause it'll be added to
                    parsedMessage = message.split(" ");                        // parse the message

                    for(int i = 2; i < parsedMessage.length; i++)
                    {
                        sentMessage += parsedMessage[i];                              // add the next word to sentMessage
                        sentMessage += " ";                                           // add a space before the next word
                    }

                    final String messageToSend = sentMessage;                         // put the message into messageToSend
                    final String buddyName = parsedMessage[1];                        // put the username into buddyName

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            System.out.println("Sending message in frame");
                            frame.sendMessage(buddyName, messageToSend);               // have the frame either create or focus a chatBox
                        }
                    });
                }

                // -----------------------------------------------------------------------------------------------------------------


                // ----------------- File Sending Stuff ----------------------------------------------------------------------------
                // 1.) the user drops the file on a ChatBox. 
                // 2.) The ChatBox sends the (FILE_XFER_REQ) to the CTC. 
                // 3.) The sender's CTC then asks the receiver's CTS if they want to accept the file. (WANT_THIS_FILE?)
                // 4.) The receiver's frame then displays the sender's name, the file name, and the file size. If they accept the file,
                //     then it displays on the ChatBox that they accepted a file, sends ACCEPTED_FILE_XFER to the CTC,
                //     and creates ServerBob to read the file.
                // 5.) The CTC receives ACCEPTED_FILE_XFER, and tells the CTS to START_FILE_XFER.
                // 6.) The CTS then has the ChatBox send the file to ServerBob, who reads it and writes it to a file. 

                else if(message.startsWith("WANT_THIS_FILE?"))                 // does the receiver want to accept the file?
                {
                    parsedMessage = message.split(" ");                         // parse the message
                    senderUsername = parsedMessage[1];                                // save the sender's username

                    String filename = parsedMessage[2];                               // save the filename
                    String fileSizeString = parsedMessage[3];                         // save the file size as a string to display

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            System.out.println("Asking if user wants file");
                            frame.wantThisFile(senderUsername, filename, fileSizeString); // have the frame ask the recipient if they want the file
                        }
                    });
                }

                else if(message.startsWith("START_FILE_XFER"))                  // the file has been accepted, start sending it
                {
                    parsedMessage = message.split(" ");                          // parse the message
                    String ip[] = parsedMessage[1].split(":");                   // get the ip address
                    port = Integer.parseInt(parsedMessage[2]);                          // get the port number
                    buddy = parsedMessage[3];                                           // get the username of the recipient
            
                    System.out.println("Starting file transfer");
                    frame.openBoxesList.get(buddy).transferFile(ip[0], port);           // have the frame get the ChatBox of the recipient and 
                                                                                        // then the ChatBox will transfer the file
                }

                // -----------------------------------------------------------------------------------------------------------------
            }
            
        }
        catch (Exception e) 
        {
            System.out.println("Error sending or receiving message - cts");  
            frame.serverDied();                                                         // if there was an error, the server has died        
        }
    }

    // ======================================================================================

    // sends the message to the CTC
    public void sendMessage(String message) throws IOException
    {
        myTalker.sendString(message);
    }

    // ======================================================================================
}

