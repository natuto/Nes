package com.liuwensong.nes_demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by song on 2017/5/13.
 */

public class mPagerAdaper extends FragmentPagerAdapter {
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragments;
    public mPagerAdaper(FragmentManager fm, ArrayList<Fragment>f, ArrayList<String> title) {
        super(fm);
        this.titleList = title;
        this.fragments = f;
    }
    public mPagerAdaper(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
