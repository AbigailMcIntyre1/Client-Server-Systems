// Abigail McIntyre
// Project 2b - web crawler
// Due 02/14/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the main function of the program
// ---------------------------------------------------------------------------------------------------------------------------

// How the program works:
// - Creates the frame.
// - The user types in a URL and presses the go button.
// - The text is taken from the text field and a scraper is created, where the text is then added as the first URL.
// - The scraper creates a DefaultListModel, which holds the URLCrawlerInfo variables that store the information about the links and emails.
// - It then calls a method, in the constructor, to harvest all the information in the given links. This method will run while all the sites
//   in the list haven't been marked as searched yet. A handler is created for each new website that is visited, and the data is parsed 
//   using that. Emails in text and the anchor tags are extracted and added to a vector inside the URLCrawlerInfo variables, and links
//   are also extracted and added as new URLCrawlerInfo variables. When a site has been parsed, it's then marked as visited using a boolean.
//   Before each site is added, it checks to see if it's already in the list. If it isn't, then it's added along with the distance of the 
//   parent + 1. If it has been visited, then it moves on and doesn't do anything with it.
// - When all the URLs in the list have been visited and the information harvested (or the timer has run out), then the links and emails
//   will be displayed in a JList in the frame in alphabetical order. 


public class CrawlerMain 
{
    public static void main(String[] args) 
    {
        CrawlerFrame myFrame = new CrawlerFrame();
    }
}