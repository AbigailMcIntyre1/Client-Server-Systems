// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------
// The hashtable that contains Users that have registered. The key is their username.
// ----------------------------------------------------------------------------------------------------------------

package Server;

import java.util.Hashtable;
import java.io.*;
import java.util.Enumeration;

public class UserList extends Hashtable<String, User>
{
    String fileName;
    int numUsers;                   // beginning of file will have numbers. Read in and use for loop

    // ======================================================================================

    // constructor for if there is no existing file
    public UserList()
    {
        System.out.println("Creating UserList!");
    }

    // ======================================================================================

    // constructor if there is an existing file
    public UserList(String fileName) throws IOException
    {
        DataInputStream dis;
        User tmpUser;
        File file;

        System.out.println("Loading UserList");
        System.out.println("------------------");

        this.fileName = fileName;

        System.out.println("Filename: " + fileName);
        file = new File(fileName);

        System.out.println("Creating input stream");
        dis = new DataInputStream(new FileInputStream(file));
        
        System.out.println("Reading numUsers");
        numUsers = dis.readInt();
        System.out.println("Number of users: " + numUsers);
    
        for(int n = 0; n < numUsers; n++)
        {
            System.out.println("Creating user");
            tmpUser = new User(dis);
            put(tmpUser.username, tmpUser);
        }

        dis.close();
    }

    // ======================================================================================

    // Stores the hashtable in a text file
    void store(String fileName) throws IOException 
    {
        Enumeration<User> userEnum;
        DataOutputStream dos;
        FileOutputStream fos;
        this.fileName = fileName;
        System.out.println(fileName + "----------------------");

        System.out.println("Creating FOS -----------------------");
        fos = new FileOutputStream(fileName);
        System.out.println("Creating DOS -----------------------");
        dos = new DataOutputStream(fos);

        System.out.println("Writing the number of users in hashtable -----------------------");
        dos.writeInt(this.size());
        System.out.println("getting enum -----------------------");
        userEnum = elements();

        while(userEnum.hasMoreElements())
        {
            System.out.println("Storing user -----------------------");
            userEnum.nextElement().store(dos);
        }
    }

    // ======================================================================================
}
