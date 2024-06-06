package com.jay.musicclone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.VievHolder> {

    ArrayList<AudioModel> songsList;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public VievHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new VievHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.VievHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        AudioModel songData = songsList.get(position);
        holder.title.setText(songData.getTitle());

        if (MyMediaPlayer.currentIndex==position){
            holder.title.setTextColor(Color.parseColor("#FF0000"));
        }else
            holder.title.setTextColor(Color.parseColor("#000000"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to another activity
                MyMediaPlayer.getInstance().reset(); // it will reset a media player
                MyMediaPlayer.currentIndex = position; //set currentindex for song
                Intent intent = new Intent(context,MusicPlayerActivity.class); //navigate passing the activity to songList
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size() ;
    }

    public class VievHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView img;

        public VievHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.musicTitle);
            img = itemView.findViewById(R.id.musicicon);
        }
    }
}