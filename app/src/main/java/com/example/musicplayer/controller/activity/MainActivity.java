package com.example.musicplayer.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.fragment.AlbumListFragment;
import com.example.musicplayer.controller.fragment.ArtistListFragment;
import com.example.musicplayer.controller.fragment.SongListFragment;
import com.example.musicplayer.model.MusicFiles;
import com.example.musicplayer.repository.MusicRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    String[] titles = {"Song", "Album", "Artist"};
    private FragmentStateAdapter mPagerAdapter;
    private ViewPager2 mViewPager2;
    public  ArrayList<MusicFiles> mMusicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission();
        setContentView(R.layout.activity_main);
        findViews();

        mMusicFiles= MusicRepository.getInstance(getApplicationContext()).getAllAudio();
//        Toast.makeText(this, "size"+mMusicFiles.size(), Toast.LENGTH_SHORT).show();

        mPagerAdapter = new ViewPagerAdapter(this);
        mViewPager2.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, mViewPager2,
                (tab, position) -> tab.setText(titles[position])
        ).attach();
    }

    private void findViews() {
        mViewPager2 = findViewById(R.id.view_pager_fragments);

    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);
        } else {
            Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
            mMusicFiles= MusicRepository.getInstance(getApplicationContext()).getAllAudio();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
                Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
                mMusicFiles= MusicRepository.getInstance(getApplicationContext()).getAllAudio();


            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return SongListFragment.newInstance();
                case 1:
                    return AlbumListFragment.newInstance();
                case 2:
                    return ArtistListFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



}