package org.example;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SMTPManager {

    public void sendNMails(ArrayList<Message> listeMessages, ArrayList<String> listeMails) {


        try (Socket socket = new Socket("localhost", 1025);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            System.out.println(in.readLine());
            out.write("EHLO localhost\n");
            out.flush();


            String[] msgToBeSent = new String[4];

            msgToBeSent[0] = "MAIL FROM: <shakira@music.com>";
            msgToBeSent[1] = "RCPT TO: <shakira@music.com>";
            msgToBeSent[2] = "DATA Bonjour, petite blague cynique\r\n.\r\n";
            msgToBeSent[3] = "QUIT";


            StringBuilder textResponse = new StringBuilder();
            String response;

            int idx = 0;
            response = in.readLine();

            System.out.println(response);

            while (idx != -1) {

                response = in.readLine();
                idx = response.indexOf('-');
                textResponse.append(response).append("\n");
            }
            System.out.println(textResponse);

            for (int k = 0; k < 4; ++k) {
                out.write(msgToBeSent[k] + "\n");
                out.flush();
                response = in.readLine();
                System.out.println(response);

            }


        }catch (IOException e){

        }


    }
}