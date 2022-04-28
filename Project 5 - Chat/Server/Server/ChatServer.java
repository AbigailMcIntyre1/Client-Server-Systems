// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// The Server Side:
//      a.) Accepts requests for a connection from a client.
//      b.) Allows users to create a user account (register).
//      c.) Allows users to log in with username and password (no encryption yet).
//      d.) Allows clients to invite another user to be a mutual contact (buddy).
//      e.) Keeps a list of buddies for each user.
//      f.) Stores information for all users on disk every time that list changes, and attempts to load user
//          information from disk automatically when first started.
//      l.) Keeps logged in clients informed about which of their buddies is online, updating them anytime
//          someone logs in or out or disconnects.
//      i.) Allows clients to initiate a chat with one of their buddies.
//      j.) Relays chat messages from one client to another after first checking that they are buddies.
//      k.) Queues messages sent from one client to an offline buddy client, forwarding them the next time the
//          buddy client logs on.
//      l.) Allow clients to log out (is this necessary?).
//      m.) Handles abrupt disconnects without crashing.
//      n.) Handles an encrypted connection.
// ----------------------------------------------------------------------------------------------------------------

package Server;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.*;

public class ChatServer 
{
    UserList userList;                          // Hashtable to hold the list of Users with their information 
    boolean keepLooking;                        // boolean to keep the loop of accepting clients going
    ServerSocket serverSocket;                  // ServerSocket to bind to a port so that the server can accept clients
    Socket normalSocket;                        // a Socket for the client to connect to the server
    ConnectionToClient ctc;                     // a ConnectionToClient so that the server can communicate with the client and execute commands
    String fileName;

    // ======================================================================================

    public ChatServer()
    {
        // ----------------------- Create or load the Hashtable -------------------------------------------------------------------------------
        File tmpFile;
        try 
        {
            fileName = "User_List.txt";                                         // store the name of the file
            tmpFile = new File(fileName);                                       // create a temporary file variable to check if the file exists

            // for new file
            if(!tmpFile.exists())
            {
                userList = new UserList();                                      // create the hashtable
                tmpFile.createNewFile();                                        // create the file
            }
            // for loading from old file
            else
            {
                userList = new UserList(fileName);                              // Create the hashtable for the users and load the data from a file
            }
        } 
        catch (IOException e2) 
        {
            System.out.println("Error creating or loading Hashtable");
        }

        // ----------------------- Start the Server -----------------------------------------------------------------------------------------

        System.out.println("Server is starting...");
        System.out.println("-----------------------");

        try 
        {
            serverSocket = new ServerSocket(3737);                              // create a server socket
        } 
        catch (IOException e1) 
        {
            System.out.println("Error constructing socket");
        }      

        // ---------------------- Loop to accept clients ------------------------------------------------------------------------------------

        keepLooking = true;                                                     // initialize the boolean for the loop to accept clients
        while(keepLooking)
        {
            try 
            {
                System.out.println("Server is waiting to accept a request for a connection...");
                normalSocket = serverSocket.accept();                           // use a regular socket to accept a connection from a client
                System.out.println("Server accepted a request.");
                System.out.println("----------------------------");
                ctc = new ConnectionToClient(normalSocket, this);               // construct a CTC using a socket
            } 
            catch (IOException e) 
            {
                System.out.println("Error constructing sockets or connecting - server");
            }
        }
    }

    // ======================================================================================
    // Stores the user in the list, and then saves the list to a file
    public void addUser(String username, User user) throws IOException
    {
        System.out.println("Putting user in the list - server");
        userList.put(username, user);
        System.out.println("Storing the userlist in a file - server");
        userList.store(fileName);
        System.out.println("Store successful - server");
    }

    // ======================================================================================
    // Saves the hashtable to a file
    public void saveList() throws IOException
    {
        userList.store(fileName);
    }

    // ======================================================================================
    // Set the ctc of the person in the list
    public void setCTC(String username, ConnectionToClient ctc)
    {
        userList.get(username).ctc = ctc;
    }

    // ======================================================================================

    public void register(String tmpUsername, String tmpPassword) throws IOException
    {
        User newUser;

        if(userList.containsKey(tmpUsername))                       // if the username is already taken
        {
            ctc.sendMessage("ALREADY_TAKEN");               // then send the message to the CTS that it's already taken, it'll throw up a dialog
        }
        else                                                        // else, go ahead and add the user to the table
        {
            System.out.println("Adding user");
            newUser = new User(tmpUsername, tmpPassword, ctc);      // create a new user
            addUser(tmpUsername, newUser);                          // add the user to the list
            ctc.sendMessage("LOGIN/REGISTER_SUCCESSFUL");   // send that the user registered successfully
        }
    }

    // ======================================================================================

    public void login(String tmpUsername, String tmpPassword) throws IOException
    {
        String buddiesString = "";                                  // make it nothing so that we can concatenate the buddies

        if(userList.containsKey(tmpUsername))                       // if the username exists
        {
            if(userList.get(tmpUsername).password.equals(tmpPassword)) // if the correct password
            {
                System.out.println("The username: " + tmpUsername + " and password: " + tmpPassword + ", match");
                setCTC(tmpUsername, ctc);                           // set the user's ctc

                // get the buddies and save them in a string variable
                for(int i = 0; i < userList.get(tmpUsername).buddies.size(); i++)
                {
                    buddiesString += userList.get(tmpUsername).buddies.get(i);
                    buddiesString += " ";
                }

                System.out.println("LOGIN/REGISTER_SUCCESSFUL " + buddiesString);
                ctc.sendMessage("LOGIN/REGISTER_SUCCESSFUL " + buddiesString);
            }
            // incorrect password
            else
            {
                ctc.sendMessage("INVALID_USERNAME_OR_PASSWORD");
            }
        }
        // username doesn't exist
        else
        {
            ctc.sendMessage("INVALID_USERNAME_OR_PASSWORD");
        }
    }

    // ======================================================================================
}
