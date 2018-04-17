package com.graterenowa.graterenowa;

//Klasa, która składa się z koła i markeru wskazujących pozycję i jej dokładność

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocIndicator {
    private CircleOptions co;
    private MarkerOptions mo;
    private Circle circ;
    private Marker mark;

    public LocIndicator(GoogleMap map, LatLng pos, double acc){
        //Stworzenie opisu koła: środek-pozycja, promień-dokładność, wypełnienie-przeźroczysty niebieski
        co = new CircleOptions()
                .center(pos)
                .fillColor(0x220000FF)
                .radius(acc)
                .strokeWidth(0.0f);
        //Stworzenie opisu markeru: pozycja-pozycja, kolor-niebieski
        mo = new MarkerOptions()
                .position(pos)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(String.valueOf(R.string.here));
        circ = map.addCircle(co);
        mark = map.addMarker(mo);
    }

    public void move(GoogleMap map, LatLng pos, double acc){
        //Przesunięcie koła i markeru
        //Usunięcie istniejących
        circ.remove();
        mark.remove();
        //Zmiana pozycji
        co.center(pos);
        co.radius(acc);
        mo.position(pos);
        //Ustawienie na mapie
        circ = map.addCircle(co);
        mark = map.addMarker(mo);

    }
}
