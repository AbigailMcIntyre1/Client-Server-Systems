// Abigail McIntyre
// Project 4 - Trojan Software
// Due 03/16/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the JPanels that disappear when the user moves their mouse over top of them to reveal a picture in the Frame underneath
// ---------------------------------------------------------------------------------------------------------------------------

import javax.swing.JPanel;
import java.awt.event.*;
// import java.util.Random;
// import java.awt.*;

public class LittleSquares extends JPanel
                            implements MouseListener
{
    // static Random rand = new Random();                   // for random colored boxes

    // ================================================================================================================
    LittleSquares()
    {
        addMouseListener(this);                             // add a mouse listener so that when the user enters the box, they disappear
    }

    // ================================================================================================================

    // when the mouse hovers over the panel, it becomes transparent
    @Override
    public void mouseEntered(MouseEvent e) 
    {
        setOpaque(false);                                   // set the panel to transparent
        repaint();                                          // repaint the panel

        // if using random color boxes instead
        // Color color = new Color(rand.nextInt(265));
        // setBackground(color);
    }

    // ================================================================================================================

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    // ================================================================================================================

}
