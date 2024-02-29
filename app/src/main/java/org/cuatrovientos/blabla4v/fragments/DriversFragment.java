package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.adapters.DriversAdapter;
import org.cuatrovientos.blabla4v.models.Site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriversFragment extends Fragment {

    ExpandableListView expandableListView;
    DriversAdapter adapter;
    List<String> sites;
    HashMap<String, List<String>> drivers;

    public DriversFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers, container, false);

        expandableListView = view.findViewById(R.id.expandibleList);
        db = FirebaseFirestore.getInstance();
        CollectionReference tablaDrivers = db.collection("driver");
        CollectionReference tablaUsers = db.collection("user");


        final boolean[] isFragmentVisible = {false};
        Button addRoute = view.findViewById(R.id.addRouteButton)  ;

        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentVisible[0]) {
                    CreateRouteFragment createRouteFragment = new CreateRouteFragment();
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction(); // Utiliza getChildFragmentManager() para obtener el FragmentManager del Fragment actual
                    fragmentTransaction.add(R.id.driversFragment, createRouteFragment, "createRouteFragment");
                    fragmentTransaction.commit();
                    isFragmentVisible[0] = true;
                } else {
                    // Cerrar el fragmento
                    FragmentManager fragmentManager = getChildFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag("createRouteFragment");
                    if (fragment != null) {
                        fragmentManager.beginTransaction().remove(fragment).commit();
                    }
                    isFragmentVisible[0] = false;
                }
            }
        });

        sites = new ArrayList<>();
        drivers = new HashMap<>();

        // Inicializar el adaptador con la lista de sitios y conductores
        adapter = new DriversAdapter(getContext(), sites, drivers);
        expandableListView.setAdapter(adapter);

        Log.e(TAG, "PASA POR AQUI");
        for (String m : municipios) {
            Log.e(TAG, "PASA POR AQUI" + m);
            final String municipio = m; // Variable final local para utilizar dentro de la devolución de llamada
            sites.add(municipio);
            Query query = tablaDrivers.whereGreaterThan("location", municipio);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<String> conductores = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String location = document.getString("location");
                        Log.e(TAG, "PASA POR AQUI");
                        if (location.equals(municipio)){
                            conductores.add(document.getString("userId"));
                        }
                    }
                    // Agregar conductores al HashMap después de que se complete la consulta
                    drivers.put(municipio, conductores);
                    adapter.notifyDataSetChanged(); // Notificar al adaptador sobre los cambios en los datos
                }
            });
        }

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Object selectedItem = drivers.get(sites.get(groupPosition)).get(childPosition);
            // Hacer algo con el conductor seleccionado si es necesario
            return false;
        });

        return view;
    }
}