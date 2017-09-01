package com.example.wiehan.tinnitusmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
                        Intent intent = new Intent(getApplicationContext(), BreathTutorialOne.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}
