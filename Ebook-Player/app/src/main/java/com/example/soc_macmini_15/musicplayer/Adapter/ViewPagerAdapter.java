package com.example.soc_macmini_15.musicplayer.Adapter;

import android.content.ContentResolver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.soc_macmini_15.musicplayer.Fragments.AllEbookFragment;
import com.example.soc_macmini_15.musicplayer.Fragments.CurrentEbookFragment;
import com.example.soc_macmini_15.musicplayer.Fragments.FavEbookFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ContentResolver contentResolver;
    private String title[] = {"All EBOOKS","CURRENT PLAYLIST", "FAVORITES"};

    public ViewPagerAdapter(FragmentManager fm, ContentResolver contentResolver) {
        super(fm);
        this.contentResolver = contentResolver;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return AllEbookFragment.getInstance(position, contentResolver);
            case 1:
                return CurrentEbookFragment.getInstance(position);
            case 2:
                return FavEbookFragment.getInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
