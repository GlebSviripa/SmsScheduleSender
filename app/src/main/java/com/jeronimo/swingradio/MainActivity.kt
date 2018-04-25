package com.jeronimo.swingradio

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

import com.crashlytics.android.Crashlytics

import io.fabric.sdk.android.Fabric

class MainActivity : Activity() {

    lateinit var mainViewPager: ViewPager
    private var mainTabLayout: TabLayout? = null
    private var adapterViewPager: MainViewPagerAdapter? = null
    internal lateinit var serviceIntent: Intent
    lateinit var sConn: ServiceConnection
    var service: PlayService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        mainViewPager = findViewById(R.id.fragment_main_view_pager)
        mainTabLayout = findViewById(R.id.fragment_main_tabs)
        adapterViewPager = MainViewPagerAdapter(fragmentManager)
        mainViewPager.adapter = adapterViewPager
        mainTabLayout!!.setupWithViewPager(mainViewPager)
        mainTabLayout!!.getTabAt(0)!!.setIcon(R.mipmap.ic_action_hardware_headset)
        mainTabLayout!!.getTabAt(1)!!.setIcon(R.mipmap.ic_action_action_receipt)
        serviceIntent = Intent(this, PlayService::class.java)
        startService(serviceIntent)
        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                service = (binder as PlayService.MyBinder).service
            }

            override fun onServiceDisconnected(name: ComponentName) {
                service = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bindService(serviceIntent, sConn,
                Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(sConn)
    }
}
