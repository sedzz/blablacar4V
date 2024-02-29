package org.cuatrovientos.blabla4v.fragments;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.adapters.DriversAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.tasks.OnSuccessListener;
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

    FirebaseFirestore db;

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



        String[] municipios = {"Abáigar","Abárzuza/Abartzuza","Abaurregaina/Abaurrea Alta","Abaurrepea/Abaurrea Baja","Aberin","Ablitas","Adiós","Aguilar de Codés","Aibar/Oibar","Allín/Allin","Allo","Altsasu/Alsasua","Améscoa Baja","Ancín/Antzin","Andosilla","Ansoáin/Antsoain","Anue","Añorbe","Aoiz/Agoitz","Araitz","Arakil","Aranarache/Aranaratxe","Aranguren","Arano","Arantza","Aras","Arbizu","Arce/Artzi","Arellano","Areso","Arguedas","Aria","Aribe","Armañanzas","Arróniz","Arruazu","Artajona","Artazu","Atez/Atetz","Auritz/Burguete","Ayegui/Aiegi","Azagra","Azuelo","Bakaiku","Barañain","Barásoain","Barbarin","Bargota","Barillas","Basaburua","Baztan","Beintza-Labaien","Beire","Belascoáin","Bera","Berbinzana","Beriáin","Berrioplano/Berriobeiti","Berriozar","Bertizarana","Betelu","Bidaurreta","Biurrun-Olcoz","Buñuel","Burgui/Burgi","Burlada/Burlata","Cabanillas","Cabredo","Cadreita","Caparroso","Cárcar","Carcastillo","Cascante","Cáseda","Castejón","Castillonuevo","Cendea de Olza","Cintruénigo","Cirauqui/Zirauki","Ciriza/Ziritza","Cizur","Corella","Cortes","Desojo","Dicastillo","Donamaria","Doneztebe/Santesteban","Echarri/Etxarri","El Busto","Elgorriaga","Enériz/Eneritz","Eratsun","Ergoiena","Erro","Eslava","Esparza de Salazar","Espronceda","Estella-Lizarra","Esteribar","Etayo","Etxalar","Etxarri Aranatz","Etxauri","Eulate","Ezcabarte","Ezcároz/Ezkaroze","Ezkurra","Ezprogui","Falces","Fitero","Fontellas","Funes","Fustiñana","Galar","Gallipienzo/Galipentzu","Gallués/Galoze","Garaioa","Garde","Garínoain","Garralda","Genevilla","Goizueta","Goñi","Güesa/Gorza","Guesálaz/Gesalatz","Guirguillano","Hiriberri/Villanueva de Aezkoa","Huarte/Uharte","Ibargoiti","Igantzi","Igúzquiza","Imotz","Irañeta","Irurtzun","Isaba/Izaba","Ituren","Iturmendi","Iza/Itza","Izagaondoa","Izalzu/Itzaltzu","Jaurrieta","Javier","Juslapeña / Txulapain","Lakuntza","Lana","Lantz","Lapoblación","Larraga","Larraona","Larraun","Lazagurría","Leache/Leatxe","Legarda","Legaria","Leitza","Lekunberri","Leoz/Leotz","Lerga","Lerín","Lesaka","Lezaun","Liédena","Lizoain","Lodosa","Lónguida/Longida","Los Arcos","Lumbier","Luquin","Luzaide/Valcarlos","Mañeru","Marañón","Marcilla","Mélida","Mendavia","Mendaza","Mendigorría","Metauten","Milagro","Mirafuentes","Miranda de Arga","Monreal/Elo","Monteagudo","Morentin","Mues","Murchante","Murieta","Murillo el Cuende","Murillo el Fruto","Muruzábal","Navascués/Nabaskoze","Nazar","Noáin","Obanos","Ochagavía/Otsagabia","Oco","Odieta","Oiz","Olaibar","Olazti/Olazagutía","Olejua","Olite/Erriberri","Olóriz/Oloritz","Orbaizeta","Orbara","Orísoain","Orkoien","Oronz","Oroz-Betelu","Orreaga/Roncesvalles","Oteiza","Pamplona/Iruña","Peralta/Azkoien","Petilla de Aragón","Piedramillera","Pitillas","Puente la Reina/Gares","Pueyo / Puiu","Ribaforada","Romanzado / Erromantzatua","Roncal/Erronkari","Sada","Saldías","Salinas de Oro/Jaitz","San Adrián","San Martín de Unx","Sangüesa/Zangoza","Sansol","Santacara","Sarriés/Sartze","Sartaguda","Sesma","Sorlada","Sunbilla","Tafalla","Tiebas-Muruarte de Reta","Tirapu","Torralba del Río","Torres del Río","Tudela","Tulebras","Ucar","Uharte Arakil","Ujué/Uxue","Ultzama","Unciti","Unzué/Untzue","Urdazubi/Urdax","Urdiain","Urraul Alto","Urraul Bajo","Urroz","Urroz-Villa","Urzainqui/Urzainki","Uterga","Uztárroz/Uztarroze","Valle de Egüés/Eguesibar","Valle de Ollo/Ollaran","Valle de Yerri/Deierri","Valtierra","Viana","Vidángoz/Bidankoze","Villafranca","Villamayor de Monjardín","Villatuerta","Villava/Atarrabia","Yesa","Zabalza/Zabaltza","Ziordia","Zizur Mayor/Zizur Nagusia","Zubieta","Zugarramurdi","Zúñiga"};

        sites = new ArrayList<>();
        drivers = new HashMap<>();

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