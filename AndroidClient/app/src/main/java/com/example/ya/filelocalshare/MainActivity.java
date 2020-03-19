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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FileExplorer explorer = new FileExplorer("/");
    FilePathShower pathShower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pathShower = new FilePathShower(getApplicationContext(), R.layout.path_view);
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

        ViewFiles("/storage/emulated/0/");
        pathShower.showPath(this, (LinearLayout) findViewById(R.id.pathViewerLayout),explorer,"/storage/emulated/0/");
    }

    Map<String,Integer> GetIcons(){
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("txt", R.drawable.ic_file_icon_txt);
        map.put("avi", R.drawable.ic_file_icon_avi);
        map.put("css", R.drawable.ic_file_icon_css);
        map.put("csv", R.drawable.ic_file_icon_csv);
        map.put("doc", R.drawable.ic_file_icon_doc);
        map.put("docx", R.drawable.ic_file_icon_doc);
        map.put("htm", R.drawable.ic_file_icon_html);
        map.put("html", R.drawable.ic_file_icon_html);
        map.put("js", R.drawable.ic_file_icon_javascript);
        map.put("jpg", R.drawable.ic_file_icon_jpg);
        map.put("jpeg", R.drawable.ic_file_icon_jpg);
        map.put("json", R.drawable.ic_file_icon_json);
        map.put("mp3", R.drawable.ic_file_icon_mp3);
        map.put("mp4", R.drawable.ic_file_icon_mp4);
        map.put("pdf", R.drawable.ic_file_icon_pdf);
        map.put("png", R.drawable.ic_file_icon_png);
        map.put("ppt", R.drawable.ic_file_icon_ppt);
        map.put("pptx", R.drawable.ic_file_icon_ppt);
        map.put("psd", R.drawable.ic_file_icon_psd);
        map.put("svg", R.drawable.ic_file_icon_svg);
        map.put("xls", R.drawable.ic_file_icon_xls);
        map.put("xml", R.drawable.ic_file_icon_xml);
        map.put("zip", R.drawable.ic_file_icon_zip);


        map.put("folder", R.drawable.ic_file_icon_folder);
        map.put("unknown", R.drawable.ic_file_icon_txt);
        return map;
    }

    public void ViewFiles(String path){
        HashSet<String> imExtensions = new HashSet<>();
        imExtensions.add("png");
        imExtensions.add("jpg");
        imExtensions.add("jpeg");
        imExtensions.add("giv");
        explorer.SetCurrentPath(path);
        File[] files = explorer.GetFiles();
        FileViewer fileViewer = new FileViewer(GetIcons(), imExtensions);
        fileViewer.SortFilesByAlphabetAndFolders(files);

        TableLayout table = findViewById(R.id.fileTable);
        table.removeAllViews();
        View[] fileViews = new View[files.length];
        for(int i =0; i< files.length; i++)
            fileViews[i] = fileViewer.GetFileView(this, files[i]);
        int rows = 4;
        int inRowCount = 5;
        TableRow row;
        for(int r = 0; r<Math.ceil((float)files.length/inRowCount); r++){
            row = new TableRow(this);
            for(int i = 0; i<inRowCount&&r*inRowCount+i<fileViews.length; i++){
                row.addView(fileViews[r*inRowCount+i]);
            }
            row.setGravity(Gravity.START);
            table.addView(row);
        }
        fileViewer.ShowImageThumbnails();
    }
    public void ViewPath(String path){
        pathShower.showPath(this, (LinearLayout) findViewById(R.id.pathViewerLayout),explorer,path);
    }
}
