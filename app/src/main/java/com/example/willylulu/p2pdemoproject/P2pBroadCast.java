package com.example.willylulu.p2pdemoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

/**
 * Created by willylulu on 2016/3/2.
 */
public class P2pBroadCast extends BroadcastReceiver{
    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private MainActivity activity;
    private P2pList p2pList;
    public P2pBroadCast(WifiP2pManager manager, Channel channel,
                      MainActivity activity){
        super();
        this.wifiP2pManager = manager;
        this.channel = channel;
        this.activity = activity;
        this.p2pList = new P2pList(this);
        this.wifiP2pManager.discoverPeers(this.channel,new WifiP2pManager.ActionListener(){

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
        this.activity.addLog("Receive:\n"+action);
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
    }

    public void exception(String s) {
        this.activity.addLog(s);
    }

    public void getPeersSuccess(WifiP2pDeviceList peers) {
        if(p2pList.getPeers().size()>0){
            this.activity.addLog("List of device: ");
            for(int i=0;i<p2pList.getPeers().size();i++){
                this.activity.addLog(p2pList.getPeers().get(i).deviceName);
            }
        }
    }
}
