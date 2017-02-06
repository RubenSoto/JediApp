package com.rubn.jediapp.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rubn.jediapp.R;
import com.rubn.jediapp.activities.LoginActivity;
import com.rubn.jediapp.activities.MainActivity;
import com.rubn.jediapp.calculator.Calculator;
import com.rubn.jediapp.calculator.Operands;

import butterknife.ButterKnife;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;


public class CalculatorFragment extends Fragment {

    private final String ALREADY_LOGGED = "Already logged";

    private Button mButtonC;
    private Button mButtonErase;
    private Button mButtonDiv;
    private Button mButtonMult;
    private Button mButtonSum;
    private Button mButtonSubs;
    private Button mButtonEqual;
    private Button mButtonDot;
    private Button mButton0;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private TextView mTextResult;
    private Calculator mCalculator;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public CalculatorFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        registerEvents(rootView);
        mCalculator = Calculator.getInstance();

        return rootView;
    }

    private void registerEvents(View rootView) {
        mTextResult = (TextView) rootView.findViewById(R.id.calc_text_result);
        mButtonC = (Button)rootView.findViewById(R.id.btn_calc_delete);
        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setDelete());
            }
        });
        mButtonErase = (Button)rootView.findViewById(R.id.btn_calc_erase);
        mButtonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setErase());
            }
        });
        mButtonDiv = (Button)rootView.findViewById(R.id.btn_calc_division);
        mButtonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setOperand(Operands.DIVISION));

            }
        });
        mButtonMult = (Button)rootView.findViewById(R.id.btn_calc_mult);
        mButtonMult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setOperand(Operands.MULTIPLICATION));
            }
        });
        mButtonSum = (Button)rootView.findViewById(R.id.btn_calc_summ);
        mButtonSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setOperand(Operands.SUM));
            }
        });
        mButtonSubs = (Button)rootView.findViewById(R.id.btn_calc_minus);
        mButtonSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setOperand(Operands.SUBSTRACT));
            }
        });
        mButtonEqual = (Button)rootView.findViewById(R.id.btn_calc_equal);
        mButtonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setOperand(Operands.EQUAL));
            }
        });
        /*mButtonDot = (Button)rootView.findViewById(R.id.btn_calc_point);
        mButtonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        mButton0 = (Button)rootView.findViewById(R.id.btn_calc_0);
        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(0));
            }
        });
        mButton1 = (Button)rootView.findViewById(R.id.btn_calc_1);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(1));
            }
        });
        mButton2 = (Button)rootView.findViewById(R.id.btn_calc_2);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(2));
            }
        });
        mButton3 = (Button)rootView.findViewById(R.id.btn_calc_3);
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(3));
            }
        });
        mButton4 = (Button)rootView.findViewById(R.id.btn_calc_4);
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(4));
            }
        });
        mButton5 = (Button)rootView.findViewById(R.id.btn_calc_5);
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(5));
            }
        });
        mButton6 = (Button)rootView.findViewById(R.id.btn_calc_6);
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(6));
            }
        });
        mButton7 = (Button)rootView.findViewById(R.id.btn_calc_7);
        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(7));
            }
        });
        mButton8 = (Button)rootView.findViewById(R.id.btn_calc_8);
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(8));
            }
        });
        mButton9 = (Button)rootView.findViewById(R.id.btn_calc_9);
        mButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextResult(mCalculator.setNumber(9));
            }
        });
    }
    private void setTextResult(double result){
        if(result == NaN || result == POSITIVE_INFINITY || result == NEGATIVE_INFINITY)
        {
            SharedPreferences settings = getActivity().getSharedPreferences(ALREADY_LOGGED, Context.MODE_PRIVATE);
            if(settings.getBoolean("StateNotification", false))
                createNotification();
            else
                createToast();
        }
        else {
            if (result % 1 == 0)
                mTextResult.setText(String.valueOf((int) result));
            else
                mTextResult.setText(String.valueOf(result));
        }
    }
    private void createNotification()
    {
        //Instanciamos Notification Manager
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // Para la notificaciones, en lugar de crearlas directamente, lo hacemos mediante
        // un Builder/contructor.
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.dice)
                        .setContentTitle("Math Error!")
                        .setContentText(getResources().getString(R.string.alert_dialog_calc));
        // Creamos un intent explicito, para abrir la app desde nuestra notificación
        Intent resultIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        //Generamos la backstack y le añadimos el intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        //Obtenemos el pending intent
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void createToast(){
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.alert_dialog_calc), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calculator_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.calc_menu_call:
                startActivity(new Intent(Intent.ACTION_DIAL, null));
                break;
            case R.id.calc_menu_nav:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                break;
            case R.id.calc_menu_logout:
                settings = getActivity().getSharedPreferences(ALREADY_LOGGED,0);
                editor = settings.edit();
                editor.putBoolean("Logged", false);
                editor.apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.calc_menu_toast:
                settings = getActivity().getSharedPreferences(ALREADY_LOGGED,0);
                editor= settings.edit();
                editor.putBoolean("StateNotification", false);
                editor.apply();
                break;
            case R.id.calc_menu_state_notification:
                settings = getActivity().getSharedPreferences(ALREADY_LOGGED,0);
                editor= settings.edit();
                editor.putBoolean("StateNotification", true);
                editor.apply();
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
