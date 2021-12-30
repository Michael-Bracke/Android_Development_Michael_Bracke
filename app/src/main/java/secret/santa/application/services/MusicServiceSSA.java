package secret.santa.application.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.secret.santa.R;


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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // basically maak je hier een nieuwe thread aan die adhv de runnable()
        // een thread zal opstarten wanneer deze wordt opgestart
        new Thread(
                new Runnable() {
            @Override
            public void run() {
                Log.e("Service", "Musicservice is gestart..");
                ssaMediaPlayer.start();
                Log.e("Service", "Is playing? " + ssaMediaPlayer.isPlaying());
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onStart(Intent intent, int startid){
        ssaMediaPlayer.start();
    }

    public void onDestroy(){
        ssaMediaPlayer.stop();
    }
}
