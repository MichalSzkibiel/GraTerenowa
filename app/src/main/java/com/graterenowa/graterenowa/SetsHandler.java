package com.graterenowa.graterenowa;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.List;

public class SetsHandler {
    List<String> game_items;

    public SetsHandler(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Zestawy");
    }
}
