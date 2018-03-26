package com.invictus.spectrummanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    private Button centralUser;
    private Button otherUser;


    private int myChannel;
    static HashMap<String,Integer> deviceList;

    //broadcast receiver to catch channel no list
    //set local list and my channel number


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeUI();
    }


    private void initializeUI(){
        centralUser = (Button) findViewById(R.id.central_user);
        otherUser = (Button) findViewById(R.id.other_user);

        deviceList = new HashMap<>();
        centralUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ControlActivity.class);
                startActivity(myIntent);
            }
        });

        otherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(myIntent);

            }
        });



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



}
