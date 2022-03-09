// Abigail McIntyre
// Project 3 - Email Notifier
// Due 02/25/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the JDialogBox that comes up when the user makes changes to their settings
// ---------------------------------------------------------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.io.*;

public class DialogBox extends JDialog 
                       implements ActionListener 
{
    JLabel servernameLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JLabel timeOptionLabel;
    JLabel playSoundLabel;
    JLabel errorLabel;

    JTextField servernameField;
    JTextField usernameField;
    // JTextField passwordField;
    JPasswordField passwordField;

    JButton saveButton;                 // a button to save the settings
    JButton cancelButton;               // a button to cancel changing the settings

    JPanel inputPanel;                  // a panel that has the input fields
    JPanel savePanel;                   // a panel that has the save and cancel buttons

    JSpinner timeOption;
    SpinnerModel spinnerModel;
    JComboBox playSoundBox;

    GroupLayout inputLayout;

    // variables to be used in other classes
    static String servername;
    static String username;
    static String password;
    static int time;
    static boolean playSound;

    // file stuff
    File file;
    FileReader fileReader;
    BufferedReader bufferedReader;
    String[] data;

    BufferedWriter bufferedWriter;
    FileWriter settingsFile;

    // ================================================================================================================

    DialogBox()
    {
        data = new String[3];
        data[0] = "imap.gmx.us";
        data[1] = "amcintyre1@gmx.com";
        data[2] = "Luvtoswim101";
        time = 15;              // set a default value just in case
        playSound = true;       // set a default value just in case
        EmailNotifier.settingsBeingShown = true;    // there is a dialog box being shown
        createLayout();
        setUpDialogBox();
    }

    // ================================================================================================================

    public void createLayout()
    {
        // load all the previously saved settings
        readFromSettings();

        // --------- set up the input fields and their labels --------------------
        servernameLabel = new JLabel("Server name: ");
        servernameField = new JTextField(25);

        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(25);

        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(25);

        errorLabel = new JLabel("Please enter valid input");
        errorLabel.setVisible(false);

        timeOptionLabel = new JLabel("Time between checking: ");
        spinnerModel = new SpinnerNumberModel(15, 1, 60, 1); 
        timeOption = new JSpinner(spinnerModel);
        Dimension d = new Dimension(50, 20);
        timeOption.setPreferredSize(d);
        timeOption.setMinimumSize(d);

        playSoundLabel = new JLabel("Play sound?");
        String[] yesNo = {"Yes", "No"};
        playSoundBox = new JComboBox(yesNo);
        playSoundBox.setSelectedIndex(0);
        playSoundBox.addActionListener(this);

        // --------- set up the JButtons and JPanels --------------------------

        saveButton = new JButton("Save");
        saveButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        inputPanel = new JPanel();
        savePanel = new JPanel();

        // ----------------- Group Layout --------------------------------------
        inputLayout = new GroupLayout(inputPanel);
        inputPanel.setLayout(inputLayout);
        inputLayout.setAutoCreateGaps(true);
        inputLayout.setAutoCreateContainerGaps(true);
        
        // ------------ group layout stuff - horizontal group ------------------
        GroupLayout.SequentialGroup hGroup = inputLayout.createSequentialGroup();
        hGroup.addGroup(inputLayout.createParallelGroup().
                    addComponent(servernameLabel)
                    .addComponent(usernameLabel)
                    .addComponent(passwordLabel)
                    .addComponent(timeOptionLabel)
                    .addComponent(playSoundLabel));
        hGroup.addGroup(inputLayout.createParallelGroup().
                    addComponent(servernameField)
                    .addComponent(usernameField)
                    .addComponent(passwordField)
                    .addComponent(timeOption)
                    .addComponent(playSoundBox));
        hGroup.addGroup(inputLayout.createParallelGroup()
                    .addComponent(errorLabel));
        inputLayout.setHorizontalGroup(hGroup);
        
        // ---------- group layout stuff - vertical group ---------------------
        GroupLayout.SequentialGroup vGroup = inputLayout.createSequentialGroup();
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(servernameLabel)
                    .addComponent(servernameField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(usernameLabel)
                    .addComponent(usernameField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(passwordLabel)
                    .addComponent(passwordField));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(timeOptionLabel)
                    .addComponent(timeOption));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(playSoundLabel)
                    .addComponent(playSoundBox));
        vGroup.addGroup(inputLayout.createParallelGroup(Alignment.BASELINE).
                    addComponent(errorLabel));
        inputLayout.setVerticalGroup(vGroup);

        savePanel.add(saveButton);
        savePanel.add(cancelButton);

        // -------------- set the text from the Settings file  -------------------------------
        if(data[0] != null && data[1] != null && data[2] != null)
        {
            servernameField.setText(data[0]);
            usernameField.setText(data[1]);
            passwordField.setText(data[2]);
        }
        // ----------------------------------------------------------------------------------

        add(inputPanel, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
    }

    // ================================================================================================================

    public void readFromSettings()
    {
        try  
        {  
            file = new File("Settings.txt");                    // creates a new instance of a file 

            if(file.createNewFile())
            {
                file.createNewFile();
            }

            else
            {
                fileReader = new FileReader(file);                  // reads the file  
                bufferedReader = new BufferedReader(fileReader);    // creates a buffered character input stream  
                String line;                                        // holds the line being read
                int count = 0;                                      // the accumulator
    
                // while there's still lines in the file to be read
                while((line=bufferedReader.readLine()) != null)  
                {  
                    data[count] = line;
                    count++;  
                }  
                fileReader.close();                                 // close the stream
            }

        }  
        catch(IOException e)  
        {  
            e.printStackTrace();  
        }  
    }    
    
    // ================================================================================================================

    public void saveToSettings()
    {
        try
        {
            settingsFile = new FileWriter("Settings.txt");
             
            bufferedWriter = new BufferedWriter(settingsFile);                      // Initialize BufferedWriter
             
            bufferedWriter.write(servernameField.getText() + '\n');                 // write all the data to a new line
            bufferedWriter.write(usernameField.getText() + '\n');
            bufferedWriter.write(passwordField.getText() + '\n');
             
            bufferedWriter.close();                                                 // Closing BufferWriter to end operation
        }
        catch (IOException except)
        {
            except.printStackTrace();
        }
    }

    // ================================================================================================================

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == cancelButton)   
        {
            EmailNotifier.settingsBeingShown = false;                   // there is no longer a dialog box being shown
            dispose();
        }                                        
        
        if(e.getSource() == saveButton)
        {
            servername = servernameField.getText();
            username = usernameField.getText();
            password = passwordField.getText();
            time = (int) timeOption.getValue();

            if(playSoundBox.getSelectedItem().equals("Yes"))
            {
                playSound = true;
            }
            else
            {
                playSound = false;
            }

            EmailNotifier.settingsBeingShown = false;                   // there is no longer a dialog box being shown
            saveToSettings();
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
    
        setTitle("Email Notifier");
    
        setVisible(true);
    }

    // ================================================================================================================
}
