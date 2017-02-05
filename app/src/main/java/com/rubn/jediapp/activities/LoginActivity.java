package com.rubn.jediapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rubn.jediapp.R;
import butterknife.ButterKnife;

import com.rubn.jediapp.realm.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity {

    final String TWITTER_KEY = "nLXcUYkIDHGkJ9fJQoDq0IQ91";
    final String TWITTER_SECRET = "yKq5yleFoaiinrMcK0rH9xSZ3NHfgPxTfeywUWJToTmG5kZ4yX";
    private TwitterLoginButton loginButton;
    private Intent intentChangeToMain;
    private final String ALREADY_LOGGED = "Already logged";
    private Button mRegisterButton;
    private Button mLoginButton;
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private Realm realm;
    private RealmConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intentChangeToMain = new Intent(LoginActivity.this, MainActivity.class);
        SharedPreferences settings = getSharedPreferences(ALREADY_LOGGED, Context.MODE_PRIVATE);
        if(settings.getBoolean("Logged", false))
            startActivity(intentChangeToMain);
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        //ButterKnife.bind(this);
        Realm.init(getApplicationContext());
        config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()

                TwitterSession session = result.data;
                Bitmap bitmap = ((BitmapDrawable)getDrawable(R.drawable.user)).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                realm = Realm.getInstance(config);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new User(session.getUserName(), null, bitmapdata, true));
                realm.commitTransaction();

                SharedPreferences settings = getSharedPreferences(ALREADY_LOGGED,0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Logged", true);
                editor.putString("User", session.getUserName());
                editor.apply();

                startActivity(intentChangeToMain);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChangeToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentChangeToRegister);
            }
        });
        mNameEditText = (EditText) findViewById(R.id.login_name_text);
        mPasswordEditText = (EditText) findViewById(R.id.login_password_text);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentChangeToMain = new Intent(LoginActivity.this, MainActivity.class);
                realm = Realm.getInstance(config);
                List<User> user = realm.where(User.class).equalTo("name",mNameEditText.getText().toString()).findAll();
                if( user.size() == 1 && !user.get(0).isTwitter()
                        && user.get(0).getPassword().equals(mPasswordEditText.getText().toString())){
                    SharedPreferences settings = getSharedPreferences(ALREADY_LOGGED,0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("Logged", true);
                    editor.putString("User", mNameEditText.getText().toString());
                    editor.apply();
                    startActivity(intentChangeToMain);
                }
                else
                    createAlertDialog();
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentChangeToMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentChangeToMain);
            }
        });*/
    }
    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_login_msg)
                .setCancelable(false)
                .setNeutralButton(R.string.alert_dialog_login_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
