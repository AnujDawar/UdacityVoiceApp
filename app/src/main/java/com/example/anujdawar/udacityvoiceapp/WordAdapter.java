package com.example.anujdawar.udacityvoiceapp;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word>
{
    private int colorId;

    public WordAdapter(Activity context, ArrayList<Word> words, int color)
    {
        super(context, 0, words);
        colorId = color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;

        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view, parent, false);

        Word currentWord = getItem(position);

        int color = ContextCompat.getColor(getContext(), colorId);

        assert currentWord != null;
        ((TextView) listItemView.findViewById(R.id.text1)).setText(currentWord.getDefaultWord());
        ((TextView) listItemView.findViewById(R.id.text2)).setText(currentWord.getTranslatedWord());

        if(currentWord.has_image())
            ((ImageView) listItemView.findViewById(R.id.imageId)).setImageResource(currentWord.getResourceId());

        else
            listItemView.findViewById(R.id.imageId).setVisibility(View.GONE);

        listItemView.findViewById(R.id.childLinear).setBackgroundColor(color);

        return listItemView;
    }
}