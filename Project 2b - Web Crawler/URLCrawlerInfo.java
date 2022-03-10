// Abigail McIntyre
// Project 2b - web crawler
// Due 02/14/2022

import java.net.URL;
import java.util.Vector;

// ------------------------------------------------------------------------------------------------------------------------------------
// stores both the URL, the distance from that URL to the seed URL, the emails found on that URL, and whether the site has been visited
// ------------------------------------------------------------------------------------------------------------------------------------

public class URLCrawlerInfo 
{
    URL url;                            // holds the information to the URL
    Vector<String> emails;              // holds the emails found on that URL
    int distance;                       // holds the distance from the seed to the current URL
    boolean visited;                    // holds whether or not the URL has been visited    
    Vector<String> sortedEmails;        // holds the emails once they've been sorted

    // initialize all the values
    URLCrawlerInfo(URL url, int distance)
    {
        this.url = url;
        this.distance = distance;
        emails = new Vector<String>();
        sortedEmails = new Vector<String>();
    }
}
