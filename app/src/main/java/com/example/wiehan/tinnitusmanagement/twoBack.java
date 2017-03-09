package com.example.wiehan.tinnitusmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class twoBack extends AppCompatActivity {

    int count;
    TextView tvCount;
    TextView explain;
    Character sequence[];
    TextView seqText;
    TextView correctText ;
    TextView wrongText ;

    int countSeq = 0 ; ;
    Button tapButton;
    int track = 0 ;
    int correct = 0 ;
    int wrong = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_back);

        final Button startSequence = (Button) findViewById(R.id.buttonStartSequence);
        final Button exitButton = (Button) findViewById(R.id.exitButton);
        tapButton = (Button) findViewById(R.id.buttonTap);

        //Create new randomize object
        Random rand = new Random();

        //Choose a random letter out of the string and add it to the sequence array.
        String letters = "ABCDEF";
        sequence = new Character[6];


        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = letters.charAt(rand.nextInt(5));
        }

        correctText = (TextView) findViewById(R.id.textCorrect) ;
        wrongText = (TextView) findViewById(R.id.textWrong) ;

        explain = (TextView) findViewById(R.id.textExplain);
        tvCount = (TextView) findViewById(R.id.textCount);
        seqText = (TextView) findViewById(R.id.textSequence);
        count = 3;

        tvCount.setText("");
        seqText.setText("");
        tapButton.setVisibility(View.INVISIBLE);

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


        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                track = 1 ;
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
            count = 3;
            tvCount.setText(""); //Note: the TextView will be visible again here.
            tapButton.setVisibility(View.VISIBLE);
            sequenceRepeat();
            return;
        }

        tvCount.setText(String.valueOf(count));

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

        tvCount.startAnimation(myAnim2);

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


    public synchronized void sequenceRepeat() {

        if(countSeq == sequence.length){
            countSeq = 0 ;
            seqText.setText("") ;
            correctText.setText(correct+"") ;
            wrongText.setText(wrong+"") ;
            return ;
        }

        //Animation for sequence
        AlphaAnimation myAnim3 = new AlphaAnimation(1.0f, 0.0f);
        myAnim3.setDuration(1000);

        seqText.setText(sequence[countSeq].toString());

        myAnim3.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Calls sartPressed method again till count down hits 0
            @Override
            public void onAnimationEnd(Animation animation) {
            if(countSeq > 0) {
                if (track == 1) {
                    int flag = 0 ;
                    for (int i = 0; i < countSeq; i++) {
                        if (sequence[countSeq] == sequence[i]) {
                            flag = 1 ;
                            correct++;
                            break;
                        }
                    }
                    if(flag == 0){
                        wrong++;
                    }
                }
            }
            track = 0 ;
            countSeq++;
            sequenceRepeat();

            }
        });

        seqText.startAnimation(myAnim3);



    }
}

