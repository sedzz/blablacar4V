package org.cuatrovientos.blabla4v.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.cuatrovientos.blabla4v.R;

import org.cuatrovientos.blabla4v.fragments.DriversFragment;
import org.cuatrovientos.blabla4v.fragments.SelectTravelFragment;
import org.cuatrovientos.blabla4v.fragments.UserSettingsFragment;
import org.cuatrovientos.blabla4v.interfaces.ApiService;
import org.cuatrovientos.blabla4v.models.Route;
import org.cuatrovientos.blabla4v.models.RouteResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions marker;

    private Polyline currentPolyline;
    private List<Marker> markers = new ArrayList<>();
    List<Route> routes = new ArrayList<>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DrawerLayout drawerLayout;
    AppCompatImageView buttonDrawerToggle;
    NavigationView navigationView;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawer);
        navigationView = findViewById(R.id.navigationView);

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.menu_user) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new UserSettingsFragment()).commit();
                } else if (itemId == R.id.menu_map) {

                } else if (itemId == R.id.menu_routes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new DriversFragment()).commit();
                } else if (itemId == R.id.menu_logout) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new SelectTravelFragment()).commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });



        if (user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                            }
                        }
                    });
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //toggleFragmentsVisibility("map");
        final boolean[] isFragmentVisible = {false};

        ImageButton buttonMap = findViewById(R.id.buttonMap);
        ImageButton buttonDrivers = findViewById(R.id.buttonVolante);
        //Button button2 = findViewById(R.id.btnMenuRoute);

        /**button2.setOnClickListener(new View.OnClickListener() {
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
        });**/

       /** buttonMap.setOnClickListener(new View.OnClickListener() {
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
        });**/
    }

    /**private void toggleFragmentsVisibility(String mapOrDrivers) {
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
    }**/

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
        marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.btn_star_big_on));
        mMap.addMarker(marker);

        createRoute();
    }

    private void createRoute() {
        Retrofit retrofit = getRetrofit();
        ApiService service = retrofit.create(ApiService.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("routes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firestore", document.getId() + " => " + document.getData());
                                String start = document.getString("startCoordinates");
                                String end = document.getString("endCoordinates");
                                String nameStart = document.getString("start");
                                String nameEnd = document.getString("end");


                                Call<RouteResponse> call = service.getRoute("5b3ce3597851110001cf6248228f116492e14a3786afd1ed138bb38f", start, end);

                                call.enqueue(new Callback<RouteResponse>() {
                                    @Override
                                    public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                                        if (response.isSuccessful()) {
                                            RouteResponse routeResponse = response.body();
                                            Log.d("API_CALL", "API call successful, response: " + response.body());
                                            drawRoute(response.body(),nameStart,nameEnd);

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
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    private int getRandomColor() {
        int alpha = 255; // fully opaque
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        return Color.argb(alpha, red, green, blue);
    }

    private void drawRoute(RouteResponse body,String start, String end) {
        PolylineOptions polylineOptions = new PolylineOptions();
        List<List<Double>> coordinatesList = body.getFeatures().get(0).getGeometry().getCoordinates();
        for (int i = 0; i < coordinatesList.size(); i++) {
            LatLng latLng = new LatLng(coordinatesList.get(i).get(1), coordinatesList.get(i).get(0));
            polylineOptions.add(latLng);

            // Si estamos en el primer o último elemento de la lista, agregamos un marcador
            if (i == 0 || i == coordinatesList.size() - 1) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                if (i == 0) {
                    markerOptions.title(start);
                } else {
                    markerOptions.title(end);
                }
                // Agregamos el marcador al mapa y a la lista de marcadores
                markers.add(mMap.addMarker(markerOptions));
            }
        }

        // Establecemos el color de la línea de la ruta
        polylineOptions.color(getRandomColor());

        // Dibujamos la nueva Polyline y la guardamos en currentPolyline
        currentPolyline = mMap.addPolyline(polylineOptions);
    }



}
