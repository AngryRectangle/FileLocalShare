package com.example.ya.filelocalshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.ya.filelocalshare.sort.FileSorter;
import com.radioactiv_gear_project.core.NetworkInteraction;
import com.radioactiv_gear_project.core.SocketWrapper;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    FileExplorer explorer;
    AndroidBrowser androidBrowser;
    SocketWrapper socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT>=23&& ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.INTERNET"},11);
        }
            final Button button = findViewById(R.id.connectButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ViewGroup)button.getParent()).removeView(button);
                    try {
                    NetworkInteraction.multicast(NetworkInteraction.getVersionByteArray(), NetworkInteraction.DEFAULT_PC_GROUP);
                        DatagramPacket[] packet = NetworkInteraction.receivePackets(NetworkInteraction.DEFAULT_ANDROID_GROUP);
                        ChooseConnectionFragment fragment = ChooseConnectionFragment.newInstance(packet);
                        ((ViewGroup)findViewById(R.id.topFrame)).addView(fragment.onCreateView(getLayoutInflater(), null, null));
                    }catch (Exception e) {
                        Log.e("ERR", e.toString());
                    }
                }
            });

                setOnClickActions();
        explorer.openDirectory(getString(R.string.default_path));
    }

    public void startListeningProgress(){
        Thread receiver = new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    while (true){
                        if (socket.receiveCode() == SocketWrapper.InteractionType.PROGRESS_SENDING)
                            Log.d("PROGRESS", socket.receiveProgress() + "");
                    /*if(wrapper.receiveCode() == SocketWrapper.InteractionType.SUCCESSFUL_SENDING)
                        break;*/
                    }
                }catch (Exception e){
                    Log.e("ERROR", e.toString());
                }
            }
        });
        receiver.start();
    }
    void connect(InetAddress address)throws IOException {
        socket = new SocketWrapper(NetworkInteraction.connect(address));
    }
    public AsyncFileSending fileSender;
    public void sendFile(File file){
            if ((fileSender==null||fileSender.getStatus()== AsyncTask.Status.FINISHED)&&socket != null) {
                fileSender = new AsyncFileSending();
                fileSender.execute(file);
                /*try {
                    socket.sendData(file);
                } catch (IOException e) {
                    Log.e("ERR", e.toString());
                }*/
            }
    }
    private class AsyncFileSending extends AsyncTask<File, String, Void> {
        @Override
        protected Void doInBackground(File... files) {
            try {
                socket.sendData(files[0]);
            } catch (IOException e) {
               publishProgress(e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.e("ERR", values[0]);
        }
    }
    private Map<String, Integer> getIcons() {
        Map<String, Integer> map = new HashMap<String, Integer>();
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

    private HashSet<String> getMediaExtensions() {
        HashSet<String> output = new HashSet<>();
        output.add("png");
        output.add("jpg");
        output.add("jpeg");
        output.add("jpe");
        output.add("gif");
        output.add("mp4");
        output.add("avi");
        output.add("pdf");
        return output;
    }

    private void createExplorer() {
        explorer = new FileExplorer();
        androidBrowser = new AndroidBrowser(
                this,
                (TableLayout) findViewById(R.id.fileTable),
                (LinearLayout) findViewById(R.id.pathViewerLayout),
                explorer,
                getIcons(), getMediaExtensions(),
                new FileViewer.FileViewOptions(5)
        );
    }
    private static boolean isTextEditDone(int actionId, KeyEvent event){
        return (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event != null &&
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }
    private PopupMenu.OnMenuItemClickListener menuItemClickListener(final ImageButton button){
        return new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.selectOrderButton) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, button);
                    popup.getMenuInflater().inflate(R.menu.order_popup_menu, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(sortMenuClickListener());
                }
                return true;
            }
        };
    }
    private PopupMenu.OnMenuItemClickListener sortMenuClickListener(){
        return new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sortByName: {
                        explorer.setSortType(FileSorter.SortType.BY_NAME);
                        break;
                    }
                    case R.id.sortByDate: {
                        explorer.setSortType(FileSorter.SortType.BY_DATE);
                        break;
                    }
                    case R.id.sortBySize: {
                        explorer.setSortType(FileSorter.SortType.BY_SIZE);
                        break;
                    }
                }
                return true;
            }
        };
    }
    private void setOnClickActions(){
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((EditText) findViewById(R.id.searchText)).getText().toString();
                if (text.length() > 0) explorer.search(text);
            }
        });

        final EditText searchField = findViewById(R.id.searchText);
        createExplorer();
        searchField.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (isTextEditDone(actionId, event)) {
                            if (event == null || !event.isShiftPressed()) {
                                explorer.search(
                                        searchField.getText().toString()
                                );
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );

        final ImageButton menuButton = findViewById(R.id.popupMenuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, menuButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(menuItemClickListener(menuButton));
            }
        });
    }
}