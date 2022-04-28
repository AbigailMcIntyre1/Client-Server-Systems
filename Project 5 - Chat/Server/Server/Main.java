// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// The main class for the Server Side

// This project comprises two applications to provide for simple network "chatting". One application is a
// central server which allows users to find each other and establish chat sessions. The other application is a
// client run by users to communicate with the central server and other clients.
// ----------------------------------------------------------------------------------------------------------------

package Server;

public class Main 
{
    public static void main(String[] args) 
    {
        ChatServer server = new ChatServer();
    }
}
