package com.graterenowa.graterenowa;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by trole_000 on 2018-04-07.
 */

public class GameFeature {
    public LatLng pos;
    public String name;
    public String quest;

    public GameFeature(LatLng position, String nm, String question){
        pos = position;
        name = nm;
        quest = question;
    }

    @Override
    public String toString(){
        return "{Position:" + pos.toString() + ", Name:" + name + ", Quest:" + quest;
    }
}
