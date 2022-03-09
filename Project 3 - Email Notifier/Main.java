// Abigail McIntyre
// Project 3 - Email Notifier
// Due 02/25/2022

// ---------------------------------------------------------------------------------------------------------------------------
// the main function of the program
// ---------------------------------------------------------------------------------------------------------------------------

// What the program does:
// Upon starting the program, a custom JDialogBox will come up and have the user enter the IMAP server name,
// their username, password, the time between checking for new emails (in minutes), and whether or not a sound 
// should play when there are new emails. It'll save these settings to a file and will remember previously entered
// settings. The cancel button will close the program. Once the user presses save, it'll check for emails once immediately
// and will show how many new messages exist since the last time some IMAP client opened the folder. The application will 
// then collapse to a system tray icon. In the background it should check periodically for new mail. If new mail is found, 
// a JOption pane should announce that, also showing the subject and sender of the most recent message, and optionally a sound 
// should be played.

public class Main 
{
    public static void main(String[] args) 
    {
        EmailNotifier emailNotifier = new EmailNotifier();
    }
}
