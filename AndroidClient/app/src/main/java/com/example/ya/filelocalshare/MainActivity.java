package com.example.ya.filelocalshare;

import androidx.activity.OnBackPressedCallback;
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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ya.filelocalshare.sort.FileSorter;

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

    FileExplorer explorer;
    AndroidBrowser androidBrowser;
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
        ImageButton searchButton= findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explorer.search(((EditText)findViewById(R.id.searchText)).getText().toString());
            }
        });

        EditText searchField = findViewById(R.id.searchText);
        createExplorer(getString(R.string.default_path));
        searchField.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                explorer.search(
                                        ((EditText)findViewById(R.id.searchText)).getText().toString()
                                );
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );
        ((ImageButton)findViewById(R.id.popupMenuButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.popupMenuButton));
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.selectSortButton){
                            PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.popupMenuButton));
                            popup.getMenuInflater().inflate(R.menu.sort_popup_menu, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                                public boolean onMenuItemClick(MenuItem item) {
                                    if(item.getItemId()==R.id.sortByName){
                                        explorer.setSortType(FileSorter.SortType.BY_NAME);
                                    }
                                    if(item.getItemId()==R.id.sortByDate){
                                        explorer.setSortType(FileSorter.SortType.BY_DATE);
                                    }
                                    if(item.getItemId()==R.id.sortBySize){
                                        explorer.setSortType(FileSorter.SortType.BY_SIZE);
                                    }
                                    return true;
                                }
                            });
                        }
                        return true;
                    }
                });
            }
        });
        explorer.openDirectory(getString(R.string.default_path));
    }

    Map<String,Integer> getIcons(){
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
        map.put("jpe", R.drawable.ic_file_icon_jpg);
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
    public void createExplorer(String path){
        HashSet<String> thumbnailsExtensions = new HashSet<>();
        thumbnailsExtensions.add("png");
        thumbnailsExtensions.add("jpg");
        thumbnailsExtensions.add("jpeg");
        thumbnailsExtensions.add("jpe");
        thumbnailsExtensions.add("gif");
        thumbnailsExtensions.add("mp4");
        thumbnailsExtensions.add("avi");
        explorer = new FileExplorer();
        androidBrowser = new AndroidBrowser(
                this,
                (TableLayout) findViewById(R.id.fileTable),
                (LinearLayout)findViewById(R.id.pathViewerLayout),
                explorer,
                getIcons(),thumbnailsExtensions,
                new FileViewer.FileViewOptions(5)
        );

    }
}
