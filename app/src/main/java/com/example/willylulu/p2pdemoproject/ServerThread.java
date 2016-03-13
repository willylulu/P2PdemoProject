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
    public ServerThread(MainActivity mainActivity){
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mainActivity = mainActivity;
    }
    public void run(){
        while(true) {
            try {

                Socket socket = serverSocket.accept();
                ConnectThread connectThread = new ConnectThread(socket, mainActivity);
                connectThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
