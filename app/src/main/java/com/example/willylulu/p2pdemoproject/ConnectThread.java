package com.example.willylulu.p2pdemoproject;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

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
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
