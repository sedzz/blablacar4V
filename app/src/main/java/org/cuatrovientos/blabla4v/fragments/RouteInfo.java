package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.models.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteInfo extends Fragment {

    private Route selectedRoute;

    private TextView textViewRoute;
    private TextView textViewDriver;
    private TextView textViewAvailableSeats;
    private TextView textViewDate;
    private TextView textViewPassengers;
    private Button buttonSaveChanges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_route_info, container, false);

        if (getArguments() != null) {
            selectedRoute = (Route) getArguments().getSerializable("selectedRoute");
        }

        textViewRoute = view.findViewById(R.id.routeInfo);
        textViewDriver = view.findViewById(R.id.driver);
        textViewAvailableSeats = view.findViewById(R.id.availableSeats);
        textViewDate = view.findViewById(R.id.dates);
        textViewPassengers = view.findViewById(R.id.passengers);
        buttonSaveChanges = view.findViewById(R.id.btnSaveChanges);
        Button buttonBack = view.findViewById(R.id.button_back);

        if (selectedRoute != null) {
            textViewRoute.setText(selectedRoute.getStart() + " - " + selectedRoute.getEnd());
            textViewAvailableSeats.setText(String.valueOf(selectedRoute.getAvailableSeats()));
            textViewDate.setText(selectedRoute.getDate() + " " + selectedRoute.getTime());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<String> passengerIds = selectedRoute.getPassengers();
            List<String> passengerNames = new ArrayList<>();
            String driverId = selectedRoute.getDriver();

            // Mover la consulta del conductor fuera del bucle de los pasajeros
            db.collection("user").document(driverId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot userDocument = task.getResult();
                        if (userDocument.exists()) {
                            // Obtén el nombre del usuario del documento
                            String driverName = userDocument.getString("name");

                            // Establece el nombre del conductor en el TextView
                            textViewDriver.setText(driverName);
                        }
                    }
                }
            });

            for (String passengerId : passengerIds) {
                db.collection("user").document(passengerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDocument = task.getResult();
                            if (userDocument.exists()) {
                                String userName = userDocument.getString("name");
                                passengerNames.add(userName);

                                // Update the passengers TextView
                                String passengers = String.join(", ", passengerNames);
                                textViewPassengers.setText(passengers);
                            }
                        }
                    }
                });
            }

            // Check if the route is full
            if (passengerIds.size() >= selectedRoute.getAvailableSeats()) {
                buttonSaveChanges.setVisibility(View.GONE); // Hide the button
            }
        }
        buttonBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());


        buttonSaveChanges.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                if (selectedRoute != null && selectedRoute.getId() != null) {
                    DocumentReference routeRef = db.collection("routes").document(selectedRoute.getId());
                    routeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    List<String> passengers = (List<String>) document.get("passengers");
                                    if (passengers != null && passengers.contains(userId)) {
                                        Toast.makeText(getActivity(), "Ya te has unido a esta ruta", Toast.LENGTH_SHORT).show();
                                    } else if (userId.equals(selectedRoute.getDriver())) {
                                        Toast.makeText(getActivity(), "No puedes unirte a tu propia ruta", Toast.LENGTH_SHORT).show();
                                    } else {
                                        routeRef.update("passengers", FieldValue.arrayUnion(userId))
                                                .addOnSuccessListener(aVoid -> {
                                                    // Get the user's name
                                                    db.collection("user").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot userDocument = task.getResult();
                                                                if (userDocument.exists()) {
                                                                    String userName = userDocument.getString("name");
                                                                    // Check if there are already names in the TextView
                                                                    if (textViewPassengers.getText().length() > 0) {
                                                                        // Append a comma and the user name to the TextView
                                                                        textViewPassengers.append(", " + userName);
                                                                    } else {
                                                                        // Append the user name to the TextView
                                                                        textViewPassengers.append(userName);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                    Toast.makeText(getActivity(), "Te has unido al trayecto", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getActivity(), "Error al unirse al trayecto", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error al obtener la ruta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    private boolean isCurrentUserDriver(String userId) {
        return selectedRoute.getDriver().equals(userId);
    }
}