package com.rubn.jediapp.activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rubn.jediapp.R;
import com.rubn.jediapp.realm.User;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private Realm realm;
    private RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //ButterKnife.bind(this);
        Realm.init(getApplicationContext());
        config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        setToolbar();
        mNameEditText = (EditText) findViewById(R.id.register_name_text);
        mPasswordEditText = (EditText) findViewById(R.id.register_password_text);

        mRegisterButton = (Button) findViewById(R.id.register_button_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(mNameEditText.getText().toString().length() > 0 && mPasswordEditText.getText().toString().length() > 0) {
                    Intent intentChangeToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    realm = Realm.getInstance(config);
                    if (realm.where(User.class).equalTo("name", mNameEditText.getText().toString()).findAll().size() > 0)
                        createAlertDialog();
                    else {
                        realm.beginTransaction();
                        Bitmap bitmap = ((BitmapDrawable)getDrawable(R.drawable.user)).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();
                        realm.copyToRealmOrUpdate(new User(mNameEditText.getText().toString(), mPasswordEditText.getText().toString(),bitmapdata, false));
                        realm.commitTransaction();
                        startActivity(intentChangeToLogin);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_register_msg)
                .setCancelable(false)
                .setNeutralButton(R.string.alert_dialog_register_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setToolbar (){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
