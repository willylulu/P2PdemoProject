package com.example.willylulu.p2pdemoproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by willylulu on 2016/3/10.
 */
public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private MainActivity mainActivity;
    public ServerThread(MainActivity mainActivity){
        try {
            serverSocket = new ServerSocket(8888);
            this.mainActivity = mainActivity;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        try {
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("Welcome!");
            printWriter.flush();
            while (socket.isConnected()){
                String s = bufferedReader.readLine();
                printWriter.println(s);
                printWriter.flush();
                mainActivity.addLog(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
