package com.jeronimo.swingradio;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by gleb on 23.12.15.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private PlayFragment playFragment;
    private BlogFragment blogFragment;
    private static int NUM_ITEMS = 2;
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(playFragment == null)
                {
                    playFragment = new PlayFragment();
                }
                return playFragment;
            case 1:
                if(blogFragment == null)
                {
                    blogFragment = new BlogFragment();
                }
                return blogFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
