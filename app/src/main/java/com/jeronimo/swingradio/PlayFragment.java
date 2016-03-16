package com.jeronimo.swingradio;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeronimo.swingradio.utils.ParsingHeaderData;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gleb on 23.12.15.
 */
public class PlayFragment extends Fragment implements MediaPlayer.OnPreparedListener, View.OnClickListener
{

    private final static String DATA_STREAM = "http://swingradio.in.ua:80/live";
    MediaPlayer mediaPlayer;
    AudioManager am;
    RelativeLayout playButton;
    RelativeLayout pauseButton;
    RelativeLayout reloadButton;
    ProgressBar progressBar;
    RelativeLayout infoLayout;
    TextView infoTextView;
    private Timer mTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);
        am = (AudioManager)getActivity().getSystemService(getActivity().AUDIO_SERVICE);

        playButton = (RelativeLayout)view.findViewById(R.id.fragment_play_play_button);
        playButton.setOnClickListener(this);
        pauseButton = (RelativeLayout)view.findViewById(R.id.fragment_play_pause_button);
        pauseButton.setOnClickListener(this);
        reloadButton = (RelativeLayout)view.findViewById(R.id.fragment_play_reload_button);
        reloadButton.setOnClickListener(this);
        progressBar = (ProgressBar)view.findViewById(R.id.fragment_play_progress_bar);
        infoLayout = (RelativeLayout)view.findViewById(R.id.fragment_play_info_layout);
        infoLayout.setOnClickListener(this);
        infoTextView = (TextView)view.findViewById(R.id.fragment_play_text_song);

        reloadMP();
        return view;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMP();
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
    private void reloadMP()
    {
        progressBar.setVisibility(View.VISIBLE);
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
        mTimer = new Timer();
        MyTimerTask mTask = new MyTimerTask();
        mTimer.schedule(mTask, 1000, 12000);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fragment_play_play_button:
                mediaPlayer.start();
                break;
            case R.id.fragment_play_pause_button:
                mediaPlayer.pause();
                break;
            case R.id.fragment_play_reload_button:
                reloadMP();
                break;
            case R.id.fragment_play_info_layout:

                break;
            default:
                break;

        }
    }



    class MyTimerTask extends TimerTask {

        String currentSong;
        String currentArtist;
        @Override
        public void run() {
            try {
                URL url = new URL(DATA_STREAM);
                ParsingHeaderData streaming = new ParsingHeaderData();
                ParsingHeaderData.TrackData trackData = streaming.getTrackDetails(url);
                currentArtist = trackData.artist;
                currentSong = trackData.title;
                Log.e("Song Artist Name ", trackData.artist);
                Log.e("Song Artist Title", trackData.title);
            } catch (Exception e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    infoTextView.setText(currentArtist + " - " + currentSong);
                }
            });
        }
    }
}
