package com.example.wiehan.tinnitusmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;



public class BreathControl extends AppCompatActivity {
    int globalCount = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_control);

        final View view = findViewById(R.id.breath_bubble);
        final Animation scaleBigger = AnimationUtils.loadAnimation(this, R.anim.scale_bigger);

        final Animation scaleSmaller = AnimationUtils.loadAnimation(this, R.anim.scale_smaller);

        Button buttonBreath = (Button) findViewById(R.id.buttunBreath);
        buttonBreath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

    }

    public void contractBubble(final View view, final Animation scaleBigger, final Animation scaleSmaller) {
        view.startAnimation(scaleSmaller);

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
                globalCount++ ;

                if (globalCount <5) {
                    expandBubble(view, scaleBigger, scaleSmaller);
                }

            }
        });

    }

    }




