package com.graterenowa.graterenowa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static int points;
    private TextView pointsView;
    private TextView gameSetView;
    private TextView totalPointsView;
    private TextView timer;
    public static long starttime;
    private Button goToTasks;
    public static MapsActivity activeMapsActivity;
    private FusedLocationProviderClient locator;
    private Circle locCircle;
    private Marker locMarker;
    private LatLng position;
    private double accuracy;
    private LocationCallback callback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            if(locationResult.getLastLocation() != null){
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                accuracy = locationResult.getLastLocation().getAccuracy();
                position = new LatLng(lat, lon);
                if (locCircle != null)
                    locCircle.remove();
                if (locMarker != null)
                    locMarker.remove();
                locCircle = mMap.addCircle(new CircleOptions()
                        .center(position)
                        .fillColor(Color.BLUE)
                        .radius(accuracy)
                        .strokeWidth(0.0f));
                locMarker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Tu jesteś"));
            }
        }
    };

    class TimeUpdater extends TimerTask {

        @Override
        public void run() {
            MapsActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    long millis = System.currentTimeMillis() - starttime;
                    int seconds = (int) (millis / 1000);
                    int totalpoints = points + 3600 - seconds;
                    int minutes = seconds / 60;
                    int hours = minutes / 60;
                    seconds = seconds % 60;
                    minutes = minutes % 60;

                    timer.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));
                    totalPointsView.setText(String.valueOf(totalpoints));
                }
            });
        }
    };

    public void updatePoints(){
        pointsView.setText(String.valueOf(points));
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activeMapsActivity = this;
        setContentView(R.layout.activity_maps);
        points = 0;
        pointsView = (TextView) findViewById(R.id.pointsView);
        updatePoints();
        starttime = System.currentTimeMillis();
        timer = (TextView) findViewById(R.id.timeView);
        gameSetView = (TextView) findViewById(R.id.gameSetLabel);
        gameSetView.setText(GameSetupActivity.current.name);
        totalPointsView = (TextView) findViewById(R.id.pointsTotalView);
        Timer time = new Timer();
        time.schedule(new TimeUpdater(), 0,500);
        goToTasks = (Button) findViewById(R.id.commissionButton);
        goToTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntent();
            }
        });
        locator = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setInterval(1000);

        locator.requestLocationUpdates(request, callback, null);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.gameMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Polygon range_of_game = mMap.addPolygon(GameSetupActivity.current.range);
        List<LatLng> points = range_of_game.getPoints();
        LatLngBounds bounds = GameSetupActivity.getBoundsFromPolygon(points.get(0), points);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    @Override
    public void onBackPressed(){
        return;
    }

    private void makeIntent(){
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
    }

    public LatLng getPosition(){
        return position;
    }

    public double getAccuracy(){
        return accuracy;
    }
}
