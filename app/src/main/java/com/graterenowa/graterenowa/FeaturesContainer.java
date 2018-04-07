package com.graterenowa.graterenowa;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by trole_000 on 2018-04-07.
 */

public class FeaturesContainer {
    public List<GameFeature> elements;
    public PolygonOptions range;
    static private String TAG = "FeaturesContainer";
    public FeaturesContainer(String json){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("features");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject Feature = arr.getJSONObject(i);
                json.simple.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
