package org.cuatrovientos.blabla4v.fragments;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log; // Importa Log solo una vez
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.adapters.DriversAdapter;
import org.cuatrovientos.blabla4v.utils.Locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DriversFragment extends Fragment {

    ExpandableListView expandableListView;
    DriversAdapter adapter;
    List<String> sites;
    HashMap<String, List<String>> drivers;

    FirebaseFirestore mFireStore;
    FirebaseAuth mAuth;

    public DriversFragment() {
        // Constructor público vacío requerido
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers, container, false);

        expandableListView = view.findViewById(R.id.expandibleList);
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        sites = new ArrayList<>();
        drivers = new HashMap<>();

        // Inicializar el adaptador con la lista de sitios y conductores
        adapter = new DriversAdapter(getContext(), sites, drivers);
        expandableListView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tablaDrivers = db.collection("driver");
        Locations locations = new Locations();
        for (String m : locations.getMunicipios()) {
            sites.add(m);
            List<String> conductores = new ArrayList<>();

            tablaDrivers.whereEqualTo("location", m).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("userId");
                        conductores.add(userId);
                    }
                } else {
                }
            });

            drivers.put(m, conductores);
            adapter.notifyDataSetChanged();
        }

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Object selectedItem = drivers.get(sites.get(groupPosition)).get(childPosition);
            return false;
        });
        return view;
    }
}