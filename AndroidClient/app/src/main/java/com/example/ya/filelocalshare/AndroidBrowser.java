package com.example.ya.filelocalshare;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ya.filelocalshare.sort.FileSorter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AndroidBrowser implements FileBrowser {
    private final Activity activity;
    private final TableLayout fileView;
    private final FileViewer fileViewer;
    private final FilePathShower pathViewer;
    private final Map<String, Integer> icons;
    private final HashSet<String> thumbnailExtensions;
    private final FileViewer.FileViewOptions options;
    private AsyncSearch asyncSearch;
    FileExplorer explorer;

    public AndroidBrowser(
            @NonNull final Activity activity,
            @NonNull final TableLayout fileView,
            @NonNull LinearLayout pathLayout, final
            @NonNull FileExplorer explorer,
            @Nullable Map<String, Integer> icons,
            @Nullable HashSet<String> thumbnailExtensions,
            @Nullable FileViewer.FileViewOptions options
    ) {
        this.activity = activity;
        this.fileView = fileView;
        fileViewer = new FileViewer(icons, thumbnailExtensions);
        pathViewer = new FilePathShower(activity, R.layout.path_view, pathLayout);
        this.explorer = explorer;
        if (icons == null) {
            icons = new HashMap<>();
            icons.put("unknown", R.drawable.ic_file_icon_txt);
        }
        this.icons = icons;
        if (thumbnailExtensions == null)
            thumbnailExtensions = new HashSet<>();
        this.thumbnailExtensions = thumbnailExtensions;
        if (options == null)
            options = new FileViewer.FileViewOptions(3);
        this.options = options;

        setHandlers();
    }

    public void displayFiles(@NonNull File[] files) {
        fileViewer.viewFiles(files, options);
    }

    public void displayFile(@NonNull File file) {
        fileViewer.viewFile(file, options);
    }

    public void displayPath(@NonNull String path) {
        pathViewer.showPath(path, explorer);
    }

    public void clearFileView() {
        fileViewer.clear();
    }

    public void startSearching(@NonNull File dir, @NonNull String target) {
        clearFileView();
        if (asyncSearch != null && asyncSearch.getStatus().equals(AsyncTask.Status.RUNNING))
            asyncSearch.cancel(false);
        asyncSearch = new AsyncSearch();
        asyncSearch.dir = dir;
        asyncSearch.execute(target);
    }

    public void cancelSearching() {
        asyncSearch.cancel(false);
        asyncSearch = null;
        clearFileView();
        displayFiles(explorer.getFiles());
        ((EditText) activity.findViewById(R.id.searchText)).setText(null);
        activity.findViewById(R.id.searchText).clearFocus();
        pathViewer.showPath(explorer.currentPath, explorer);
    }

    private class AsyncSearch extends AsyncTask<String, File, Void> {
        File dir;

        @Override
        protected Void doInBackground(String... targetString) {
            ArrayList<File> dirs = new ArrayList<>();
            dirs.add(dir);
            File[] files;
            for (int i = 0; i < dirs.size() && !isCancelled(); i++) {
                files = dirs.get(i).listFiles();
                for (int j = 0; j < files.length; j++) {
                    if (files[j].getName().contains(targetString[0]))
                        publishProgress(files[j]);
                    if (files[j].isDirectory())
                        dirs.add(files[j]);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(File... file) {
            if (!isCancelled()) displayFile(file[0]);
        }
    }

    private void sortViewedFiles(@NonNull FileSorter.SortType type) {
        ArrayList<File> sortedFiles = FileSorter.sort(fileViewer.getViewedFiles(), type);
        File[] fileToView = sortedFiles.toArray(new File[0]);
        fileViewer.clear();
        fileViewer.viewFiles(fileToView, options);
    }

    private boolean isSearchUsed(){
        return asyncSearch != null && (
                asyncSearch.getStatus().equals(AsyncTask.Status.RUNNING) ||
                        asyncSearch.getStatus().equals(AsyncTask.Status.FINISHED));
    }

    private void viewPreviousPath(){
        String[] path = explorer.currentPath.split("[/]");
        if (path.length > 3) {
            String fullPath = "";
            for (int i = 0; i < path.length - 1; i++)
                fullPath += path[i] + "/";
            explorer.openDirectory(fullPath);
        }
    }

    private void setHandlers(){
        setBackButtonHandler();
        setFileViewHandler();
        setStartSearchingCallback();
        setSelectPathHandler();
        setSortChangeHandler();
        setClearViewHandler();
    }
    private void setBackButtonHandler() {
        MainActivity main = (MainActivity) activity;
        main.getOnBackPressedDispatcher().addCallback(
                main,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (isSearchUsed())
                            cancelSearching();
                        else
                            viewPreviousPath();
                    }
                });
    }
    private void setFileViewHandler(){
        explorer.addOpenDirListener(new FileExplorer.ExplorerCallback() {
            @Override
            public void execute(String path) {
                displayFiles(explorer.getFiles());
                displayPath(path);
            }
        });
    }
    private void setStartSearchingCallback() {
        explorer.addStartSearchingListener(new FileExplorer.ExplorerCallback() {
            @Override
            public void execute(String target) {
                startSearching(new File(explorer.currentPath), target);
                pathViewer.showPath(explorer.currentPath, explorer);
                pathViewer.addElement("Search", null, null, false);
            }
        });
    }
    private void setSelectPathHandler(){
        pathViewer.handler = new FilePathShower.SelectPathHandler() {
            @Override
            public void execute(String fullpath) {
                explorer.openDirectory(fullpath);
            }
        };
    }
    private void setSortChangeHandler(){
        explorer.addSortChangeListener(new FileExplorer.SortChangeCallback() {
            @Override
            public void execute(FileSorter.SortType type) {
                sortViewedFiles(type);
            }
        });
        fileViewer.fileViewer = new FileViewer.FileViewHandler() {
            @Override
            public void execute(File file, FileViewer.FileViewOptions options) {
                View view = fileViewer.getFileView(activity, file, explorer);
                int rowCount = fileView.getChildCount();
                TableRow row = (TableRow) fileView.getChildAt(rowCount - 1);
                if (rowCount == 0) {
                    row = new TableRow(activity);
                    fileView.addView(row);
                }

                int columnCount = row.getChildCount();
                if (columnCount >= options.columns) {
                    row = new TableRow(activity);
                    fileView.addView(row);
                }
                row.addView(view);
            }
        };
    }
    private void setClearViewHandler(){
        fileViewer.clearViewHandler = new FileViewer.ClearViewHandler() {
            @Override
            public void execute() {
                fileView.removeAllViews();
            }
        };
    }
}