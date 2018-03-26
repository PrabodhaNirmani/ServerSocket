package com.invictus.spectrummanager.Models;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.invictus.spectrummanager.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by acer on 3/25/2018.
 */

public class ReceiveBroadcastMessage extends AsyncTask<Void, Void, Void> {

    String msg="";
    @Override
    protected Void doInBackground(Void... voids) {


        int PORT=7800;

       // String IP=voids[0];



        Socket socket=null;
        ServerSocket ss=null;
        InputStreamReader isr;
        BufferedReader br;
        System.out.println(" Trying to connecting server");
        try
        {


            InetAddress ip = InetAddress.getByName ("192.168.43.1");
            socket = new Socket(ip,PORT);
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
            msg = br.readLine();
            Log.d("Tag", msg);


        }
        catch(SocketException socketexception)
        {
            socketexception.printStackTrace();
            System.out.println("Socket Exception");
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
            System.out.println("Error ocuure : io exception");

        }
// closing the socket
        finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


//    @Override
//    protected Void doInBackground(Context... strings) {
//
//        // join a Multicast group and send the group salutations
//
//        String msg = "Hello";
//        InetAddress group = null;
//
//        Context context = strings[0];
//
//
//        try {
//
//            /* Acquire MultiCast Lock */
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiManager.MulticastLock multicastLock = wifi.createMulticastLock("multicastLock");
//            multicastLock.setReferenceCounted(true);
//            multicastLock.acquire();
//
//
//            group = InetAddress.getByName("228.5.6.7");
//            MulticastSocket s = new MulticastSocket(6789);
//
//
//                /* Set NetworkInterface of MultiCast Socket */
//            NetworkInterface wifiNetworkInterface = findWifiNetworkInterface();
//            if (wifiNetworkInterface != null) s.setNetworkInterface(wifiNetworkInterface);
//
//
//
//            s.joinGroup(group);
//            DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
//                    group, 6789);
//            s.send(hi);
//            // get their responses!
//            byte[] buf = new byte[1000];
//            DatagramPacket recv = new DatagramPacket(buf, buf.length);
//            s.receive(recv);
//            Log.d("tag",recv.toString());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }


    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    //@Override
//    protected Void doInBackground(Context... contexts) {
//        try {
//            socket = new MulticastSocket(4446);
//            InetAddress group = InetAddress.getByName("230.0.0.0");
//            socket.joinGroup(group);
//            while (true) {
//                DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                socket.receive(packet);
//                String received = new String(
//                        packet.getData(), 0, packet.getLength());
//                if ("end".equals(received)) {
//                    break;
//                }
//            }
//            socket.leaveGroup(group);
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.showChannelAllocation(msg);
        MainActivity.showToast(msg);
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
