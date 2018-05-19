package com.example.anujdawar.udacityvoiceapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersFragment extends Fragment
{
    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        final ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("lutti","one", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("otiiko","two", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("tolookosu","three", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("oyyisa","four", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("massokka","five", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("temmokka","six", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("kenekaku","seven", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("kawinta","eight", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("wo'e","nine", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("na' aacha","ten", R.drawable.number_ten, R.raw.number_ten));

        ListView myListView = (ListView) rootView.findViewById(R.id.numbersList);
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.numbers_color);
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
