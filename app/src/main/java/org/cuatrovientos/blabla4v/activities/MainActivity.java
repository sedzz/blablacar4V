package org.cuatrovientos.blabla4v.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.interfaces.ApiService;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toggleFragmentsVisibility("map");

        ImageButton buttonMap = findViewById(R.id.buttonMap);
        ImageButton buttonDrivers = findViewById(R.id.buttonVolante);

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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (mapOrDrivers.equalsIgnoreCase("map")) {
            ft.hide(driversFragment);
            ft.show(mapFragment);
        } else {
            ft.hide(mapFragment);
            ft.show(driversFragment);
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

    public void createRoute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Object> response = getRetrofit().create(ApiService.class).getRoute("", "", "").execute();
                    if (response.isSuccessful()) {
                        System.out.println("Route created");
                    } else {
                        System.out.println("Error creating route");
                    }
                } catch (IOException e) {
                    System.out.println("Error creating route");

                }
            }
        }).start();
    }


        public Retrofit getRetrofit() {
            return new Retrofit.Builder()
                    .baseUrl("https://api.openrouteservice.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

