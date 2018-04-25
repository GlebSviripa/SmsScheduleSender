package com.jeronimo.swingradio.utils

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.support.v4.app.NotificationCompat

import com.jeronimo.swingradio.MainActivity
import com.jeronimo.swingradio.PlayService
import com.jeronimo.swingradio.R

/**
 * Created by Gleb on 11/1/2016.
 */

object NotificationUpdateManager {

    private val NOTOFICATION_ID = 753

    fun updateNotification(service: Service, artist: String, song: String, isPlaying: Boolean) {
        val intent = Intent(service, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(service, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)
        val builder = NotificationCompat.Builder(service)
        builder.setContentTitle(song)
                .setContentText(artist)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        val playPauseIntent = Intent()
        playPauseIntent.action = PlayService.ACTION_PLAY_PAUSE
        val notification: Notification
        notification = builder.build()
        service.startForeground(NOTOFICATION_ID, notification)

    }
}
