package com.murali.hariprahlad.gbiadmin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Hari Prahlad on 30-12-2016.
 */

public class CheckActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(firstRun==false)//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
            Intent i=new Intent(CheckActivity.this,SplashActivity.class);
            startActivity(i);
            finish();
        }
        else
        {

            Intent a=new Intent(CheckActivity.this,MainActivity.class);
            startActivity(a);
            finish();
        }
    }

}
