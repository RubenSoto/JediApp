package com.rubn.jediapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.material.joanbarroso.flipper.CoolImageFlipper;
import com.rubn.jediapp.R;
import com.rubn.jediapp.activities.LoginActivity;
import com.rubn.jediapp.realm.RankingPuntuation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MemoryGameFragment extends Fragment {

    private final String ALREADY_LOGGED = "Already logged";
    private ArrayList<Boolean> tapped = new ArrayList<Boolean>(Collections.nCopies(16, false));
    private ArrayList<ImageView> cards = new ArrayList<>(16);
    private CoolImageFlipper mFlipper;
    private boolean firstTapped = false;
    private int firstNum;
    private boolean flip = true;
    private View rootView;
    private TextView mMemoryIntent;
    private TextView mMemoryLoser;
    private TextView mMemoryLoser2;
    private TextView mMemoryLoser3;
    private int intentsFlip = 0;
    private List<Drawable> cardsViews = new ArrayList<>(16);

    public MemoryGameFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_memory_game, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        mFlipper = new CoolImageFlipper(getActivity());
        mMemoryIntent = (TextView) rootView.findViewById(R.id.memory_intent);
        mMemoryLoser = (TextView) rootView.findViewById(R.id.memory_text_loser);
        mMemoryLoser2 = (TextView) rootView.findViewById(R.id.memory_text_loser2);
        mMemoryLoser3 = (TextView) rootView.findViewById(R.id.memory_text_loser3);

        fillCardsViews();
        resetCards();
        registerEvents();

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.memory_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mem_toolbar_reset:
                for (int i = 0; i < cards.size(); i++) {
                    if (tapped.get(i))
                        mFlipper.flipImage(getResources().getDrawable(R.drawable.dice), cards.get(i));
                }
                flip = true;
                firstTapped = false;
                tapped = new ArrayList<>(Collections.nCopies(16, false));
                intentsFlip = 0;
                resetCards();
                mMemoryIntent.setText(String.valueOf(0));
                mMemoryLoser.setVisibility(View.INVISIBLE);
                mMemoryLoser2.setVisibility(View.INVISIBLE);
                mMemoryLoser3.setVisibility(View.INVISIBLE);
                break;
            case R.id.mem_toolbar_logout:
                SharedPreferences settings = getActivity().getSharedPreferences(ALREADY_LOGGED,0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Logged", false);
                editor.apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillCardsViews(){
        Drawable cocktail = getResources().getDrawable(R.drawable.cocktail);
        Drawable diamonds = getResources().getDrawable(R.drawable.diamonds);
        Drawable chips = getResources().getDrawable(R.drawable.chips);
        Drawable slot_machine  = getResources().getDrawable(R.drawable.slot_machine);
        Drawable suits = getResources().getDrawable(R.drawable.suits);
        Drawable roulette = getResources().getDrawable(R.drawable.roulette);
        Drawable money = getResources().getDrawable(R.drawable.money);
        Drawable money_luck = getResources().getDrawable(R.drawable.money_luck);
        for (int i = 0; i < 2; i++)
        {
            cardsViews.add(cocktail);
            cardsViews.add(diamonds);
            cardsViews.add(chips);
            cardsViews.add(slot_machine);
            cardsViews.add(suits);
            cardsViews.add(roulette);
            cardsViews.add(money);
            cardsViews.add(money_luck);

        }
    }

    private void resetCards() {
        Collections.shuffle(cardsViews);
    }

    private void registerEvents() {
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_0_column_0));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_0_column_1));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_0_column_2));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_0_column_3));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_1_column_0));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_1_column_1));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_1_column_2));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_1_column_3));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_2_column_0));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_2_column_1));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_2_column_2));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_2_column_3));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_3_column_0));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_3_column_1));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_3_column_2));
        cards.add((ImageView) rootView.findViewById(R.id.btn_memory_row_3_column_3));


        cards.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(0) && flip) {
                    mFlipper.flipImage(cardsViews.get(0), cards.get(0));
                    tapped.set(0, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 0;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 0);
                    }
                }
            }
        });
        cards.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(1)&& flip) {
                    mFlipper.flipImage(cardsViews.get(1), cards.get(1));
                    tapped.set(1, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 1;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 1);
                    }
                }
            }
        });
        cards.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(2) && flip) {
                    mFlipper.flipImage(cardsViews.get(2), cards.get(2));
                    tapped.set(2, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 2;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 2);
                    }
                }
            }
        });
        cards.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(3) && flip) {
                    mFlipper.flipImage(cardsViews.get(3), cards.get(3));
                    tapped.set(3, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 3;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 3);
                    }
                }
            }
        });
        cards.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(4) && flip) {
                    mFlipper.flipImage(cardsViews.get(4), cards.get(4));
                    tapped.set(4, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 4;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 4);
                    }
                }
            }
        });
        cards.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(5) && flip) {
                    mFlipper.flipImage(cardsViews.get(5), cards.get(5));
                    tapped.set(5, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 5;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 5);
                    }
                }
            }
        });
        cards.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(6) && flip) {
                    mFlipper.flipImage(cardsViews.get(6), cards.get(6));
                    tapped.set(6, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 6;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 6);
                    }
                }
            }
        });
        cards.get(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(7) && flip) {
                    mFlipper.flipImage(cardsViews.get(7), cards.get(7));
                    tapped.set(7, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 7;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 7);
                    }
                }
            }
        });
        cards.get(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(8) && flip) {
                    mFlipper.flipImage(cardsViews.get(8), cards.get(8));
                    tapped.set(8, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 8;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 8);
                    }
                }
            }
        });
        cards.get(9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(9) && flip) {
                    mFlipper.flipImage(cardsViews.get(9), cards.get(9));
                    tapped.set(9, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 9;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 9);
                    }
                }
            }
        });
        cards.get(10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(10) && flip) {
                    mFlipper.flipImage(cardsViews.get(10), cards.get(10));
                    tapped.set(10, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 10;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 10);
                    }
                }
            }
        });
        cards.get(11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(11) && flip) {
                    mFlipper.flipImage(cardsViews.get(11), cards.get(11));
                    tapped.set(11, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 11;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 11);
                    }
                }
            }
        });
        cards.get(12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(12) && flip) {
                    mFlipper.flipImage(cardsViews.get(12), cards.get(12));
                    tapped.set(12, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 12;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 12);
                    }
                }
            }
        });
        cards.get(13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(13) && flip) {
                    mFlipper.flipImage(cardsViews.get(13), cards.get(13));
                    tapped.set(13, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 13;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 13);
                    }
                }
            }
        });
        cards.get(14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(14) && flip) {
                    mFlipper.flipImage(cardsViews.get(14), cards.get(14));
                    tapped.set(14, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 14;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 14);
                    }
                }
            }
        });
        cards.get(15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tapped.get(15) && flip) {
                    mFlipper.flipImage(cardsViews.get(15), cards.get(15));
                    tapped.set(15, true);
                    if (!firstTapped) {
                        firstTapped = true;
                        firstNum = 15;
                    } else {
                        firstTapped = flip = false;
                        MemoryTask mem = new MemoryTask();
                        mem.execute(firstNum, 15);
                    }
                }
            }
        });
    }

    private void createAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_dialog_mem)
                .setCancelable(false)
                .setNeutralButton(R.string.alert_dialog_mem_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveIntents() {
        Realm.init(getActivity());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new RankingPuntuation(
                this.getActivity().getSharedPreferences(ALREADY_LOGGED, Context.MODE_PRIVATE).getString("User","Dummy"),
                (int)new Date().getTime(),intentsFlip));
        realm.commitTransaction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MemoryTask extends AsyncTask<Integer, Integer, Integer[]> {
        @Override
        protected Integer[] doInBackground(Integer... integers) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return integers;
        }

        @Override
        protected void onPostExecute(Integer[] s) {

            if(cardsViews.get(s[0]) != cardsViews.get(s[1])) {
                mFlipper.flipImage(getResources().getDrawable(R.drawable.dice), cards.get(s[0]));
                tapped.set(s[0], false);
                mFlipper.flipImage(getResources().getDrawable(R.drawable.dice), cards.get(s[1]));
                tapped.set(s[1], false);
            }
            flip = true;
            intentsFlip++;
            mMemoryIntent.setText(String.valueOf(intentsFlip));
            if(intentsFlip > 10)
                mMemoryLoser.setVisibility(View.VISIBLE);
            if (intentsFlip > 12)
                mMemoryLoser2.setVisibility(View.VISIBLE);
            if (intentsFlip > 15)
                mMemoryLoser3.setVisibility(View.VISIBLE);
            boolean allTapped = true;
            for (boolean tap : tapped)
            {
                if(!tap) {
                    allTapped = false;
                    break;
                }
            }
            if(allTapped) {
                saveIntents();
                createAlertDialog();
            }
            super.onPostExecute(s);
        }
    }


}
