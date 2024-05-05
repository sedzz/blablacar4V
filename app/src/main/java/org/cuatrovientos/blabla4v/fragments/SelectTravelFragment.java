package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.utils.Locations;

public class SelectTravelFragment extends Fragment {


    private boolean primeraSeleccion = true;

    public SelectTravelFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_travel, container, false);

        Spinner spinnerVuelta = view.findViewById(R.id.spinnerVuelta);
        Spinner spinnerIda = view.findViewById(R.id.spinnerIda);

//        String[] opciones = {"Cuatrovientos","Opción 1", "Opción 2", "Opción 3"};
        Locations locations = new Locations();

        String[] municipiosArray = locations.getCoordenadas().keySet().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, municipiosArray);

        spinnerVuelta.setAdapter(adapter);
        spinnerIda.setAdapter(adapter);

        // Establecer la primera opción por defecto en ambos Spinners
        spinnerVuelta.setSelection(1);
        spinnerIda.setSelection(1);

        // Banderas para controlar la primera selección
        final boolean[] primeraSeleccionVuelta = {true};
        final boolean[] primeraSeleccionIda = {true};
        final boolean[] seleccionado = {true};

        spinnerVuelta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                if (!primeraSeleccionVuelta[0] && seleccionado[0]) {
                    seleccionado[0] = false;
                    // Verifica si el valor seleccionado es "cuatrovientos"
                    if (!selectedValue.equals("cuatrovientos")) {
                        spinnerIda.setSelection(0); // Configura automáticamente el otro spinner en la "Opción 2"
                        spinnerIda.setEnabled(false);
                    }
                }
                primeraSeleccionVuelta[0] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Maneja el caso en el que no se selecciona ningún elemento
            }
        });

        spinnerIda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                if (!primeraSeleccionIda[0] && seleccionado[0]) {
                    seleccionado[0] = false;
                    // Verifica si el valor seleccionado es "cuatrovientos"
                    if (!selectedValue.equals("cuatrovientos")) {
                        spinnerVuelta.setSelection(0); // Configura automáticamente el otro spinner en la "Opción 2"
                        spinnerVuelta.setEnabled(false);
                    }
                }
                primeraSeleccionIda[0] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Maneja el caso en el que no se selecciona ningún elemento
            }
        });
        return view;
    }
}