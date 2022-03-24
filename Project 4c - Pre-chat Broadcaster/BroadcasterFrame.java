// Abigail McIntyre
// Project 4c - Pre-chat Broadcaster
// Done  03/24/2022

// ---------------------------------------------------------------------------------------------------------------------------
// Sets up the JFrame. When the client starts executing, a JOptionPane InputDialog should be
// displayed. If the user closes the JOptionPane with only whitespace input or no input, the program should exit.
// Otherwise the user's input should be saved as the identification (username) for that client.
// The client JFrame should display two buttons, [Send] and [Exit], a text field, and a label.
// [Send] should be disabled if and only if the text field is empty or contains only white space.
// Each time the user clicks [Send], the text in the text field should be sent to the server-side's connection to client
// ---------------------------------------------------------------------------------------------------------------------------

import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.*;

import javax.swing.*;

public class BroadcasterFrame extends JFrame
                              implements ActionListener
{
    BroadcasterClient client;
    JButton sendButton;
    JButton exitButton;
    JTextField inputField;
    JLabel displayLabel;
    JPanel buttonPanel;
    JPanel displayPanel;
    ConnectionToServer cts;

    // ======================================================================================

    BroadcasterFrame()
    {
        // ----------------------------------------------------------------------------------

        String username = JOptionPane.showInputDialog("Username:");         // get the client's username

        // if the username isn't empty or just whitespace, send the username off
        if(username != null && !username.isEmpty() && !username.isBlank())
        {
            // start the client and send the client's username   
            try 
            {
                client = new BroadcasterClient(username, this);
                cts = new ConnectionToServer(username, this);
            } 
            catch (UnknownHostException e) 
            {
                JOptionPane.showMessageDialog(null, "Error connecting to server", "Error", JOptionPane.ERROR_MESSAGE);
            } 
            catch (IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Error connecting to server", "Error", JOptionPane.ERROR_MESSAGE);
            }  
        }
        // if it is, exit
        else
            System.exit(0);  

        // ----------------------------------------------------------------------------------

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);

        inputField = new JTextField(20);
        displayLabel = new JLabel("   ");

        buttonPanel = new JPanel();
        displayPanel = new JPanel();

        buttonPanel.add(inputField);
        buttonPanel.add(sendButton);
        buttonPanel.add(exitButton);

        displayPanel.add(displayLabel);

        add(displayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH); 

        setupMainFrame();                                                   // create the frame
        // ----------------------------------------------------------------------------------
    }

    // ======================================================================================

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == sendButton)
        {
            // the text in the text field should be sent to the server-side's connection to client
            String message;
            try 
            {
                message = inputField.getText();
                if(!message.isBlank())
                {
                    cts.sendMessage(message);
                    System.out.println("Sending message from actionPerformed");
                }
                inputField.setText("");
            } 
            catch (IOException e1) 
            {
                System.out.println("Error sending message from ActionPerformed");
            }
        }

        if(e.getSource() == exitButton)
            System.exit(0);
    }
        
    // ======================================================================================

    public void setupMainFrame() 
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(50 * d.width / 100, 50 * d.height / 100);                   
        setLocation(d.width / 4, d.height / 4);                  

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Broadcaster");                                             

        setVisible(true);
    }

    // ======================================================================================
}
