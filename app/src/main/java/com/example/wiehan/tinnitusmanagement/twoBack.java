package com.example.wiehan.tinnitusmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class twoBack extends AppCompatActivity {

    int count ;
    TextView tv ;
    TextView explain ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_back);

        final Button startSequence = (Button) findViewById(R.id.buttonStartSequence);
        final Button exitButton = (Button) findViewById(R.id.exitButton);
        explain = (TextView) findViewById(R.id.textExplain) ;
        tv = (TextView) findViewById(R.id.textCount) ;
        count = 3 ;

        tv.setText("");

        //Changes screens when button is clicked.
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitPressed(exitButton);
            }
        });

        startSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceButton(startSequence, exitButton);
            }
        });
    }

    //If continue button is pressed go back to the selector screen.
    public void exitPressed(final Button button) {
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
                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                startActivity(intent);
            }
        });

    }

    public void startPressed() {
        if (count == 0) {
            count = 3 ;
            tv.setText(""); //Note: the TextView will be visible again here.
            return;
        }

        tv.setText(String.valueOf(count));

        //Animation for countdown
        final Animation myAnim2 = AnimationUtils.loadAnimation(this, R.anim.countdown);

        myAnim2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Calls sartPressed method again till count down hits 0
            @Override
            public void onAnimationEnd(Animation animation) {
                count--;
                startPressed();
            }
        });

        tv.startAnimation(myAnim2);

    }

    public void bounceButton(final Button startSequence, final Button exitButton) {
        //Animation for button bounce
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 25);
        myAnim.setInterpolator(interpolator);
        startSequence.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Set visibility of explination text, start- and exit buttons to false.
                startSequence.setVisibility(View.INVISIBLE);
                exitButton.setVisibility(View.INVISIBLE);
                explain.setVisibility(View.INVISIBLE);
                startPressed();
            }
        });
    }

    }

