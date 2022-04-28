// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// The JFrame. The client creates this and it has the DefaultListModel which contains the User's buddy list. It also has
// the login, register, logout, and add buddy buttons.
// ----------------------------------------------------------------------------------------------------------------

package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Hashtable;

public class ChatFrame extends JFrame
                        implements ActionListener, MouseListener
{
    JButton addBuddyButton;                                     
    JButton loginButton;
    JButton registerButton;
    JButton logoutButton;

    JLabel buddiesLabel;

    JPanel miscPanel;                                           // contains the label for the buddies
    JPanel buttonPanel;                                         // contains the addBuddy, login, register, and logout buttons
    ChatClient client;                                          // the client that's passed so we can access some of its methods and data members

    JScrollPane buddyPanel;                                     // the panel with the JList
    DefaultListModel<String> buddiesListModel;                  // holds the buddies the user has
    JList<String> buddiesBox;                                   // the JList that holds the DefaultListModel;

    Hashtable<String, ChatBox> openBoxesList;
    Object buddyName;

    public ChatFrame(ChatClient client)
    {
        this.client = client;
        openBoxesList = new Hashtable<String, ChatBox>();

        // set up the DefaultListModel for the buddies
        buddiesListModel = new DefaultListModel<String>();
        buddiesBox = new JList<String>(buddiesListModel);
        buddiesBox.addMouseListener(this);

        buddiesLabel = new JLabel("Your Buddies");

        // set up the buttons
        addBuddyButton = new JButton("Add Buddy");
        addBuddyButton.addActionListener(this);
        addBuddyButton.setEnabled(false);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        logoutButton.setEnabled(false);

        // set up the panels and add to them 
        miscPanel = new JPanel();
        miscPanel.add(buddiesLabel);

        buttonPanel = new JPanel();
        buttonPanel.add(addBuddyButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(logoutButton);

        buddyPanel = new JScrollPane(buddiesBox);

        add(miscPanel, BorderLayout.NORTH);
        add(buddyPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH); 

        setupMainFrame();
    }

    // ================================================================================================================

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        // ----------------------- add buddy ---------------------------------
        if(e.getSource() == addBuddyButton)
        {
            String buddyToAdd = JOptionPane.showInputDialog("Username that you would like to send a buddy request to");

            if(!buddyToAdd.isBlank())
            {
                try 
                {
                    client.cts.sendMessage("BUDDY_REQUEST " + buddyToAdd);
                } 
                catch (IOException e1) 
                {
                    JOptionPane.showMessageDialog(this, "Error sending buddy request.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        // ----------------------- register ---------------------------------
        if(e.getSource() == registerButton)
        {
            try 
            {
                client.register();
            } 
            catch (IOException e1) 
            {
                System.out.println("Error registering - frame");
            }
        }

        // ----------------------- login ---------------------------------
        else if(e.getSource() == loginButton)
        {
            try 
            {
                client.login();
            } 
            catch (IOException e1) 
            {
                System.out.println("Error logging in - frame");
            }
        }

        // ----------------------- logout ---------------------------------
        else if(e.getSource() == logoutButton)
        {
            client.logout();
        }
    }

    // ================================================================================================================
    // misc methods to bring up dialog boxes
    
    public void usernameTakenDialog()
    {
        JOptionPane.showMessageDialog(this, "Username already taken, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --------------------------------------------------------

    public void invalidUsernameOrPassword()
    {
        JOptionPane.showMessageDialog(this, "Invalid username or password, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --------------------------------------------------------

    public void serverDied()
    {
        JOptionPane.showMessageDialog(this, "Sorry, the server has died. Please come back later.", "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    // --------------------------------------------------------

    public void requestedBuddyOffline(String potentialBuddy)
    {
        JOptionPane.showMessageDialog(this, "The buddy you have requested, " + potentialBuddy + ", is offline." , "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --------------------------------------------------------

    public void alreadyBuddies(String potentialBuddy)
    {
        JOptionPane.showMessageDialog(this, "You are already buddies with " + potentialBuddy, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ================================================================================================================

    public void wantThisFile(String senderUsername, String filename, String fileSize) //, String filePath)
    {
        int result = JOptionPane.showConfirmDialog(this, senderUsername + " is trying to send " + filename + " which is " + fileSize + 
                                                    " bytes, do you want to accept?", "Want this file?", JOptionPane.YES_NO_OPTION);

        if(result == JOptionPane.YES_OPTION)
        {
            try 
            {
                client.cts.sendMessage("ACCEPTED_FILE_XFER " + senderUsername + " " + filename + " " + fileSize); 
                openBoxesList.get(senderUsername).addText("Accepted file: " + filename + " from: " + senderUsername + 
                " size: " + fileSize + " bytes", 1);

                ServerBob serverBob = new ServerBob(Long.parseLong(fileSize), 1234, filename);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        else if (result == JOptionPane.NO_OPTION)
        {
            System.out.println("File transfer denied.");
        }
        else 
        {
            System.out.println("File transfer denied.");
        }
    }

    // ================================================================================================================

    public void youHaveABuddyRequest(String potentialBuddy)
    {
        int result = JOptionPane.showConfirmDialog(this,"You have a Buddy Request from " + potentialBuddy + " would you like to accept?", 
                    "Buddy Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION)
        {
            try 
            {
                // send the request to the ADD_BUDDY in the CTC
                client.cts.sendMessage("ADD_BUDDY " + potentialBuddy);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        else if (result == JOptionPane.NO_OPTION)
        {
            System.out.println("Buddy request denied");
        }
        else 
        {
            System.out.println("Buddy request denied");
        }
    }

    // ================================================================================================================
    // set up the window area and size
    public void setupMainFrame()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(40 * d.width / 100, 60 * d.height / 100);                   
        setLocation(d.width / 9, d.height / 9);                  

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Please Login/Register");                                             

        setVisible(true);
    }

    // ================================================================================================================

	@Override
	public void mouseClicked(MouseEvent e) 
    {
        int index;

        buddiesBox = (JList<String>) e.getSource();
        if (e.getClickCount() == 2)                                                 // if the user has double clicked
        {
          index = buddiesBox.locationToIndex(e.getPoint());                         // get the location of where the user clicked

          if (index >= 0) 
          {
            buddyName = buddiesBox.getModel().getElementAt(index);                  // get the name of where the user clicked

            ChatBox buddyChatBox = openBoxesList.get(buddyName);
                
                if(buddyChatBox == null)
                {
                    buddyChatBox = new ChatBox(buddyName.toString(), client.cts, this);
                    openBoxesList.put(buddyName.toString(), buddyChatBox);
                }
                buddyChatBox.requestFocus();
          }
        }
	}

    // ================================================================================================================

    public void sendMessage(String buddyName, String msg)
    {
        ChatBox buddyChatBox = openBoxesList.get(buddyName);
        if(buddyChatBox == null)
        {
            buddyChatBox = new ChatBox(buddyName.toString(), client.cts, this);
            openBoxesList.put(buddyName, buddyChatBox);
            System.out.println("buddyChatBox is null, creating new one");
        }
        buddyChatBox.requestFocus();
        buddyChatBox.addText(msg, 0);
    }
    
    // ================================================================================================================

	@Override
	public void mousePressed(MouseEvent e) 
    { }

	@Override
	public void mouseReleased(MouseEvent e) 
    { }

	@Override
	public void mouseEntered(MouseEvent e) 
    { }

	@Override
	public void mouseExited(MouseEvent e) 
    { }

    // ================================================================================================================
    
}
