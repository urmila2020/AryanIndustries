package com.org.aryansoft.foodonwheels;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.org.aryansoft.foodonwheels.Utils.GPSTracker;
import com.org.aryansoft.foodonwheels.Utils.Utility;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "DashboardActivity";
    private Context context;
    private Boolean doubleBackToExitPressedOnce = false;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    String mPermission2 = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        init();

    }

    Geocoder geocoder;
    List<Address> addresses;
    TextView tvCurrentAddress;
    EditText etSearchRestaurant;

    private void init() {
        tvCurrentAddress = findViewById(R.id.tvCurrentAddress);
        etSearchRestaurant = findViewById(R.id.etSearchRestaurant);
        try {
            if (ActivityCompat.checkSelfPermission(DashboardActivity.this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{mPermission, mPermission2},
                        REQUEST_CODE_PERMISSION);
            } else {
                getFormattedAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void getFormattedAddress(){
        // create class object
        geocoder = new Geocoder(this, Locale.getDefault());
        String tmpAddress="";
        String location = "";
        GPSTracker gps = new GPSTracker(this);
        double latitude;
        double longitude;

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude  = gps.getLatitude();
            longitude = gps.getLongitude();

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                Log.d(TAG, "getAddress: " + addresses);
                if (!addresses.isEmpty()) {
                    tmpAddress = addresses.get(0).getAddressLine(0)
//                            + "\nlocality: " + addresses.get(0).getLocality() + "\nAdminArea: "
//                            + addresses.get(0).getAdminArea() + "\nSubAdminArea: " + addresses.get(0).getSubAdminArea() + "\nCountry: "
//                            + addresses.get(0).getCountryName() + "\nFeatureName:" + addresses.get(0).getFeatureName() + "\nPostal: "
//                            + addresses.get(0).getPostalCode() + "\nSubLocality:" + addresses.get(0).getSubLocality() + "\nPremises:"
//                            + addresses.get(0).getPremises() + "\nThoroughfare:" + addresses.get(0).getThoroughfare()
                    ;

                    tvCurrentAddress.setText(tmpAddress);
                    location = addresses.get(0).getLocality();// gets city

                } else {
                    Toast.makeText(context, "Could not detect location. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Utility.saveOnSharedPreferences(context, tmpAddress, "last_known_address");
            Utility.saveOnSharedPreferences(context, location, "last_known_city");
            Utility.saveOnSharedPreferences(context, latitude, "last_known_latitude");
            Utility.saveOnSharedPreferences(context, longitude, "last_known_longitude");


        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
//                startActivity(new Intent(DrawerActivity.this, TripActivity.class));
        } else if (id == R.id.nav_gallery) {
//            startActivity(new Intent(DrawerActivity.this, HomeActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}

