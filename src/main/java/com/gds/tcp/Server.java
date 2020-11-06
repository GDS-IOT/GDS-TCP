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

    public static void main(String []args) throws IOException, InterruptedException {
        while(true) {
            System.out.println("Server Started");
            ServerSocket serverSocket = null;
            Socket client = null;
            try {
                serverSocket = new ServerSocket(1045);
                client = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                String fromMsg = fromClient.readLine();
                String toMsg = "";
                while (true) {
                    if (null == fromMsg)
                        fromMsg = "null";
                    toMsg = "Message Received :: ".concat(fromMsg);
                    toClient.write(toMsg);
                    toClient.newLine();
                    toClient.flush();
                    fromMsg = fromClient.readLine();
                    if(null == fromMsg){
                        System.out.println("Message is null");
                        client.close();
                        serverSocket.close();
                        break;
                    }
                }
            } catch (IOException ie) {
                ie.printStackTrace();
                if (null != client) {
                    client.close();
                    serverSocket.close();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                if (null != client) {
                    client.close();
                    serverSocket.close();
                }
                Thread.sleep(1000);
            }
        }
    }
}
