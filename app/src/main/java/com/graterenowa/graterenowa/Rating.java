package com.graterenowa.graterenowa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//Okno rankingu, twoja działka Karol
public class Rating extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mASL;
    private DatabaseReference mDatabase;
    private String score;
    private ListView ranking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ranking=(ListView)findViewById(R.id.Ranking);
        mAuth=FirebaseAuth.getInstance();
        FD= FirebaseDatabase.getInstance();//.getReference("Wynik");
        mDatabase=FD.getReference();
        FirebaseUser user= mAuth.getCurrentUser();
        score= user.getUid();

        mASL= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user !=null){
                    //Log.d(score,"Uzytkownik:"+ user.getUid());
                   toastMessage("osiagnal wyniki:"+user.getUid());//tu trzeba zrobić, aby pobierała wynik z Score
                }
                else
                {
                    //Log.d(score,"brak uzytkownika");
                    toastMessage("brak wyniku");
                }
            }
        };
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
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
    private  void showData(DataSnapshot DS){
        for (DataSnapshot ds: DS.getChildren()){
            Score sc = new Score();
            sc.setPseudonim(ds.child(score).getValue(Score.class).getPseudonim());
            sc.setScore(ds.child(score).getValue(Score.class).getScore());

            //Log.d(TELECOM_SERVICE,"pseudonim:" +sc.getPseudonim());//TAG wywala, więc wrzuciłem narazie cokolwiek co może pasować
            //Log.d(TELECOM_SERVICE,"wynik:" +sc.getScore());

            ArrayList<String> array= new ArrayList<>();
            array.add(sc.getPseudonim());
            array.add(sc.getScore());
            ArrayAdapter adapter = new ArrayAdapter(this,R.layout.activity_rating,array);
            ranking.setAdapter(adapter);
        }

    }
    /*@Override
    public void OnStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mASL);
    }
    //@Override
    public void OnStop()
    {
        super.onStop();
        if(mASL != null) {
            mAuth.removeAuthStateListener(mASL);
        }
    }*/
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
