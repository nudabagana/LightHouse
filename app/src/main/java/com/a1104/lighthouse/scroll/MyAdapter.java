package com.a1104.lighthouse.scroll;

import  android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.a1104.lighthouse.Fragments.CalendarScreenFragment;
import com.a1104.lighthouse.Fragments.PhotoScreenFragment;
import com.a1104.lighthouse.Fragments.PhotoScreenFragment2;
import com.a1104.lighthouse.Fragments.TaskScreenFragment;
import com.a1104.lighthouse.Fragments.WeatherScreenFragment;

public class MyAdapter extends FragmentStatePagerAdapter {

    static final int NUMBER_OF_PAGES = 4;

    public MyAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);

    }
    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 : return CalendarScreenFragment.newInstance();
            case 1 : return TaskScreenFragment.newInstance();
            case 2 : return WeatherScreenFragment.newInstance();
            //case 3 : return PhotoScreenFragment.newInstance();
            case 3 : return PhotoScreenFragment2.newInstance();
            default : return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }
}

