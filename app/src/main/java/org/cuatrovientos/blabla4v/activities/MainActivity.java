package org.cuatrovientos.blabla4v.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.android.gms.maps.model.MarkerOptions;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.interfaces.ApiService;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.cuatrovientos.blabla4v.fragments.SelectTravelFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toggleFragmentsVisibility("map");
        final boolean[] isFragmentVisible = {false};

        ImageButton buttonMap = findViewById(R.id.buttonMap);
        ImageButton buttonDrivers = findViewById(R.id.buttonVolante);
        Button button2 = findViewById(R.id.btnMenuRoute);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentVisible[0]) {
                    SelectTravelFragment selectTravelFragment = new SelectTravelFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.map, selectTravelFragment, "selectTravelFragment");
                    fragmentTransaction.commit();
                    isFragmentVisible[0] = true;
                } else {
                    // Cerrar el fragmento
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag("selectTravelFragment");
                    if (fragment != null) {
                        fragmentManager.beginTransaction().remove(fragment).commit();
                    }
                    isFragmentVisible[0] = false;
                }
            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFragmentsVisibility("map");
            }
        });

        buttonDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFragmentsVisibility("drivers");
            }
        });
    }

    private void toggleFragmentsVisibility(String mapOrDrivers) {
        Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        Fragment driversFragment = getSupportFragmentManager().findFragmentById(R.id.driversFragment);
        Fragment filtroFragment = getSupportFragmentManager().findFragmentById(R.id.btnMenuRoute);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (mapOrDrivers.equalsIgnoreCase("map")) {
            ft.hide(driversFragment);
            ft.show(mapFragment);
            ft.show(filtroFragment);
        } else {
            ft.hide(mapFragment);
            ft.show(driversFragment);
            ft.hide(filtroFragment);
        }
        ft.commit();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pamplona = new LatLng(42.81687, -1.64323);
        CameraPosition camera = new CameraPosition.Builder().target(pamplona).zoom(11).tilt(30).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
}
