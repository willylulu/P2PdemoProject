package com.example.willylulu.p2pdemoproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;

/**
 * Created by willylulu on 2016/3/10.
 */
public class JavaTCPServer {

    private ServerSocket serverSocket;
    private Thread serverThread = null;

    public JavaTCPServer() {
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
    }

    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(8888);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket = serverSocket.accept();
                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;
        private BufferedReader input;
        private String messageSend;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.setInput(new BufferedReader(new InputStreamReader(
                        this.clientSocket.getInputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    messageSend = input.readLine();

                    PrintWriter printWriter = new PrintWriter(
                            clientSocket.getOutputStream(), true);

                    printWriter.println(messageSend);
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

                /*
                 * try { String read = input.readLine();
                 * updateConversationHandler.post(new updateUIThread(read)); }
                 * catch (IOException e) { e.printStackTrace(); }
                 */
        public void setInput(BufferedReader input) {
            this.input = input;
        }
    }

}