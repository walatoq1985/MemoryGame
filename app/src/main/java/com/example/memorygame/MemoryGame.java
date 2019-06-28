package com.example.memorygame;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MemoryGame extends AppCompatActivity {

    int[] fruit = new int[]{
            R.drawable.apple, R.drawable.banana, R.drawable.carrot2, R.drawable.corn,
            R.drawable.grapes, R.drawable.orange, R.drawable.pear, R.drawable.pumpkin,
            R.drawable.watermelon, R.drawable.lemon};
    int[] animal = new int[]{
            R.drawable.fish, R.drawable.frog, R.drawable.dolphin, R.drawable.whale,
            R.drawable.turtle, R.drawable.snake, R.drawable.crab, R.drawable.shell,
            R.drawable.octopus, R.drawable.fish_outline};
    int[] photos;
    int backgroundPhoto = R.drawable.gift; //you can change the background here
    boolean turn = true;
    TextView textViewPlayer1; //attempt for player1
    TextView textViewPlayer2; //attempt for player2
    TextView timeTextView;//for game time
    ImageView curView = null;
    int photosIndexLength;
    int numOfPlayer;
    int currentPos = -1;
    int[] photosIndex;
    int attemptPlayer1 = 0;// how much try to solve the game
    int attemptPlayer2 = 0;// how much try to solve the game
    int attemptPlayer = 0;// how much try to solve the game
    int countPair = 0;
    int typeOfPhotos;
    //timer attribute
    private static final long START_TIME_IN_MILLIS = 86400000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    CountDownTimer countDownTimer;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        Intent intent = getIntent();
        int[] intentArray;
        intentArray = intent.getIntArrayExtra(MainActivity.EXTRA_Game_Information);
        photosIndexLength = intentArray[0];
        numOfPlayer = intentArray[1];
        typeOfPhotos = intentArray[2];
        if (typeOfPhotos == 2) {
            photos = fruit;
        } else {
            photos = animal;
        }

        photosIndex = new int[photosIndexLength];
        //if tow players
        // make player2 Attempt text visible
        if (numOfPlayer == 2) {
            textViewPlayer2 = findViewById(R.id.textViewPlayer2);
            textViewPlayer2.setVisibility(View.VISIBLE);
            newGame2();
        } else {
            newGame1();
        }

    }

    public void onClickShowAlert() {

        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(MemoryGame.this);
        // Set the dialog title and message.
        myAlertBuilder.setTitle("You Win!");
        myAlertBuilder.setMessage("Do you want to play one more time");
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button.
                        Toast.makeText(getApplicationContext(), "Game restarted ",
                                Toast.LENGTH_SHORT).show();
                        resetTime();

                        countPair = 0;
                        newGame1();
                    }
                });
        myAlertBuilder.setNegativeButton("No thanks", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog.
                        // Toast.makeText(getApplicationContext(), "Game  Canceled",Toast.LENGTH_SHORT).show();
                        //go to mainActivity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
        // Create and show the AlertDialog.
        myAlertBuilder.show();


    }

    //generate a random view Image
    public void generateRandomViewImage() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < photosIndex.length / 2; i++) {
            list.add((i));
        }
        Collections.shuffle(list);
        for (int i = 0; i < photosIndex.length / 2; i++) {
            photosIndex[i] = list.get(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < photosIndex.length / 2; i++) {
            photosIndex[i + photosIndex.length / 2] = list.get(i);
        }
    }

    //initialize a new Game for one player
    public void newGame1() {
        attemptPlayer = 0;
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.a);
        textViewPlayer1 = findViewById(R.id.textViewPlayer1);
        timeTextView = findViewById(R.id.textViewTime);
        //start timer
        startTimer();

        //generate a random view Image
        generateRandomViewImage();
        //set image adapter
        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, final View view, int nextPosition, long id) {
                if (currentPos < 0) {
                    currentPos = nextPosition;
                    curView = (ImageView) view;
                    ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                } else {

                    if (currentPos == nextPosition) {
                        curView.setImageResource(backgroundPhoto);
                    }

                    if (photosIndex[currentPos] != photosIndex[nextPosition]) {
                        //when wrong choice
                        attemptPlayer++;
                        String Attempt = "Player 1:   " + attemptPlayer;
                        textViewPlayer1.setText(Attempt);
                        ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 1s = 1000ms

                                curView.setImageResource(backgroundPhoto);
                                ((ImageView) view).setImageResource(backgroundPhoto);
                            }
                        }, 1000);

                    } else {
                        ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                        //when right choice
                        attemptPlayer++;
                        String Attempt = "Player:" + attemptPlayer;
                        textViewPlayer1.setText(Attempt);
                        countPair++;
                        if (countPair == photosIndexLength / 2) {
                            mp.start();
                            Toast.makeText(MemoryGame.this, "You Win!", Toast.LENGTH_LONG).show();
                            onClickShowAlert();
                        }
                    }
                    currentPos = -1;
                }
            }
        });


    }


    public void newGame2() {

        textViewPlayer1 = findViewById(R.id.textViewPlayer1);
        textViewPlayer2 = findViewById(R.id.textViewPlayer2);
        timeTextView = findViewById(R.id.textViewTime);
        startTimer();//start timer
        generateRandomViewImage();//generate a random view Image
        ImageAdapter imageAdapter = new ImageAdapter(this);//set image adapter
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int nextPosition, long id) {

                if (currentPos < 0) {
                    currentPos = nextPosition;
                    curView = (ImageView) view;
                    ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                } else {

                    if (currentPos == nextPosition) {
                        curView.setImageResource(backgroundPhoto);
                    }
                    if (turn) {//first player

                        if (photosIndex[currentPos] != photosIndex[nextPosition]) {
                            //wrong choice
                            /*
                            attemptPlayer1++;
                            textViewPlayer1.setText("Player 1:" + attemptPlayer1);
                            */
                            textViewPlayer1.setBackgroundColor(Color.parseColor("#ff0000"));
                            ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);

                            Toast.makeText(MemoryGame.this, "Not Match!", Toast.LENGTH_LONG).show();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 1s = 1000ms

                                    curView.setImageResource(backgroundPhoto);
                                    ((ImageView) view).setImageResource(backgroundPhoto);
                                }
                            }, 1000);

                            turn = false;


                        } else if (photosIndex[currentPos] == photosIndex[nextPosition]) {


                            ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                            //when right choice

                            attemptPlayer1++;
                            String Attempt = "Player 1:   " + attemptPlayer1;
                            textViewPlayer1.setText(Attempt);
                            countPair++;

                            if (countPair == photosIndexLength / 2) {
                                //mp.start();

                                Toast.makeText(MemoryGame.this, "You Win!", Toast.LENGTH_LONG).show();


                                onClickShowAlert();
                                //when wrong choice


                            }
                        }

                    } else {//second player
                        if (photosIndex[currentPos] != photosIndex[nextPosition]) {
                            textViewPlayer1.setBackgroundColor(Color.parseColor("#00ff00"));
                            //when wrong choice
                            /*attemptPlayer2++;

                            textViewPlayer2.setText("Player 2:" + attemptPlayer2);
                            */
                            ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                            Toast.makeText(MemoryGame.this, "Not Match!", Toast.LENGTH_LONG).show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 1s = 1000ms

                                    curView.setImageResource(backgroundPhoto);
                                    ((ImageView) view).setImageResource(backgroundPhoto);
                                }
                            }, 1000);


                            turn = true;

                        } else if (photosIndex[currentPos] == photosIndex[nextPosition]) {
                            ((ImageView) view).setImageResource(photos[photosIndex[nextPosition]]);
                            //when right choice

                            attemptPlayer2++;

                            String Attempt = "Player 2:   " + attemptPlayer2;

                            textViewPlayer2.setText(Attempt);

                            countPair++;
                            if (countPair == photosIndexLength / 2) {
                                //mp.start();
                                Toast.makeText(MemoryGame.this, "You Win!", Toast.LENGTH_LONG).show();
                                resetTime();
                                onClickShowAlert();
                            }
                        }

                    }
                    currentPos = -1;
                }

            }


        });


    }

    private void startTimer() {
        countDownTimer =
                new CountDownTimer(mTimeLeftInMillis, 1000) {
                    public void onTick(long millisUntilFinished) {
                        time = (mTimeLeftInMillis - millisUntilFinished) / 1000;
                        long seconds = time % 60;
                        long minutes = time / 60;
                        String timer = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                        timeTextView.setText(timer);
                    }

                    @Override
                    public void onFinish() {
                        finish();

                    }
                }.start();
    }

    public void resetTime() {
        countDownTimer.cancel();

    }

    /**
     * Make empty grid
     */
    class ImageAdapter extends BaseAdapter {

        private Context context;

        private ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return photosIndexLength;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(this.context);
                imageView.setLayoutParams(new GridView.LayoutParams(240, 240));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } else imageView = (ImageView) convertView;
            imageView.setImageResource(backgroundPhoto);
            return imageView;
        }
    }
}
