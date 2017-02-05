package com.rubn.jediapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.rubn.jediapp.R;
import com.rubn.jediapp.activities.LoginActivity;
import com.rubn.jediapp.service.MediaPlayerService;

import butterknife.ButterKnife;


public class MusicFragment extends Fragment {

    private final String ALREADY_LOGGED = "Already logged";
    private View rootView;
    private Intent mIntentMusic;
    private ImageButton mButtonPlay;
    private ImageButton mButtonStop;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        mButtonPlay = (ImageButton) rootView.findViewById(R.id.music_play);
        mButtonStop = (ImageButton) rootView.findViewById(R.id.music_stop);
        //mButtonStop.setVisibility(View.INVISIBLE);
        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mButtonPlay.setVisibility(View.INVISIBLE);
                mButtonStop.setVisibility(View.VISIBLE);*/
                mIntentMusic = new Intent(getActivity(), MediaPlayerService.class);
                getActivity().startService(mIntentMusic);

            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* mButtonPlay.setVisibility(View.VISIBLE);
                mButtonStop.setVisibility(View.INVISIBLE);*/
                getActivity().stopService(mIntentMusic);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.music_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.music_toolbar_logout:
                SharedPreferences settings = getActivity().getSharedPreferences(ALREADY_LOGGED,0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Logged", false);
                editor.apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
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
