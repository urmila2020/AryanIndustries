package com.org.aryansoft.foodonwheels.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utility {

    public static void saveOnSharedPreferences(Context context, Object data, String NameOfString) {
        if(data instanceof Float){
            SharedPreferences.Editor editor = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE).edit();
            editor.putFloat(NameOfString, (Float)data);
            editor.apply();
        }
        else  if(data instanceof Integer){
            SharedPreferences.Editor editor = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE).edit();
            editor.putInt(NameOfString, (int)data);
            editor.apply();
        }
        else  if(data instanceof String){
            SharedPreferences.Editor editor = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE).edit();
            editor.putString(NameOfString, (String)data);
            editor.apply();
        }
        else  if(data instanceof Boolean){
            SharedPreferences.Editor editor = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE).edit();
            editor.putBoolean(NameOfString, (Boolean) data);
            editor.apply();
        }
        else  if(data instanceof Double){
            SharedPreferences.Editor editor = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE).edit();
            editor.putLong(NameOfString, Double.doubleToRawLongBits((Double) data));
            editor.apply();
        }
    }

    public static Object getFromSharedPreferences(Context context, String NameOfString, Constants.DataType dataType, Object defaultData){
        Object object = null;
        SharedPreferences prefs = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE);
        switch (dataType){
            case FLOAT:
                object  = prefs.getFloat(NameOfString, (float)defaultData);
                break;
            case INT:
                object  = prefs.getInt(NameOfString, (int) defaultData);
                break;
            case STRING:
                object  = prefs.getString(NameOfString, (String) defaultData);
                break;
            case BOOLEAN:
                object  = prefs.getBoolean(NameOfString, (Boolean) defaultData);
                break;
        }
        return  object;
    }

    public static void showDialog(String msg, Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static String getDate(String strDate){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate;
        String s="";
        try {
            startDate = (Date)formatter.parse(strDate);
            Format formattera = new SimpleDateFormat("dd MMM");
            s = formattera.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  s;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isDeviceOnline(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());
        if(!isOnline){
            return false;
        }

        return isOnline;
    }

}
