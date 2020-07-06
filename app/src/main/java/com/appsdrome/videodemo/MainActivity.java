package com.appsdrome.videodemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private ImageButton mButtonPlay;
    private ImageButton mButtonStop;
    private ImageButton mButtonPause;

    private Handler mHandler;
    private VideoView videoView;
    Runnable t;
    Boolean flag =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Get the widget reference from xml layout
        mButtonPlay = findViewById(R.id.btn_play);
        mButtonStop = findViewById(R.id.btn_stop);
        mButtonPause = findViewById(R.id.btn_pause);
        videoView = findViewById(R.id.videoView);

        // Initialize the handler
        mHandler = new Handler();

        // Initially disable the buttons
        mButtonStop.setEnabled(false);
        mButtonPause.setEnabled(false);

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag){
                    stopPlaying();

                    startPlaying();

                    handlerStart();
                }

                 if(flag){

                     resumePlaying();

                 }
                }


        });

        // Set a click listener for top playing button
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();

            }
        });

        // Click listener for pause button
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    pausePlay();
            }
        });


    }

    private void handlerStart() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 10000);
                Log.e("milisecond","mHandlerRun");
                pausePlay();
                showDialog();
            }
        }, 10000);
    }


    private void startPlaying() {
        // Initialize media player
        String path = "android.resource://" + mActivity.getPackageName() + "/" + R.raw.coin;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();

        // Start the media player
        Toast.makeText(mContext, "Media Player is playing.", Toast.LENGTH_SHORT).show();

        mButtonPlay.setEnabled(false);
        mButtonPause.setEnabled(true);
        mButtonStop.setEnabled(true);
        flag = true;
    }

    private void resumePlaying() {
        if(videoView!=null){
            Log.e("message","resume call");
            videoView.start();
            mButtonPause.setEnabled(true);
            mButtonPlay.setEnabled(true);
            mButtonStop.setEnabled(true);
            flag=false;
        }
    }

    private void showDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("DO you want to continue video")
                .setMessage("DO you want to continue video")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        resumePlaying();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      stopPlaying();
                    }
                })
                .show();
    }

    private void pausePlay() {
        if(videoView!=null && videoView.isPlaying()) {
            Log.e("message", "pause call");
            videoView.pause();
            mButtonPause.setEnabled(false);
            mButtonStop.setEnabled(false);
            mButtonPlay.setEnabled(true);
            flag = true;
        }
    }

    private void stopPlaying() {
        // If media player is not null then try to stop it
        if(videoView!=null){
            videoView.stopPlayback();
            if(mHandler!=null){
                mHandler.removeCallbacksAndMessages(0);
            }
        }
        mButtonStop.setEnabled(false);
        mButtonPause.setEnabled(false);
        mButtonPlay.setEnabled(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("message","landscape mode");
            mHandler.removeMessages(0);
            pausePlay();
        } else {
            Log.e("message","portrate mode");
            mHandler.postDelayed(t,1000);
            resumePlaying();
        }
    }
}