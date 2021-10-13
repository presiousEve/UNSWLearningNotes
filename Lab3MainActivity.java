package com.example.jackchu.lab3_gps;

import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    Location location;
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final TextView GPS_status = (TextView) findViewById(R.id.GPS_status);
        final TextView locationInfo= (TextView) findViewById(R.id.locationInfo);
        Button btnGetGpsStatus = (Button) findViewById(R.id.btnGpsStatus);
        final Button btnGetLocation = (Button) findViewById(R.id.btnLocation);


        btnGetGpsStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            //get GPS info, show GPS status
            public void onClick(View view) {
                if (GpsStatus()) {
                    GPS_status.setText("GPS is active.");
                    btnGetLocation.setVisibility(View.VISIBLE);
                    btnGetLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String txt = GetLocation();
                            locationInfo.setText(txt);
                        }
                    });
                } else
                    showAlertDialog();
            }
        });
    }


    public String GetLocation() {
        Toast.makeText(getApplicationContext(), "Fetching location details, please wait a minute.", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //check GPS
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //check Network
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//声明输出信息
        Double altitude = 0.0;
        Double longitute = 0.0;
        Double latitude = 0.0;
        long time = 0;
        String provider = "";
        float speed = 0f;
        float accuracy = 0f;

//先用Network再用GPS查找Location信息
        if (!isGPSEnabled && !isNetworkEnabled) {
            return "can not get your location.";
        } else {
        //Network可用
            if (isNetworkEnabled) {
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            5000,
                            5, listener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location != null) {
                        altitude = location.getAltitude();
                        latitude = location.getLatitude();
                        longitute = location.getLongitude();
                        time = location.getTime();
                        provider = location.getProvider();
                        speed = location.getSpeed();
                        accuracy = location.getAccuracy();
                    }else{
                        return "Error 001: location==null";
                    }
                }else{
                    return "Error 002: locationManager==null";
                }
            }else{
            //GPS可用
                //GPSEnabled == true
                if(location==null){
                    if(locationManager!=null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                5000,
                                5,  listener);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location!=null){
                            altitude = location.getAltitude();
                            latitude = location.getLatitude();
                            longitute = location.getLongitude();
                            time = location.getTime();
                            provider = location.getProvider();
                            speed = location.getSpeed();
                            accuracy = location.getAccuracy();
                        }else{
                            return "Error 004: location == null";
                        }
                    }else{
                        return "Error 003: locationmanager == null";
                    }
                }
            }
        }

    //get Time Info，from Unix form to normal form
        Timestamp timestamp = new Timestamp(time);
        return "Date/Time: "+timestamp+"\nProvider: "+provider+"\nAccuracy: "+accuracy+"\nLatitude: "+latitude+"\nLongtitude: "
                +longitute+"\nAltitude: "+altitude+"\nspeed: "+speed;

    }

//get GPS info
    public boolean GpsStatus(){
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps_status;
    }


    public void showAlertDialog(){
//        String mss = "GPS is not enabled. Do you want to go to settings menu?";
        String mss = "GPS is not enabled. Please turn on the GPS service and try again.";
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Setting the GPS");
        mBuilder.setMessage(mss);

        /*
        Dialog with two buttons, optional part.
         */
//        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        mBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(), "good for now", Toast.LENGTH_SHORT).show();
//            }
//        });

        /*
        Dialog with one button.
         */
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.create().show();
    }

}
