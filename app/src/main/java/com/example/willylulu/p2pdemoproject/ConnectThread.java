package com.example.willylulu.p2pdemoproject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by willylulu on 2016/3/6.
 */
public class ConnectThread extends Thread{
    private Socket socket;
    private MainActivity mainActivity;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    public ConnectThread(Socket socket, MainActivity activity) {
        this.socket = socket;
        this.mainActivity = activity;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream());
            this.printWriter.println("Hello! "+this.socket.getInetAddress());
            this.printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try {
                String read = bufferedReader.readLine();
                mainActivity.addLog(read);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendText(String string){
        mainActivity.addLog("Send to:" + socket.getInetAddress());
        printWriter.println(string);
        printWriter.flush();
    }
}
