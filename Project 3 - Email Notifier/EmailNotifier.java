// Abigail McIntyre
// Project 3 - Email Notifier
// Due 02/25/2022

// ---------------------------------------------------------------------------------------------------------------------------
// checks for new emails when the timer goes off. If there is new mail, a dialog box will come up showing the number of messages
// in the folder, who it's from, and the subject. It also creates the tray icon
// ---------------------------------------------------------------------------------------------------------------------------

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.mail.*;
import java.util.Properties;

public class EmailNotifier implements ActionListener
{
    // ----------------------------
    Timer timer;                                        // the timer for checking for new mail
    TrayIcon trayIcon;                                  // the icon for when the program goes into the tray
    SystemTray tray;
    PopupMenu popupMenu;                                // the popup menu that shows up when right clicking on the icon in the tray
    DialogBox box;                                      // the dialog box that comes up when the user clicks on settings or on startup
    int timeInMillis;                                   // the time the user selected converted into millis
    // ----------------------------
    Properties  props;
    String host;                                        // the name of the server
    String username;                                    // the user's email username
    String password;                                    // the user's email password
    String protocolProvider  = "imaps";                 // the protocol used
    Session session;
    Authenticator authenticator;                        // obtains authentication for a network connection                      
    Store store;                                        // for storing and retrieving messages
    Folder inboxFolder;                                 // a folder for mail messages
    // ----------------------------
    static NewMessageBox messagesBox;                   // the dialog that comes up when a new message is found
    static boolean dialogBeingShown;
    static boolean areNewMessages;
    static boolean settingsBeingShown;
    // ----------------------------

    EmailNotifier()
    {
        // create the settings box on entry to the program
        box = new DialogBox();
        createPopupMenu();

        // set the values from the dialog box
        host = DialogBox.servername;
        username = DialogBox.username;
        password = DialogBox.password;
        dialogBeingShown = false;
        areNewMessages = false;
        settingsBeingShown = false;

        // immediately check for mail
        getMail();
    }

    // ================================================================================================================

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        // ----------------- timer ------------------------------------------------
        
        if (e.getActionCommand().equals("TIMER"))
        {  
            System.out.println("Timer went off, checking mail!");
            getMail();
        }

        // ----------------- settings ------------------------------------------------
        
        else if (e.getActionCommand().equals("SETTINGS"))
        {
            // if there isn't already a dialog box up
            if(settingsBeingShown == false)
            {
                box = new DialogBox();                                          // create a new settings box
                host = DialogBox.servername;                                    // set the data from the box to this data
                username = DialogBox.username;
                password = DialogBox.password;
            }
        }
        
        // ----------------- sound ---------------------------------------------------

        else if (e.getActionCommand().equals("SOUND?"))
        {
            // if the user wants to play sound, set the boolean to true
            if(JOptionPane.showConfirmDialog(null, "Play sound when a new email is found?", "choose one", JOptionPane.YES_NO_OPTION) 
               == JOptionPane.YES_OPTION)
                DialogBox.playSound = true;

            // if the user doesn't want to play sound, set the boolean to false
            else
                DialogBox.playSound = false;
        }
        
        // ----------------- exit ------------------------------------------------------

        else if (e.getActionCommand().equals("EXIT"))
        {
            tray.remove(trayIcon);
            System.exit(0);
        }
    }

    // ================================================================================================================
    // starts the process of getting the mail information
    void getMail()
    {
        try
        {
            props = new Properties();
            authenticator = null;     
            session = Session.getInstance(props, authenticator);

            store = session.getStore(protocolProvider);
            store.connect(host, username, password);

            System.out.println("Number of folders: " + store.getPersonalNamespaces().length);

            inboxFolder = store.getFolder("INBOX");
            inboxFolder.open(Folder.READ_WRITE);

            processFolder(inboxFolder);

            System.out.println("Closing folder");
            inboxFolder.close(false);  // close but don't delete messages
            store.close();

        }

        catch (NoSuchProviderException nspe)
        {
            System.out.println("No such email protocol provider: " + protocolProvider);
            JOptionPane.showMessageDialog(null, "No such email protocol provider", "Error", JOptionPane.ERROR_MESSAGE);
            // if the input information is wrong
            box = new DialogBox();
            host = DialogBox.servername;
            username = DialogBox.username;
            password = DialogBox.password;
        }
        catch (MessagingException me)
        {
            System.out.println("Messaging exception:");
            JOptionPane.showMessageDialog(null, "Messaging exception", "Error", JOptionPane.ERROR_MESSAGE);
            // if the input information is wrong
            box = new DialogBox();
            host = DialogBox.servername;
            username = DialogBox.username;
            password = DialogBox.password;
        }
    }

    // ================================================================================================================
    // gets the information about the inbox folder
    static void processFolder(Folder  mailboxFolder) throws MessagingException
    {
        Message message;
        int numMessagesInFolder;

        System.out.println("Processing folder");

        // get how many messages are in the inbox
        numMessagesInFolder = mailboxFolder.getMessageCount();
        System.out.println("Total number of messages in folder: " + numMessagesInFolder);

        if (mailboxFolder.hasNewMessages())             
        {
            System.out.println("New messages arrived since folder was last opened!");
            areNewMessages = true;

            if(DialogBox.playSound = true)
            {
                System.out.println("Play sound option is selected, so playing sound!");
                new PlayWave("yoda-message-from-the-darkside.wav").start();
            }
        }

        else
            System.out.println("No new messages.");

        System.out.println("Number of messages flagged RECENT: " + mailboxFolder.getNewMessageCount());
        System.out.println("Number of messages not flagged SEEN: " + mailboxFolder.getUnreadMessageCount());
        System.out.println("=============================================");

        message = mailboxFolder.getMessage(numMessagesInFolder); // gets the most recent message
        
        processMessage(message, mailboxFolder.getNewMessageCount());
    }

    // ================================================================================================================
    // gets the information about the newest message received
    static void processMessage(Message msg, int newMessages)
    {
        String[]  headerList;
        try
        {
            System.out.println("From: " + msg.getFrom()[0]);

            headerList = msg.getHeader("to");
            if (headerList != null)
                System.out.println("To: " + headerList[0]);
        
            headerList = msg.getHeader("date");
            if (headerList != null)
                System.out.println("Date header: " + headerList[0]);
        
            System.out.println("Received date is: " + msg.getReceivedDate());
            System.out.println("Subject is: " + msg.getSubject());
            System.out.println("=================================");

            // if the dialog box is being shown and there are no new messages to display, then return
            if(dialogBeingShown == true && areNewMessages == false)
            {
                return;
            }
            // if the dialog box is being shown and there are new messages to display, then update the box
            else if(dialogBeingShown == true && areNewMessages == true)
            {
                messagesBox.dispose();
                messagesBox = new NewMessageBox(String.valueOf(msg.getFrom()[0]), msg.getSubject(), newMessages);
                areNewMessages = false;
                dialogBeingShown = true;
            }
            // if the dialog box is not being shown and there are new messages, create a dialog box
            else // (dialogBeingShown == false && areNewMessages == true)
            {
                if(areNewMessages == true)
                {
                    messagesBox = new NewMessageBox(String.valueOf(msg.getFrom()[0]), msg.getSubject(), newMessages);
                    dialogBeingShown = true;
                    areNewMessages = false;
                }
            }
        }

        catch (MessagingException me)
        {
            System.out.println("MessagingException found.  Bad message??");
            me.printStackTrace();
        }
        
    }  // end processMessage(...)


    // ================================================================================================================
    // creates the popup menu when the icon in the tray is clicked
    public void createPopupMenu()
    {
        MenuItem  menuItem;
        popupMenu = new PopupMenu();
        menuItem = new MenuItem("Settings");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("SETTINGS");
        popupMenu.add(menuItem);

        menuItem = new MenuItem("Sound Settings");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("SOUND?");
        popupMenu.add(menuItem);

        menuItem = new MenuItem("Exit");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("EXIT");
        popupMenu.add(menuItem);

        if (SystemTray.isSupported()) 
        {
            tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("icon.png"), "Email Notifier");
            trayIcon.setPopupMenu(popupMenu);
            trayIcon.setImageAutoSize(true);
            try
            {
                tray.add(trayIcon);
            }
            catch (AWTException e)
            {
                System.out.println("TrayIcon could not be added.");
                return;
            }
        }
        else
            System.exit(0);

        // set the timer to how long the user wants to wait
        timeInMillis = 60000 * DialogBox.time;
        timer = new Timer(timeInMillis, this);
        timer.setActionCommand("TIMER");
        timer.setRepeats(true);
        timer.start();
    }

    // ================================================================================================================
} // end class EmailNotifier
