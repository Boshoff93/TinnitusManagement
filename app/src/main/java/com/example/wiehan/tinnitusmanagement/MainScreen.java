package com.example.wiehan.tinnitusmanagement;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        start = (Button) findViewById(R.id.buttonMain);
        Button about = (Button) findViewById(R.id.buttonAbout);

        final Context context = getApplicationContext();
        final CharSequence text = "Created by Interactions Design and Modeling Lab. Copyright © 2017";
        final int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.makeText(context, text, duration).show();
            }
        });

        start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        vibe.vibrate(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        final AlertDialog.Builder silentAppAlert = new AlertDialog.Builder(MainScreen.this);
                        silentAppAlert.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                                        startActivity(intent);
                                    }
                                });

                        silentAppAlert.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        final AlertDialog.Builder airPlaneModeAlert = new AlertDialog.Builder(MainScreen.this);
                        airPlaneModeAlert.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        //Make sure users phone is in silent mode before proceeding
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted()) {
                            final AlertDialog silentAlert = silentAppAlert.create();
                            silentAlert.getWindow().setBackgroundDrawableResource(R.drawable.textview_custom);
                            silentAlert.setMessage("IN ORDER FOR THE EXERCISE TO BE SUCCESSFUL, DO NOT DISTURB PERMISSON IS REQUIRED TO SWITCH THE DEVICE TO SILENT MODE. DO YOU WISH TO CONTINUE");
                            silentAlert.show();
                        } else {
                            boolean isEnabled = Settings.System.getInt(
                                    getContentResolver(),
                                    Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                            if(!isEnabled) {
                                final AlertDialog airPlaneMode = airPlaneModeAlert.create();
                                airPlaneMode.getWindow().setBackgroundDrawableResource(R.drawable.textview_custom);
                                airPlaneMode.setMessage("TO CONTINUE PLEASE SWITCH THE DEVICE TO AIRPLANE MODE TO PREVENT ANY DISTRACTIONS");
                                airPlaneMode.show();
                            } else {
                                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                    Intent intent = new Intent(getApplicationContext(), BreathTutorialOne.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), BreathTutorialOne.class);
                                    startActivity(intent);
                                }
                            }
                        }

                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed()
    {


    }
}
