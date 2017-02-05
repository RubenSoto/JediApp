package com.rubn.jediapp.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rubn.jediapp.R;
import com.rubn.jediapp.fragments.CalculatorFragment;
import com.rubn.jediapp.fragments.MemoryGameFragment;
import com.rubn.jediapp.fragments.MusicFragment;
import com.rubn.jediapp.fragments.ProfileFragment;
import com.rubn.jediapp.fragments.RankingFragment;

import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity{
    private static final String CURRENT_FRAGMENT = "Current Fragment";
    Fragment mCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolbar();
        setActionButton();

    }

    private void setToolbar (){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setActionButton(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        final Fragment fragment = (mCurrentFragment != null)? mCurrentFragment : new MemoryGameFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        if(mCurrentFragment != null)
            bottomNavigationView.getMenu().getItem(getPositionFragmentinViewer(mCurrentFragment.getClass().getSimpleName())).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragmentAction = null;
                        switch (item.getItemId()){
                            case R.id.action_mem_game:
                                fragmentAction = new MemoryGameFragment();
                                break;
                            case R.id.action_rank:
                                fragmentAction = new RankingFragment();
                                break;
                            case R.id.action_music:
                                fragmentAction = new MusicFragment();
                                break;
                            case R.id.action_profile:
                                fragmentAction = new ProfileFragment();
                                break;
                            case R.id.action_calculator:
                                fragmentAction = new CalculatorFragment();
                                break;
                        }
                        mCurrentFragment = fragmentAction;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,fragmentAction)
                                .commit();
                        return true;
                    }
                });
    }

    private int getPositionFragmentinViewer(String fragmentId){
        switch (fragmentId)
        {
            case "MemoryGameFragment":
                return 0;
            case "RankingFragment":
                return 1;
            case "MusicFragment":
                return 2;
            case "ProfileFragment":
                return 3;
            case "CalculatorFragment":
                return 4;
            default:
                return -1;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && mCurrentFragment != null)
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT, mCurrentFragment);
    }

    @Override
    public void onBackPressed() {
    }
}
