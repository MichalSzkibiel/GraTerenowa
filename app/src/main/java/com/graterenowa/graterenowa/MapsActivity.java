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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

//Activity z widokiem mapy gry

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static int points;
    private TextView pointsView;
    private TextView totalPointsView;
    private TextView timer;
    public static long starttime;
    public static MapsActivity activeMapsActivity;
    private LocIndicator locIndicator;
    private LatLng position;
    private double accuracy;
    private static List<MarkerOptions> green_markers;
    private static List<MarkerOptions> red_markers;
    private LocationCallback callback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            //Po każdej lokalizacji należy:
            if(locationResult.getLastLocation() != null){
                //Pobrać szerokość i długość geograficzną oraz dokładność lokalizacji
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                accuracy = locationResult.getLastLocation().getAccuracy();
                //Przypisanie współrzędnych do pozycji
                position = new LatLng(lat, lon);
                //Ustawienie pozycji
                if (locIndicator != null)
                    locIndicator.move(mMap, position, accuracy);
                else
                    locIndicator = new LocIndicator(mMap, position, accuracy);

            }
        }
    };

    class TimeUpdater extends TimerTask {
        @Override
        public void run() {
            MapsActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Pobór danych dotyczących czasu, aktualizacja timera i całkowitej ilości punktów
                    long millis = System.currentTimeMillis() - starttime;
                    int seconds = (int) (millis / 1000);
                    int totalpoints = 3600 - seconds;
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

    public static void initialize_static_content(){
        //Funkcja, która inicjalizuje zmienne statyczne - przed pierwszym rozpoczęciem Activity
        points = 0;
        starttime = System.currentTimeMillis();
        green_markers = new ArrayList<MarkerOptions>();
        red_markers = new ArrayList<MarkerOptions>();
    }

    public void updatePoints(boolean found, int idx, LatLng position){
        //Gdy dzieje się jakieś zgłoszenie, to ta funkcja robi zmiany na mapie
        //Aktualizacja liczby punktów
        pointsView.setText(String.valueOf(points));
        if (position != null){
            //Jeżeli jest null, to było wywołanie z funkcji OnCreate
            //Jeżeli nie null, to było zgłoszenie
            if (found){
                //Ustawienie markera dobrej odpowiedzi
                //zielony
                //nazwa - klucz
                MarkerOptions new_marker = new MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(GameSetupActivity.current.elements.get(idx).name + "\n" + GameSetupActivity.current.elements.get(idx).quest);
                //Wstawienie markera na mapę i dodanie do listy zielonych markerów
                mMap.addMarker(new_marker);
                green_markers.add(new_marker);
            }
            else{
                //Ustawienie markera złej odpowiedzi
                //czerwony
                //klucz
                MarkerOptions new_marker = new MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(GameSetupActivity.current.elements.get(idx).quest);
                //Wstawienie markera na mapę i dodanie do listy czerwonych markerów
                mMap.addMarker(new_marker);
                red_markers.add(new_marker);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        //Pobranie pozycji z intentu
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lon = intent.getDoubleExtra("lon", 0.0);
        position = new LatLng(lat, lon);
        //Przypisanie wskazania na activeMapsActivity
        activeMapsActivity = this;
        //Uzupełnienie podglądu punktów
        pointsView = (TextView) findViewById(R.id.pointsView);
        updatePoints(false, -1, null);
        //Ustawienie tytułu zestawu
        TextView gameSetView = (TextView) findViewById(R.id.gameSetLabel);
        gameSetView.setText(GameSetupActivity.current.name);
        //Ustawienia podglądu czasu
        timer = (TextView) findViewById(R.id.timeView);
        totalPointsView = (TextView) findViewById(R.id.pointsTotalView);
        Timer time = new Timer();
        time.schedule(new TimeUpdater(), 0,500);
        //Ustawienia przycisku do podglądu zadań
        Button goToTasks = (Button) findViewById(R.id.commissionButton);
        goToTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntent();
            }
        });
        //Ustawienia lokalizatora
        FusedLocationProviderClient locator = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        locator.requestLocationUpdates(request, callback, null);
        //Pobranie map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.gameMap);
        mapFragment.getMapAsync(this);
        //Rysowanie zielonych markerów
        for (int i = 0; i < green_markers.size(); ++i){
            mMap.addMarker(green_markers.get(i));
        }
        //Rysowanie czerwonych markerów
        for (int i = 0; i < red_markers.size(); ++i){
            mMap.addMarker(red_markers.get(i));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Rysowanie według pewnego stylu
        mMap = googleMap;
        mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.map_style)));
        //Narysowanie poligonu
        Polygon range_of_game = mMap.addPolygon(GameSetupActivity.current.range);
        //Pobór bbox poligonu
        List<LatLng> points = range_of_game.getPoints();
        LatLngBounds bounds = GameSetupActivity.getBoundsFromPolygon(points.get(0), points);
        //Scentrowanie kamery na obszar gry
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    @Override
    public void onBackPressed(){
        //Wyłączenie możliwości wyłączenia ekranu poprzez wciśnięcie przycisku back
    }

    private void makeIntent(){
        //Uruchomienie listy zadań
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
