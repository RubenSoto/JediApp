package com.rubn.jediapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rubn.jediapp.R;
import com.rubn.jediapp.activities.LoginActivity;
import com.rubn.jediapp.adapters.RecycleRankingAdapter;
import com.rubn.jediapp.realm.RankingPuntuation;

import java.util.Date;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class RankingFragment extends Fragment {

    private final String ALREADY_LOGGED = "Already logged";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    private RecycleRankingAdapter mRecycleRankingAdapter;
    private Realm realm;

    public RankingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mRecyclerView);
        mLinearLayout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayout);
        Realm.init(getActivity());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        //createDummyRankings(realm);
        mRecycleRankingAdapter = new RecycleRankingAdapter(realm);
        mRecyclerView.setAdapter(mRecycleRankingAdapter);

        return rootView;
    }

    private void createDummyRankings() {

        for(int i = 0; i < 10; i++){
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new RankingPuntuation("Dummy"+(i%5),(int)new Date().getTime(),5+i));
            realm.commitTransaction();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ranking_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ranking_toolbar_logout:
                SharedPreferences settings = getActivity().getSharedPreferences(ALREADY_LOGGED,0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Logged", false);
                editor.apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ranking_toolbar_reset:
                realm.beginTransaction();
                realm.where(RankingPuntuation.class).findAll().deleteAllFromRealm();
                realm.commitTransaction();
                mRecycleRankingAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
