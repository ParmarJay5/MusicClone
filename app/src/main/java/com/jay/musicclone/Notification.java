package com.jay.musicclone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import services.NotificationService;

public class Notification {

    public static final String CHANNEL_ID = "chhanel11";
    public static final String PREVIOUS = "previous";
    public static final String PLAY = "play";
    public static final String NEXT = "next";

    public static Notification notification;

    public static void createNotification(Context context, Track track, int playbtn, int paush, int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
            MediaSession mediaSessionCompat = new MediaSession(context,"tag");

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getImage());

            PendingIntent pendingIntentPrevious;
            int previous;

            if (paush == 0){
                pendingIntentPrevious = null;
                previous = 0;
            }else{
                Intent intentPrevious = new Intent(context, NotificationService.class)
                        .setAction(PREVIOUS);

                pendingIntentPrevious = PendingIntent.getBroadcast(context,0,intentPrevious,PendingIntent.FLAG_UPDATE_CURRENT);

                previous = R.drawable.previous;
            }

            Intent intentPlay = new Intent(context, NotificationService.class)
                    .setAction(PLAY);

            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);



            PendingIntent pendingIntentNext;
            int next;

            if (paush == size){
                pendingIntentNext = null;
                next = 0;
            }else{
                Intent intentNext = new Intent(context, NotificationService.class)
                        .setAction(NEXT);

                pendingIntentNext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);

                next = R.drawable.next;



                //Create Notification
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.musicicon)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(previous,"Previous", pendingIntentPrevious)
                    .addAction(playbtn,"Play", pendingIntentPlay)
                    .addAction(next,"Next", pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionInCompatView(0 ,1 , 2)
                            .setMediaSesson(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
        }


    }

    private static class MediaSessionCompat {
        public MediaSessionCompat(Context context, String tag) {

        }
    }
}