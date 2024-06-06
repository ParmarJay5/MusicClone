package com.jay.musicclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noSongText;
    ArrayList<AudioModel> songList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.songItem);
        noSongText = findViewById(R.id.noSongs);

        if (checkPermission() == false){
            requestPermission();
            return;
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

//        String selection = MediaStore.Audio.Media.IS_MUSIC +" !=0";

        //  All the data, Music file will be Store in Cursor
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);

        // Access the cursor using ArrayList
        while(cursor.moveToNext()){
            //get data  and stored in AudioMedel
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            //cursor.getString(1) for data , (0) for title , (2) for duration

            if (new File(songData.getPath()).exists())
                songList.add(songData);

            //in songList we will add songData  , delete the songs it stored in database and we access the  data base it show dlt song also
        }

        // we dont have any song that it show us No Song Found TEXTVIEW
        if (songList.size()==0){noSongText.setVisibility(View.VISIBLE);}
        else {
            // show music in recyclerView
            recyclerView.setAdapter(new MusicListAdapter(songList,MainActivity.this)); // Pass Songs List
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }

    }


    void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(MainActivity.this,"Allow Permission",Toast.LENGTH_LONG);
        }
        else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0510);


    }


    //Notification



//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (recyclerView!=null){
//            recyclerView.setAdapter(new MusicListAdapter(songList,getApplicationContext()));
//        }
//    }
}
