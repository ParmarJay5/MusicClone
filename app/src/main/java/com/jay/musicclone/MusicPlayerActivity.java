package com.jay.musicclone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import services.Playable;

public class MusicPlayerActivity extends AppCompatActivity implements Playable {
    TextView titleTV,currenttimeTV,totaltimeTV;
    SeekBar seekBar;
    ImageView pausePlay,nextbtn,previousbtn,musicicon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x;
        
    // notification

    NotificationManager notificationManager;
    List<Track> tracks;
    int position = 0;
    boolean isPlaying = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTV = findViewById(R.id.songTitle);
        currenttimeTV = findViewById(R.id.current_time);
        totaltimeTV = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seekbar);
        pausePlay = findViewById(R.id.pause);
        nextbtn = findViewById(R.id.next);
        previousbtn = findViewById(R.id.previous);
        musicicon = findViewById(R.id.micon);

        titleTV.setSelected(true);


        songsList =(ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();


        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currenttimeTV.setText(convertoMMSS(mediaPlayer.getCurrentPosition()+""));

                    if (mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.pause);
                        musicicon.setRotation(x++);
                    }
                    else {
                        pausePlay.setImageResource(R.drawable.play);
                        musicicon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void setResourcesWithMusic(){       //click the song in list, the song is current song
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTV.setText(currentSong.getTitle());

        totaltimeTV.setText(convertoMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextbtn.setOnClickListener(v-> playNextSong());
        previousbtn.setOnClickListener(v-> playPreviousSong());

        playMusic();

    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
        mediaPlayer.setDataSource(currentSong.getPath());
        mediaPlayer.prepare();
        mediaPlayer.start();

        seekBar.setProgress(0);   // song start at 0.00
        seekBar.setMax(mediaPlayer.getDuration()); // song end at duration of song
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    private void playNextSong(){

        if (MyMediaPlayer.currentIndex == songsList.size()-1) // if song is last than it can not  move to next song
            return;
        MyMediaPlayer.currentIndex +=1;         // increase mediaplayer
        mediaPlayer.reset();
        setResourcesWithMusic();        // set new music title,time and set play pause previous setted
    }

    private void playPreviousSong(){
        if (MyMediaPlayer.currentIndex == 0) //if song is first than it can not move to previous song
            return;
        MyMediaPlayer.currentIndex -=1;         // increase mediaplayer
        mediaPlayer.reset();
        setResourcesWithMusic();      // set new music title,time and set play pause previous setted

    }

    private void pausePlay(){
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();


    }



    public static String convertoMMSS(String duration){
        Long millis = Long.parseLong(duration); //convert Milli seconds
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));  // conver Min and Second

    }


    //Notification

    popluateTracks();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createChannel();
        registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
        startService(new Intent(getBaseContext(),Notification.class));
    }

        pausePlay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View Object v;
        v) {
            if (isPlaying){
                onPause();
            } else {
                onPlay();
            }
        }
    });
}

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Object CreateNotification;
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Jayu", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //populate list with tracks
    private void popluateTracks(){
        tracks = new ArrayList<>();
        
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                
                case CreateNotification.ACTION_PREVIUOS:
                    onPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (isPlaying){
                        onPause();
                    } else {
                        onPlay();
                    }
                    break;
                case Notification.createNotification(MusicPlayerActivity.this, tracks.get(position),
                        R.drawable.pause, position, tracks.size()-1).ACTION_NEXT:
                    onNext();
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onPrevious() {
        position--;
        Notification CreateNotification;
        Notification.createNotification(MusicPlayerActivity.this, tracks.get(position),
        R.drawable.pause, position, tracks.size()-1);
        titleTV.setText(tracks.get(position).getTitle());

    }

    @Override
    public void onPlay() {
        Notification.createNotification(MusicPlayerActivity.this, tracks.get(position),
        R.drawable.pause, position, tracks.size()-1);
        pausePlay.setImageResource(R.drawable.pause);
        titleTV.setText(tracks.get(position).getTitle());
        isPlaying = true;

    }

    @Override
    public void onPause() {

        Notification CreateNotification = null;
        Notification.createNotification(MusicPlayerActivity.this, tracks.get(position),
        R.drawable.play, position, tracks.size()-1);
        pausePlay.setImageResource(R.drawable.play);
        titleTV.setText(tracks.get(position).getTitle());
        isPlaying = false;


        }

    @Override
    public void onNext() {

        position++;
        Notification CreateNotification;
        Notification.createNotification(MusicPlayerActivity.this, tracks.get(position),
                R.drawable.previous, position, tracks.size()-1);
        titleTV.setText(tracks.get(position).getTitle());

    }
}