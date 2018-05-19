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

public class PhrasesFragment extends Fragment {

    public PhrasesFragment() {
        // Required empty public constructor
    }

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        final ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("minto wukus", "Where are you going?", R.raw.phrase_where_are_you_going));
        words.add(new Word("tinne oyaase'ne", "What is your name?", R.raw.phrase_what_is_your_name));
        words.add(new Word("oyaaset...", "My name is...", R.raw.phrase_my_name_is));
        words.add(new Word("michekses?", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("kuchi achit", "I'm feeling good", R.raw.phrase_im_feeling_good));
        words.add(new Word("eenes'aa?", "Are you coming?", R.raw.phrase_are_you_coming));
        words.add(new Word("hee' eenem", "Yes, I'm coming.", R.raw.phrase_yes_im_coming));

        ListView myListView = (ListView) rootView.findViewById(R.id.numbersList);
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.phrases_color);
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
