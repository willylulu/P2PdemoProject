package com.example.willylulu.p2pdemoproject;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.View;
import android.widget.Button;

/**
 * Created by willylulu on 2016/3/4.
 */
public class DeviceButton extends Button{
    private WifiP2pDevice wifiP2pDevice;
    private P2pBroadCast p2pBroadCast;
    public DeviceButton(final MainActivity context, final P2pBroadCast p2pBroadCast, final WifiP2pDevice wifiP2pDevice) {
        super(context);
        this.wifiP2pDevice = wifiP2pDevice;
        this.p2pBroadCast = p2pBroadCast;
        setText(this.wifiP2pDevice.deviceName);
        setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                context.addLog("Connecting " + wifiP2pDevice.deviceName);
                p2pBroadCast.p2PConnect(wifiP2pDevice);
            }
        });
    }
}
