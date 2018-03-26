package com.invictus.spectrummanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.invictus.spectrummanager.Models.ReceiveBroadcastMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    private Button advertiseMe;
    private Button requestChannel;
    private ListView messsageList;
    private static TextView tv;
    private static TextView myCh;
    private static Context context;


    private int myChannel;
    private HashMap<String,Integer> deviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        initializeUI();

//        String ip=getClientList().get(0);
        new ReceiveBroadcastMessage().execute();



    }


    public static void showChannelAllocation(String msg){
//        String[] lst=msg.split(" ");
//        String line="";
//        for (String m:lst){
//            line+=m+"\n";
//        }
        tv.setText(msg);



    }


    public static void showToast(String msg){
        Toast.makeText(context, msg,
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initializeUI(){
        advertiseMe = (Button) findViewById(R.id.advertise);
        requestChannel = (Button) findViewById(R.id.request_channel);
        messsageList = (ListView) findViewById(R.id.msg_list);
        tv = findViewById(R.id.showChannels);
        myCh = findViewById(R.id.my_channel);


        advertiseMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag","clicked");
                //new AdverticeMessage().execute();

            }
        });

        requestChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public static int getIPAddress() {
        int ip = 0;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ip = connectionInfo.getIpAddress();
            }
        }
        return ip;
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //print from intent
        }
    }


    private ArrayList<String> getClientList() {
        ArrayList<String> clientList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] clientInfo = line.split(" +");
                String mac = clientInfo[3];
                if (mac.matches("..:..:..:..:..:..")) { // To make sure its not the title
                    clientList.add(clientInfo[0]);
                }
            }
        } catch (java.io.IOException aE) {
            aE.printStackTrace();
            return null;
        }
        return clientList;
    }


    public class AdverticeMessage extends AsyncTask<Context, Void, Void> {

        String msg="";
        protected DatagramSocket socket = null;
        protected byte[] buf = new byte[256];
        InetAddress group;

        @Override
        protected Void doInBackground(Context... contexts) {
            String multicastMessage="Hello";
            try {
                socket = new DatagramSocket();
                group = InetAddress.getByName("230.0.0.0");
                buf = multicastMessage.getBytes();

                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);
                socket.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // MainActivity.showToast(msg);
        }



    }



}
