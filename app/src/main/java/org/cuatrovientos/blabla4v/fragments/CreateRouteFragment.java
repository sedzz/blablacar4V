package org.cuatrovientos.blabla4v.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        EditText editTextDate = view.findViewById(R.id.editTextDate);
        EditText editTextTime = view.findViewById(R.id.editTextTime);
        Button buttonCreateRoute = view.findViewById(R.id.buttonCreateRoute);

        Locations locations = new Locations();


        List<String> municipiosList = new ArrayList<>(locations.getCoordenadas().keySet());

        Collections.sort(municipiosList);

        String[] municipiosArray = municipiosList.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, municipiosArray);

        spinnerStart.setAdapter(adapter);
        spinnerEnd.setAdapter(adapter);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editTextTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        buttonCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start = spinnerStart.getSelectedItem().toString();
                String end = spinnerEnd.getSelectedItem().toString();
                int availableSeats = Integer.parseInt(editTextSeats.getText().toString());
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();

                // Obtener las coordenadas de las ubicaciones de inicio y fin
                String startCoordinates = locations.getCoordenadas().get(start);
                String endCoordinates = locations.getCoordenadas().get(end);

                // Invertir las coordenadas
                String invertedStartCoordinates = invertCoordinates(startCoordinates);
                String invertedEndCoordinates = invertCoordinates(endCoordinates);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    db.collection("user").document(currentUser.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Map<String, Object> ruta = new HashMap<>();
                                    ruta.put("driver", currentUser.getUid());
                                    ruta.put("start", start);
                                    ruta.put("end", end);
                                    ruta.put("startCoordinates", invertedStartCoordinates); // Agregar las coordenadas de inicio invertidas
                                    ruta.put("endCoordinates", invertedEndCoordinates); // Agregar las coordenadas de fin invertidas
                                    ruta.put("availableSeats", availableSeats);
                                    ruta.put("date", date);
                                    ruta.put("time", time);
                                    ruta.put("passengers", Arrays.asList());

                                    db.collection("routes")
                                            .add(ruta)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(requireContext(), "Ruta creada exitosamente", Toast.LENGTH_SHORT).show();
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

            private String invertCoordinates(String coordinates) {
                String[] splitCoordinates = coordinates.split(",");
                return splitCoordinates[1] + "," + splitCoordinates[0];
            }
        });



        return view;
    }
}