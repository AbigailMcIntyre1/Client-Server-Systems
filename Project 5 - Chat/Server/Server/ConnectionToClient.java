// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// The part of the server that communicates with the clients. Recieves the commands the client sends and executes them.
// ----------------------------------------------------------------------------------------------------------------

package Server;

import java.io.IOException;
import java.net.Socket;

public class ConnectionToClient implements Runnable
{
    Talker talker;
    ChatServer server;
    User newUser;                                   // a User for when a client registers, gets added to the Hashtable
    Socket socket;

    // ======================================================================================

    ConnectionToClient(Socket socket, ChatServer server) throws IOException
    {
        talker = new Talker(socket);                // construct a talker
        this.server = server;                       // save the server
        this.socket = socket;
        new Thread(this).start();                   // start the thread
    }

    // ======================================================================================

    @Override
    public void run()
    {
        boolean keepRunning;                        // keeps the loop running to handle commands from the CTS
        String message;                             // the message that the client sends to be handled
        String tmpUsername;                         // the username received upon connection
        String tmpPassword;                         // the password received upon connection

        try 
        {
            // ------------------------ receive username and password -----------------------------------------------------------

            tmpUsername = talker.receiveMessage();              // receive the username
            tmpPassword = talker.receiveMessage();              // then receive the password

            // ------------------------ loop to handle commands from CTS --------------------------------------------------------

            keepRunning = true;                    
            while(keepRunning)
            {
                System.out.println("-------------------------");
                System.out.println("Waiting to receive in CTC...");

                message = talker.receiveMessage();

                System.out.println("Received in CTC: " + message);
                
                // ------------------ Login/Register Stuff ----------------------------------------------------------
                // if registering and the username is already taken, then the program will send that message and they can try again
                // if logging in and the username or password is incorrect, then the program will send that message and they can try again
                // if logging in was successful then it'll send the list of buddies to the frame to be displayed and buttons will be enabled

                if(message.startsWith("REGISTER"))
                {
                    System.out.println("Preparing to register...");
                    server.register(tmpUsername, tmpPassword);
                }

                else if(message.startsWith("LOGIN"))
                {
                    server.login(tmpUsername, tmpPassword);
                }

                // -----------------------------------------------------------------------------------------------------------------


                // -------------------- Buddy stuff ----------------------------------------------------------------------------
                // how the buddy stuff works:
                // 1.) frame will use the cts to send a BUDDY_REQUEST to the ctc
                // 2.) the ctc will send that PENDING_BUDDY_REQUEST from the requested buddy's ctc to their cts
                // 3.) their cts will display in a popup on the frame if they want to accept, if they accept, it sends
                //     a ADD_BUDDY to their ctc
                // 4.) from there, the ctc sends an ACCEPTED_BUDDY_REQUEST to their cts
                // 5.) it adds the new buddy to the listModel and then sends ACCEPTED_BUDDY_REQUEST back to the ctc
                // 6.) from there it uses the original sender's ctc to send the accepted friend's username to their cts (ADD_BUDDY)
                // 7.) from the cts it adds the accepted friend to the listModel

                else if(message.startsWith("BUDDY_REQUEST"))
                {
                    String parsedMessage[] = message.split(" ");
                    String potentialBuddyName = parsedMessage[1];
                    User tmpUser = server.userList.get(potentialBuddyName);
                    
                    // if not yourself
                    if(!potentialBuddyName.equals(tmpUsername))
                    {
                        // if not already buddies
                        if(!server.userList.get(tmpUsername).buddies.contains(potentialBuddyName))
                        {
                            // if the user is online
                            if(tmpUser.ctc != null)
                            {
                                tmpUser.ctc.sendMessage("PENDING_BUDDY_REQUEST " + tmpUsername);
                            }
                            else if(tmpUser.ctc == null)
                            {
                                sendMessage("BUDDY_REQUESTED_OFFLINE " + potentialBuddyName);
                            }
                        }
                        else if(server.userList.get(tmpUsername).buddies.contains(potentialBuddyName))
                        {
                            sendMessage("ALREADY_BUDDIES " + potentialBuddyName);
                        }
                    }
                }

                else if(message.startsWith("ACCEPTED_BUDDY_REQUEST"))
                {
                    String parsedMessage[] = message.split(" ");
                    User tmpUser = server.userList.get(parsedMessage[1]);

                    System.out.println("Adding " + tmpUsername + " to " + tmpUser.username + "'s buddy list");
                    tmpUser.buddies.add(tmpUsername);
                    server.saveList();
                    tmpUser.ctc.sendMessage("ADD_BUDDY " + tmpUsername);
                }

                else if(message.startsWith("ADD_BUDDY"))
                {
                    String parsedMessage[] = message.split(" ");
                    String newBuddyName = parsedMessage[1];

                    System.out.println("Adding " + newBuddyName + " to " + tmpUsername + "'s buddy list");
                    server.userList.get(tmpUsername).buddies.add(newBuddyName);
                    server.saveList();
                    talker.sendString("ACCEPTED_BUDDY_REQUEST " + newBuddyName);
                }

                // -----------------------------------------------------------------------------------------------------------------


                // -------------------- Chatting stuff ----------------------------------------------------------------------------
                // when a message has been forwarded from the sender's ChatBox to their CTC to the receiver's CTS,
                // parse the message to separate the command, sender name, and message. And then have the frame display
                // the sent message in the receiver's ChatBox.

                else if(message.startsWith("FORWARD_THIS"))
                {
                    String sentMessage = new String("");
                    String parsedMessage[] = message.split(" ");
                    String chattingWithUsername = parsedMessage[1];

                    for(int i = 2; i < parsedMessage.length; i++)
                    {
                        sentMessage += parsedMessage[i];
                        sentMessage += " ";
                    }

                    User toUser = server.userList.get(chattingWithUsername);

                    // If the user is online
                    if(toUser.ctc != null)
                    {   
                        toUser.ctc.sendMessage("MSG_FORWARDED " + tmpUsername + " " + sentMessage);
                    } 
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

                else if(message.startsWith("FILE_XFER_REQ"))
                {
                    String parsedMessage[] = message.split(" ");
                    String buddyName = parsedMessage[1];
                    String filenameString = parsedMessage[2];
                    String fileSize = parsedMessage[3];

                    if(server.userList.get(buddyName).ctc != null)          // if the receiver is online
                    {
                        server.userList.get(buddyName).ctc.sendMessage("WANT_THIS_FILE? " + tmpUsername + " " + filenameString + " " + fileSize);
                    }
                    // else add to pending list
                }

                else if(message.startsWith("ACCEPTED_FILE_XFER"))
                {
                    String parsedMessage[] = message.split(" ");
                    String senderUsername = parsedMessage[1];
                    String filename = parsedMessage[2];
                    String fileSize = parsedMessage[3];

                    User toUser = server.userList.get(senderUsername);                     

                    if(toUser != null)
                    {
                        if(toUser.ctc != null)
                        {
                            toUser.ctc.sendMessage("START_FILE_XFER " + socket.getInetAddress().toString().substring(1) + " " + 1234 + " " 
                                                   + tmpUsername + " " + filename + " " + fileSize); 
                        } 
                        else 
                        {
                            System.out.println("Error sending file");
                        }
                    }
                }
            }

            // -----------------------------------------------------------------------------------------------------------------
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

