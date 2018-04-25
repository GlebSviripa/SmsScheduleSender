package com.jeronimo.swingradio

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.content.WakefulBroadcastReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Created by gleb on 23.12.15.
 */
class PlayFragment : Fragment(), View.OnClickListener {

    internal lateinit var playButton: RelativeLayout
    internal lateinit var pauseButton: RelativeLayout
    internal lateinit var reloadButton: RelativeLayout
    internal lateinit var progressBar: ProgressBar
    internal lateinit var infoLayout: RelativeLayout
    internal lateinit var infoTextView: TextView
    internal lateinit var logoImageView: ImageView
    internal lateinit var animation: Animation
    internal lateinit var logoAnimation: Animation


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation = AnimationUtils.loadAnimation(activity, R.anim.button_anim)
        logoAnimation = AnimationUtils.loadAnimation(activity, R.anim.logo_anim)
        logoAnimation.interpolator = LinearInterpolator()

        playButton = view.findViewById(R.id.fragment_play_play_button)
        playButton.setOnClickListener(this)
        pauseButton = view.findViewById(R.id.fragment_play_pause_button)
        pauseButton.setOnClickListener(this)
        reloadButton = view.findViewById(R.id.fragment_play_reload_button)
        reloadButton.setOnClickListener(this)
        progressBar = view.findViewById(R.id.fragment_play_progress_bar)
        infoLayout = view.findViewById(R.id.fragment_play_info_layout)
        infoTextView = view.findViewById(R.id.fragment_play_text_song)
        infoLayout.setOnClickListener(this)
        logoImageView = view.findViewById(R.id.fragment_play_image_view)
        LocalBroadcastManager.getInstance(activity).registerReceiver(object : WakefulBroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val state = intent.getIntExtra(PlayService.STATE, -1)

                when (state) {
                    PlayService.STATE_PLAYING -> onServicePlay()
                    PlayService.STATE_PAUSED -> onServicePause()
                    PlayService.STATE_RELOADING -> onReloadMP()
                    else -> {
                    }
                }
            }
        }, IntentFilter(PlayService.EVENT_NAME))
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    private fun onServicePlay() {
        logoImageView.startAnimation(logoAnimation)
        progressBar.visibility = View.INVISIBLE
    }

    private fun onServicePause() {
        logoImageView.clearAnimation()
    }

    private fun onReloadMP() {
        progressBar.visibility = View.VISIBLE
        logoImageView.clearAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.fragment_play_play_button -> {
                if ((activity as MainActivity).service != null) {
                    (activity as MainActivity).service!!.startMusic()
                }
                v.startAnimation(animation)
            }
            R.id.fragment_play_pause_button -> {
                if ((activity as MainActivity).service != null) {
                    (activity as MainActivity).service!!.stopMusic()
                }
                v.startAnimation(animation)
            }
            R.id.fragment_play_reload_button -> {
                if ((activity as MainActivity).service != null) {
                    (activity as MainActivity).service!!.reloadMP()
                }
                v.startAnimation(animation)
            }
            else -> {
            }
        }
    }

    companion object {
        private val DATA_STREAM = "http://swingradio.in.ua:80/live"
    }


}
