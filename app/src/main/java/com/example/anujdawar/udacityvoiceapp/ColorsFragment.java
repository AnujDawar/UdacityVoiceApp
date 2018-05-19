package com.example.anujdawar.udacityvoiceapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ColorsFragment extends Fragment
{
    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public ColorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        final ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("wetwtti","red", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("tayayyi","mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("chokokki","green", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("co'ki","dusty yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("takaakki","brown", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("lutti7","black", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("lutti8","gray", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("lutti9","white", R.drawable.color_white, R.raw.color_white));

        ListView myListView = (ListView) rootView.findViewById(R.id.numbersList);
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.colors_color);
        myListView.setAdapter(adapter);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
                releaseMedia();

                int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == audioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getResourceSound());
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            releaseMedia();
                        }
                    });
                }
            }
        });

        return rootView;
    }

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == audioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == audioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                // pause playing and reset
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }

            else if(focusChange == audioManager.AUDIOFOCUS_GAIN)
            {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }

            else if(focusChange == audioManager.AUDIOFOCUS_LOSS)
            {
                releaseMedia();
            }
        }
    };

    void releaseMedia()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMedia();
    }
}
