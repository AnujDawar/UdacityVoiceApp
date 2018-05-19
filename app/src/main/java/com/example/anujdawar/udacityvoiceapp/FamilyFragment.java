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

import java.util.ArrayList;

public class FamilyFragment extends Fragment {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public FamilyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        final ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("epe", "father", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("eta", "mother", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("angsi", "son", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("tune", "daughter", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("taachi", "older brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("chalitti", "younger brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("dada", "grand father", R.drawable.family_grandfather, R.raw.family_grandfather));
        words.add(new Word("dadi", "grand mother", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("behen", "younger sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));

        ListView myListView = (ListView) rootView.findViewById(R.id.numbersList);
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.family_color);
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
