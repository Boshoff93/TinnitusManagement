package com.example.wiehan.tinnitusmanagement;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;import android.widget.ImageButton;
import android.widget.SeekBar;


public class BreathControl extends AppCompatActivity {
    int globalCount = 0;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_control);

        final View view = findViewById(R.id.breath_bubble);
        final Animation scaleBigger = AnimationUtils.loadAnimation(this, R.anim.scale_bigger);

        final Animation scaleSmaller = AnimationUtils.loadAnimation(this, R.anim.scale_smaller);

        final ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setTag(1);

        final Button buttonBreath = (Button) findViewById(R.id.buttunBreath);

        mp = MediaPlayer.create(this, R.raw.whitenoise);

        final SeekBar volumeSeekbar = (SeekBar) findViewById(R.id.volumeAdjust);

        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); ;

        volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

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

        buttonBreath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonAnimation(buttonBreath, view, scaleBigger, scaleSmaller, mp);
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


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    public void startButtonAnimation(Button buttonBreath, final View view, final Animation scaleBigger, final Animation scaleSmaller, final MediaPlayer mp) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        buttonBreath.startAnimation(myAnim);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        // 0.2 is the bounce amplitude and 20 is the frequency
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 25);
        myAnim.setInterpolator(interpolator);

        myAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Once Animation ends change screens
            @Override
            public void onAnimationEnd(Animation animation) {
                mp.start();
                expandBubble(view, scaleBigger, scaleSmaller);
            }
        });

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
                contractBubble(view, scaleBigger, scaleSmaller);
                scaleBigger.setDuration(scaleBigger.getDuration() + 200);
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
                scaleSmaller.setDuration(scaleSmaller.getDuration() + 200);

                if (globalCount < 15) {
                    expandBubble(view, scaleBigger, scaleSmaller);
                } else {
                    scaleBigger.setDuration(2000);
                    scaleSmaller.setDuration(2000);
                }

            }
        });

    }
}




