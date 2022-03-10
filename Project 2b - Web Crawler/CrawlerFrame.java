// Abigail McIntyre
// Project 2b - web crawler
// Due 02/14/2022

// ---------------------------------------------------------------------------------------------------------------------------
// creates the GUI for the user to interact with and displays the JList of links and emails that was harvested
// ---------------------------------------------------------------------------------------------------------------------------

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

public class CrawlerFrame extends JFrame
                         implements ActionListener
{
    Scraper scraper;                                             // harvest information from the links
    URL url;                                                     // holds a URL from the textBox
    JTextField inputField;                                       // the textfield where the URL will be entered
    JButton goButton;                                            // the user presses it to start harvesting info
    JPanel mainPanel;                                            // the panel with the buttons and textField
    JScrollPane outputPanel;                                     // the panel with the JList
    DefaultListModel<String> linkList;                           // holds the URL's and their info
    JList<String> listBox;                                       // the JList that holds the DefaultListModel

    // ================================================================================================================

    CrawlerFrame()
    {
        // set up buttons and text field
        goButton = new JButton("Go");
        goButton.addActionListener(this);

        inputField = new JTextField(60);

        // set up the DefaultListModel for the links and their info
        linkList = new DefaultListModel<String>();
        listBox = new JList<String>(linkList);

        // set up and add the panels and buttons
        mainPanel = new JPanel();
        mainPanel.add(inputField);
        mainPanel.add(goButton);

        outputPanel = new JScrollPane(listBox);

        add(outputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH); 

        // set everything up
        setupMainFrame();
    }

    // ================================================================================================================

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource().equals(goButton))
        {
            try
            {
                url = new URL(inputField.getText());                            // get the URL from the text box
                scraper = new Scraper(url);                                     // create the scraper with the url

                // add the urls and emails to the list
                for(int i = 0; i < scraper.URLs.size(); i++)
                {
                    linkList.addElement("***************************************");
                    linkList.addElement("link: " + scraper.URLs.get(i).url.toString());
        
                    if(scraper.URLs.get(i).emails.size() > 0)
                    {
                        for(int j = 0; j < scraper.URLs.get(i).emails.size(); j++)
                        {
                            linkList.addElement("email: " + scraper.URLs.get(i).emails.get(j));
                        }
                        
                        sortEmails(i);

                        for(int j = 0; j < scraper.URLs.get(i).sortedEmails.size(); j++)
                        {
                            linkList.addElement("email: " + scraper.URLs.get(i).sortedEmails.get(j));
                        }
                        
                    }
                } 
            }
        
            catch(MalformedURLException mue)
            {
                JOptionPane.showMessageDialog(null, "Malformed URL!");
            }
        
            inputField.setText("");                                             // clear the input box
        }
    }

    // ================================================================================================================
    // sort the vector of emails in alphabetical order
    public void sortEmails(int i)
    {
        int pos = 0;

        for (int j = 0; j < scraper.URLs.get(i).emails.size(); j++) 
        { 
            pos = j; 
            for (int k = j + 1; k < scraper.URLs.get(i).emails.size(); k++) 
            {
                if(scraper.URLs.get(i).emails.get(k).compareTo(scraper.URLs.get(i).emails.get(pos)) == 0)
                {
                    scraper.URLs.get(i).emails.removeElement(scraper.URLs.get(i).emails.get(pos));
                }

                if(scraper.URLs.get(i).emails.get(k).compareTo(scraper.URLs.get(i).emails.get(pos)) > 0)
                {
                    pos = k;
                }
            }

            
            scraper.URLs.get(i).sortedEmails.addElement(scraper.URLs.get(i).emails.get(pos));

            System.out.println("Adding: " + scraper.URLs.get(i).emails.get(pos));
            System.out.println("Removing: " + scraper.URLs.get(i).emails.get(pos));

            scraper.URLs.get(i).emails.removeElement(scraper.URLs.get(i).emails.get(pos));
        } 
    }
    
    // ================================================================================================================
    // set up the window area and size
    public void setupMainFrame()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(80 * d.width / 100, 80 * d.height / 100);                   
        setLocation(d.width / 9, d.height / 9);                  

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Web Crawler");                                             

        setVisible(true);
    }

    // ================================================================================================================
}


