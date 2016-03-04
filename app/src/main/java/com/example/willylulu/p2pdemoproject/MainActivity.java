package com.example.willylulu.p2pdemoproject;

import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private P2pBroadCast p2pBroadCast;
    private IntentFilter intentFilter;
    private MainActivity self;
    private List<String> BS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addLog("starting log v10");
        self = this;
        Button reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView)findViewById(R.id.Log_text);
                textView.setText("Reset\n");
                unregisterReceiver(p2pBroadCast);
                wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                channel = wifiP2pManager.initialize(self, getMainLooper(), null);
                p2pBroadCast = new P2pBroadCast(wifiP2pManager,channel,self);
                registerReceiver(p2pBroadCast, intentFilter);
            }
        });
        BS = new ArrayList<>();

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        p2pBroadCast = new P2pBroadCast(wifiP2pManager,channel,this);
        addLog("Object finished");

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        addLog("intent filter finished");
    }
    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(p2pBroadCast, intentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(p2pBroadCast);
    }
    public void addLog(String log){
        TextView textView = (TextView)findViewById(R.id.Log_text);
        String temp = (String) textView.getText();
        temp+=log+"\n";
        textView.setText(temp);
    }

    public void addDevices() {
        addLog("Add Buttons!");
        List<WifiP2pDevice> peers = this.p2pBroadCast.getPeers();
        LinearLayout l = (LinearLayout)findViewById(R.id.devices);
        l.removeAllViews();
        for (int i=0;i<peers.size();i++){
            WifiP2pDevice wifiP2pDevice = peers.get(i);
            DeviceButton deviceButton = new DeviceButton(this,this.p2pBroadCast,wifiP2pDevice);
            l.addView(deviceButton);
        }
    }

    public void addButton(String text) {
        LinearLayout l = (LinearLayout)findViewById(R.id.Buttons);
        if(text.equals("Show") && BS.indexOf(text)==-1){
            Button button = new Button(this);
            button.setText(text);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDevices();
                }
            });
            l.addView(button);
            BS.add(text);
        }
    }
}
