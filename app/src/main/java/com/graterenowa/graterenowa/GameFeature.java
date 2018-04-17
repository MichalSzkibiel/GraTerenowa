package com.graterenowa.graterenowa;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by trole_000 on 2018-04-07.
 * Klasa przechowująca informacje o obiekcie gry
 * pozycja
 * nazwa
 * klucz
 * ilość zgłoszeń
 * czy został odnaleziony
 */

public class GameFeature {
    public LatLng pos;
    public String name;
    public String quest;
    public int commissions;
    public boolean found;

    public GameFeature(LatLng position, String nm, String question){
        //Przypisanie pozycji
        pos = position;
        //Przypisanie nazwy i klucza. Zamiana sztucznych podkreślników na spacje, a dolarów na przecinki
        nm = nm.replace("_", " ");
        nm = nm.replace("$", ",");
        name = nm;
        question = question.replace("_", " ");
        question = question.replace("$", ",");
        quest = question;
        //Na początku zero zgłoszeń i obiekt nie znaleziony
        commissions = 0;
        found = false;
    }

    @Override
    public String toString(){
        return "{Position:" + pos.toString() + ", Name:" + name + ", Quest:" + quest;
    }
}
