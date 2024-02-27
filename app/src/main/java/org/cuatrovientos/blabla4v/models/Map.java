package org.cuatrovientos.blabla4v.models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.cuatrovientos.blabla4v.R;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkGPS();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void checkGPS(){
        try {
            int gpsSignal = Settings.Secure.getInt(this.getContentResolver(),Settings.Secure.LOCATION_MODE);
            if (gpsSignal==0){
                showInfoAlert();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showInfoAlert(){
        new AlertDialog.Builder(this)
                .setTitle("GPS Alert")
                .setMessage("You don´t have the GPS signal enabled, Would you like to enable the GPS signal now?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pamplona = new LatLng(42.81687, -1.64323);
        mMap.addMarker(new MarkerOptions().position(pamplona));
        CameraPosition camera = new CameraPosition.Builder().target(pamplona).zoom(12).tilt(30).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
}
