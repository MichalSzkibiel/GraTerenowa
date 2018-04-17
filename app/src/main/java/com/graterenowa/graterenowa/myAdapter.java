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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
 * Klasa opisująca RecyclerView przechowującego listę zadań
 */

public class myAdapter extends RecyclerView.Adapter {

    private RecyclerView mRecyclerView;
    private int idx;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView questView;
        private TextView commNumber;

        MyViewHolder(View pItem) {
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
        //Ustawienie działania na kliknięcie elementu - zgłoszenie
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pobór indeksu
                idx = mRecyclerView.getChildAdapterPosition(v);
                //Jeżeli jest to obiekt już znaleziony to nie dzieje się nic
                if (GameSetupActivity.current.elements.get(idx).found){
                    return;
                }
                //Upewnienie
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
        //Pobór liczby zgłoszeń i zwiększenie ich liczby o jeden
        int comm = GameSetupActivity.current.elements.get(idx).commissions++;
        //Pobór pozycji obiektu i pozycji gracza
        LatLng posOfObject = GameSetupActivity.current.elements.get(idx).pos;
        LatLng posOfPlayer = MapsActivity.activeMapsActivity.getPosition();
        //Pobór dokładności pozycji gracza
        double accuracy = MapsActivity.activeMapsActivity.getAccuracy();
        //Obliczenie odległości pomiędzy pozycjami
        double distance = distance(posOfObject.latitude, posOfObject.longitude, posOfPlayer.latitude, posOfPlayer.longitude);
        if (accuracy > 100){
            //Gdy dokładność jest mniejsza niż 100 m to nie ma zgłoszenia
            //Zmniejszenie o jeden liczby zgłoszeń
            GameSetupActivity.current.elements.get(idx).commissions--;
            //Wywołanie stosownego komunikatu
            TaskListActivity.activeTasksListActivity.accuracy_dialog();
            return;
        }
        else if (distance <= accuracy){
            //Jeżeli odległość jest mniejsza niż dokładność, to dobra odpowiedź
            //Przyznanie punktów
            MapsActivity.points += 5000;
            //Ustalenie, że obiekt jest znaleziony
            GameSetupActivity.current.elements.get(idx).found = true;
            //Wywołanie okna dobrej odpowiedzi z przekazanym identyfikatorem obiektu
            Intent intent = new Intent(TaskListActivity.activeTasksListActivity, CorrectAnswer.class);
            intent.putExtra("index", idx);
            TaskListActivity.activeTasksListActivity.startActivity(intent);
            //Obsługa mapy dla dobrej odpowiedzi
            MapsActivity.activeMapsActivity.updatePoints(true, idx, posOfObject);
        }
        else{
            //Jeżeli nie, to zła odpowiedź
            //Normalnie traci się 1000 punktów, chyba że to pierwsze zgłoszenie, za które nie traci się punktów
            int points_loss = 1000;
            if (comm == 0){
                points_loss = 0;
            }
            MapsActivity.points -= points_loss;
            //Wywołanie okna złej odpowiedzi z przekazanym identyfikatorem obiektu
            Intent intent = new Intent(TaskListActivity.activeTasksListActivity, BadAnswer.class);
            intent.putExtra("index", idx);
            TaskListActivity.activeTasksListActivity.startActivity(intent);
            //Obsługa mapy dla złej odpowiedzi
            MapsActivity.activeMapsActivity.updatePoints(false, idx, posOfPlayer);
        }
        //Odświeżenie widoku
        notifyDataSetChanged();
        //Zakończenie działania listy zadań
        TaskListActivity.activeTasksListActivity.finish();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        //Ustawienie podglądu klucza i liczby zgłoszeń
        ((MyViewHolder) viewHolder).questView.setText(GameSetupActivity.current.elements.get(i).quest);
        ((MyViewHolder) viewHolder).commNumber.setText(String.valueOf(GameSetupActivity.current.elements.get(i).commissions));
        //Zgłoszone zakreślamy na zielono
        if (GameSetupActivity.current.elements.get(i).found)
            ((MyViewHolder) viewHolder).itemView.setBackgroundColor(Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return GameSetupActivity.current.elements.size();
    }

    private double distance(double lat_a, double lng_a, double lat_b, double lng_b)
    {
        //Funkcja licząca odległość pomiędzy punktami na sferze
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        double meterConversion = 1609.0;

        return distance * meterConversion;
    }
}