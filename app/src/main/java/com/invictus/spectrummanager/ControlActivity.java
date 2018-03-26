package com.invictus.spectrummanager;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class ControlActivity extends AppCompatActivity {


    private Button assignChannel;
    private ListView messsageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        initializeUI();
    }


    private void initializeUI(){
        assignChannel = (Button) findViewById(R.id.assign_channels);
        messsageList = (ListView) findViewById(R.id.msg_list);

        assignChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  getClientList();
                sendMessages();


            }
        });



    }


    private void sendMessages(){
        new RetrieveFeedTask().execute();
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
                    Log.d("Ip", clientInfo[0]);
                }
            }
        } catch (java.io.IOException aE) {
            aE.printStackTrace();
            return null;
        }
        return clientList;
    }



    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {


        PrintWriter pw;




        private DatagramSocket socket;
        private InetAddress group;
        private byte[] buf;


//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            String multicastMessage="Hello from server";
//            try {
//                socket = new DatagramSocket();
//                group = InetAddress.getByName("230.0.0.0");
//                buf = multicastMessage.getBytes();
//
//                DatagramPacket packet
//                        = new DatagramPacket(buf, buf.length, group, 4446);
//                socket.send(packet);
//                socket.close();
//            } catch (SocketException e) {
//                e.printStackTrace();
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }

        //        @Override
//        protected Void doInBackground(Void... voids) {
//            // join a Multicast group and send the group salutations
//
//            String msg = "Hello from server";
//            InetAddress group = null;
//            try {
//
//                 /* Acquire MultiCast Lock */
//                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                WifiManager.MulticastLock multicastLock = wifi.createMulticastLock("multicastLock");
//                multicastLock.setReferenceCounted(true);
//                multicastLock.acquire();
//
//                group = InetAddress.getByName("228.5.6.7");
//                MulticastSocket s = new MulticastSocket(6789);
//
//
//                /* Set NetworkInterface of MultiCast Socket */
//                NetworkInterface wifiNetworkInterface = findWifiNetworkInterface();
//                if (wifiNetworkInterface != null) s.setNetworkInterface(wifiNetworkInterface);
//
//                s.joinGroup(group);
//                DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
//                        group, 6789);
//                s.send(hi);
//                // get their responses!
//                byte[] buf = new byte[1000];
//                DatagramPacket recv = new DatagramPacket(buf, buf.length);
//                s.receive(recv);
//                Log.d("t",recv.toString());
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//

        @Override
        protected Void doInBackground(Void... voids) {
            int PORT = 7800;

            //Server Socket declaration
            ServerSocket serversocket = null;
            Socket socket = null;
            System.out.println(" Waiting !! ");

            try
            {
                //  Initialising the ServerSocket with input as port number
                serversocket =  new ServerSocket(PORT);

                try
                {

                    // makes a socket connection for clients
                    // accept method waiting for connection(listening mode)
                    socket = serversocket.accept();


                    pw = new PrintWriter(socket.getOutputStream());
                    String message=generateString();
                    pw.write(message);
                    pw.flush();
                    pw.close();
                    // Close the Socket connection
                    socket.close();
                }
                catch(SocketException socketexception)
                {
                    System.out.println("Server problem  "+socketexception.getMessage());
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                    System.out.println("Something wrong error occure" + exception.getMessage()) ;
                }

                //get address of socket
                System.out.println(" Connection from :  " + (socket != null ? socket.getInetAddress() : null));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * Finds Network Interface of Wifi Ethernet.
         *
         * @return
         */
        public NetworkInterface findWifiNetworkInterface() {

            Enumeration<NetworkInterface> enumeration = null;

            try {
                enumeration = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {
                e.printStackTrace();
            }

            NetworkInterface wlan0 = null;

            while (enumeration.hasMoreElements()) {

                wlan0 = enumeration.nextElement();

                if (wlan0.getName().equals("wlan0")) {
                    return wlan0;
                }
            }

            return null;
        }
    }


    public String generateString(){
        ArrayList<String> clients=getClientList();

//        Log.d("Tag0")
        String msg="";
        int chNo=1;
        for(String ip:clients){
            msg+=ip+":"+chNo+",";
            HomeActivity.deviceList.put(ip,chNo);
            chNo+=1;

        }

        return msg;
    }






}
