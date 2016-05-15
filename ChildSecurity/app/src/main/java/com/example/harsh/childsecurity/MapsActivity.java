package com.example.harsh.childsecurity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout maps;
    LinearLayout.LayoutParams layoutParams;
    ParseUser parseUser;
    Button tracking,report_missing,report_child_labor;
    public static Double latitude;
    public static Double longitude;
    public static String username;
    public static GoogleMap googleMap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitude = 0.0;
        longitude = 0.0;
        username = "";

        maps = (LinearLayout) findViewById(R.id.maps);
        layoutParams = (LinearLayout.LayoutParams) maps.getLayoutParams();

        parseUser = ParseUser.getCurrentUser();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels;
        float dpWidth = outMetrics.widthPixels;

        layoutParams.height = (int) (dpHeight - 175 * density);
        layoutParams.width = (int) dpWidth;
        maps.setLayoutParams(layoutParams);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            getLastKnownLocation();
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);

        tracking = (Button) findViewById(R.id.tracking_button);
        report_missing=(Button)findViewById(R.id.reportmissing);
        report_child_labor=(Button)findViewById(R.id.reportchildlabor);


        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TrackingActivity.class));
            }
        });

        report_missing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), First.class));
            }
        });

        report_child_labor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EmailActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (latitude != 0.0 && longitude != 0.0) {
                googleMap1.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(username));
                LatLng source = new LatLng(latitude, longitude);
                googleMap1.moveCamera(CameraUpdateFactory.newLatLng(source));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(source, 15.0f);
                googleMap1.animateCamera(update);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        Location location = null;
        try {
            location = getLastKnownLocation();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (location != null) {
            double clati = location.getLatitude();
            double clongi = location.getLongitude();

            LatLng source = new LatLng(clati, clongi);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(source));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(source, 15.0f);
            googleMap.animateCamera(update);
        }
        googleMap1 = googleMap;
    }

    private Location getLastKnownLocation() throws SecurityException {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location bestLocation = null;
        for (String provider : mLocationManager.getProviders(true)) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l != null && (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())) {
                bestLocation = l;
            }
        }
        assert bestLocation != null;
        parseUser.put("latitude", bestLocation.getLatitude());
        parseUser.put("longitude", bestLocation.getLongitude());
        parseUser.saveInBackground();
        return bestLocation;
    }
}
