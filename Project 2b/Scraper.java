// Abigail McIntyre
// Project 2b - web crawler
// Due 02/14/2022

// ---------------------------------------------------------------------------------------------------------------------------
// reads from the input stream and delegates handling those events by calling (automatically performing callbacks to) the 
// appropriate method in the ParserCallback class
// ---------------------------------------------------------------------------------------------------------------------------

import java.net.*;
import java.io.*;
import javax.swing.text.html.parser.*;

public class Scraper 
{
    TagHandler handler;                                                                     // handles the tags found
    InputStreamReader inputStream;                                                          // the stream for the urls
    ParserDelegator parserDelegator;                                                        // the parser to parse the page
    URLListModel URLs;                                                                      // holds the URLs and their info
    URLCrawlerInfo currentURL;                                                              // the current url and information that goes along with it
    static long time;                                                                       // the current time
    static long startTime;                                                                  // the time the program started

    // ================================================================================================================

    Scraper(URL url)
    {
        time = System.currentTimeMillis();                                       // get the current time
        startTime = System.currentTimeMillis();                                  // get the start time of the program
        this.URLs = new URLListModel();                                          // create a new list model
        URLs.addElement(new URLCrawlerInfo(url, 0));                             // create a new URL object to hold the site data
        harvestTheirInfo();                                                      // harvest all the information
    }

    // ================================================================================================================

    public void harvestTheirInfo()
    {
        // while all the sites in the list haven't been searched and the max expansion time hasn't been reached
        while(!URLs.allVisited() && time < Params.MAX_EXPANSION_TIME + startTime)
        {
            currentURL = URLs.nextURL();
            handler = new TagHandler(URLs, currentURL);                                    // create the handler for the tags
            time = System.currentTimeMillis();                                             // update the time

            // steal all their information
            try 
            {
                inputStream = new InputStreamReader(currentURL.url.openStream());                          // open the input stream
                parserDelegator = new ParserDelegator();                                        // create the new parser
                parserDelegator.parse(inputStream, handler, true);                              // parse the page

                URLs.nextURL().visited = true;
            } 
            catch (IOException e) 
            {
                System.out.println("input exception in Scraper");

                // so that the program stops trying to visit links with exceptions over and over again causing errors
                URLs.removeElement(currentURL);
            }  
        }  
                                 
    }

    // ================================================================================================================

}


