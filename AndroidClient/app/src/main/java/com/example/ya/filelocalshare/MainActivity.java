package com.example.ya.filelocalshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
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
                    AsyncTask<String, Void, Socket> s = new AsyncRequest().execute(ipField.getText().toString());
                    Socket socket = s.get();
                    FileTransmitter fileTransmitter = new FileTransmitter(new DataOutputStream(socket.getOutputStream()));

                    if (Build.VERSION.SDK_INT>=23&&ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"},10);
                    }


                    File file = new File("/storage/emulated/0/test.jpg");
                    byte[] bArray = new byte[(int) file.length()];
                    try{
                        FileInputStream fis = new FileInputStream(file);
                        fis.read(bArray);
                        fis.close();

                    }catch(IOException ioExp){
                        ioExp.printStackTrace();
                    }
                    fileTransmitter.PrepareBytes(bArray);
                    Thread t = fileTransmitter.SendMessage();
                    t.start();
                }catch (Exception e){
                    Log.e("Tag",e.toString());
                }
            }
        });
    }
}
