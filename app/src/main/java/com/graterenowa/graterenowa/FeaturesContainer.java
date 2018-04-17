package com.graterenowa.graterenowa;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trole_000 on 2018-04-07.
 * Klasa przechowująca zestaw gry: jego nazwę, obiekty do znalezienia i zasięg gry
 */

public class FeaturesContainer {
    public String name;
    public List<GameFeature> elements;
    public PolygonOptions range;

    public FeaturesContainer(String nm, String json){
        //Konstruktor, który parsuje jsona jako String
        //Proste przypisanie nazwy
        name = nm;
        //Inicjalizacja listy obiektów i poligonu
        elements = new ArrayList<GameFeature>();
        range = new PolygonOptions();
        try {
            //Parsowanie GeoJSONa
            JSONObject main = new JSONObject(json);
            JSONArray features = main.getJSONArray("features");
            for (int i = 0; i < features.length(); ++i){
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                String type = geometry.getString("type");
                if (type.equals("Point")){
                    //Jeżeli punkt, to jest kolejny obiekt gry
                    JSONArray coords = (JSONArray) geometry.get("coordinates");
                    LatLng pos = new LatLng((double)coords.get(1), (double)coords.get(0));
                    JSONObject properties = feature.getJSONObject("properties");
                    String name = properties.getString("name");
                    String quest = properties.getString("quest");
                    GameFeature toAdd = new GameFeature(pos, name, quest);
                    elements.add(toAdd);
                }
                else{
                    //Jeżeli poligon, to jest zasięg gry
                    JSONArray coords1 = (JSONArray) geometry.get("coordinates");
                    for (int j = 0; j < coords1.length(); ++j){
                        JSONArray coords2 = (JSONArray) coords1.get(j);
                        for (int k = 0; k < coords2.length(); ++k){
                            JSONArray coords3 = (JSONArray) coords2.get(k);
                            LatLng point = new LatLng((double)coords3.get(1), (double)coords3.get(0));
                            range.add(point);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
