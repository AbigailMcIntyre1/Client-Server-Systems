// Abigail McIntyre
// Project 2b - web crawler
// Due 02/14/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the DefaultListModel of the program. Contains the functions to handle going through the list of links.
// ---------------------------------------------------------------------------------------------------------------------------

import java.net.*;
import javax.swing.*;

public class URLListModel extends DefaultListModel<URLCrawlerInfo>
{
    URL tempUrl;

    // ================================================================================================================
    // checks if the URL is already in the list or not
    public boolean isInList(String url)
    {
        System.out.println("Checking if: " + url + " is in the list.");

        // go through the links in the list
        for(int i = 0; i < this.size(); i++)
        {
            // If the link is in the list already
            if(this.get(i).url.toString().equals(url))      
            {    
                System.out.println(url + " is in the list already");

                // then return true
                return true;
            }
        }
        System.out.println(url + " is not in the list already");
        return false; 
    }

    // ================================================================================================================

    public boolean isInListEmail(String email)
    {
        System.out.println("Checking if: " + email + " is in the list.");

        // go through the links in the list
        for(int i = 0; i < this.size(); i++)
        {
            for(int j = 0; j < this.get(i).emails.size(); j++)
            {
                if(this.get(i).emails.get(j).equals(email))
                {
                    System.out.println(email + " is in the list already");

                    // then return true
                    return true;
                }
            }
        }
        System.out.println(email + " is not in the list already");
        return false; 
    }

    // ================================================================================================================
    // adds the URL to the list
    public void addURL(URLCrawlerInfo parent, String url)
    {
        // if it's not in the list, then add it to the list
        if(!isInList(url))                                          
        {
            try 
            {
                tempUrl = new URL(url);
                System.out.println("Adding : " + url + " to the list.");
                this.addElement(new URLCrawlerInfo(tempUrl, parent.distance + 1));
            } 
            catch (MalformedURLException e) 
            {
                System.out.println("Malformed URL when converting " + url + " to a url to get " + tempUrl);
            }
        }
    }

    // ================================================================================================================
    // checks to see if all the nodes in the list have been visited
    public boolean allVisited()
    {
        System.out.println("Checking if all the URLs have been visited");
        for(int i = 0; i < this.size(); i++)
        {
             // if there's a URL that hasn't been marked as visited, then return false
            if(this.get(i).visited == false)                               
            {
                return false;
            }
        }
        return true;       // it got mad again
    }

    // ================================================================================================================
    // get the next node in the list to be visited
    public URLCrawlerInfo nextURL()
    {
        // if all the nodes have not been visited
        if(!allVisited())                                           
        {
            // go through them 
            for (int i = 0; i < this.size(); i++)
            {
                // get that one hasn't been visted
                if(this.get(i).visited == false)
                {
                    System.out.println(this.get(i).url + " has not been visited, returning it");

                    // return them
                    return this.get(i);
                }
            }
        }
        return null;        // and once again
    }

    // ================================================================================================================
}
