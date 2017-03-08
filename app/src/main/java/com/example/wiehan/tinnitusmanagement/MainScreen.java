package com.example.wiehan.tinnitusmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    Button start ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        start = (Button)findViewById(R.id.buttonMain);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonAnimation(start);
            }
        });
    }

    public void startButtonAnimation(Button startButton) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        startButton.startAnimation(myAnim);

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
}
