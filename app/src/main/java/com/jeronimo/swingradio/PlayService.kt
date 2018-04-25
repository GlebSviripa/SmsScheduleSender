package com.jeronimo.swingradio

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.jeronimo.swingradio.utils.NotificationUpdateManager
import com.jeronimo.swingradio.utils.ParsingHeaderData
import java.io.IOException
import java.net.URL
import java.util.*

class PlayService : Service(), MediaPlayer.OnPreparedListener {

    private var binder: MyBinder? = null
    private var mediaPlayer: MediaPlayer? = null
    internal lateinit var am: AudioManager
    private var mTimer: Timer? = null


    override fun onCreate() {
        super.onCreate()
        binder = MyBinder()
        am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        NotificationUpdateManager.updateNotification(this, "", "", false)
        reloadMP()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    private fun releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer!!.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun reloadMP() {
        releaseMP()
        val infoIntent = Intent(EVENT_NAME)
        infoIntent.putExtra(STATE, STATE_RELOADING)
        LocalBroadcastManager.getInstance(this).sendBroadcast(infoIntent)
        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer!!.setDataSource(DATA_STREAM)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer!!.setOnPreparedListener(this)
        mediaPlayer!!.prepareAsync()

        mTimer = Timer()
        val mTask = MyTimerTask()
        mTimer!!.schedule(mTask, 1000, 12000)
    }

    override fun onDestroy() {
        releaseMP()
        mTimer!!.cancel()
        super.onDestroy()

    }

    override fun onPrepared(mp: MediaPlayer) {
        val infoIntent = Intent(EVENT_NAME)
        infoIntent.putExtra(STATE, STATE_PLAYING)
        LocalBroadcastManager.getInstance(this).sendBroadcast(infoIntent)
        mp.start()
    }

    fun stopMusic() {
        val infoIntent = Intent(EVENT_NAME)
        infoIntent.putExtra(STATE, STATE_PAUSED)
        LocalBroadcastManager.getInstance(this).sendBroadcast(infoIntent)
        mediaPlayer!!.pause()
    }

    fun startMusic() {
        val infoIntent = Intent(EVENT_NAME)
        infoIntent.putExtra(STATE, STATE_PLAYING)
        LocalBroadcastManager.getInstance(this).sendBroadcast(infoIntent)
        mediaPlayer!!.start()
    }

    internal inner class MyBinder : Binder() {
        val service: PlayService
            get() = this@PlayService
    }

    internal inner class MyTimerTask : TimerTask() {

        var currentSong: String = ""
        var currentArtist: String = ""

        override fun run() {
            try {
                val url = URL(DATA_STREAM)
                val streaming = ParsingHeaderData()
                val trackData = streaming.getTrackDetails(url)
                currentArtist = trackData.artist
                currentSong = trackData.title
                Log.e("Song Artist Name ", trackData.artist)
                Log.e("Song Artist Title", trackData.title)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val infoIntent = Intent(EVENT_NAME)
            infoIntent.putExtra(ARTIST_NAME, currentArtist)
            infoIntent.putExtra(SONG_NAME, currentSong)
            LocalBroadcastManager.getInstance(this@PlayService).sendBroadcast(infoIntent)
            NotificationUpdateManager.updateNotification(this@PlayService, currentArtist, currentSong, mediaPlayer!!.isPlaying)
        }
    }

    companion object {


        val EVENT_NAME = "radio_event"

        private val ARTIST_NAME = "artist_name"

        private val SONG_NAME = "song_name"

        val ACTION_PLAY_PAUSE = "play_pause_action"

        val ACTION_RELOAD = "action_reload"

        val ACTION_CLOSE = "close_action"

        val STATE_PLAYING = 0

        val STATE_RELOADING = 1

        val STATE_PAUSED = 2

        val STATE = "state"
        private val DATA_STREAM = "http://swingradio.in.ua:80/live"
    }
}
