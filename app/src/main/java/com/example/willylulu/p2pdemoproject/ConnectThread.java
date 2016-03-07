package com.example.willylulu.p2pdemoproject;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import java.net.Socket;

/**
 * Created by willylulu on 2016/3/6.
 */
public class ConnectThread extends Thread{
    private Socket socket;
    private MainActivity mainActivity;
    public ConnectThread(Socket socket, MainActivity activity) {
        this.socket = socket;
        this.mainActivity = activity;
    }

    public void run(){
        this.mainActivity.addLog("Start running");
        while(this.socket.isConnected()){

        }
    }
}
