package com.jeronimo.swingradio;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    public ViewPager mainViewPager;
    private TabLayout mainTabLayout;
    private MainViewPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        mainViewPager = (ViewPager)findViewById(R.id.fragment_main_view_pager);
        mainTabLayout = (TabLayout)findViewById(R.id.fragment_main_tabs);
        adapterViewPager = new MainViewPagerAdapter(getFragmentManager());
        mainViewPager.setAdapter(adapterViewPager);
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainTabLayout.getTabAt(0).setIcon(R.mipmap.ic_action_hardware_headset);
        mainTabLayout.getTabAt(1).setIcon(R.mipmap.ic_action_action_receipt);
    }
}
