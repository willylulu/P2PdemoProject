package com.example.willylulu.p2pdemoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by willylulu on 2016/3/2.
 */
public class P2pBroadCast extends BroadcastReceiver{
    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private MainActivity activity;
    public P2pList p2pList;
    private P2pBroadCast self;
    private WifiP2pInfo info;
    private ServerSocket serverSocket;
    public String message;
    public P2pBroadCast(WifiP2pManager manager, Channel channel,
                      final MainActivity activity){
        super();
        this.wifiP2pManager = manager;
        this.channel = channel;
        this.activity = activity;
        this.p2pList = new P2pList(this);
        this.self = this;
        this.wifiP2pManager.discoverPeers(this.channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                p2pDetectSuccess();
            }

            @Override
            public void onFailure(int reason) {
                p2pDetectFailure(reason);
            }
        });
    }
    public void SocketListen(){
        JavaTCPServer jsp = new JavaTCPServer();
    }
    private void p2pDetectFailure(int reason) {
        switch (reason){
            case 1: this.activity.addLog("P2P_UNSUPPORTED");
            case 0: this.activity.addLog("P2P_ERROR");
            case 2: this.activity.addLog("P2P_BUSY");
        }
    }

    private void p2pDetectSuccess() {
        this.activity.addLog("Enable DiscoverPeers!");
    }
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        this.activity.addLog("Receive:\n" + action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                this.activity.addLog("able P2P");
            }
            else {
                this.activity.addLog("unable P2P");
            }
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            this.activity.addLog("There are other people!");
            if (wifiP2pManager != null) {
                wifiP2pManager.requestPeers(channel, p2pList);
            }
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (wifiP2pManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // We are connected with the other device, request connection
                // info to find group owner IP

                wifiP2pManager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener(){

                    @Override
                    public void onConnectionInfoAvailable(final WifiP2pInfo info) {

                        // InetAddress from WifiP2pInfo struct.
                        // After the group negotiation, we can determine the group owner.
                        if (info.groupFormed && info.isGroupOwner) {
                            // Do whatever tasks are specific to the group owner.
                            // One common case is creating a server thread and accepting
                            // incoming connections
                            activity.addLog("Boss!");
                        } else if (info.groupFormed) {
                            // The other device acts as the client. In this case,
                            // you'll want to create a client thread that connects to the group
                            // owner.
                            activity.addLog("Client!");
                            self.info = info;
                        }
                    }
                });
            }
        }
    }

    public void SocketInvatation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket Rsocket = new Socket();
                    Rsocket.bind(null);
                    Rsocket.connect(new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), 8888), 500);
                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(Rsocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(Rsocket.getOutputStream());
                    writer.println(new StringBuffer("Dick"));
                    writer.flush();
                    while (Rsocket.isConnected()){
                        message = bufferReader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void exception(String s) {
        this.activity.addLog(s);
    }

    public void getPeersSuccess() {
        this.activity.addLog("Found success!");
    }

    public void p2PConnect(WifiP2pDevice wifiP2pDevice) {
        WifiP2pDevice device;
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                activity.addLog("Connect success!!!");
            }

            @Override
            public void onFailure(int reason) {
                activity.addLog("Connect Fail!!!");
            }
        });
    }
}
