package com.graterenowa.graterenowa;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Okienko wyboru zestawu gry terenowej, moja działka
public class GameSetupActivity extends FragmentActivity {

    private GoogleMap mMap;
    private Spinner chooseSpinner;
    private FeaturesContainer current;
    public static String TAG = "My";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        MapFragment mapFragment = MapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng pos = new LatLng(52.25, 21.0);
                mMap.moveCamera(CameraUpdateFactory.zoomBy(15));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            }
        });
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
                mMap.addPolygon(current.range);

            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
