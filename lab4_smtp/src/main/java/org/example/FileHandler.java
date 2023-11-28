package org.example;

import java.util.Random;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

record Message(String head, String body) {
}


public class FileHandler {

    private int sizeMessageFile;
    private int sizeMailsFile;
    private BufferedReader fileMails;
    private BufferedReader fileMessages;
    private String[] contentMails;
    private String[] contentMessages;

    public FileHandler(String victimMailsFile, String messageFile, Charset enc) throws IOException {
        try {
            fileMails = new BufferedReader(new InputStreamReader(new FileInputStream(victimMailsFile), enc));
            fileMessages = new BufferedReader(new InputStreamReader(new FileInputStream(messageFile), enc));
            //charger le contenu du fichier de messages
            StringBuilder text = new StringBuilder();
            for (String line; (line = fileMessages.readLine()) != null; ) {
                text.append(line);
                ++sizeMessageFile;
            }
            //contentMails = new String[fileSize + 1];
            contentMessages = text.toString().split(",");
            //charger le contenu du fichier des mails
            StringBuilder text2 = new StringBuilder();
            for (String line; (line = fileMails.readLine()) != null; ) {
                text2.append(line);
                ++sizeMailsFile;
            }
            contentMails = text2.toString().split(",");
        } catch (IOException e) {
            System.out.println("Error opening file: ->" + e.getMessage());
        } finally {
            //TODO ajuster ce bordel
            assert fileMails != null;
            fileMails.close();
            assert fileMessages != null;
            fileMessages.close();
        }
    }

    public String fetchMail() {
        Random rand = new Random();
        int nth = rand.nextInt(sizeMailsFile);
        return contentMails[nth];
    }

    public Message fetchMessage() {
        Random rand = new Random();
        //tirage de l'index au sort.
        int headIdx = rand.nextInt(sizeMessageFile);
        //Si l'index de l'objet du message est impair alors on est sur le corps du message -> reculer
        //et affecter son body.
        if (headIdx % 2 != 0) {
            --headIdx;
        }
        int bodyIdx = headIdx + 1;
        return new Message(contentMessages[headIdx], contentMessages[bodyIdx]);
    }

    public ArrayList<String> fetchNMail(int n) {
        ArrayList<String> res = new ArrayList<>();

        for (int k = 0; k < n; ++k) {
            res.add(fetchMail());
        }
        return res;
    }


}
