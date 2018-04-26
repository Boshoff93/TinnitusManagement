package com.example.wiehan.tinnitusmanagement;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.text.DateFormat;
import java.util.Date;

import static android.R.attr.content;
import static android.R.attr.data;
import static android.R.attr.outAnimation;

public class MainScreen extends AppCompatActivity {

    Button start;
    EditText id ;
    int initialOpen = 0;
    static String input ;
    static String bpmValue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Give necessary permissions to write to applications internal storage
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        id = (EditText) findViewById(R.id.idInput);
        id.setText(input);
        id.setEnabled(false);
        id.setGravity(Gravity.CENTER);
        start = (Button) findViewById(R.id.buttonMain);
        Button about = (Button) findViewById(R.id.buttonAbout);

        final Context context = getApplicationContext();
        final CharSequence text = "Created by Interactions Design and Modeling Lab. Copyright © 2017";
        final int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //About button displays a simple toast message, to state the creators of the application
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.makeText(context, text, duration).show();
            }
        });


        /*When start screen is pressed, the application will check if "Do Not Distrub Permissions" are given, if not a warning screen will be displayed to ask user to give access.
        * If the phone is not switched to airplane mode, a warning message will be displayed stating that airplain mode needs to be switched on, and the app will not continue to the exercise screen.
        * If the above two requirements are met, the application will direct the user to the exercise screen screen
        */
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

    //Sets the app's breathing pattern. This is used by the writeFile method to state what breathing pattern was selected on start of the exercise
    public static void setBPM(String value) {
        bpmValue = value ;
    }

    //Method can be accessed by other parts of the program by referencing MainScreen.writeFile
    public static void writeFile(String appendType) {

        try {
            //Write to folder called /Tinnitus App - Participant Folder/Participant_Name - App Data.txt
            File path = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Tinnitus App - Participant Folder");
            if (!path.exists()) {
                path.mkdirs();
            }

            File myFile = new File(path, input + " - App Data.txt");
            FileOutputStream fOut = new FileOutputStream(myFile,true);
            OutputStreamWriter writeLogs = new OutputStreamWriter(fOut);

            switch (appendType) {
                case "Insert Name":                writeLogs.append("\n"+input+" (Login Time: " + new Date(System.currentTimeMillis())+")\n\n"); break;
                case "Main Screen Opended":        writeLogs.append("Main Screen Opened:\t\t" + new Date(System.currentTimeMillis())+"\n"); break;
                case "Main Screen Closed":         writeLogs.append("Main Screen Closed:\t\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Exercise Screen Opened":     writeLogs.append("Exercise Screen Opened:\t" + new Date(System.currentTimeMillis()) + "\n"); break;
                case "Exercise Screen Closed":     writeLogs.append("Exercise Screen Closed:\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial Started":           writeLogs.append("Tutorial Sarted:\t\t\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial Closed":            writeLogs.append("Tutorial Closed:\t\t\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial 1":                 writeLogs.append("Tutorial Screen 1 Opended:\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial 2":                 writeLogs.append("Tutorial Screen 2 Opended:\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial 3":                 writeLogs.append("Tutorial Screen 3 Opended:\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial 4":                 writeLogs.append("Tutorial Screen 4 Opended:\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Tutorial 5":                 writeLogs.append("Tutorial Screen 5 Opended:\t" + new Date(System.currentTimeMillis())+"\n") ; break;
                case "Exercise Started":           writeLogs.append("Exercise Started:\t\t\t" + new Date(System.currentTimeMillis()) + " - Breathing Pattern: " + bpmValue +" Breaths per Minute\n") ; break;
                case "Exercise Ended":             writeLogs.append("Exercise Ended:\t\t\t" + new Date(System.currentTimeMillis())+"\n") ; break;

            }
            writeLogs.close();
            fOut.close();
        }

        catch (java.io.IOException e) {

            e.printStackTrace();
        }

    }

    public static void setFileName(String id) {
        input = id ;
    }

    @Override
    protected  void onStart() {
        writeFile("Main Screen Opended");
        super.onStart();
    }

    @Override
    protected void onPause() {
        writeFile("Main Screen Closed");
        super.onPause();
    }

    //Do nothiong on back press
    @Override
    public void onBackPressed()
    {
    }
}
