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

    Button tapButton;
    Button exitButton ;
    Button startSequence ;

    int countSeq = 0 ; ;

    int track = 0 ;
    int correct = 0 ;
    int wrong = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_back);

        startSequence = (Button) findViewById(R.id.buttonStartSequence);
        exitButton = (Button) findViewById(R.id.exitButton);
        tapButton = (Button) findViewById(R.id.buttonTap);

        sequence = new Character[15];

        correctText = (TextView) findViewById(R.id.textCorrect) ;
        wrongText = (TextView) findViewById(R.id.textWrong) ;

        explain = (TextView) findViewById(R.id.textExplain);
        tvCount = (TextView) findViewById(R.id.textCount);
        seqText = (TextView) findViewById(R.id.textSequence);
        count = 3;

        tvCount.setText("");
        seqText.setText("");
        tapButton.setVisibility(View.INVISIBLE);
        correctText.setVisibility(View.INVISIBLE);
        wrongText.setVisibility(View.INVISIBLE);

        //Changes screens when button is clicked.
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitPressed();
            }
        });

        startSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceButton();
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
    public void exitPressed() {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        exitButton.startAnimation(myAnim);

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
            correct = 0 ;
            wrong = 0 ;

            /* Call radomizeSeq function with a string of letters. Random letters are chosen out of
               the string and added to the sequence array.*/
            randomizeSeq("ABCDEFG");

            correctText.setVisibility(View.INVISIBLE);
            wrongText.setVisibility(View.INVISIBLE);

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

    public void bounceButton() {
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
                explain.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                correctText.setVisibility(View.INVISIBLE);
                wrongText.setVisibility(View.INVISIBLE);

                startPressed();
            }
        });
    }


    public synchronized void sequenceRepeat() {

        if(countSeq == sequence.length){
            countSeq = 0 ;
            seqText.setText("") ;
            correctText.setText("CORRECT: " + correct) ;
            wrongText.setText("WRONG: " + wrong);
            startSequence.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
            tapButton.setVisibility(View.INVISIBLE);
            explain.setText("YOUR SCORES ARE IN\nTO PLAY AGAIN PRESS START\nOTHERWISE PRESS EXIT");
            explain.setVisibility(View.VISIBLE);
            correctText.setVisibility(View.VISIBLE);
            wrongText.setVisibility(View.VISIBLE);
            return ;
        }

        //Animation for sequence
        AlphaAnimation myAnim3 = new AlphaAnimation(1.0f, 0.0f);
        myAnim3.setDuration(750);

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
            if(!(countSeq % 2 == 1)) {
                if (countSeq > 0) {
                    if (track == 1) {
                        int flag = 0;
                        for (int i = 0; i < countSeq; i++) {
                            if (sequence[countSeq] == sequence[i]) {
                                flag = 1;
                                correct++;
                                break;
                            }
                        }
                        if (flag == 0) {
                            wrong++;
                        }
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

    public void randomizeSeq(String letters) {
        //Create new randomize object
        Random rand = new Random();

        for (int i = 0; i < sequence.length; i++) {
            if (i % 2 == 1) {
                sequence[i] = '.';
            } else {
                sequence[i] = letters.charAt(rand.nextInt(5));
            }
        }
    }
}

