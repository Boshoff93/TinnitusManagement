package com.example.wiehan.tinnitusmanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class BreathTutorialOne extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TutorialManager tutorialManager;
    private TextView[] dots;
    Button next, skip;
    int globalCount = 0 ;
    private int desiredSpeed = 800;
    private LinearLayout dotsLayout;
    private int[] layouts;
    int tutorialFlag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstTime = MyPreferences.isFirst(BreathTutorialOne.this);

        if (isFirstTime) {
            MainScreen.writeFile("Tutorial Started");
            MainScreen.writeFile("Tutorial 1");
            tutorialFlag = 1;

            tutorialManager = new TutorialManager(this);
            if (!tutorialManager.Check()) {
                tutorialManager.setFirst(false);
                Intent i = new Intent(BreathTutorialOne.this, BreathControl.class);
                startActivity(i);
                finish();
            }

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            setContentView(R.layout.activity_tutorial);

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            dotsLayout = (LinearLayout) findViewById(R.id.LayoutDots);
            skip = (Button) findViewById(R.id.buttonSkip);
            next = (Button) findViewById(R.id.buttonNext);
            layouts = new int[]{R.layout.activity_breath_tutorial_one, R.layout.activity_breath_tutorial_two, R.layout.activity_breath_tutorial_three,
                    R.layout.activity_breath_tutorial_four, R.layout.activity_breath_tutorial_demo};

            addBottomDots(0);
            changeStatusBarColor();
            viewPagerAdapter = new ViewPagerAdapter();
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(viewListener);


            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(BreathTutorialOne.this, BreathControl.class);
                    startActivity(i);
                    finish();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        viewPager.setCurrentItem(current);
                    } else {
                        Intent i = new Intent(BreathTutorialOne.this, BreathControl.class);
                        startActivity(i);
                        finish();
                    }

                }
            });
        } else {
            Intent i = new Intent(BreathTutorialOne.this, BreathControl.class);
            startActivity(i);
        }
    }

    private void addBottomDots(int position) {

        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0) {
            dots[position].setTextColor(colorActive[position]);
        }

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                next.setText("PROCEED");
                skip.setVisibility(View.GONE);
                final View view = findViewById(R.id.bubbleTutDemo);
                final TextView textViewAnim = (TextView) findViewById(R.id.tvAnim);
                final Animation scaleBigger = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_bigger);
                final Animation scaleSmaller = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_smaller);
                final Animation breatheInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.countdown);
                scaleBigger.setDuration(2000);
                scaleSmaller.setDuration(2000);
                expandBubble(view, textViewAnim, scaleBigger, scaleSmaller, breatheInAnim);
            } else if (position == 3) {
                next.setTextColor(Color.parseColor("#ffffff"));
                skip.setTextColor(Color.parseColor("#ffffff"));
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
                globalCount = 0 ;
            } else {
                next.setText("NEXT");
                next.setTextColor(Color.parseColor("#000000"));
                skip.setTextColor(Color.parseColor("#000000"));
                skip.setVisibility(View.VISIBLE);
                globalCount = 0 ;
            }

            if(position == 0) {
                MainScreen.writeFile("Tutorial 1");
                tutorialFlag = 1;
            } else if (position == 1) {
                MainScreen.writeFile("Tutorial 2");
                tutorialFlag = 1;
            } else if (position == 2) {
                MainScreen.writeFile("Tutorial 3");
                tutorialFlag = 1;
            } else if (position == 3) {
                MainScreen.writeFile("Tutorial 4");
                tutorialFlag = 1;
            } else if (position == 4) {
                MainScreen.writeFile("Tutorial 5");
                tutorialFlag = 1;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }

    private void expandBubble(final View view, final TextView textViewAnim, final Animation scaleBigger, final Animation scaleSmaller, final Animation breatheInAnim) {

        textViewAnim.setText("BREATHE IN");
        textViewAnim.startAnimation(breatheInAnim);

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
                if (scaleBigger.getDuration() < desiredSpeed) {
                    scaleBigger.setDuration(scaleBigger.getDuration() + 500);
                }
                contractBubble(view, textViewAnim, scaleBigger, scaleSmaller, breatheInAnim);
            }
        });

        breatheInAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Once Animation ends change screens
            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }

    public void contractBubble(final View view, final TextView textViewAnim, final Animation scaleBigger, final Animation scaleSmaller, final Animation breatheInAnim) {

        textViewAnim.setText("BREATHE OUT");
        textViewAnim.startAnimation(breatheInAnim);

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

                if (scaleSmaller.getDuration() < desiredSpeed) {
                    scaleSmaller.setDuration(scaleSmaller.getDuration() + 500);
                }
                if (globalCount < 4) {
                    expandBubble(view, textViewAnim, scaleBigger, scaleSmaller, breatheInAnim);
                }
            }
        });

        breatheInAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //Once Animation ends change screens
            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }

    @Override
    protected void onPause() {
        if(tutorialFlag == 1) {
            MainScreen.writeFile("Tutorial Closed");
            tutorialFlag = 0 ;
        }
        super.onPause();
    }

}