package com.example.ya.filelocalshare;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.io.File;

public class FileViewer {
    Map<String,Integer> iconResources;
    HashSet<String> imageExtensions;
    static final int FileView = R.layout.file_view;
    AsyncBitmapSettings asyncBitmapSettings = new AsyncBitmapSettings();
    ArrayList<BitmapHolder> bitmapHolders = new ArrayList<>();

    public FileViewer( Map<String,Integer> iconResources, HashSet<String> imageExtensions){
        this.iconResources = iconResources;
        this.imageExtensions = imageExtensions;
    }

    public View GetFileView(Activity activity, File file) {
        LayoutInflater ltInflater = activity.getLayoutInflater();
        View output = ltInflater.inflate(FileView, null, false);
        ((TextView) output.findViewById(R.id.fileName)).setText(file.getName());
        int iconId = iconResources.get("unknown");
        if (file.isDirectory()) {
            iconId = iconResources.get("folder");
            setOnClickActionForFolder((MainActivity)activity, output, file);
        }
        else {
            String extension = GetFileExtension(file.getName());
            if (iconResources.containsKey(extension))
                iconId = iconResources.get(extension);
        }
        ((ImageView)output.findViewById(R.id.fileIcon)).setImageResource(iconId);
        if(imageExtensions.contains(GetFileExtension(file.getName()))){
            //bitmapHolders.add(new BitmapHolder(null, (ImageView)output.findViewById(R.id.fileIcon), file));
            Glide.with(activity)
                    .load(file)
                    .centerCrop()
                    .placeholder(R.drawable.ic_file_icon_jpg)
                    .into((ImageView)output.findViewById(R.id.fileIcon));
        }
        return output;
    }
    public void ShowImageThumbnails(){
        //asyncBitmapSettings.cancel(true);
        asyncBitmapSettings.execute(bitmapHolders.toArray(new BitmapHolder[0]));
        bitmapHolders.clear();
    }
    private String GetFileExtension(String extension){
        char[] textArray = extension.toCharArray();
        for(int i = extension.length()-1; i>=0; i--){
            if(textArray[i]=='.'){
                char[] output = new char[extension.length()-i-1];
                extension.getChars(i+1, extension.length(), output, 0);
                return new String(output);
            }
        }
        return extension;
    }
    public static void SortFilesByAlphabetAndFolders(File[] input){
        ArrayList<File> outputFiles = new ArrayList<>();
        ArrayList<File> outputDirs = new ArrayList<>();
        for(int i =0; i<input.length; i++)
            if(input[i].isDirectory())
                outputDirs.add(input[i]);
            else
                outputFiles.add(input[i]);
        Collections.sort(outputFiles);
        Collections.sort(outputDirs);
        outputDirs.addAll(outputFiles);
        input = outputDirs.toArray(input);
    }

    private void setOnClickActionForFolder(final MainActivity activity, final View view, final File file){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.explorer.SetCurrentPath(file.getAbsolutePath());
                activity.ViewPath(file.getAbsolutePath());
                activity.ViewFiles(file.getAbsolutePath()+"/");
            }
        });
    }
    private Bitmap getBitmap(File file){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // Get bitmap dimensions before reading...
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        int largerSide = Math.max(width, height);
        opts.inJustDecodeBounds = false; // This time it's for real!
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        int sampleSize = Math.round(largerSide/128f);
        opts.inSampleSize = sampleSize;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath(), opts), 130, 130);
    }

    private File[] GetSpecificFiles(File[] files, HashSet<String> extensions){
        ArrayList<File> output = new ArrayList<>();
        for(int i =0; i < files.length; i++)
            if(extensions.contains(GetFileExtension(files[i].getName())))
                output.add(files[i]);
            return output.toArray(new File[0]);
    }


    public class BitmapHolder {
        Bitmap bitmap;
        ImageView view;
        File file;

        public BitmapHolder(Bitmap bitmap, ImageView view, File file) {
            this.bitmap = bitmap;
            this.view = view;
            this.file = file;
        }
    }
    private class AsyncBitmapSettings extends AsyncTask<BitmapHolder[], BitmapHolder, Void> {
        @Override
        protected Void doInBackground(BitmapHolder[]... holders) {
            android.os.Process.setThreadPriority(  android.os.Process.THREAD_PRIORITY_BACKGROUND +   android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
            for(int i =0; i<holders[0].length; i++){

                holders[0][i].bitmap = getBitmap(holders[0][i].file);
                publishProgress(holders[0][i]);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(BitmapHolder... holder) {
            holder[0].view.setImageBitmap(holder[0].bitmap);
        }
    }
}
