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

import java.util.List;

public class GameSetupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner chooseSpinner;
    public static FeaturesContainer current;
    private Polygon range_of_game;
    private FusedLocationProviderClient locator;
    private Circle locCircle;
    private Marker locMarker;
    private LatLng position;
    private LocationCallback callback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            if(locationResult.getLastLocation() != null){
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                double accuracy = locationResult.getLastLocation().getAccuracy();
                position = new LatLng(lat, lon);
                if (locCircle != null)
                    locCircle.remove();
                if (locMarker != null)
                    locMarker.remove();
                locCircle = mMap.addCircle(new CircleOptions()
                                    .center(position)
                                    .fillColor(0x220000FF)
                                    .radius(accuracy)
                                    .strokeWidth(0.0f));
                locMarker = mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                    .title("Tu jesteś"));
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
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.setupMap);
        mapFragment.getMapAsync(this);
        Log.d(TAG, mapFragment.toString());
        chooseSpinner = findViewById(R.id.setChoose);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.game_sets, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        chooseSpinner.setAdapter(adapter);
        chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                if (range_of_game != null)
                    range_of_game.remove();
                String json = "";
                String name = "";
                switch (parent.getItemAtPosition(pos).toString()) {
                    case "Politechnika Warszawska":
                        json = getResources().getString(R.string.Politechnika_Warszawska_set);
                        name = "Politechnika Warszawska";
<<<<<<< HEAD
                        break;//Break jest konieczny do zamkniecia funkcji switch, ale tutaj powoduje zatrzymanie aplikacji
                    //czyzby JSON byl zly? Sprawdzalem 3-krotnie!
                    case "Pole Mokotowskie":
                        json = getResources().getString(R.string.Pole_Mokotowskie_set);
                        name = "Pole Mokotowskie";
                   /* default:
                        return;*/
=======
                        break;
                    case "Pole Mokotowskie":
                        json = getResources().getString(R.string.Pole_Mokotowskie_set);
                        name = "Pole Mokotowskie";
                        break;
>>>>>>> 6902fa8652b7f422727b1fcffedcebee1c3bceb4
                }
                current = new FeaturesContainer(name, json);
                range_of_game = mMap.addPolygon(current.range);
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
        Button beginGame = (Button) findViewById(R.id.BeginGameButton);
        beginGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == null){
                    createDialog();
                    return;
                }
                else if(!is_in_a_range()){
                    createRangeDialog();
                    return;
                }
                makeIntent();
            }
        });
        locator = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setInterval(1000);

        locator.requestLocationUpdates(request, callback, null);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pos = new LatLng(52.25, 21.0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
    }

    public static LatLngBounds getBoundsFromPolygon(LatLng position, List<LatLng> points){
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
        Intent startGame = new Intent(this, MapsActivity.class);
        startGame.putExtra("lat", position.latitude);
        startGame.putExtra("lon", position.longitude);
        startActivity(startGame);
        finish();
    }
    private void createDialog(){
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
                double lat3 = points.get((++it)%points.size()).latitude;
                if (lon3 > lon0 && lon2 < lon0 || lon3 < lon0 && lon2 > lon0){
                    count++;
                }
            }
        }
        return count%2 == 1;
    }
}
