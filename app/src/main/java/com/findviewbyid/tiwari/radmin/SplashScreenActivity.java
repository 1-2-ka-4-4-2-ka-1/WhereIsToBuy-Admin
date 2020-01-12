package com.findviewbyid.tiwari.radmin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {



    AnimatedVectorDrawable avd1;
    AnimatedVectorDrawableCompat avd2;

    public ImageView splash_logo;
    public static AppCompatActivity activity_splash;


    public static final String SAVED_PASS = "saved_password";
    public static final String LOGGED_IN = "logged_in";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences(SAVED_PASS, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //activity finish from another
        activity_splash = this;

        splash_logo = findViewById(R.id.id_splash_logo);
        animate(splash_logo);

    }


    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer>
    {
        @Override
        protected Integer doInBackground(Integer... params) {
            boolean loggged =checkLoggedIn();
            if (loggged)
            {
                return 1;
            }
            else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 0)
            {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, splash_logo,"logoTransition");
                startActivity(intent, options.toBundle());
                // SplashScreenActivity.this.finish();
            }
            else {
                Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, splash_logo,"logoTransition");
                startActivity(intent, options.toBundle());
            }
        }
    }



    public boolean checkLoggedIn(){

        boolean is_user_loggedIn=  sharedPreferences.getBoolean(LOGGED_IN,false);
        if(is_user_loggedIn)
        {
            return true;
        }
        else return false;
    }



    public void animate(final ImageView view) {


        ImageView v = view;
        Drawable d = v.getDrawable();


        if (d instanceof AnimatedVectorDrawableCompat) {

            avd2 = (AnimatedVectorDrawableCompat) d;
            avd2.start();
            avd2.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    avd2.stop();
                    new MyAsyncTask().execute();
                    Log.i("msg","1");
                }
            });
        }
        else if (d instanceof AnimatedVectorDrawable) {
            avd1 = (AnimatedVectorDrawable) d;
            avd1.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                avd1.registerAnimationCallback(new Animatable2.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        avd1.stop();
                        new MyAsyncTask().execute();
                        Log.i("msg","2");
                    }
                });
            }
        }
    }




}
