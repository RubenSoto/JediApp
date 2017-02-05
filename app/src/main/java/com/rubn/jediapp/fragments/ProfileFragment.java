package com.rubn.jediapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubn.jediapp.R;
import com.rubn.jediapp.activities.LoginActivity;
import com.rubn.jediapp.realm.RankingPuntuation;
import com.rubn.jediapp.realm.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private final String ALREADY_LOGGED = "Already logged";
    private final int SELECT_PICTURE = 100;
    private TextView mTextName;
    private TextView mTextIntents;
    private TextView mTextLocation;
    private ImageView mImageAvatar;
    private Realm realm;
    private RealmConfiguration config;
    private List<Address> addressList;
    private User user;


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        Realm.init(getActivity());
        config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        mTextName = (TextView) rootView.findViewById(R.id.profile_name);
        String userName = this.getActivity().getSharedPreferences(ALREADY_LOGGED, Context.MODE_PRIVATE).getString("User", "Dummy");
        mTextName.setText(userName);
        mTextIntents = (TextView) rootView.findViewById(R.id.profile_best_intents);
        mTextLocation = (TextView) rootView.findViewById(R.id.profile_location);
        mImageAvatar = (ImageView) rootView.findViewById(R.id.profile_picture);
        realm = Realm.getInstance(config);
        List<User> users = realm.where(User.class).equalTo("name",userName).findAll();
        if(users.size() > 0) {
            user = users.get(0);
            mImageAvatar.setImageBitmap(BitmapFactory.decodeByteArray(users.get(0).getImage(), 0, users.get(0).getImage().length));
        }
        getBestIntent(userName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocation();
        }
        mImageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder gc = new Geocoder(getActivity().getApplicationContext());
                try {
                    //5 mxresults
                    addressList = gc.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < addressList.size(); ++i) {
                    mTextLocation.setText(addressList.get(i).getAddressLine(0));
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
            return;
        }
        //provider, tiempo, distancia, listener
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void getBestIntent(String userName) {
        List<RankingPuntuation> rank = realm.where(RankingPuntuation.class).equalTo("name",userName).findAllSorted("intents", Sort.ASCENDING);
        if(rank.size() > 0)
            mTextIntents.setText(getResources().getString(R.string.profile_best)+ " " + String.valueOf(rank.get(0).getIntents()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_toolbar_logout:
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    mImageAvatar.setImageURI(selectedImageUri);
                    realm = Realm.getInstance(config);
                    realm.beginTransaction();
                    Bitmap bitmap = ((BitmapDrawable)mImageAvatar.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();
                    user.setImage(bitmapdata);
                    realm.copyToRealmOrUpdate(user);
                    realm.commitTransaction();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
