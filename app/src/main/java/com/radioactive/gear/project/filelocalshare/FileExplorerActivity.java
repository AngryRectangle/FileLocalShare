package com.radioactive.gear.project.filelocalshare;

import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.radioactive.gear.project.filelocalshare.custom_view.FileExplorerFragment;
import com.radioactive.gear.project.filelocalshare.custom_view.MediaExplorerFragment;
import com.radioactive.gear.project.filelocalshare.custom_view.ViewPagerAdapter;
import com.radioactive.gear.project.filelocalshare.sort.FileSorter;
import com.radioactive.gear.project.filelocalshare.util.file_provider.PathFileProvider;

public class FileExplorerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PathFileProvider provider = new PathFileProvider("/storage/emulated/0/Download/");
        provider.setSort(FileSorter.SortType.BY_NAME);
        provider.loadFiles();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FileExplorerFragment("/storage/emulated/0/", FileSorter.SortType.BY_NAME), getResources().getString(R.string.storage));
        String[] projectionPhoto = {MediaStore.Audio.AudioColumns.DATA};
        adapter.addFragment(new MediaExplorerFragment(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhoto, MediaStore.Audio.Media.DATE_ADDED + " DESC"), getResources().getString(R.string.photo));
        adapter.addFragment(new FileExplorerFragment("/storage/emulated/0/", FileSorter.SortType.BY_NAME), getResources().getString(R.string.downloads));
        adapter.addFragment(new MediaExplorerFragment(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionPhoto, MediaStore.Audio.Media.DATE_ADDED + " DESC"), getResources().getString(R.string.video));
        viewPager.setAdapter(adapter);
    }

}
