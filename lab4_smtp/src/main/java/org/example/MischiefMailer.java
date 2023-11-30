package org.example;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class MischiefMailer {
    public static void main(String[] args) throws IOException {

        if (args.length < 3){
            System.out.println("Vous devez donner un nombre de groupe !\n");
            System.out.println("Le programme va se terminer\n");
            return;
        }

        FileHandler fh = new FileHandler(args[1], args[2], StandardCharsets.UTF_8);

        Random ran = new Random();
        SMTPManager SMTPm = new SMTPManager("localhost", 1025);

        // pour chaque groupe qui va être formé
        for (int k = 0; k < Integer.parseInt(args[0]); ++k) {
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
