package com.graterenowa.graterenowa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by trole_000 on 2018-04-09.
 */

public class myAdapter extends RecyclerView.Adapter {

    private RecyclerView mRecyclerView;
    private int idx;

    private static String TAG = "myAdapter";

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView questView;
        private TextView commNumber;

        public MyViewHolder(View pItem) {
            super(pItem);
           questView = (TextView) pItem.findViewById(R.id.quest);
           commNumber = (TextView) pItem.findViewById(R.id.commissionNumber);
        }
    }

    // konstruktor adaptera
    public myAdapter(RecyclerView pRecyclerView){
        mRecyclerView = pRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx = mRecyclerView.getChildAdapterPosition(v);
                new AlertDialog.Builder(TaskListActivity.activeTasksListActivity)
                        .setMessage("Czy na pewno chcesz zgłosić znalezienie punktu do klucza " + GameSetupActivity.current.elements.get(idx).quest + "?")
                        .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                serveCommission();
                            }
                        })
                        .show();


            }
        });

        return new MyViewHolder(view);
    }

    private void serveCommission(){
        int comm = GameSetupActivity.current.elements.get(idx).commissions++;
        LatLng posOfObject = GameSetupActivity.current.elements.get(idx).pos;
        LatLng position = MapsActivity.activeMapsActivity.getPosition();
        double accuracy = MapsActivity.activeMapsActivity.getAccuracy();
        double distance = distance(posOfObject.latitude, posOfObject.longitude, position.latitude, position.longitude);
        if (distance <= accuracy){
            MapsActivity.points += 5000;
            GameSetupActivity.current.elements.get(idx).found = true;
            Intent intent = new Intent(TaskListActivity.activeTasksListActivity, CorrectAnswer.class);
            intent.putExtra("index", idx);
            TaskListActivity.activeTasksListActivity.startActivity(intent);
            MapsActivity.activeMapsActivity.updatePoints(true, idx, position);
        }
        else{
            int points_loss = 1000;
            if (comm == 0){
                points_loss = 0;
            }
            MapsActivity.points -= points_loss;
            Intent intent = new Intent(TaskListActivity.activeTasksListActivity, BadAnswer.class);
            intent.putExtra("index", idx);
            TaskListActivity.activeTasksListActivity.startActivity(intent);
            MapsActivity.activeMapsActivity.updatePoints(false, idx, position);
        }
        notifyDataSetChanged();
        TaskListActivity.activeTasksListActivity.finish();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyViewHolder) viewHolder).questView.setText(GameSetupActivity.current.elements.get(i).quest);
        ((MyViewHolder) viewHolder).commNumber.setText(String.valueOf(GameSetupActivity.current.elements.get(i).commissions));
        if (GameSetupActivity.current.elements.get(i).found == true)
            ((MyViewHolder) viewHolder).itemView.setBackgroundColor(Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return GameSetupActivity.current.elements.size();
    }

    public double distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Double(distance * meterConversion).doubleValue();
    }
}