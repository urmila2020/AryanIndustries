package com.org.aryansoft.foodonwheels;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.org.aryansoft.foodonwheels.Utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Locale;


public class MyApp extends Application {
    private static MyApp instance = null;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
//        new ErrorLogger(this);
        Log.d("MyApp", "onCreate");

        //default language english
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

    }



    public static MyApp getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }


}
