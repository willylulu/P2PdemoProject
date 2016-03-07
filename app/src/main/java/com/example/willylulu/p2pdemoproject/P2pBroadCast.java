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

import java.io.IOException;
import java.net.InetAddress;
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
    private ServerSocket serverSocket;
    private Socket socket;
    public P2pBroadCast(WifiP2pManager manager, Channel channel,
                      final MainActivity activity){
        super();
        this.wifiP2pManager = manager;
        this.channel = channel;
        this.activity = activity;
        this.p2pList = new P2pList(this);
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                activity.addLog("Thread running");
                try {
                    serverSocket = new ServerSocket(8888);
                    socket = serverSocket.accept();
                    activity.addLog("dick");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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
        String action = intent.getAction();
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
                wifiP2pManager.requestPeers(channel,p2pList);
            }
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (wifiP2pManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

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
                            // incoming connections.
                            activity.addLog("BOSS");
                        } else if (info.groupFormed) {
                            // The other device acts as the client. In this case,
                            // you'll want to create a client thread that connects to the group
                            // owner.
                            activity.addLog("Client");
                            InetAddress groupOwnerAddress = info.groupOwnerAddress;
                            String s=groupOwnerAddress.getHostAddress();
                            int port = 8888;
                            activity.addLog(s);
                            socket  = new Socket();
                            try {
                                socket.bind(null);
                                activity.addLog(socket.getLocalAddress().toString());
                                activity.addLog(socket.getInetAddress().toString());
                                //socket.connect(new InetSocketAddress(s, port), 500);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
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

    public void createG() {
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                activity.addLog("Build success!!!");
            }

            @Override
            public void onFailure(int reason) {
                activity.addLog("Build Fail!!!");
            }
        });
    }
}
