package com.example.wiehan.tinnitusmanagement;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ResourceBundle;

/**
 * Created by wiehan on 2017/06/10.
 */

/*Tutorial manager used for tutorial screens to make sure tutorial screens only display automatically once after initial installation.
* After the app is opened for the first time, setFirst will be set to false from BreathTutorialOne.
 */
public class TutorialManager {

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    Context context ;

    public TutorialManager(Context context) {
        this.context = context ;
        pref = context.getSharedPreferences("first",0) ;
        editor = pref.edit();
    }

    public void setFirst(boolean isFirst) {
        editor.putBoolean("check", isFirst) ;
        editor.commit();
    }

    public boolean Check() {
        return pref.getBoolean("check", true) ;
    }

}

