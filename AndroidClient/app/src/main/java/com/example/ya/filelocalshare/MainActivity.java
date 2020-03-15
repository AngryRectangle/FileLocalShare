package com.example.ya.filelocalshare;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.ipText);
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        text.setText( Formatter.formatIpAddress(ip));
        Button button = findViewById(R.id.connectButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText ipField = view.getRootView().findViewById(R.id.ipField);

                    class AsyncRequest extends AsyncTask<String, Void, Socket> {

                        @Override
                        protected Socket doInBackground(String... arg) {
                            try {
                                Socket s = new Socket(InetAddress.getByName(arg[0]), 5000);
                                return s;
                            } catch (Exception e) {
                                return  null;
                            }
                        }
                    }
                    new AsyncRequest().execute(ipField.getText().toString());
                }catch (Exception e){
                    Log.e("Tag",e.toString());
                }
            }
        });
    }
}
