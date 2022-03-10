// Abigail McIntyre
// Project 2b - web crawler
// Due 02/14/2022

// ---------------------------------------------------------------------------------------------------------------------------
// provides the various methods to handle the various callback events when certain tags or other text are read from the 
// input stream; these methods are the listener methods similar to actionPerformed(...) or valueChanged(...)
// ---------------------------------------------------------------------------------------------------------------------------

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.*;

public class TagHandler extends HTMLEditorKit.ParserCallback
{
    String attributeStr;                                                                                // holds the attribute in string form
    String regExString;                                                                                 // holds the regular expression for the matcher
    String testString;                                                                                  // holds the data input to be matched against the pattern
    Pattern pattern;                                                                                    // holds the pattern for the matcher
    Matcher matcher;                                                                                    // holds the matcher variable
    Boolean done;                                                                                       // to signal whether the text has finished being pattern matched

    URLListModel URLs;
    URLCrawlerInfo parent; 
    URL tempUrl;
    String tempUrlString;
    long time;
    
    // ================================================================================================================
    TagHandler(URLListModel URLs, URLCrawlerInfo parent)
    {
        regExString = new String("[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");  
        this.URLs = URLs;
        this.parent = parent; 
        System.out.println("========== Visiting: " + parent.url.toString() + "========");                                                                                                                            
    }

    // ================================================================================================================
    // Looks for start tags. If there's an anchor tag, then it'll check for MailTo: to see if it's an email, if not, then it's a link.
    // It then adds them to the parent node
    @Override
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {
        time = System.currentTimeMillis();

        // if it's an anchor tag and 
        if(t == HTML.Tag.A && time < Params.MAX_RUN_TIME + Scraper.startTime)
        {
            attributeStr = new String(a.getAttribute(HTML.Attribute.HREF).toString());                  // change the attribute to a string

            if(attributeStr != null && attributeStr.toUpperCase().contains("MAILTO:") && parent.distance < Params.MAX_RADIUS)   // if it's an email in the anchor tag
            {
                System.out.println("email " + attributeStr);
                parent.emails.addElement(attributeStr);
            } 
            else if(attributeStr != null && parent.distance < Params.MAX_RADIUS)                                                                                       // if it's a link in the anchor tag
            {
                String completedURL = "";   // have to initialize it or it gets mad

                if(attributeStr.startsWith("http"))
                {
                    URLs.addURL(parent, attributeStr);
                }
                else if(attributeStr.startsWith("/"))
                {                   
                    // If parent starts with /                   
                    if(parent.url.toString().charAt(parent.url.toString().length() - 1) == '/')
                    { 
                        completedURL = parent.url.toString().substring(0, parent.url.toString().length()-1) + attributeStr;
                    }
                    // If parent ends with /
                    else 
                    {
                        completedURL = parent.url.toString() + attributeStr;
                    }
                } 
                //If the relative path doesn't start with / or http
                else 
                {    
                    // If the parent ends in a /                                                                                                   
                    if(parent.url.toString().charAt(parent.url.toString().length() - 1) == '/')
                    {  
                        completedURL = parent.url.toString() + attributeStr;
                    } 
                    // if not, add a /
                    else 
                    {                                                                    
                        completedURL = parent.url.toString() + "/" + attributeStr;
                    }
                }

                URLs.addURL(parent, completedURL);
            }
        }
    }

    // ================================================================================================================
    @Override
    public void handleText(char[] data, int pos)
    {
        pattern = Pattern.compile(regExString);
        testString = String.valueOf(data);
        matcher = pattern.matcher(testString); 

        done = false;
        while (!done)
        {
            if (matcher.find())
            {
                String email = testString.substring(matcher.start(), matcher.end());
                System.out.println("An email was found: " + email);

                parent.emails.addElement(email);
                matcher.region(matcher.end(), testString.length());
            }
            else
                done = true; // no more matches
        }
    }

    // ================================================================================================================

    @Override
    public void handleEndTag(HTML.Tag tag, int pos) { }
}
