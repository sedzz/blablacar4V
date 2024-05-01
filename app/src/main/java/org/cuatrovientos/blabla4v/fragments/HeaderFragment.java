package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.cuatrovientos.blabla4v.R;

public class HeaderFragment extends Fragment {

    public HeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        ImageView profileImage = view.findViewById(R.id.profile); // Asegúrate de que este es el ID correcto para la imagen de perfil
        //TextView title = view.findViewById(R.id.title); // Asegúrate de que este es el ID correcto para el título

       // title.setText("Mi título"); // Establece el texto del título

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea una nueva instancia de UserSettingsFragment
                UserSettingsFragment userSettingsFragment = new UserSettingsFragment();

                // Usa FragmentTransaction para reemplazar el fragmento actual con UserSettingsFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.menuInterfaz, userSettingsFragment); // Ahora usamos R.id.menuInterfaz como el contenedor de fragmentos
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}