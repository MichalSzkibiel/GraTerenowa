package com.graterenowa.graterenowa;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GameSetupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner chooseSpinner;
    public static FeaturesContainer current;
    private Polygon range_of_game;
    public static String TAG = "My";
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
                        return;
                    case "Pole Mokotowskie":
                        json = getResources().getString(R.string.Pole_Mokotowskie_set);
                        name = "Pole Mokotowskie";
                }
                current = new FeaturesContainer(name, json);
                range_of_game = mMap.addPolygon(current.range);
                List<LatLng> points = range_of_game.getPoints();
                LatLngBounds bounds = getBoundsFromPolygon(points);
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
                makeIntent();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pos = new LatLng(52.25, 21.0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
    }

    public static LatLngBounds getBoundsFromPolygon(List<LatLng> points){
        double latn = points.get(0).latitude;
        double lats = points.get(0).latitude;
        double lone = points.get(0).longitude;
        double lonw = points.get(0).longitude;
        for (int i = 1; i < points.size(); ++i){
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
}
