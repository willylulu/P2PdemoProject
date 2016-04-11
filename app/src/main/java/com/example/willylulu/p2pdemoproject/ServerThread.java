package com.example.willylulu.p2pdemoproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by willylulu on 2016/3/10.
 */
public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private MainActivity mainActivity;
    private ConnectThread connectThread;
    private P2pBroadCast p2pBroadCast;
    public ServerThread(MainActivity mainActivity, P2pBroadCast p2pBroadCast){
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mainActivity = mainActivity;
        this.p2pBroadCast = p2pBroadCast;
    }
    public void run(){
        while(true) {
            try {

                Socket socket = serverSocket.accept();
                connectThread = new ConnectThread(socket, mainActivity);
                p2pBroadCast.setConnectThread(connectThread);
                connectThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
