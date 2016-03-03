package com.example.willylulu.p2pdemoproject;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willylulu on 2016/3/3.
 */
public class P2pList implements WifiP2pManager.PeerListListener{
    private P2pBroadCast parent;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    public P2pList(P2pBroadCast parent){
        this.parent = parent;
    }
    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        this.peers.clear();
        this.peers.addAll(peers.getDeviceList());
        if(this.peers.size()==0){
            this.parent.exception("Not found peers!");
        }
        else{
            this.parent.getPeersSuccess(peers);
        }
    }
    public List<WifiP2pDevice> getPeers(){
        return peers;
    }
}
