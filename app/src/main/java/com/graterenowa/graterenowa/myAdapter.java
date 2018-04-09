package com.graterenowa.graterenowa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by trole_000 on 2018-04-09.
 */

public class myAdapter extends RecyclerView.Adapter {

    private RecyclerView mRecyclerView;

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

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyViewHolder) viewHolder).questView.setText(GameSetupActivity.current.elements.get(i).quest);
        ((MyViewHolder) viewHolder).commNumber.setText(String.valueOf(GameSetupActivity.current.elements.get(i).commissions));
    }

    @Override
    public int getItemCount() {
        return GameSetupActivity.current.elements.size();
    }
}