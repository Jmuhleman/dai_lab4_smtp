package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class SMTPManager {
    private final String ipAdress;
    private final int port;
    private final String from;
    private final String rcpt;
    private final String data;
    private final String quit;
    private final String close;
    private final String endData;
    private final String newLine;
    SMTPManager(String ip, int p){
        ipAdress = ip;
        port = p;
        from = "MAIL FROM:<";
        rcpt = "RCPT TO:<";
        data = "DATA";
        quit = "QUIT";
        close = ">";
        endData = "\r\n.\r\n";
        newLine = "\n";
    }

    public void sendNMails(Message message, ArrayList<String> listeMails) {


        try (Socket socket = new Socket(ipAdress, port);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            System.out.println(in.readLine());

            out.write("EHLO localhost" + newLine);
            out.flush();

            System.out.println(in.readLine());
            for (String line = "-" ; line.contains("-") ; line = in.readLine()){}

            out.write(from + listeMails.get(0) + close + newLine);
            out.flush();
            System.out.println(in.readLine());
            String dst = "";
            for (int k = 0 ; k < listeMails.size() - 1 ; ++k) {
                out.write(rcpt + listeMails.get(k + 1) + close + newLine);
                out.flush();
                dst = String.join(", ", listeMails);
            }
            System.out.println(in.readLine());

            out.write(data + newLine);
            out.flush();
            System.out.println(in.readLine());

            out.write("From: " + listeMails.get(0)  + newLine);
            out.flush();

            out.write("To: " + dst + newLine);
            out.flush();

            out.write("Date: " + " Tue, 18 Nov 2014 15:57:11 +0000"  + newLine);
            out.flush();

            out.write("Content-Type: text/plain; charset=utf-8" +  newLine);
            out.write("Subject: " + "=?utf-8?B?" + Base64.getEncoder().encodeToString(message.head().getBytes()) + "?=" + newLine + newLine);

            out.flush();
            out.write( message.body() + endData);
            out.flush();
            System.out.println(in.readLine());

            out.write(quit + newLine);
            out.flush();

        }catch (IOException e){
            System.out.println("erreur lors de la connection avec ton SMTP mon coco\n");
        }


    }
}