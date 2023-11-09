package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SmtpClient {

    public static void main(String args[]) throws IOException {

        try (Socket socket = new Socket("localhost", 1025);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))){

            out.write("EHLO localhost\n");
            out.flush();

            StringBuilder textResponse = new StringBuilder();
            String response = "";
            int index = -1;

            while (index == -1) {
                index = response.indexOf(" ");
                response = in.readLine();
                textResponse.append(response).append("\n");
            }

            System.out.println(textResponse);
            }
            catch (IOException e){

            }




        }






}
