package com.graterenowa.graterenowa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


//Okno rankingu, twoja działka Karol
public class Rating extends AppCompatActivity {

    private FirebaseDatabase FD;
    private DatabaseReference mDatabase;
    private String score;
    private RecyclerView ranking;
    private static String TAG="Rating";
    private List<Score>listWyn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listWyn=new ArrayList<Score>();
        ranking=(RecyclerView)findViewById(R.id.Ranking);
        FD= FirebaseDatabase.getInstance();
        mDatabase=FD.getReference("Wynik");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rat= dataSnapshot.getValue().toString();
                try {
                    JSONObject rat2json = new JSONObject(rat);
                    Iterator<String> keys = rat2json.keys();
                    while (keys.hasNext()){
                        String key = keys.next().toString();
                        JSONObject val= rat2json.getJSONObject(key);
                        listWyn.add(new Score(val));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(listWyn, new Comparator<Score>() {
                    @Override
                    public int compare(Score score, Score t1) {
                        //sortowanie po wynikach
                        if(Integer.parseInt(score.score)> Integer.parseInt(t1.score)){
                        return -1;// jezeli wynik jest lepszy- idzie ku gorze tablicy
                        }
                        else if(Integer.parseInt(score.score)< Integer.parseInt(t1.score)){
                            return 1;// jesli nizszy to w dol
                        }
                        else{
                            return 0;
                        }
                    }
                });
                showData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Button ret = (Button) findViewById(R.id.button5);
        ret.setOnClickListener(new View.OnClickListener() {
            //wracamy do glownego menu
            @Override
            public void onClick(View view) {
                finish();//zamyka okno
            }
        });

    }
    private  void showData(){
        List<String> parsedScore = new ArrayList<>();
        for (int i = 0; i < listWyn.size(); ++i) {
            parsedScore.add(listWyn.get(i).getPseudonim() + "\t" + listWyn.get(i).getName() + "\t" + listWyn.get(i).getScore());
        }
        ranking.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ranking.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RatingAdapter(listWyn);
        ranking.setAdapter(mAdapter);

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
