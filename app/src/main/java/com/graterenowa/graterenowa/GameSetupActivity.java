package com.graterenowa.graterenowa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//Activity służące do wyboru zestawu gry

public class GameSetupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static FeaturesContainer current;
    private Polygon range_of_game;
    private SetsHandler sh;
    private LocIndicator locIndicator;
    private LatLng position;
    private Spinner chooseSpinner;
    private ArrayAdapter<String> chooseSpinnerAdapter;
    private LocationCallback callback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            //Po każdej lokalizacji należy:
            if(locationResult.getLastLocation() != null){
                //Pobrać szerokość i długość geograficzną oraz dokładność lokalizacji
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                double accuracy = locationResult.getLastLocation().getAccuracy();
                //Przypisanie współrzędnych do pozycji
                position = new LatLng(lat, lon);
                //Ustawienie pozycji
                if (locIndicator != null)
                    locIndicator.move(mMap, position, accuracy);
                else
                    locIndicator = new LocIndicator(mMap, position, accuracy);

            }
            else{
                Log.d(TAG, "Brak wspolrzednych.");
            }
        }
    };
    public static String TAG = "GameSetup";
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        //Pobranie map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.setupMap);
        mapFragment.getMapAsync(this);
        //Pobór zestawów z firebase'a
        sh = new SetsHandler(this);

        //Ustawienie opcji spinnera
        chooseSpinner = findViewById(R.id.setChoose);
        chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                int pos, long id) {
                // Jeżeli jest poligon, to go usuń
                if (range_of_game != null)
                    range_of_game.remove();
                //Pobranie nazwy ze spinnera i zestawu z SetsHandlera
                String name = parent.getItemAtPosition(pos).toString();
                JSONObject json = sh.getSet(name);
                //Ustawienie zestawu obecnego na wybrany
                current = new FeaturesContainer(name, json);
                //Rysowanie zasięgu gry
                range_of_game = mMap.addPolygon(current.range);
                //Ustawienie okna mapy na takie, by pokazywało poligon i pozycję
                List<LatLng> points = range_of_game.getPoints();
                LatLngBounds bounds;
                if (position != null)
                    bounds = getBoundsFromPolygon(position, points);
                else
                    bounds = getBoundsFromPolygon(points.get(0), points);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));

            }
            public void onNothingSelected(AdapterView<?> parent) {
               // Another interface callback
            }
        });
        //Znalezienie najbliższego - callback
        Button findClosest = (Button) findViewById(R.id.findClosestButton);
        findClosest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == null || chooseSpinnerAdapter == null){
                    //Bez tych danych nie da się znaleźć najbliższego zestawu
                    noDataDialog();
                }
                //Wyszukiwanie zestawu najbliższego w prostej linii
                ArrayList<LatLng> centroids = sh.getCentroids();
                double dist_min = 99999999999999999999.0;
                int min_idx = -1;
                for (int i = 0; i < centroids.size(); ++i){
                    double dist = myAdapter.distance(position.latitude, position.longitude, centroids.get(i).latitude, centroids.get(i).longitude);
                    if (dist < dist_min) {
                        dist_min = dist;
                        min_idx = i;
                    }
                }
                //Ustawienie najbliższego zestawu
                String name = sh.getName(centroids.get(min_idx));
                chooseSpinner.setSelection(chooseSpinnerAdapter.getPosition(name));
            }
        });
        //Rozpoczęcie gry - callback
        Button beginGame = (Button) findViewById(R.id.BeginGameButton);
        beginGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == null){
                    //Nie wybrano zestawu
                    createDialog();
                    return;
                }
                else if(!is_in_a_range()){
                    //Gracz poza zasięgiem
                    createRangeDialog();
                    return;
                }
                makeIntent();
            }
        });
        //Ustalenie opcji lokalizacji
        FusedLocationProviderClient locator = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setInterval(1000);

        locator.requestLocationUpdates(request, callback, null);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Warszawa
        mMap = googleMap;
        LatLng pos = new LatLng(52.25, 21.0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
    }

    public static LatLngBounds getBoundsFromPolygon(LatLng position, List<LatLng> points){
        //Wyszukiwanie bbox poligonu i pozycji
        double latn = position.latitude;
        double lats = position.latitude;
        double lone = position.longitude;
        double lonw = position.longitude;
        for (int i = 0; i < points.size(); ++i){
            if (points.get(i).latitude > latn)
                latn = points.get(i).latitude;
            else if (points.get(i).latitude < lats)
                lats = points.get(i).latitude;
            if (points.get(i).longitude > lone)
                lone = points.get(i).longitude;
            else if (points.get(i).longitude < lonw)
                lonw = points.get(i).longitude;
        }
        LatLng ne = new LatLng(latn, lone);
        LatLng sw = new LatLng(lats, lonw);
        return new LatLngBounds(sw, ne);
    }

    private void makeIntent(){
        //Uruchomienie gry
        Intent startGame = new Intent(this, MapsActivity.class);
        //Dodanie pierwszej pozycji
        startGame.putExtra("lat", position.latitude);
        startGame.putExtra("lon", position.longitude);
        //Inicjalizacja punktów, czasu i list markerów
        MapsActivity.initialize_static_content();
        //Rozpoczęcie gry i koniec obecnego activity
        startActivity(startGame);
        finish();
    }
    private void createDialog(){
        //Informacja, że gracz chciał rozpocząć grę bez wyboru zestawu
        new AlertDialog.Builder(this)
                .setMessage("Nie wybrałeś jeszcze zestawu.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void createRangeDialog(){
        //Informacja, że gracz chciał rozpocząć grę poza obszarem
        new AlertDialog.Builder(this)
                .setMessage("Znajdujesz się poza zasięgiem zestawu. Grę można rozpocząć będąc dopiero w zasięgu zestawu")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private boolean is_in_a_range(){
        //Standardowy algorytm sprawdzający, czy punkt znajduje się wewnątrz poligonu - parzystość przecięć
        List<LatLng> points = range_of_game.getPoints();
        int count = 0;
        if (position == null)
            return false;
        double lon0 = position.longitude;
        double lat0 = position.latitude;
        for (int i = 0; i < points.size(); ++i){
            double lat1 = points.get(i).latitude;
            double lat2 = points.get((i + 1)%points.size()).latitude;
            double lon1 = points.get(i).longitude;
            double lon2 = points.get((i + 1)%points.size()).longitude;

            if (lon1 > lon0 && lon2 < lon0 || lon1 < lon0 && lon2 > lon0) {
                double a = (lon1 - lon2)/(lat1 - lat2);
                double b = lat1 - lon1*a;
                if (lon0*a + b > lat0) {
                    count++;
                }
                else if(lon0*a + b == lat0){
                    return true;
                }
            }
            else if(lon2 == lon0){
                if (lat0 == lat2){
                    return true;
                }
                double lon3 = 0.0;
                int it = i + 1;
                do{
                    lon3 = points.get((++it)%points.size()).longitude;
                }while(lon3 == lon2);
                if (lon3 > lon0 && lon2 < lon0 || lon3 < lon0 && lon2 > lon0){
                    count++;
                }
            }
        }
        return count%2 == 1;
    }

    public void setSpinnerList(List<String> set_names){
        //Funkcja, która służy do ustawienia nazw zestawów w spinnerzez po ich pobraniu
        chooseSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, set_names);
        chooseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSpinner.setAdapter(chooseSpinnerAdapter);
        //Zamiana opisu na stosowny w tej chwili
        TextView chooseSetText = (TextView) findViewById(R.id.setChooseText);
        chooseSetText.setText(R.string.setChooseText);
    }

    private void noDataDialog(){
        new AlertDialog.Builder(this)
                .setMessage("Brak pozycji lub zestawy nie zostały jeszcze pobrane")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}
