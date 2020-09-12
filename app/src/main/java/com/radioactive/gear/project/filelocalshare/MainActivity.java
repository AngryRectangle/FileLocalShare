package com.radioactive.gear.project.filelocalshare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.radioactive.gear.project.filelocalshare.sort.FileSorter;
import com.radioactive.gear.project.core.NetworkInteraction;
import com.radioactive.gear.project.core.Receiver;
import com.radioactive.gear.project.core.SocketWrapper;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static com.radioactive.gear.project.core.NetworkInteraction.DEFAULT_PORT;

public class MainActivity extends AppCompatActivity {

    FileExplorer explorer;
    AndroidBrowser androidBrowser;
    SocketWrapper socket;
    SendingProgressMonitor monitor;
    LayoutInflater inflater;
    SendingQueue queue;
    FileSendingVisualizer visualizer;
    boolean isConnecting = false;
    Receiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = getLayoutInflater();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.INTERNET"}, 11);
        }
        final Button button = findViewById(R.id.connectButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup) button.getParent()).removeView(button);
                isConnecting = true;
                final MulticastSocket socket;

                try {
                    NetworkInteraction.multicast(NetworkInteraction.getVersionByteArray(), NetworkInteraction.DEFAULT_PC_GROUP);
                    socket = new MulticastSocket(DEFAULT_PORT);
                    socket.joinGroup(InetAddress.getByName(NetworkInteraction.DEFAULT_ANDROID_GROUP));
                    final ChooseConnectionFragment fragment = new ChooseConnectionFragment();
                    ((ViewGroup) findViewById(R.id.topFrame)).addView(fragment.onCreateView(getLayoutInflater(), null, null));

                    receiver = new Receiver(socket, new Receiver.ResultHandler() {
                        @Override
                        public void execute(final DatagramPacket packet) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fragment.addButton(packet);
                                }
                            });
                        }
                    });
                    receiver.start();
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                }
            }
        });

        setOnClickActions();
        explorer.openDirectory(getString(R.string.default_path));
    }

    void connect(InetAddress address) throws IOException {
        socket = new SocketWrapper(NetworkInteraction.connect(address));
        monitor = new SendingProgressMonitor(socket);
        queue = new SendingQueue(socket);
        visualizer = new FileSendingVisualizer((LinearLayout) findViewById(R.id.connectionList), this);
        monitor.progressListener = new SendingProgressMonitor.ProgressHandler() {
            @Override
            public void execute(SocketWrapper.InteractionType type, long progress) {
                if (type == SocketWrapper.InteractionType.PROGRESS_SENDING)
                    visualizer.setProgress(progress);
                if (type == SocketWrapper.InteractionType.SUCCESSFUL_SENDING)
                    visualizer.removeFirstTicket();
            }
        };
    }

    public void sendFile(File file) {
        queue.addFileToQueue(file);
        queue.startDataTransmitting();
        visualizer.addProgressTicket(file);
    }

    private void createExplorer() {
        explorer = new FileExplorer();
        androidBrowser = new AndroidBrowser(
                this,
                (TableLayout) findViewById(R.id.fileTable),
                (LinearLayout) findViewById(R.id.pathViewerLayout),
                explorer,
                new FileViewer.FileViewOptions(5)
        );
    }

    private static boolean isTextEditDone(int actionId, KeyEvent event) {
        return (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event != null &&
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }

    private PopupMenu.OnMenuItemClickListener menuItemClickListener(final ImageButton button) {
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

    private PopupMenu.OnMenuItemClickListener sortMenuClickListener() {
        return new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
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

    private void setOnClickActions() {
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