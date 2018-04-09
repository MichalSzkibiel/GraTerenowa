package com.graterenowa.graterenowa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//Podsumowanie, twoja działka Karol
public class Summary extends AppCompatActivity {

    public TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Intent wynik= getIntent();
        int points=wynik.getIntExtra("Points",0);
        String ans="Gratulacje, w grze zdobyles"+ points + "punktow!";
        score=(TextView)findViewById(R.id.twojepodsumowanie);
        score.setText(ans);
    }
}
