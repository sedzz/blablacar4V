package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.adapters.RouteExpandableListAdapter;
import org.cuatrovientos.blabla4v.models.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriversFragment extends Fragment {

    private ExpandableListView expandableListViewRoutes;
    private RouteExpandableListAdapter routeExpandableListAdapter;
    private List<Route> listDataHeader;
    private HashMap<Route, List<String>> listDataChild;

    public DriversFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers, container, false);

        Button addRouteButton = view.findViewById(R.id.addRouteButton);
        expandableListViewRoutes = view.findViewById(R.id.expandibleList);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        routeExpandableListAdapter = new RouteExpandableListAdapter(requireContext(), listDataHeader, listDataChild);
        expandableListViewRoutes.setAdapter(routeExpandableListAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("routes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Route route = document.toObject(Route.class);
                            listDataHeader.add(route);
                            listDataChild.put(route, route.getPassengers());
                        }
                        routeExpandableListAdapter.notifyDataSetChanged();
                    }
                });


        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                CreateRouteFragment createRouteFragment = (CreateRouteFragment) fragmentManager.findFragmentByTag("createRouteFragment");

                if (createRouteFragment == null || !createRouteFragment.isVisible()) {
                    createRouteFragment = new CreateRouteFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.driversFragment, createRouteFragment, "createRouteFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    fragmentManager.popBackStack();
                }
            }
        });

        return view;
    }
}