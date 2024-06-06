package com.jay.musicclone;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;  // static variable

    public static MediaPlayer  getInstance(){   // static method
        if (instance == null){
            instance =  new MediaPlayer();
        }
        return instance;
    }
    public static int currentIndex = -1;
}