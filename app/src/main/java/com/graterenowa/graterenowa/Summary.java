package com.graterenowa.graterenowa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Podsumowanie, twoja działka Karol
public class Summary extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public TextView score;
    public EditText nick;
    private int points;
    private boolean is_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        is_submit=false;
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent wynik= getIntent();
        points=wynik.getIntExtra("Points",0);
        String ans="Gratulacje, w grze zdobyles: "+ points + " punktow!";
        score=(TextView)findViewById(R.id.twojepodsumowanie);
        score.setText(ans);
        nick = (EditText)findViewById(R.id.nickname);
        mDatabase = FirebaseDatabase.getInstance().getReference("Wynik");
        Button KiP = (Button) findViewById(R.id.zakoncz);
        KiP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();//Koniec i powrot do glownego menu
            }
        });
        Button ZapWyn=(Button) findViewById(R.id.ZapiszWynik);
        ZapWyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSum();//dodaje wynik do rankingu
            }
        });
    }
    private void AddSum(){
        if(is_submit){
            Toast.makeText(this,"Juz wpisales swoj wynik do rankingu :)",Toast.LENGTH_LONG).show();
            return;
        }
        String wynik= String.valueOf(points);
        String pseudo=nick.getText().toString().trim();
        if(!TextUtils.isEmpty(pseudo)){
            is_submit=true;//sprawdzamy jeden
            String id= mDatabase.push().getKey();
            Score score = new Score(pseudo,wynik,GameSetupActivity.current.name);//pobieranie nazwy zestawu
            mDatabase.child(id).setValue(score);
            Toast.makeText(this,"Twoj wynik zapisany zostal do tablicy rankingow",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Twoj wynik nie wpisal sie poprawnie do tablicy wynikow, Szkoda :(",Toast.LENGTH_LONG).show();
        }
    }
}
