package com.example.wiehan.tinnitusmanagement;

/**
 * Created by Wiehan on 3/7/2017.
 */

class MyBounceInterpolator implements android.view.animation.Interpolator {
    double Amplitude = 5;
    double Frequency = 5;

    MyBounceInterpolator(double amplitude, double frequency) {
        Amplitude = amplitude;
        Frequency = frequency;
    }


    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ Amplitude) *
                Math.cos(Frequency * time) + 1);
    }
}
