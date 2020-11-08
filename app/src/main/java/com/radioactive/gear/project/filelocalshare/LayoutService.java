package com.radioactive.gear.project.filelocalshare;

import android.view.LayoutInflater;
import android.view.View;

public class LayoutService {
    private LayoutInflater _inflater;
    private NavigationStack _navigation;

    public View openLayout(int view){
        _navigation.put(view);
        return _inflater.inflate(view, null, false);
    }

    public void openPrevious(){

    }
}
