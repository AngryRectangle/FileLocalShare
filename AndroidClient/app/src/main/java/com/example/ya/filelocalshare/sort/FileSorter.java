package com.example.ya.filelocalshare.sort;

import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FileSorter {
    public enum SortType{
        BY_NAME,
        BY_SIZE,
        BY_DATE
    }

    public static File[] sort(File[] files, SortType type){
        ArrayList<File> output = new ArrayList<>(Arrays.asList(files));
        output = sort(output, type);
        return output.toArray(new File[0]);
    }
    public static ArrayList<File> sort(ArrayList<File> files, SortType type){
        ArrayList<File>[] filesSplitted= splitFileDirs(files);
        switch (type) {
            case BY_NAME: {
                for (int i = 0; i < 2; i++)
                    sortByName(filesSplitted[i]);
                break;
            }
            case BY_DATE: {
                for (int i = 0; i < 2; i++)
                    sortByDate(filesSplitted[i]);
                break;
            }
            case BY_SIZE: {
                for (int i = 0; i < 2; i++)
                    sortBySize(filesSplitted[i]);
                break;
            }
        }
        filesSplitted[0].addAll(filesSplitted[1]);
        return filesSplitted[0];
    }

    private static ArrayList<File>[] splitFileDirs(ArrayList<File> files){
        ArrayList<File> outputDirs = new ArrayList<>();
        ArrayList<File> outputFiles = new ArrayList<>();
        for(int i =0; i<files.size(); i++)
            if(files.get(i).isDirectory())
                outputDirs.add(files.get(i));
            else
                outputFiles.add(files.get(i));
        return (ArrayList<File>[]) new ArrayList[]{outputDirs, outputFiles};
    }
    private static void sortByName(ArrayList<File> file){
        Collections.sort(file);
    }
    private static void sortBySize(ArrayList<File> file){
        Collections.sort(file, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                return -longComparator(a.length(), b.length());
            }
        });
    }
    private static void sortByDate(ArrayList<File> file){
        Collections.sort(file, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                return -longComparator(a.lastModified(), b.lastModified());
            }
        });
    }
    private static int longComparator(Long a, Long b){
        return a<b?-1:(a>b?1:0);
    }
}
