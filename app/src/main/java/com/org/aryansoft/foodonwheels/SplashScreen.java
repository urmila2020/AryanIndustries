package com.org.aryansoft.foodonwheels;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.org.aryansoft.foodonwheels.Utils.Utility;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private Context context;
    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        context = this;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Utility.isDeviceOnline(context)) {
                    startActivity(new Intent(context, DashboardActivity.class));
                    finish();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("No Internet Connection!");
                    alertDialog.setMessage("Please check your internet connection and try again.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(new Intent(context, SplashScreen.class));
                                    finish();
                                }
                            });
                    alertDialog.show();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
