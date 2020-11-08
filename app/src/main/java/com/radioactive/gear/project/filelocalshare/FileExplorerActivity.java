package com.radioactive.gear.project.filelocalshare;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.radioactive.gear.project.filelocalshare.custom_view.FileExplorerFragment;
import com.radioactive.gear.project.filelocalshare.custom_view.ViewPagerAdapter;

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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FileExplorerFragment(), getResources().getString(R.string.storage));
        adapter.addFragment(new FileExplorerFragment(), getResources().getString(R.string.photo));
        adapter.addFragment(new FileExplorerFragment(), getResources().getString(R.string.downloads));
        adapter.addFragment(new FileExplorerFragment(), getResources().getString(R.string.video));
        adapter.addFragment(new FileExplorerFragment(), getResources().getString(R.string.documents));
        viewPager.setAdapter(adapter);
    }

}
