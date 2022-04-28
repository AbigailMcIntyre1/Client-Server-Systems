// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// the JDialogBox that comes up when the user logs in
// ----------------------------------------------------------------------------------------------------------------

package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class ChatLoginDialog extends JDialog
                            implements ActionListener, KeyListener
{
    String username;
    String password; 

    JButton loginButton;

    JLabel usernameLabel;
    JLabel passwordLabel;

    JTextField usernameField;
    JPasswordField passwordField;

    JPanel inputPanel;                  // a panel that has the input fields
    JPanel buttonPanel;                 // a panel that has the login and register buttons

    GroupLayout inputLayout;

    public ChatLoginDialog()
    {
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        usernameField = new JTextField(25);
        usernameLabel = new JLabel("Username: ");

        passwordField = new JPasswordField(25);
        passwordLabel = new JLabel("Password: ");
        passwordField.addKeyListener(this);
        
        inputPanel = new JPanel();
        buttonPanel = new JPanel();

        // ----------------- Group Layout --------------------------------------
        inputLayout = new GroupLayout(inputPanel);
        inputPanel.setLayout(inputLayout);
        inputLayout.setAutoCreateGaps(true);
        inputLayout.setAutoCreateContainerGaps(true);
        
        // ------------ group layout stuff - horizontal group ------------------
        GroupLayout.SequentialGroup hGroup = inputLayout.createSequentialGroup();
        hGroup.addGroup(inputLayout.createParallelGroup().
                    addComponent(usernameLabel)
                    .addComponent(usernameLabel)
                    .addComponent(passwordLabel));
        hGroup.addGroup(inputLayout.createParallelGroup().
                    addComponent(usernameField)
                    .addComponent(passwordField));
        inputLayout.setHorizontalGroup(hGroup);
        
        // ---------- group layout stuff - vertical group ---------------------
        GroupLayout.SequentialGroup vGroup = inputLayout.createSequentialGroup();
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(usernameLabel)
                    .addComponent(usernameField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(usernameLabel)
                    .addComponent(usernameField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(passwordLabel)
                    .addComponent(passwordField));
        inputLayout.setVerticalGroup(vGroup);

        buttonPanel.add(loginButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setUpDialogBox();
    }

    // ================================================================================================================

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(!usernameField.getText().isBlank() && !passwordField.getText().isBlank())
        {
            username = usernameField.getText();
            password = passwordField.getText();
            dispose();
        }
    }

    // ================================================================================================================
    // set up the JDialog
    private void setUpDialogBox()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
    
        setSize(30 * d.width / 100, 30 * d.height / 100);                   // 30 is the screen percentage to take up
        setLocation(d.width / 3, d.height / 3);
    
        setTitle("Login");
    
        setVisible(true);
    }

    // ================================================================================================================

    @Override
    public void keyPressed(KeyEvent e) 
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            loginButton.doClick();
        }
    }

    // ================================================================================================================

    @Override
    public void keyTyped(KeyEvent e) 
    { }

    @Override
    public void keyReleased(KeyEvent e) 
    { }

    // ================================================================================================================
}