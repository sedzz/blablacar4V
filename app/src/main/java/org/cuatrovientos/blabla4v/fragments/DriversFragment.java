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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

        routeExpandableListAdapter = new RouteExpandableListAdapter(requireContext(), listDataHeader, listDataChild, expandableListViewRoutes);        expandableListViewRoutes.setAdapter(routeExpandableListAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();

        // Establecer el día de la semana como el primer día de la semana
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date startDate = calendar.getTime();

        // Avanzar 6 días para obtener el último día de la semana
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = calendar.getTime();

        // Formatear las fechas como cadenas en el formato "dd-MM-yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String startDateString = sdf.format(startDate);
        String endDateString = sdf.format(endDate);

        // Realizar la consulta para obtener las rutas de esta semana
        db.collection("routes")
                .whereGreaterThanOrEqualTo("date", startDateString)
                .whereLessThanOrEqualTo("date", endDateString)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Route route = document.toObject(Route.class);
                            route.setId(document.getId());
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
                    fragmentTransaction.add(R.id.drivers_fragment, createRouteFragment, "createRouteFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(createRouteFragment);
                    fragmentTransaction.commit();
                    fragmentManager.popBackStack();
                }
            }
        });

        return view;
    }
}