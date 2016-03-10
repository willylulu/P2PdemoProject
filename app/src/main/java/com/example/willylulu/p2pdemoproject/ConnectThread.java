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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try {
                String read = bufferedReader.readLine();
                mainActivity.addLog(read);
                if(read.equals("Welcome!")){
                    mainActivity.OpenInput();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendText(String string){
        printWriter.println(string);
        printWriter.flush();
    }
}
