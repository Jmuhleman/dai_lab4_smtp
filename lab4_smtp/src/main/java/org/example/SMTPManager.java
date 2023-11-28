package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SMTPManager {
    private String ipAdress;
    private int port;
    private String from;
    private String rcpt;
    private String data;
    private String quit;
    private String close;
    private String endData;
    private String newLine;
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
            for (String line = "-" ; line.contains("-") ; line = in.readLine()){

            }

            out.write(from + listeMails.get(0) + close + newLine);
            out.flush();
            System.out.println(in.readLine());

            out.write(rcpt + listeMails.get(1) + close + newLine);
            out.flush();
            System.out.println(in.readLine());

            out.write(data + newLine);
            out.flush();

            out.write(data + message.head() + message.body() + endData);
            out.flush();
            System.out.println(in.readLine());



        }catch (IOException e){

        }


    }
}