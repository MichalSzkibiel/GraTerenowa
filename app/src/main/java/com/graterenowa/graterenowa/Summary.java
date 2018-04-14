package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Podsumowanie, twoja działka Karol
public class Summary extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Intent wynik= getIntent();
        int points=wynik.getIntExtra("Points",0);
        String ans="Gratulacje, w grze zdobyles: "+ points + " punktow!";
        score=(TextView)findViewById(R.id.twojepodsumowanie);
        score.setText(ans);
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
        String wynik=score.getText().toString().trim();
        if(!TextUtils.isEmpty(wynik)){
            String id= mDatabase.push().getKey();
            Score score = new Score(id,wynik);
            mDatabase.child(id).setValue(score);
            Toast.makeText(this,"Twoj wynik zapisany zostal do tablicy rankingow",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Twoj wynik nie wpisal sie poprawnie do tablicy wynikow, Szkoda :-(",Toast.LENGTH_LONG).show();
        }
    }
}
