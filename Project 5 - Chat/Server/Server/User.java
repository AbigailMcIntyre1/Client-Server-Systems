// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// Holds the information of the individual user 
// ----------------------------------------------------------------------------------------------------------------

package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class User 
{
    String username;
    String password;
    Vector<String> buddies;
    ConnectionToClient ctc;
    int numBuddies;

    // ======================================================================================

    // if reading from file
    public User(DataInputStream dis) throws IOException
    {
        buddies = new Vector<String>();

        System.out.println("Reading username");
        username = dis.readUTF();
        System.out.println("Username: " + username);
        System.out.println("Reading password");
        password = dis.readUTF();
        System.out.println("Password: " + password);

        System.out.println("Reading number of buddies");
        numBuddies = dis.readInt();
        System.out.println("number of buddies: " + numBuddies);

        for(int n = 0; n < numBuddies; n++)
        {
            buddies.add(dis.readUTF());
        }
    }

    // ======================================================================================

    // if not reading from file
    public User(String username, String password, ConnectionToClient ctc)
    {
        this.username = username;
        this.password = password;
        this.ctc = ctc;
        numBuddies = 0;
        buddies = new Vector<String>();
    }

    // ======================================================================================

    public void store(DataOutputStream dos) throws IOException 
    {
        System.out.println("============== writing username: " + username);
        dos.writeUTF(username);
        System.out.println("============== writing password: " + password);
        dos.writeUTF(password);
        System.out.println("============== writing number of buddies: " + buddies.size());
        dos.writeInt(buddies.size());
        
        for(int n = 0; n < buddies.size(); n++)
        {
            System.out.println("============== writing buddies: " + buddies.elementAt(n));
            dos.writeUTF(buddies.elementAt(n));
        }

    }

    // ======================================================================================
}
