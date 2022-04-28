// Abigail McIntyre
// Project 5 - Chat Project
// Due 04-22-2022

// ----------------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------------

package Client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBob implements Runnable                          // (P2PServer), constructed after receiving "yes" from JOptionPane
{
    long fileSize;
    int port;
    String filename;
    ServerSocket serverSocket;

    // ================================================================================================================
    
    public ServerBob(long fileSize, int port, String filename)
    {
        this.fileSize = fileSize;
        this.port = port;
        this.filename = filename;

        try 
        {
            serverSocket = new ServerSocket(port);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        new Thread(this).start();                   // start the thread
    }

    // ================================================================================================================

    @Override
    public void run() 
    {
        Socket socket;
        InputStream instreamFromNet;
        FileOutputStream outstreamToFile;
        byte[] buffer;
        int numBytesRead;
        long totalNumBytesRead;

        try 
        {
            socket = serverSocket.accept();
            instreamFromNet = socket.getInputStream();
            System.out.println("Creating file...");
            outstreamToFile = new FileOutputStream("new" + filename);
            System.out.println("Creating buffer...");
            buffer = new byte[128];
            numBytesRead = instreamFromNet.read(buffer);
            outstreamToFile.write(buffer, 0, numBytesRead); 
            System.out.println("Reading numBytesRead... " + numBytesRead);

            totalNumBytesRead = numBytesRead;
            System.out.println("fileSize is " + fileSize);
            System.out.println("numBytesRead is: " + numBytesRead);

            while (totalNumBytesRead < fileSize)
            {
                System.out.println("Reading...");
                numBytesRead = instreamFromNet.read(buffer);                
                totalNumBytesRead += numBytesRead;                 
                System.out.println("totalBytesRead is: " + totalNumBytesRead);
                outstreamToFile.write(buffer, 0, numBytesRead);         
            }           

            instreamFromNet.close();
            outstreamToFile.close();
            serverSocket.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Error in ServerBob");
        }
    }

    // ================================================================================================================
}
