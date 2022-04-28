// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// The JFrame. The client creates this and it has the DefaultListModel which contains the User's buddy list. It also has
// the login, register, logout, and add buddy buttons.
// ----------------------------------------------------------------------------------------------------------------

package Client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

public class ChatBox extends JDialog 
                     implements ActionListener, DocumentListener, KeyListener, DropTargetListener
{
    JEditorPane myEditorPane;
    JTextField textField;
    JButton sendButton;
    ConnectionToServer cts;
    String username;
    JPanel buttonPanel;
    JScrollPane scrollPane;
    ChatFrame frame;
    DropTarget dropTarget;
    String userSendingUsername; 
    java.util.List<File> fileList;
    File filePath;

    public ChatBox(String username, ConnectionToServer cts, ChatFrame frame)
    {
        this.cts = cts;
        this.username = username;
        this.frame = frame;
        this.userSendingUsername = userSendingUsername;

        myEditorPane = new JEditorPane();
        myEditorPane.setEditable(false);
        myEditorPane.setContentType("text/html");

        myEditorPane.setText(
            "<div align = \"center\">"                          +
                "<font color = \"black\">"                      +
                    "<b>"                                       +
                        "Chatting with " + username             + 
                    "</b>"                                      +
                "</font>"                                       +
            "</div>");

        scrollPane = new JScrollPane(myEditorPane);

        textField = new JTextField(50);
        textField.getDocument().addDocumentListener(this);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);

        textField.addKeyListener(this);

        buttonPanel = new JPanel();

        dropTarget = new DropTarget(myEditorPane, this);                             // for dropping files - gives it the component and listener

        buttonPanel.add(textField);
        buttonPanel.add(sendButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setUpDialogBox();

        textField.requestFocus();
    }

    // ================================================================================================================

    public void addText(String txt, int side)
    {
        HTMLDocument doc;
        Element html;
        Element body;

        doc = (HTMLDocument)myEditorPane.getDocument();
        html = doc.getRootElements()[0];
        body = html.getElement(1);

        try 
        {
            String text = "";
            if(side == 0)
            {
                text = "<div><p style=\"font-family: sans-serif; position: absolute; bottom: 0;\" align=\"left\">" + txt + "</p></div>";
            } 
            else 
            {
                text = "<div><p style=\"font-family: sans-serif; position: absolute; bottom: 0; color: blue; \" align=\"right\">" + txt + "</p></div>";
            }
            doc.insertBeforeEnd(body, text);
            myEditorPane.setCaretPosition(myEditorPane.getDocument().getLength());
        } 
        catch (Exception e)
        {
            System.out.println("Exception when adding text to pane in ChatBox");
        }
    }

    // ================================================================================================================

    @Override
	public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == sendButton)
        {
            try 
            {
                cts.sendMessage("FORWARD_THIS " + username + " " + textField.getText());
                addText(textField.getText(), 1);
                textField.setText("");
                textField.requestFocus();
            } 
            catch (IOException e1) 
            {
                System.out.println("Error sending message in ChatBox");
            }
        }
	}

    // ================================================================================================================

	@Override
	public void changedUpdate(DocumentEvent e) 
    {
        if(!textField.getText().isBlank())
        {
            sendButton.setEnabled(true);
        }
        else
        {
            sendButton.setEnabled(false);
        }
	}

    // ================================================================================================================

	@Override
	public void insertUpdate(DocumentEvent e) 
    {
        if(!textField.getText().isBlank())
        {
            sendButton.setEnabled(true);
        }
        else
        {
            sendButton.setEnabled(false);
        }
	}

    // ================================================================================================================

	@Override
	public void removeUpdate(DocumentEvent e) 
    {
        if(!textField.getText().isBlank())
        {
            sendButton.setEnabled(true);
        }
        else
        {
            sendButton.setEnabled(false);
        }
	}

    // ================================================================================================================
    // set up the JDialog
    private void setUpDialogBox()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
    
        setSize(40 * d.width / 100, 70 * d.height / 100);                   // 30 is the screen percentage to take up
        setLocation(d.width / 4, d.height / 5);
    
        setTitle("Chatting with " + username);
    
        setVisible(true);
    }

    // ================================================================================================================

    @Override
    public void keyPressed(KeyEvent e) 
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            sendButton.doClick();
        }
    }

    // ================================================================================================================

    @Override
    public void drop(DropTargetDropEvent dtde) 
    {
        Transferable transferableData;
        fileList = null;

        myEditorPane.setBackground(Color.WHITE);

        transferableData = dtde.getTransferable();
        try
        {
            if(transferableData.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                fileList = (java.util.List<File>)(transferableData.getTransferData(DataFlavor.javaFileListFlavor));
                filePath = fileList.get(0);
                cts.sendMessage("FILE_XFER_REQ " + username + " " + fileList.get(0).getName() + " " + fileList.get(0).length());
                addText("Attempting to send file: " + fileList.get(0).getName(), 1);   
            }
    
            else
            {
                System.out.println("File list flavor not supported.");
            }
        }
    
        catch(UnsupportedFlavorException ufe)
        {
            System.out.println("Unsupported flavor found!");
            ufe.printStackTrace();
        }
        
        catch(IOException ioe)
        {
            System.out.println("IOException found getting transferable data!");
            ioe.printStackTrace();
        }
    
    }

    // ================================================================================================================

    public void transferFile(String ip, int port)
    {
        FileInputStream instreamFromFile;
        OutputStream outstreamToBob;
        byte[] buffer;
        int numBytesRead; 
        long totalNumBytesRead;

        try
        {
            File infile = fileList.get(0);
            System.out.println("Created infile");
    
            long fileSize = infile.length();
    
            Socket socket = new Socket(ip, port);
    
            instreamFromFile = new FileInputStream(infile);
            System.out.println("Created instreamFromFile");
    
            outstreamToBob = socket.getOutputStream();
            System.out.println("Created outstreamToBob");
    
            buffer = new byte[128];
            System.out.println("Created buffer");
    
            numBytesRead = instreamFromFile.read(buffer);
            System.out.println("numBytesRead: " + numBytesRead);
            outstreamToBob.write(buffer, 0, numBytesRead);
            totalNumBytesRead = numBytesRead;
            System.out.println("fileSize is " + fileSize);
    
            while (totalNumBytesRead < fileSize)
            {
                System.out.println("Reading...");
                numBytesRead = instreamFromFile.read(buffer);                
                totalNumBytesRead += numBytesRead;                 
                System.out.println("totalBytesRead is: " + totalNumBytesRead);
                outstreamToBob.write(buffer, 0, numBytesRead);         
            }        
    
            instreamFromFile.close();
            outstreamToBob.close();
            socket.close();
        }
        catch(IOException io)
        {
            System.out.println("Exception in chatbox when transfering files");
        }
        
    }

    // ================================================================================================================

    @Override
    public void dragExit(DropTargetEvent dte) 
    {
        myEditorPane.setBackground(Color.WHITE);
    }

    // ================================================================================================================

    @Override
    public void dragEnter(DropTargetDragEvent dtde) 
    { 
        myEditorPane.setBackground(Color.LIGHT_GRAY);
    }

    // ================================================================================================================

    @Override
    public void dragOver(DropTargetDragEvent dtde) { }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }

    // ================================================================================================================

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    // ================================================================================================================
}