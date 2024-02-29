package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriversFragment extends Fragment {

    ExpandableListView expandableListView;
    DriversAdapter adapter;
    List<String> sites;
    HashMap<String, List<String>> drivers;

    public DriversFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers, container, false);

        expandableListView = view.findViewById(R.id.expandibleList);

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






        String[] municipios = {
                "Abáigar",
                "Abárzuza/Abartzuza",
                "Abaurregaina/Abaurrea Alta",
                "Abaurrepea/Abaurrea Baja",
                "Aberin",
                "Ablitas",
                "Adiós",
                "Aguilar de Codés",
                "Aibar/Oibar",
                "Allín/Allin",
                "Allo",
                "Altsasu/Alsasua",
                "Améscoa Baja",
                "Ancín/Antzin",
                "Andosilla",
                "Ansoáin/Antsoain",
                "Anue",
                "Añorbe",
                "Aoiz/Agoitz",
                "Araitz",
                "Arakil",
                "Aranarache/Aranaratxe",
                "Aranguren",
                "Arano",
                "Arantza",
                "Aras",
                "Arbizu",
                "Arce/Artzi",
                "Arellano",
                "Areso",
                "Arguedas",
                "Aria",
                "Aribe",
                "Armañanzas",
                "Arróniz",
                "Arruazu",
                "Artajona",
                "Artazu",
                "Atez/Atetz",
                "Auritz/Burguete",
                "Ayegui/Aiegi",
                "Azagra",
                "Azuelo",
                "Bakaiku",
                "Barañain",
                "Barásoain",
                "Barbarin",
                "Bargota",
                "Barillas",
                "Basaburua",
                "Baztan",
                "Beintza-Labaien",
                "Beire",
                "Belascoáin",
                "Bera",
                "Berbinzana",
                "Beriáin",
                "Berrioplano/Berriobeiti",
                "Berriozar",
                "Bertizarana",
                "Betelu",
                "Bidaurreta",
                "Biurrun-Olcoz",
                "Buñuel",
                "Burgui/Burgi",
                "Burlada/Burlata",
                "Cabanillas",
                "Cabredo",
                "Cadreita",
                "Caparroso",
                "Cárcar",
                "Carcastillo",
                "Cascante",
                "Cáseda",
                "Castejón",
                "Castillonuevo",
                "Cendea de Olza",
                "Cintruénigo",
                "Cirauqui/Zirauki",
                "Ciriza/Ziritza",
                "Cizur",
                "Corella",
                "Cortes",
                "Desojo",
                "Dicastillo",
                "Donamaria",
                "Doneztebe/Santesteban",
                "Echarri/Etxarri",
                "El Busto",
                "Elgorriaga",
                "Enériz/Eneritz",
                "Eratsun",
                "Ergoiena",
                "Erro",
                "Eslava",
                "Esparza de Salazar",
                "Espronceda",
                "Estella-Lizarra",
                "Esteribar",
                "Etayo",
                "Etxalar",
                "Etxarri Aranatz",
                "Etxauri",
                "Eulate",
                "Ezcabarte",
                "Ezcároz/Ezkaroze",
                "Ezkurra",
                "Ezprogui",
                "Falces",
                "Fitero",
                "Fontellas",
                "Funes",
                "Fustiñana",
                "Galar",
                "Gallipienzo/Galipentzu",
                "Gallués/Galoze",
                "Garaioa",
                "Garde",
                "Garínoain",
                "Garralda",
                "Genevilla",
                "Goizueta",
                "Goñi",
                "Güesa/Gorza",
                "Guesálaz/Gesalatz",
                "Guirguillano",
                "Hiriberri/Villanueva de Aezkoa",
                "Huarte/Uharte",
                "Ibargoiti",
                "Igantzi",
                "Igúzquiza",
                "Imotz",
                "Irañeta",
                "Irurtzun",
                "Isaba/Izaba",
                "Ituren",
                "Iturmendi",
                "Iza/Itza",
                "Izagaondoa",
                "Izalzu/Itzaltzu",
                "Jaurrieta",
                "Javier",
                "Juslapeña / Txulapain",
                "Lakuntza",
                "Lana",
                "Lantz",
                "Lapoblación",
                "Larraga",
                "Larraona",
                "Larraun",
                "Lazagurría",
                "Leache/Leatxe",
                "Legarda",
                "Legaria",
                "Leitza",
                "Lekunberri",
                "Leoz/Leotz",
                "Lerga",
                "Lerín",
                "Lesaka",
                "Lezaun",
                "Liédena",
                "Lizoain",
                "Lodosa",
                "Lónguida/Longida",
                "Los Arcos",
                "Lumbier",
                "Luquin",
                "Luzaide/Valcarlos",
                "Mañeru",
                "Marañón",
                "Marcilla",
                "Mélida",
                "Mendavia",
                "Mendaza",
                "Mendigorría",
                "Metauten",
                "Milagro",
                "Mirafuentes",
                "Miranda de Arga",
                "Monreal/Elo",
                "Monteagudo",
                "Morentin",
                "Mues",
                "Murchante",
                "Murieta",
                "Murillo el Cuende",
                "Murillo el Fruto",
                "Muruzábal",
                "Navascués/Nabaskoze",
                "Nazar",
                "Noáin",
                "Obanos",
                "Ochagavía/Otsagabia",
                "Oco",
                "Odieta",
                "Oiz",
                "Olaibar",
                "Olazti/Olazagutía",
                "Olejua",
                "Olite/Erriberri",
                "Olóriz/Oloritz",
                "Orbaizeta",
                "Orbara",
                "Orísoain",
                "Orkoien",
                "Oronz",
                "Oroz-Betelu",
                "Orreaga/Roncesvalles",
                "Oteiza",
                "Pamplona/Iruña",
                "Peralta/Azkoien",
                "Petilla de Aragón",
                "Piedramillera",
                "Pitillas",
                "Puente la Reina/Gares",
                "Pueyo / Puiu",
                "Ribaforada",
                "Romanzado / Erromantzatua",
                "Roncal/Erronkari",
                "Sada",
                "Saldías",
                "Salinas de Oro/Jaitz",
                "San Adrián",
                "San Martín de Unx",
                "Sangüesa/Zangoza",
                "Sansol",
                "Santacara",
                "Sarriés/Sartze",
                "Sartaguda",
                "Sesma",
                "Sorlada",
                "Sunbilla",
                "Tafalla",
                "Tiebas-Muruarte de Reta",
                "Tirapu",
                "Torralba del Río",
                "Torres del Río",
                "Tudela",
                "Tulebras",
                "Ucar",
                "Uharte Arakil",
                "Ujué/Uxue",
                "Ultzama",
                "Unciti",
                "Unzué/Untzue",
                "Urdazubi/Urdax",
                "Urdiain",
                "Urraul Alto",
                "Urraul Bajo",
                "Urroz",
                "Urroz-Villa",
                "Urzainqui/Urzainki",
                "Uterga",
                "Uztárroz/Uztarroze",
                "Valle de Egüés/Eguesibar",
                "Valle de Ollo/Ollaran",
                "Valle de Yerri/Deierri",
                "Valtierra",
                "Viana",
                "Vidángoz/Bidankoze",
                "Villafranca",
                "Villamayor de Monjardín",
                "Villatuerta",
                "Villava/Atarrabia",
                "Yesa",
                "Zabalza/Zabaltza",
                "Ziordia",
                "Zizur Mayor/Zizur Nagusia",
                "Zubieta",
                "Zugarramurdi",
                "Zúñiga"
        };

        sites = new ArrayList<>();
        drivers = new HashMap<>();

        // Inicializar el adaptador con la lista de sitios y conductores
        adapter = new DriversAdapter(getContext(), sites, drivers);
        expandableListView.setAdapter(adapter);

        // Referencia a la base de datos de Firebase
        DatabaseReference databaseDrivers = FirebaseDatabase.getInstance().getReference().child("drivers");
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference().child("user");

        databaseDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (String municipio : municipios) {

                    List<String> conductores = new ArrayList<>();

                    for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                        String location = driverSnapshot.child("location").getValue(String.class);
                        String driverId = driverSnapshot.child("userId").getValue(String.class);

                        if (location.equalsIgnoreCase(municipio)) {
                            databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot userSnapshot) {

                                    for (DataSnapshot u: userSnapshot.getChildren()){
                                        String userId = u.child("id").getValue(String.class);

                                        if (userId.equals(driverId)){
                                            String userName = u.child("name").getValue(String.class);
                                            conductores.add(userName);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                    sites.add(municipio);
                    drivers.put(municipio, conductores);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Object selectedItem = drivers.get(sites.get(groupPosition)).get(childPosition);
            return false;
        });
        return view;
    }
}