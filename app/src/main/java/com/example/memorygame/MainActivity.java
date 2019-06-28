package com.example.memorygame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_Game_Information = "";
    int PhotosIndex = 12;
    int onePlayer = 1;
    int typeOfPhotos = 1;
    Dialog dialog;
    ImageButton audioOn, audioOff, touchSoundOn, touchSoundOff, lunchgame;
    ImageView clo_1,clo_2;
    Switch typeOfPhotosSwitch;
    boolean audioVisible;
    boolean touchVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(MainActivity.this, SoundService.class)); //start background music service
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        audioOn = findViewById(R.id.audio_on);
        audioOff = findViewById(R.id.audio_off);
        touchSoundOn = findViewById(R.id.touch_sound_on);
        touchSoundOff = findViewById(R.id.touch_sound_off);
        lunchgame = findViewById(R.id.lunchMemoryGame);
        typeOfPhotosSwitch = findViewById(R.id.typeOfPhotos_switch);
        clo_1=findViewById(R.id.cloud_1_id);
        clo_2=findViewById(R.id.cloud_2_id);
        animateButton();
        translateButton();


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("audioVisible_key", audioVisible);
        savedInstanceState.putBoolean("touchVisible_key", touchVisible);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        audioVisible = savedInstanceState.getBoolean("audioVisible_key");
        if (audioVisible) {
            audioOn.setVisibility(View.INVISIBLE);
            audioOff.setVisibility(View.VISIBLE);
            stopService(new Intent(MainActivity.this, SoundService.class));//stop background song service
        } else {
            audioOn.setVisibility(View.VISIBLE);
            audioOff.setVisibility(View.INVISIBLE);
            startService(new Intent(MainActivity.this, SoundService.class));//stop background song service
        }
        touchVisible = savedInstanceState.getBoolean("touchVisible_key");
        if (touchVisible) {
            touchSoundOff.setVisibility(View.VISIBLE);
            touchSoundOn.setVisibility(View.INVISIBLE);
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        } else {
            touchSoundOn.setVisibility(View.VISIBLE);
            touchSoundOff.setVisibility(View.INVISIBLE);
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, SoundService.class));//stop background song service
        super.onDestroy();
    }

    public void onRadioButtonOnePlayerClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch ((view.getId())) {
            case R.id.one_Player:
                if (checked)
                    // onePlayer
                    onePlayer = 1;
                break;
            case R.id.two_Player:
                if (checked)
                    // twoPlayers
                    onePlayer = 2;
                break;
            default:
                onePlayer = 1;
                break;
        }

    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.easy:
                if (checked)
                    // Easy game

                    PhotosIndex = 12;
                break;
            case R.id.intermediate:
                if (checked)
                    // Intermediate game

                    PhotosIndex = 16;
                break;
            case R.id.expert:
                if (checked)
                    // Expert game

                    PhotosIndex = 20;

                break;

            default:
                PhotosIndex = 12;
                break;
        }

    }

    public void audioOff(View view) {
        audioVisible = true;
        audioOn.setVisibility(View.INVISIBLE);
        audioOff.setVisibility(View.VISIBLE);
        stopService(new Intent(MainActivity.this, SoundService.class));//stop background song service
    }

    public void audioOn(View view) {
        audioVisible = false;
        audioOn.setVisibility(View.VISIBLE);
        audioOff.setVisibility(View.INVISIBLE);
        startService(new Intent(MainActivity.this, SoundService.class));//start background music service

    }

    public void touch_sound_off(View view) {
        touchVisible = true;
        touchSoundOff.setVisibility(View.VISIBLE);
        touchSoundOn.setVisibility(View.INVISIBLE);
        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);

    }

    public void touch_sound_on(View view) {
        touchVisible = false;
        touchSoundOn.setVisibility(View.VISIBLE);
        touchSoundOff.setVisibility(View.INVISIBLE);
        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);

    }

    public void lunchMemoryGame(View view) {
        Intent intent = new Intent(this, MemoryGame.class);
        int[] array = new int[3];
        array[0] = PhotosIndex;
        array[1] = onePlayer;
        if (typeOfPhotosSwitch.isChecked()) {
            typeOfPhotos = 2;
        } else {
            typeOfPhotos = 1;
        }
        array[2] = typeOfPhotos;
        intent.putExtra(EXTRA_Game_Information, array);
        startActivity(intent);
    }

    public void exitGame(View view) {
        AlertDialog.Builder exitAlertBuilder;
        exitAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        exitAlertBuilder.setTitle("Oh! Sorry");
        exitAlertBuilder.setMessage("Do you want really exit ? ");
        exitAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
                System.exit(0);
            }
        });
        exitAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        exitAlertBuilder.show();

    }

    public void onSettingClick(View view) {
        dialog.setContentView(R.layout.popup_window);
        dialog.setTitle("Setting");
        dialog.show();
    }

    public void closeSettingsDialog(View view) {
        dialog.cancel();
    }

    void animateButton() {
        // Load the animation
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use custom animation interpolator to achieve the bounce effect
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1.5, 20);
        myAnim.setInterpolator(interpolator);
        // Animate the button
        lunchgame.startAnimation(myAnim);
        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animateButton();
            }
        });
    }
    public void translateButton(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translat);
        clo_1.setAnimation(animation);//set animation for first cloud
        clo_2.setAnimation(animation);//set animation for second cloud
    }
}
