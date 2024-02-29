package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.cuatrovientos.blabla4v.R;

public class CreateRouteFragment extends Fragment {


    public CreateRouteFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


       // Spinner spinnerVuelta = view.findviewby

        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }
}