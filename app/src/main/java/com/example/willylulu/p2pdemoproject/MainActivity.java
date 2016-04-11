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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private P2pBroadCast p2pBroadCast;
    private IntentFilter intentFilter;
    private MainActivity self;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addLog("starting log ver 10.0!");
        self = this;

        Button show = (Button)findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addDevices();
            }
        });

        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                p2pBroadCast.sendText(editText.getText().toString());
            }
        });

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
    }
    public void addLog(final String log){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.Log_text);
                String temp = (String) textView.getText();
                temp += log + "\n";
                textView.setText(temp);
            }
        });
    }
    public void addDevices(){
        this.addLog("addDevices!");
        List<WifiP2pDevice> l = p2pBroadCast.p2pList.getPeers();
        LinearLayout ll = (LinearLayout) findViewById(R.id.devices);
        ll.removeAllViews();
        for(int i=0;i<l.size();i++){
            DeviceButton deviceButton  = new DeviceButton(this,p2pBroadCast,l.get(i));
            ll.addView(deviceButton);
        }
    }
    
    public void change_device_name(final String name)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView devicename= (TextView)findViewById(R.id.device_name);
                devicename.setText(name);
            }
        });
    }
}
