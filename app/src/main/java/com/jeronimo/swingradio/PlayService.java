package com.jeronimo.swingradio;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.View;

import java.io.IOException;
import java.util.Timer;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener {
    private static final int NOTOFICATION_ID = 753;

    private MyBinder binder;
    private final static String DATA_STREAM = "http://swingradio.in.ua:80/live";
    MediaPlayer mediaPlayer;
    AudioManager am;
    public PlayService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
        am = (AudioManager)getSystemService(AUDIO_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_action_hardware_headset)
                .setContentIntent(pendingIntent)
                .setContentText("Swing Radio");
        Notification notification;
        if (Build.VERSION.SDK_INT < 16)
            notification = builder.getNotification();
        else
            notification = builder.build();
        startForeground(NOTOFICATION_ID, notification);
        reloadMP();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return binder;
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void reloadMP()
    {
        releaseMP();
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(DATA_STREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onDestroy() {
        releaseMP();
        super.onDestroy();

    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void stopMusic()
    {
        mediaPlayer.pause();
    }

    public void startMusic()
    {
        mediaPlayer.start();
    }

    class MyBinder extends Binder {
        PlayService getService() {
            return PlayService.this;
        }
    }
}
