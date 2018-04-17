package com.graterenowa.graterenowa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

//Okno przechowujące listę zadań

public class TaskListActivity extends AppCompatActivity {

    private int points;
    public static TaskListActivity activeTasksListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        //Ustawienie tego activity na aktywne
        activeTasksListActivity = this;
        //Ustawienie RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new myAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //Ustawienie przycisku powrotu
        Button mReturnButton = (Button) findViewById(R.id.returnToMap);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent();
            }
        });
        //Ustawienie przycisku zakończenia
        Button mEndGameButton = (Button) findViewById(R.id.endGame);
        mEndGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_game();
            }
        });
    }

    private void returnIntent(){
        finish();
    }

    private void end_game(){
        //Obliczenie punktów - suma punktów za czas i za zadania
        long millis = System.currentTimeMillis() - MapsActivity.starttime;
        int seconds = (int) (millis / 1000);
        points = MapsActivity.points + 3600 - seconds;
        //Upewnienie
        new AlertDialog.Builder(this)
                .setMessage("Czy na pewno chcesz skończyć grę z wynikiem " + String.valueOf(points) + "?")
                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        endIntent();
                    }
                })
                .show();
    }

    private void endIntent(){
        //Przejście do ekranu podsumowania z przekazanymi punktami
        Intent intent = new Intent(this, Summary.class);
        intent.putExtra("Points", points);
        startActivity(intent);
        //Zakończenie działania mapy i tego activity
        MapsActivity.activeMapsActivity.finish();
        finish();
    }

    public void accuracy_dialog(){
        //Dialog informujący o tym, że pozycja jest za mało dokładna
        new AlertDialog.Builder(this)
                .setMessage("Dokładność pozycji jest większa niż 100 metrów. Zgłoszenie nieważne.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}

