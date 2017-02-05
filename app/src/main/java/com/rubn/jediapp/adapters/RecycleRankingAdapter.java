package com.rubn.jediapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubn.jediapp.R;
import com.rubn.jediapp.realm.RankingPuntuation;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;


public class RecycleRankingAdapter extends RecyclerView.Adapter<RecycleRankingAdapter.AdapterViewHolder>{

    private List<RankingPuntuation> mRanking;

    public RecycleRankingAdapter(Realm realm){
        mRanking = realm.where(RankingPuntuation.class).findAllSorted("intents", Sort.ASCENDING,"date",Sort.ASCENDING);
    }

    @Override
    public RecycleRankingAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_recycle_layout, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleRankingAdapter.AdapterViewHolder adapterViewholder, int position) {
        adapterViewholder.rank.setText(String.valueOf(position+1));
        adapterViewholder.name.setText(mRanking.get(position).getName());
        adapterViewholder.puntuation.setText(String.valueOf(mRanking.get(position).getIntents()));
    }

    @Override
    public int getItemCount() {
        return mRanking.size();
    }



    //Definimos una clase viewholder que funciona como adapter para
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        /*
        *  Mantener una referencia a los elementos de nuestro ListView mientras el usuario realiza
        *  scrolling en nuestra aplicación. Así que cada vez que obtenemos la vista de un item,
        *  evitamos las frecuentes llamadas a findViewById, la cuál se realizaría únicamente la primera vez y el resto
        *  llamaríamos a la referencia en el ViewHolder, ahorrándonos procesamiento.
        */

        TextView rank;
        TextView name;
        TextView puntuation;
        View v;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.rank = (TextView) itemView.findViewById(R.id.recycle_row_rank);
            this.name = (TextView) itemView.findViewById(R.id.recycle_row_name);
            this.puntuation = (TextView) itemView.findViewById(R.id.recycle_row_puntuation);
        }
    }
}
