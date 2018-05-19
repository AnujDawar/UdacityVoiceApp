package com.example.anujdawar.udacityvoiceapp;

public class Word
{
    String defaultWord, translatedWord;
    int resourceImageId = -1;
    int resourceSound;

    public Word(String defaultWord, String translatedWord, int resourceImageId, int resourceSound)
    {
        this.defaultWord = defaultWord;
        this.translatedWord = translatedWord;
        this.resourceImageId = resourceImageId;
        this.resourceSound = resourceSound;
    }

    public Word(String defaultWord, String translatedWord)
    {
        this.defaultWord = defaultWord;
        this.translatedWord = translatedWord;
    }

    public Word(String defaultWord, String translatedWord, int resourceSound)
    {
        this.defaultWord = defaultWord;
        this.translatedWord = translatedWord;
        this.resourceSound = resourceSound;
    }

    public String getDefaultWord()
    {
        return defaultWord;
    }

    public String getTranslatedWord()
    {
        return translatedWord;
    }

    public int getResourceId() {
        return resourceImageId;
    }

    public boolean has_image()
    {
        return resourceImageId != -1;
    }

    public int getResourceSound() {
        return resourceSound;
    }
}