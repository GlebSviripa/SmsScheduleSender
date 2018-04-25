package com.jeronimo.swingradio

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentPagerAdapter

/**
 * Created by gleb on 23.12.15.
 */
class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var playFragment: PlayFragment? = null
    private var blogFragment: BlogFragment? = null

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                if (playFragment == null) {
                    playFragment = PlayFragment()
                }
                return playFragment
            }
            1 -> {
                if (blogFragment == null) {
                    blogFragment = BlogFragment()
                }
                return blogFragment
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    companion object {
        private val NUM_ITEMS = 2
    }
}
