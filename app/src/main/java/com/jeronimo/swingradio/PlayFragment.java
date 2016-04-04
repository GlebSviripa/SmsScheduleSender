package com.jeronimo.swingradio;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
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
public class PlayFragment extends Fragment implements  View.OnClickListener
{
    private final static String DATA_STREAM = "http://swingradio.in.ua:80/live";

    RelativeLayout playButton;
    RelativeLayout pauseButton;
    RelativeLayout reloadButton;
    ProgressBar progressBar;
    RelativeLayout infoLayout;
    TextView infoTextView;
    ImageView logoImageView;
    private Timer mTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);

        playButton = (RelativeLayout)view.findViewById(R.id.fragment_play_play_button);
        playButton.setOnClickListener(this);
        pauseButton = (RelativeLayout)view.findViewById(R.id.fragment_play_pause_button);
        pauseButton.setOnClickListener(this);
        reloadButton = (RelativeLayout)view.findViewById(R.id.fragment_play_reload_button);
        reloadButton.setOnClickListener(this);
        progressBar = (ProgressBar)view.findViewById(R.id.fragment_play_progress_bar);
        infoLayout = (RelativeLayout)view.findViewById(R.id.fragment_play_info_layout);
        infoTextView = (TextView)view.findViewById(R.id.fragment_play_text_song);
        infoLayout.setOnClickListener(this);
        logoImageView = (ImageView) view.findViewById(R.id.fragment_play_image_view);

        reloadMP();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    private void reloadMP()
    {
        progressBar.setVisibility(View.VISIBLE);
        if(((MainActivity)getActivity()).service != null)
        {
            ((MainActivity)getActivity()).service.reloadMP();
        }

        mTimer = new Timer();
        MyTimerTask mTask = new MyTimerTask();
        mTimer.schedule(mTask, 1000, 12000);
    }

    @Override
    public void onDestroyView() {
        mTimer.cancel();
        super.onDestroyView();

    }

    @Override
    public void onClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_anim);
        Animation logoAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.logo_anim);
        logoAnimation.setInterpolator(new LinearInterpolator());

        switch (v.getId())
        {

            case R.id.fragment_play_play_button:

                logoImageView.startAnimation(logoAnimation);
                if(((MainActivity)getActivity()).service != null)
                {
                    ((MainActivity)getActivity()).service.startMusic();
                }

                v.startAnimation(animation);
                break;
            case R.id.fragment_play_pause_button:
                logoImageView.clearAnimation();
                if(((MainActivity)getActivity()).service != null)
                {
                    ((MainActivity)getActivity()).service.stopMusic();
                }

                v.startAnimation(animation);
                break;
            case R.id.fragment_play_reload_button:
                logoImageView.clearAnimation();
                v.startAnimation(animation);
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
