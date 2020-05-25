package com.org.aryansoft.foodonwheels;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.org.aryansoft.foodonwheels.Models.Restaurant;
import com.org.aryansoft.foodonwheels.Utils.GPSTracker;
import com.org.aryansoft.foodonwheels.Utils.Utility;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

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

            //get restaurant list
            new LoadRestaurantsByLocation().execute(String.valueOf(latitude), String.valueOf(longitude));

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private class LoadRestaurantsByLocation extends AsyncTask<String, Void, String> {

        ProgressDialog AsycDialog = new ProgressDialog(context);
        List<Restaurant> hotelList = new ArrayList<>();
        RecyclerView rvRestaurantList;
        ImageView ivEmptyView;
        TextView tvEmptyView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AsycDialog.setCancelable(false);
            AsycDialog.show();
            rvRestaurantList = findViewById(R.id.rvRestaurantList);
            ivEmptyView = findViewById(R.id.ivEmptyView);
            tvEmptyView = findViewById(R.id.tvEmptyView);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("https://www..php");
                String latitude = urls[0];
                String longitude = urls[1];

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(30000);
                urlConnection.setConnectTimeout(30000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write( "&latitude=" + latitude + "&longitude=" + longitude );

                writer.flush();
                writer.close();
                os.close();

                int responseCode=urlConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    String strReturn = stringBuilder.toString();

                    try {
                        hotelList.clear();
                        JSONArray valarray = new JSONArray(strReturn);
                        for (int i = 0; i < valarray.length(); i++) {
                            String restaurantId = valarray.getJSONObject(i).optString("restaurantId");
                            String restaurantName = valarray.getJSONObject(i).optString("restaurantName");
                            String restaurantImage = valarray.getJSONObject(i).optString("restaurantImage");
                            String restaurantDescription = valarray.getJSONObject(i).optString("restaurantDescription");
                            double rating = valarray.getJSONObject(i).optDouble("rating");
                            boolean isVegOnly = valarray.getJSONObject(i).optBoolean("isVegOnly");
                            boolean isClosed = valarray.getJSONObject(i).optBoolean("isClosed");

                            //if error
                            String message = valarray.getJSONObject(i).optString("message");

                            hotelList.add(new Restaurant(
                                    restaurantId, restaurantName,
                                    restaurantImage, restaurantDescription,
                                    rating, isVegOnly,
                                    isClosed
                            ));

                            if (!message.equals("")){
                                return message;
                            }

                        }
                    } catch (JSONException e) {
                        Log.e("JSON", "There was an error parsing the JSON", e);
                    }
                } else {
                    return String.valueOf(responseCode);
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            AsycDialog.dismiss();

            if (response.equals("Hotel Not Found!!")){
                //no restaurant found at location
                ivEmptyView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.VISIBLE);
                rvRestaurantList.setVisibility(View.GONE);
            } else if (hotelList.isEmpty()) {
                //no restaurant found at location
                ivEmptyView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.VISIBLE);
                rvRestaurantList.setVisibility(View.GONE);
            } else {
                ivEmptyView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.GONE);
                rvRestaurantList.setVisibility(View.VISIBLE);
                RVRestaurantAdapter rvRestaurantAdapter = new RVRestaurantAdapter(context, hotelList);
                rvRestaurantList.setLayoutManager(new GridLayoutManager(context,2));
                rvRestaurantList.setAdapter(rvRestaurantAdapter);
            }
        }
    }

    public class RVRestaurantAdapter extends RecyclerView.Adapter<RVRestaurantAdapter.MyViewHolder> {

        private Context mContext;
        private List<Restaurant> mData;


        public RVRestaurantAdapter(Context context, List<Restaurant> mData) {
            this.mContext = context;
            this.mData = mData;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.row_restaurant_details, parent, false);
            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {


            if (!mData.get(position).getRestaurantImage().equals("")) {
//                Glide.with(mContext)
//                        .load(mData.get(position).getRestaurantImage())
//                        .placeholder().into();
            } else {
                //placeholder image
            }

            //set hotel related data
//            holder.tv_title.setText(mData.get(position).getRestaurantName());

            //set on hotel click listener
//            holder.cardview_id.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (Utility.isDeviceOnline(mContext)) {
//
//                        //send to hotel menu
//
//                    } else {
//                        Toast.makeText(mContext, "No Internet Connection!!", Toast.LENGTH_LONG).show();
//                    }
//
//
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {



            public MyViewHolder(View itemView) {
                super(itemView);
                //initialise components from layout
            }
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

