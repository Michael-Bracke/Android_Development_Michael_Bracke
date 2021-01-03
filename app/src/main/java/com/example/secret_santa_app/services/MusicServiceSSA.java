package com.example.secret_santa_app.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.example.secret_santa_app.R;

public class MusicServiceSSA extends Service {

    private MediaPlayer ssaMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        ssaMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.christmas_background);
        ssaMediaPlayer.setLooping(false);
    }

    public void onStart(Intent intent, int startid){
        ssaMediaPlayer.start();
    }

    public void onDestroy(){
        ssaMediaPlayer.stop();
    }
}
