package com.graterenowa.graterenowa;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Klasa służąca do pobierania danych o zestawach z firebase'a

public class SetsHandler {
    private Map<String, JSONObject> game_sets;
    private Map<LatLng, String> centroids;
    private GameSetupActivity call;
    private static String TAG = "SetsHandler";

    public SetsHandler(GameSetupActivity caller){
        //Przypisanie wywoływacza i inicjalizacja słowników
        call = caller;
        game_sets = new HashMap<String, JSONObject>();
        centroids = new HashMap<LatLng, String>();
        //Połączenie z bazą danych
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Zestawy");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Pobór danych
                String array = dataSnapshot.getValue().toString();
                try {
                    JSONArray jsonArray = new JSONArray(array);
                    for(int i = 0; i < jsonArray.length(); ++i){
                        //Dla każdego zestawu pobór nazwy i przypisanie do słownika obiektu pod kluczem nazwy
                        JSONObject game_set = jsonArray.getJSONObject(i);
                        String name = game_set.getString("name");
                        //Zamiana znaków umownych
                        name = name.replace("_", " ");
                        name = name.replace("$", ",");
                        game_sets.put(name, game_set);
                        //Pobór współrzędnych centroidu
                        JSONObject centroid = game_set.getJSONObject("centroid");
                        JSONObject geometry = centroid.getJSONObject("geometry");
                        JSONArray coords = (JSONArray) geometry.get("coordinates");
                        LatLng pos = new LatLng((double)coords.get(1), (double)coords.get(0));
                        centroids.put(pos, name);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Ustawienie nazw zestawów w spinnerze
                call.setSpinnerList(new ArrayList<String>(game_sets.keySet()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<LatLng> getCentroids(){
        return new ArrayList<>(centroids.keySet());
    }

    public String getName(LatLng pos){
        return centroids.get(pos);
    }

    public JSONObject getSet(String key){
        //Akcesor elementów w słowniku
        return game_sets.get(key);
    }
}
