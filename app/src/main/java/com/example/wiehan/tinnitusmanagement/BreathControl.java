package com.example.wiehan.tinnitusmanagement;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BreathControl extends AppCompatActivity {
    int globalCount;
    MediaPlayer mp;
    private Spinner spinner  ;
    private int desiredSpeed;
    Button buttonBreath;
    boolean backGroundHide = false ;
    boolean keepStartButtonInvisible = false;
    View background;
    SeekBar volumeSeekbar;
    Button buttonExit;
    Button buttonTutorial;
    ImageButton playButton;
    TextView dropDownLabel;
    ActionBar actionBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_control);

        final View view = findViewById(R.id.breath_bubble);
        final Animation scaleBigger = AnimationUtils.loadAnimation(this, R.anim.scale_bigger);
        final Animation scaleSmaller = AnimationUtils.loadAnimation(this, R.anim.scale_smaller);
        scaleBigger.setDuration(2000);
        scaleSmaller.setDuration(2000);

        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setTag(1);

        buttonBreath = (Button) findViewById(R.id.buttunBreath);
        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonTutorial = (Button) findViewById(R.id.buttonTutorial);

        mp = MediaPlayer.create(this, R.raw.whitenoise);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        volumeSeekbar = (SeekBar) findViewById(R.id.volumeAdjust);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        background = (View) findViewById(R.id.activity_breath_control) ;
        actionBar = getSupportActionBar();

        addItemsOnSpinner();
        volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        final AlertDialog.Builder headPhoneAlert = new AlertDialog.Builder(BreathControl.this);
        headPhoneAlert.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        globalCount = 0;
                        keepStartButtonInvisible = true;
                        mp.start();
                        buttonBreath.setVisibility(View.INVISIBLE);
                        expandBubble(view, scaleBigger, scaleSmaller);
                    }
                });

        headPhoneAlert.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        final AlertDialog hpAlert = headPhoneAlert.create();
        hpAlert.getWindow().setBackgroundDrawableResource(R.drawable.textview_custom);
        hpAlert.setMessage("THE SYSTEM HAS DETECTED THAT YOU DON'T HAVE ANY EARPHONES CONNECTED. ARE YOU SURE YOU WANT TO CONTINUE?");

        dropDownLabel = (TextView) findViewById(R.id.textBPM) ;

        background.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (!backGroundHide) {
                    ObjectAnimator colorFade = ObjectAnimator.ofObject(background, "backgroundColor", new ArgbEvaluator(), Color.argb(255, 255, 255, 255), 0xff000000);
                    volumeSeekbar.setVisibility(View.INVISIBLE);
                    buttonExit.setVisibility(View.INVISIBLE);
                    buttonBreath.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    dropDownLabel.setVisibility(View.INVISIBLE);
                    buttonTutorial.setVisibility(View.INVISIBLE);

                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    getSupportActionBar().hide();

                    actionBar.setBackgroundDrawable(new ColorDrawable(0xff000000));
                    actionBar.setDisplayShowTitleEnabled(false);

                    colorFade.setDuration(1000);
                    colorFade.start();
                    backGroundHide = true ;
                    return true;
                } else {
                    ObjectAnimator colorFade = ObjectAnimator.ofObject(background, "backgroundColor", new ArgbEvaluator(), 0xff000000, Color.argb(255, 255, 255, 255));
                    volumeSeekbar.setVisibility(View.VISIBLE);
                    buttonExit.setVisibility(View.VISIBLE);
                    if(keepStartButtonInvisible){
                        buttonBreath.setVisibility(View.INVISIBLE);
                    } else {
                        buttonBreath.setVisibility(View.VISIBLE);
                    }
                    playButton.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    dropDownLabel.setVisibility(View.VISIBLE);
                    buttonTutorial.setVisibility(View.VISIBLE);
                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
                    actionBar.setDisplayShowTitleEnabled(true);

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getSupportActionBar().show();

                    colorFade.setDuration(1000);
                    colorFade.start();
                    backGroundHide = false ;
                }
                return true;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String bpm = spinner.getItemAtPosition(position).toString() ;
                if(bpm.equals("6")) {
                   desiredSpeed = 5000;
                } else if(bpm.equals("9")) {
                   desiredSpeed = 3350; ;
                } else {
                   desiredSpeed = 2500;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //Update the MediaPlayer's position while the user drags the SeekBar
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });

        buttonBreath.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AudioManager checkHeadphones = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        vibe.vibrate(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (checkHeadphones.isWiredHeadsetOn() || checkHeadphones.isBluetoothA2dpOn()) {
                            globalCount = 0;
                            keepStartButtonInvisible = true;
                            mp.start();
                            buttonBreath.setVisibility(View.INVISIBLE);
                            expandBubble(view, scaleBigger, scaleSmaller);

                        } else {
                            hpAlert.show();
                        }
                        break;
                }
                return false;
            }
        });

        buttonExit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        vibe.vibrate(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        mp.stop();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playButton.getTag().equals(1)) {
                    playButton.setImageResource(R.drawable.mute);
                    playButton.setTag(2);
                    mp.setVolume(0, 0);
                } else {
                    playButton.setImageResource(R.drawable.play);
                    playButton.setTag(1);
                    mp.setVolume(1, 1);
                }
            }
        });

        buttonTutorial.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        vibe.vibrate(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        MyPreferences.resetFirst(getApplicationContext());
                        finish();
                        Intent i = new Intent(getApplicationContext(), BreathTutorialOne.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    //If continue button is pressed go back to the selector screen.
    public void expandBubble(final View view, final Animation scaleBigger, final Animation scaleSmaller) {
        view.startAnimation(scaleBigger);

        scaleBigger.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Once Animation ends change screens
            @Override
            public void onAnimationEnd(Animation animation) {
                if(scaleBigger.getDuration() < desiredSpeed) {
                    scaleBigger.setDuration(scaleBigger.getDuration() + 200);
                }

                contractBubble(view, scaleBigger, scaleSmaller);
            }
        });

    }

    public void contractBubble(final View view, final Animation scaleBigger, final Animation scaleSmaller) {
        view.startAnimation(scaleSmaller);
        scaleSmaller.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Once Animation ends change screens
            @Override
            public void onAnimationEnd(Animation animation) {

                globalCount++;

                if(scaleSmaller.getDuration() < desiredSpeed) {
                    scaleSmaller.setDuration(scaleSmaller.getDuration() + 200);
                }
                if (globalCount < 30) {
                    expandBubble(view, scaleBigger, scaleSmaller);
                } else {
                    mp.stop();
                    keepStartButtonInvisible = false;
                    try {
                        mp.prepare();
                        mp.seekTo(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scaleBigger.setDuration(2000);
                    scaleSmaller.setDuration(2000);
                    final CharSequence text = "Done! If you want to redo the exercise, press the start button.";
                    final Context context = getApplicationContext();

                    volumeSeekbar.setVisibility(View.VISIBLE);
                    buttonExit.setVisibility(View.VISIBLE);
                    buttonBreath.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    dropDownLabel.setVisibility(View.VISIBLE);
                    buttonTutorial.setVisibility(View.VISIBLE);

                    if(backGroundHide) {
                        ObjectAnimator colorFade = ObjectAnimator.ofObject(background, "backgroundColor", new ArgbEvaluator(), 0xff000000, Color.argb(255, 255, 255, 255));
                        colorFade.setDuration(1000);
                        colorFade.start();
                    }
                    backGroundHide = false;
                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
                    actionBar.setDisplayShowTitleEnabled(true);

                    Toast toast = Toast.makeText(context, text ,Toast.LENGTH_LONG) ;
                    toast.setGravity(Gravity.TOP,0,1000);
                    toast.show();
                }

            }
        });

    }

    // Add items into spinner dynamically
    public void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.bpmDropdown);
        List<Integer> list = new ArrayList<Integer>();
        list.add(6);
        list.add(9);
        list.add(12);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(BreathControl.this, MainScreen.class));
        finish();

    }
}




