package org.cuatrovientos.blabla4v.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.cuatrovientos.blabla4v.R;

import org.cuatrovientos.blabla4v.fragments.SelectTravelFragment;
import org.cuatrovientos.blabla4v.interfaces.ApiService;
import org.cuatrovientos.blabla4v.models.RouteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions marker;
    String start = "";
    String end = "";
    private Polyline currentPolyline;
    private List<Marker> markers = new ArrayList<>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                Toast.makeText(MainActivity.this, "Token: " + idToken, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

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
        Button buttonRoute = findViewById(R.id.btnMenuRoute);
        Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        Fragment driversFragment = getSupportFragmentManager().findFragmentById(R.id.driversFragment);
        Fragment filtroFragment = getSupportFragmentManager().findFragmentById(R.id.btnMenuRoute);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (mapOrDrivers.equalsIgnoreCase("map")) {
            ft.hide(driversFragment);
            ft.show(mapFragment);
            buttonRoute.setVisibility(View.VISIBLE);

        } else {
            ft.hide(mapFragment);
            ft.show(driversFragment);
            buttonRoute.setVisibility(View.INVISIBLE);
        }
        ft.commit();
    }

    private Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        marker = new MarkerOptions();



        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(15);
        LatLng cuatrovientos = new LatLng(42.8242834, -1.659874);
        LatLng pamplona = new LatLng(42.81687, -1.64323);

        CameraPosition camera = new CameraPosition.Builder().target(pamplona).zoom(11).tilt(30).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));


        marker.position(cuatrovientos);
        marker.title("Centro de Formación Profesional Cuatrovientos");
        marker.draggable(true);
       // marker.snippet("Caja de texto para introducir datos mas extensos");
        marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.btn_star_big_on));
        mMap.addMarker(marker);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(start.isEmpty()){
                    start = latLng.longitude+","+latLng.latitude;
                } else if (end.isEmpty()) {
                    end = latLng.longitude+","+latLng.latitude;
                    createRoute();
                    // Reset start and end for the next route
                    start = "";
                    end = "";
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MainActivity.this,"Click long on \n"+
                        "Lat: "+latLng.latitude+"\n"+
                        "Lng: "+latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.hideInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.showInfoWindow();
                Toast.makeText(MainActivity.this,"Marker Dragged to \n"+
                        "Lat: "+marker.getPosition().latitude+"\n"+
                        "Lng: "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void createRoute() {
        Retrofit retrofit = getRetrofit();
        ApiService service = retrofit.create(ApiService.class);
        Call<RouteResponse> call = service.getRoute("5b3ce3597851110001cf6248228f116492e14a3786afd1ed138bb38f", start, end);

        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    RouteResponse routeResponse = response.body();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Route created", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("API_CALL", "API call successful, response: " + response.body());
                    drawRoute(response.body());

                } else {
                    Log.d("API_CALL", "API call unsuccessful, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Log.d("API_CALL", "API call failed, error: " + t.getMessage());
            }
        });
    }

    private void drawRoute(RouteResponse body) {
        // Si ya existe una Polyline, la eliminamos
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        // Eliminamos los marcadores existentes
        for (Marker marker : markers) {
            marker.remove();
        }
        // Limpiamos la lista de marcadores
        markers.clear();

        PolylineOptions polylineOptions = new PolylineOptions();
        List<List<Double>> coordinatesList = body.getFeatures().get(0).getGeometry().getCoordinates();
        for (int i = 0; i < coordinatesList.size(); i++) {
            LatLng latLng = new LatLng(coordinatesList.get(i).get(1), coordinatesList.get(i).get(0));
            polylineOptions.add(latLng);

            // Si estamos en el primer o último elemento de la lista, agregamos un marcador
            if (i == 0 || i == coordinatesList.size() - 1) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                if (i == 0) {
                    markerOptions.title("Start");
                } else {
                    markerOptions.title("End");
                }
                // Agregamos el marcador al mapa y a la lista de marcadores
                markers.add(mMap.addMarker(markerOptions));
            }
        }

        // Dibujamos la nueva Polyline y la guardamos en currentPolyline
        currentPolyline = mMap.addPolyline(polylineOptions);
    }

}
