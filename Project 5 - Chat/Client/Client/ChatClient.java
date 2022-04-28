// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// ChatClient creates the JDialog box that pops up upon starting the program. It then creates the JFrame which is invisible
// until the user successfully connects. It creates the CTS and sends the username, password, and either register or login commands.

// * means not done yet

// Client Side:
//      a.) Opens a dialog box for username/password with buttons for
//          i.) creating an account (registering) 
//          ii.) logging in to an existing account
//          iii.) canceling
//      b.) Opens a main window showing a buddy list and buttons for
//          i.) sending a buddy-request (i.e., a request to be a buddy)
//          ii.) initiating a chat with a selected buddy (disabled if no buddy is selected)
//              (handling a double click on the buddy is better than just a button and is sufficient without a button)
//          iii.) logging out
//      c.) The buddy list should show the status of each buddy using an icon or a special character or color.
//      *d.) The buddy list should display a pop-up avatar (small image) when the mouse hovers over the buddy
//      *e.) A buddy's status should change from "typing" to "pending" when no mouse nor keyboard events
//          have been generated for some fixed period of time but a message has been partially entered.
//      f.) Displays a modal dialog for initiating a buddy-request when corresponding button is clicked.
//      g.) Displays a yes/no pop-up box upon receiving from the server a message that someone wants to be a buddy.
//      h.) Displays a pop-up box upon receiving from the server a message that a buddy-request invitation has been accepted or rejected.
//      i.) Displays a dialog for chatting (chat-box) when
//          i.) exactly one buddy is selected and the [chat] button is clicked (optional if ii works)
//          ii.) the user double-clicks on a buddy in the buddy list
//      j.) Displays the chat (conversation) in a chat-box, with each party identified by different colors.
//      *k.) Plays a sampled sound file (e.g., .wav) when a chat session is initiated by a buddy, or
//           when a message is received after there has been no activity for more than X minutes
//      l.) Sends a file to the other user when the file is dropped on that other user's chat box after the sender
//          has confirmed and the other user has agreed to accept the file. *A progress bar shows the progress 
//          in transmitting/receiving the file.
// ----------------------------------------------------------------------------------------------------------------

package Client;

import java.io.IOException;

public class ChatClient 
{
    ChatFrame myFrame;
    ConnectionToServer cts;
    ChatLoginDialog loginDialog;
    ChatRegisterDialog registerDialog;
    String tmpUsername;                         // create a temporary username
    String tmpPassword;                         // create a temporary password

    // ======================================================================================

    public ChatClient() throws IOException
    {
        myFrame = new ChatFrame(this);                      // create the JFrame so we can pass it to the cts
    }

    // ======================================================================================

    public void login() throws IOException
    {
        loginDialog = new ChatLoginDialog();                // create the dialog box to get the username and password
        if(loginDialog.username != null && loginDialog.password != null)
        {
            tmpUsername = loginDialog.username;             // store the username in the data member
            tmpPassword = loginDialog.password;             // store the password in the data member
    
            cts = new ConnectionToServer(tmpUsername, myFrame, this);     // create the ConnectionToServer
    
            cts.sendMessage(tmpUsername);                   // send the username to the CTC
            System.out.println("CTS sending username: " +  tmpUsername);
            cts.sendMessage(tmpPassword);                   // send the password to the CTC
            System.out.println("CTS sending password: " +  tmpPassword);
            cts.sendMessage("LOGIN");                       // send the command to the CTC
        }
    }

    // ======================================================================================

    public void register() throws IOException
    {
        registerDialog = new ChatRegisterDialog();          // create the dialog box to get the username and password
        if(registerDialog.username != null && registerDialog.password != null)
        {
            tmpUsername = registerDialog.username;          // store the username in the data member
            tmpPassword = registerDialog.password;          // store the password in the data member
    
            cts = new ConnectionToServer(tmpUsername, myFrame, this);     // create the ConnectionToServer
    
            cts.sendMessage(tmpUsername);                   // send the username to the CTC
            System.out.println("CTS sending username: " +  tmpUsername);
            cts.sendMessage(tmpPassword);                   // send the password to the CTC
            System.out.println("CTS sending password: " +  tmpPassword);
            cts.sendMessage("REGISTER");                    // send the command to the CTC
        }
    }

    // ======================================================================================
    // logs the user out. Gets rid of the current frame and makes the cts null. Then a new frame pops up for the user to re-log in
    public void logout() 
    {
        myFrame.dispose();
        cts = null;
        myFrame = new ChatFrame(this);
    }

    // ======================================================================================
    // adds the buddies to the frame upon user login
    public void updateBuddiesOnLogin()
    {
        String buddyToAdd;

        cts.buddies.size();
        System.out.println("Adding buddies, number: " + cts.buddies.size());

        
        for(int i = 0; i < cts.buddies.size(); i++)
        {
            buddyToAdd = cts.buddies.elementAt(i);

            System.out.println("Adding: " + buddyToAdd);
            myFrame.buddiesListModel.addElement(buddyToAdd);
        }
    }

    // ======================================================================================
    // adds the buddies to the frame upon user login
    public void updateBuddiesOnStatusChangeOffline()
    {
        String buddyToAdd;

        System.out.println("Adding buddies, number: " + cts.buddies.size());
        myFrame.buddiesListModel.removeAllElements();
        
        
        for(int i = 0; i < cts.buddies.size(); i++)
        {
            buddyToAdd = cts.buddies.elementAt(i);

            System.out.println("Adding: " + buddyToAdd);
            myFrame.buddiesListModel.addElement(buddyToAdd);
        }
    }

    // ======================================================================================

    public void sendOfflineStatusUpdateToBuddies() throws IOException
    {
        cts.sendMessage("STATUS_UPDATE_OFFLINE");
    }
}
