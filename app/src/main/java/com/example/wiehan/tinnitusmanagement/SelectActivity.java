package com.example.wiehan.tinnitusmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);

        final ImageButton twoBackButton = (ImageButton) findViewById(R.id.twoBackButton);

        final ImageButton breathingButton = (ImageButton) findViewById(R.id.breathingButton);



        //Changes screens when button is clicked.
        twoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonAnimation(twoBackButton,"twoBack");
            }
        });

        //Changes screens to breath control activity when button is clicked.
        breathingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonAnimation(breathingButton,"breathing");
            }
        });

    }

    public void startButtonAnimation(ImageButton button, final String name) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        button.startAnimation(myAnim);

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

                if(name.equals("twoBack")) {
                    Intent intent = new Intent(getApplicationContext(), twoBack.class);
                    startActivity(intent);
                } else if(name.equals("breathing")) {
                    Intent intent = new Intent(getApplicationContext(), BreathControl.class);
                    startActivity(intent);
                }
            }
        });

    }
    }
