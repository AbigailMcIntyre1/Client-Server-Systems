// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// Holds the information of the buddy
// ----------------------------------------------------------------------------------------------------------------


package Client;

public class Buddy 
{
    String username;
    boolean online;

    public Buddy(String username, boolean online)
    {
        this.username = username;
        this.online = online;
    }

    public String toString()
    {
        String returnString = "";
        if(online)
        {
            returnString += "*";
        }
        return returnString + username;
    }
}
