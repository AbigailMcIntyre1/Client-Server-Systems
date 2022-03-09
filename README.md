# Client-Server Systems
<b>Project 2b:</b><br>
<br>
  A Web-Crawler that lets the user enter a URL and then it goes through all the links and emails on it and stores them in a list. It then goes through all of those links and does   the same thing until it either hits a radius of 2 or the timer for the program runs out.
<br>
<b>Project 3: Email Notifier</b><br>
<br>
   Upon starting the program, a custom JDialogBox will come up and have the user enter the IMAP server name,
   their username, password, the time between checking for new emails (in minutes), and whether or not a sound 
   should play when there are new emails. It'll save these settings to a file and will remember previously entered
   settings. The cancel button will close the program. Once the user presses save, it'll check for emails once immediately
   and will show how many new messages exist since the last time some IMAP client opened the folder. The application will 
   then collapse to a system tray icon. In the background it should check periodically for new mail. If new mail is found, 
   a JOption pane should announce that, also showing the subject and sender of the most recent message, and optionally a sound 
   should be played.
