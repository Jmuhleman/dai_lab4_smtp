package org.example;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class MischiefMailer {
    enum Parameters {NB_GROUPS, MAILS_FILE, MESSAGES_FILE, IP_ADDRESS, PORT}
    public static void main(String[] args) throws IOException {

        if (args.length < 5){
            System.out.println("You have to give 5 arguments!");
            System.out.println("1. You must give a group number!");
            System.out.println("2. You must give a mail file!");
            System.out.println("3. You must give a message file!");
            System.out.println("4. You must give an IP address!");
            System.out.println("5. You must give a port!");
            System.out.println("The program will finish now.");
            return;
        }

        FileHandler fh = new FileHandler(args[Parameters.MAILS_FILE.ordinal()], args[Parameters.MESSAGES_FILE.ordinal()], StandardCharsets.UTF_8);

        Random ran = new Random();
        SMTPManager SMTPm = new SMTPManager(args[Parameters.IP_ADDRESS.ordinal()], Integer.parseInt(args[Parameters.PORT.ordinal()]));


        // pour chaque groupe qui va être formé
        for (int k = 0; k < Integer.parseInt(args[Parameters.NB_GROUPS.ordinal()]); ++k) {
            //tirer un nombre aléatoire [2-5] ->x
            int nVictims = ran.nextInt(2, 6);

            //Obtenir x mails
            ArrayList<String> listVic = fh.fetchNMail(nVictims);

            //obtenir 1 message (head + body)
            Message msg = fh.fetchMessage();

            //Envoyer en une fois x-1 mail avec idx:0 comme expéditeur au x-1 receveurs
            SMTPm.sendNMails(msg, listVic);
        }
    }
}
