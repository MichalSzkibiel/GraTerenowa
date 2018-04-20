package com.graterenowa.graterenowa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//Okno startowe, twoja działka Karol
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button graj = (Button) findViewById(R.id.button2);
        Button rating = (Button) findViewById(R.id.button3);
        Button regul = (Button) findViewById(R.id.button4);
        Button exit = (Button) findViewById(R.id.button6);

        graj.setOnClickListener(new View.OnClickListener() {
        //zaczynamy grac!!!
            @Override
            public void onClick(View view) {
                Intent grac = new Intent(getApplicationContext(),GameSetupActivity.class);//Jeśli wpiszemy tu Login, to można dodać ekran logowania
                startActivity(grac);
            }
        });
        //chcemy sprawdzic wyniki
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wyn = new Intent(getApplicationContext(),Rating.class);
                startActivity(wyn);
            }
        });
        //chcemy sprawdzic regulami
        regul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(getApplicationContext(),RegulationsActivity.class);
                startActivity(reg);
            }
        });
        //wychodzimy z gry!
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View content) {
                 onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog show = new AlertDialog.Builder(this)
                .setTitle("Koniec grania??? :-(")
                .setMessage("Czy na pewno chcesz Zakonczyc gre?")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        MainActivity.super.onBackPressed();
                    }
                })
                .show();
    }
}

