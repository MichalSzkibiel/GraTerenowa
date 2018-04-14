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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TaskListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mReturnButton;
    private Button mEndGameButton;
    private int points;
    public static Activity activeTasksListActivity;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        activeTasksListActivity = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new myAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mReturnButton = (Button) findViewById(R.id.returnToMap);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent();
            }
        });

        mEndGameButton = (Button) findViewById(R.id.endGame);
        mEndGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_game();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("odwiedzone");
    }

    private void returnIntent(){
        finish();
    }

    private void end_game(){
        long millis = System.currentTimeMillis() - MapsActivity.starttime;
        int seconds = (int) (millis / 1000);
        points = MapsActivity.points + 3600 - seconds;
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
        Intent intent = new Intent(this, Summary.class);
        intent.putExtra("Points", points);
        startActivity(intent);
        MapsActivity.activeMapsActivity.finish();
        finish();
    }

}

