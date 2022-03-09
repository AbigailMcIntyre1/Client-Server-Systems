// Abigail McIntyre
// Project 3 - Email Notifier
// Due 02/25/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the JDialogBox that comes up when the user has new emails
// ---------------------------------------------------------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class NewMessageBox extends JDialog 
                            implements ActionListener
{
    String fromm;
    String subjectt;
    int numNewMessagess;

    JLabel fromLabel;
    JLabel subjectLabel;
    JLabel numNewMessagesLabel;

    JTextField fromField;
    JTextField subjectField;
    JTextField numNewMessagesField;

    JButton okButton;
    GroupLayout inputLayout;
    JPanel topPanel;
    JPanel bottomPanel;

    NewMessageBox(String from, String subject, int numNewMessages)
    {
        // ---------- take the values ------------------
        fromm = from;
        subjectt = subject;
        numNewMessagess = numNewMessages;

        // --------- set up the labels and text fields and set them to the values taken ------------
        fromLabel = new JLabel("From:");
        subjectLabel = new JLabel("Subject:");
        numNewMessagesLabel = new JLabel("Number of new Messages: ");

        fromField = new JTextField(25);
        fromField.setEditable(false);
        fromField.setText(from);

        subjectField = new JTextField(25);
        subjectField.setEditable(false);
        subjectField.setText(subject);

        numNewMessagesField = new JTextField(25);
        numNewMessagesField.setEditable(false);
        numNewMessagesField.setText(Integer.toString(numNewMessagess));

        okButton = new JButton("Okay");
        okButton.addActionListener(this);

        topPanel = new JPanel();
        bottomPanel = new JPanel();

        // ----------------- Group Layout --------------------------------------

        inputLayout = new GroupLayout(topPanel);
        topPanel.setLayout(inputLayout);
        inputLayout.setAutoCreateGaps(true);
        inputLayout.setAutoCreateContainerGaps(true);

        // ------------ group layout stuff - horizontal group ------------------
        GroupLayout.SequentialGroup hGroup = inputLayout.createSequentialGroup();
        hGroup.addGroup(inputLayout.createParallelGroup().
                    addComponent(fromLabel)
                    .addComponent(subjectLabel)
                    .addComponent(numNewMessagesLabel));
        hGroup.addGroup(inputLayout.createParallelGroup().
                    addComponent(fromField)
                    .addComponent(subjectField)
                    .addComponent(numNewMessagesField));
        inputLayout.setHorizontalGroup(hGroup);

        // ---------- group layout stuff - vertical group ---------------------
        GroupLayout.SequentialGroup vGroup = inputLayout.createSequentialGroup();
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(fromLabel)
                    .addComponent(fromField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(subjectLabel)
                    .addComponent(subjectField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(numNewMessagesLabel)
                    .addComponent(numNewMessagesField));
        inputLayout.setVerticalGroup(vGroup);

        // ---------- add the panels and set up the box ----------------------
        bottomPanel.add(okButton);
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setUpNewMessageBox();
    }

    // ================================================================================================================

    @Override
    public void actionPerformed(ActionEvent e) 
    {
       if(e.getSource().equals(okButton))
       {
           dispose();
       }
    }

    // ================================================================================================================

    // set up the JDialog
    private void setUpNewMessageBox()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
    
        setSize(30 * d.width / 100, 20 * d.height / 100);                   // 30 is the screen percentage to take up
        setLocation(d.width / 3, d.height / 3);
    
        setTitle("Email Notifier - New Mail!");
    
        setVisible(true);
    }

    // ================================================================================================================
}
