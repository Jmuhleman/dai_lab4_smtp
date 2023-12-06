# Labo 4 STMP
---

Auteurs: Julien Mühlemann and Cristhian Ronquillo 

---
## 1. Brief description of our project
In this project, We will develop a TCP client application in Java. 
Our client application will use the Socket API to communicate with an SMTP server.

Our mission is to develop a client application that automatically plays e-mail pranks on a list of victims:

* As configuration, the user of your program should provide
  1.  the victims list: a file with a list of e-mail addresses,
  2. the messages list: a file with several e-mail messages (subject and body),
  3. the number of groups n on which the e-mail prank is played. This can be provided e.g., as a command line argument.
* Our program form n groups by selecting 2-5 e-mail addresses from the file for each group. 
The first address of the group is the sender, the others are the receivers (victims).
* For each group, our program select one of the e-mail messages.
* The respective messages are then sent to the different groups using the SMTP protocol.

### Example
Consider that our program generates a group G1. 
The group sender is Bob. The group recipients are Alice, Claire and Peter. 
When the prank is played on group G1, then your program should pick one of the fake messages. 
It should communicate with an SMTP server, so that Alice, Claire and Peter receive an e-mail, which appears to be sent by Bob.

## 2. Instructions for setting up your mock SMTP server.
You can use [MailDev]/(https://github.com/maildev/maildev) as a mock SMTP server for your tests. 

Use docker to start the server:
`docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev`

After that, you can access the web interface of the server at http://localhost:1080.
![mailDev.png](lab4_smtp%2Fsrc%2Fmain%2Fressources%2Ffigures%2FmailDev.png)

## 3. Clear and simple instructions for configuring our tool and running a prank campaign.
For the configuration of our tool, you need to create 2 files, one that contains emails victims and other one which contains
the message to send in the `src/main/resources` folder.

To execute our tool, you need to run the `main` method of the `MischiefMailer` class.
And not forget to add the number of groups, 
the path of the victims file, the path of the messages file , 
the ip address of the server and the port to use in the order as parameters.

For example:  
java -jar lab4_smtp-1.0-SNAPSHOT.jar 3 C:\Users\crist\laboDAI\dai_lab4_smtp\lab4_smtp\src\main\ressources\victims.txt C:\Users\crist\laboDAI\dai_lab4_smtp\lab4_smtp\src\main\ressources\messages.txt localhost 1025
  
* first parameter : 3
* second parameter : C:\Users\crist\laboDAI\dai_lab4_smtp\lab4_smtp\src\main\ressources\victims.txt
* third parameter : C:\Users\crist\laboDAI\dai_lab4_smtp\lab4_smtp\src\main\ressources\messages.txt
* fourth parameter: localhost
* fifth parameter : 1025
## 4. Description of our implementation
![DiagrammeUML.png](lab4_smtp%2Fsrc%2Fmain%2Fressources%2Ffigures%2FDiagrammeUML.png)
We have implemented our program in the following way:
* The `MischiefMailer` class is the main class of our program. It is responsible for calling the different classes which are responsible for the different tasks of our program.
Here's a summary of its functionality:
    1. It reads the configuration file and extracts the different parameters.
    2. It reads the victims file and extracts the different victims.
    3. It reads the messages file and extracts the different messages.
    4. It creates the different groups of victims.
    5. It creates the different pranks.
    6. It sends the different pranks to the SMTP server.

---
  

* The `FileHandler` class is responsible for reading the mail and message files.
  Here's a summary of its functionality:
  1. Constructor and File Reading:
     * The constructor takes three parameters: victimMailsFile (path to the mail data file), messageFile (path to the message data file), and enc (the charset for file encoding).
     * It initializes BufferedReader objects for reading the contents of these files.
     * The content of the message file is read and split into contentMessages array using "$" as a delimiter.
     * Similarly, the content of the mail file is read into contentMails array, also split using "$" as a delimiter.
     * The size of each file (number of lines or entries) is tracked by sizeMessageFile and sizeMailsFile.

  2. Data Retrieval Methods:
     * fetchMail(): Returns a random email address from the contentMails array. It uses a Random object to select a random index.
     * fetchMessage(): Returns a Message object consisting of a head (subject) and a body (message content). It randomly selects an index and adjusts it to ensure the head and body are correctly paired.
     * fetchNMail(int n): Returns an ArrayList of n random email addresses. It repeatedly calls fetchMail() to populate the list.
     * fetchNMessage(int n): Returns an ArrayList of n random messages. It repeatedly calls fetchMessage() to populate the list.
  3. Class Design:
     * The class uses the record keyword to define a simple immutable data carrier, Message, which holds a head and a body part of a message.
     * Private variables are used to manage the state of the object, such as the size of files and the content arrays.

---
  
* The `SMTPManager` class is designed for managing SMTP (Simple Mail Transfer Protocol) operations. Here are the key points of its implementation:

  1. Constructor and Field Initialization:
     * The constructor takes two parameters: ip (the IP address of the SMTP server) and p (the port number).
     * It initializes several string constants used in SMTP communication, including from, rcpt, data, quit, close, endData, and newLine.

  2. sendNMails Method:
     * Purpose: Sends an email message to multiple recipients.
     * Parameters: Takes a Message object (with a head and a body) and an ArrayList<String> of email addresses.
     * Process:
       * Establishes a socket connection to the SMTP server using the provided IP address and port.
       * Creates BufferedReader and BufferedWriter for handling input and output streams with UTF-8 encoding.
       * Follows SMTP protocol steps:
       * Sends an EHLO command to initiate the SMTP session.
       * Sets the sender's email address using the MAIL FROM command.
       * Adds recipient addresses using RCPT TO commands for each address in the list except the first one, which is used as the sender.
       * Sends the DATA command to start the content transmission.
       * Constructs and sends the email headers, including From, To, Date, and Subject. The subject is encoded in Base64 with UTF-8.
       * Sends the email body and ends the data section with endData.
       * Terminates the SMTP session with the QUIT command.

### Example of communication between our program and the SMTP server
![dialogue.png](lab4_smtp%2Fsrc%2Fmain%2Fressources%2Ffigures%2Fdialogue.png)
### Interception of the email sent by our program using MailDev
![interception.png](lab4_smtp%2Fsrc%2Fmain%2Fressources%2Ffigures%2Finterception.png)