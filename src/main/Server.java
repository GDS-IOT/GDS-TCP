package com.gds.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Sujith Ramanathan
 *
 */
public class Server {

    public static void main(String []args) {
        System.out.println("Server Started");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10008);
            Socket client =  serverSocket.accept();
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            String fromMsg = fromClient.readLine();
            String toMsg = "";
            while(null != fromMsg && !"".equals(fromMsg)) {
                toMsg = "Message Received :: ".concat(fromMsg);
                toClient.write(toMsg);
                toClient.newLine();
                toClient.flush();
                fromMsg = fromClient.readLine();
            }
        }catch(IOException ie) {
            ie.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}
