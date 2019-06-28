package com.example.memorygame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SoundService extends Service {
    /**
     * create media player with bensound_cute music
     */
    MediaPlayer player;
    public SoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        player=MediaPlayer.create(this,R.raw.bensound_cute);//create player with bensound_cute music
    }
    public void onDestroy(){
        player.stop();

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        player.start();
        return Service.START_NOT_STICKY;
    }

}
