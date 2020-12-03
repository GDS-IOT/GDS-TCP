package com.gds.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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

    public static void viaString() {
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

    public static void viaByte() {
        System.out.println("Server Started - Bytes");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10008);
            Socket client =  serverSocket.accept();
            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

            byte []clientData = null;
            byte []toMsg = new byte[2];
            toMsg[0] = 2; toMsg[1] = 1;

            while(true) {
                try {
                    clientData = new byte[fromClient.readInt()];
                    fromClient.readFully(clientData);
                    for(int i=0; i < clientData.length; i++) {
                        System.out.print((int)clientData[i]+" ");
                    }
                    System.out.print("\nSuccessfully Received");
                    toClient.writeInt(2);
                    toClient.write(toMsg);
                }catch(EOFException e) {
                    System.out.println("\nReinitializing");
                    client =  serverSocket.accept();
                    fromClient = new DataInputStream(client.getInputStream());
                    toClient = new DataOutputStream(client.getOutputStream());
                    System.out.println("Reinitialized");
                }
            }
        }catch(IOException ie) {
            ie.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String []args) {
        viaByte();
    }

}

