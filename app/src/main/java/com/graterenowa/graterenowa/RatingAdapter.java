package com.graterenowa.graterenowa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Karol Jakub on 20.04.2018.
 */

class RatingAdapter extends RecyclerView.Adapter {
    private List<Score> list;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView place;
        private TextView setScore;
        private TextView psuedonim;
        private TextView setName;


        MyViewHolder(View pItem) {
            super(pItem);
            place = (TextView) pItem.findViewById(R.id.place);
            setScore = (TextView) pItem.findViewById(R.id.setScore);
            psuedonim = (TextView) pItem.findViewById(R.id.pseudonim);
            setName = (TextView) pItem.findViewById(R.id.setName);
        }
    }

    // konstruktor rankingu
    public RatingAdapter(List<Score> listWyn){
        list = listWyn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        //Ustawienie działania na kliknięcie elementu - zgłoszenie
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.score, viewGroup, false);
        return new RatingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        //Ustawienie danych do rankingu
        ((MyViewHolder) viewHolder).place.setText(String.valueOf(i+ 1));
        ((MyViewHolder) viewHolder).setScore.setText(list.get(i).getScore());
        ((MyViewHolder) viewHolder).psuedonim.setText(list.get(i).getPseudonim());
        ((MyViewHolder) viewHolder).setName.setText(list.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
