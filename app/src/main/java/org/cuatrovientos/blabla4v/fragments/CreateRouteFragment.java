package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.utils.Locations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreateRouteFragment extends Fragment {

    public CreateRouteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_route, container, false);

        Spinner spinnerStart = view.findViewById(R.id.spinnerStart);
        Spinner spinnerEnd = view.findViewById(R.id.spinnerEnd);
        EditText editTextSeats = view.findViewById(R.id.editTextSeats);
        Button buttonCreateRoute = view.findViewById(R.id.buttonCreateRoute);

        Locations locations = new Locations();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, locations.getMunicipios());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStart.setAdapter(adapter);
        spinnerEnd.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        buttonCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start = spinnerStart.getSelectedItem().toString();
                String end = spinnerEnd.getSelectedItem().toString();
                int availableSeats = Integer.parseInt(editTextSeats.getText().toString());

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    db.collection("user").document(currentUser.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String currentUserName = documentSnapshot.getString("name");

                                    Map<String, Object> ruta = new HashMap<>();
                                    ruta.put("driver", currentUserName);
                                    ruta.put("start", start);
                                    ruta.put("end", end);
                                    ruta.put("availableSeats", availableSeats);
                                    ruta.put("passengers", Arrays.asList()); // Aquí puedes agregar los usuarios que se asignarán a la ruta

                                    db.collection("routes")
                                            .add(ruta)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(requireContext(), "Ruta creada exitosamente", Toast.LENGTH_SHORT).show();
                                                    // Vuelve al fragmento anterior después de crear la ruta
                                                    requireActivity().getSupportFragmentManager().popBackStack();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(requireContext(), "Error al crear la ruta", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                }
            }
        });

        return view;
    }
}