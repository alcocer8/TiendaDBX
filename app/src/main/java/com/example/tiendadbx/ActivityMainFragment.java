package com.example.tiendadbx;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ActivityMainFragment extends Fragment implements View.OnClickListener {

    public ActivityMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_main, container, false);

        componentes(view);

        return view;
    }

    private void componentes(View view) {
        view.findViewById(R.id.btn_agregar).setOnClickListener(this);
        view.findViewById(R.id.btn_editar).setOnClickListener(this);
        view.findViewById(R.id.btn_venta).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        if (id == R.id.btn_agregar) {
            intent = new Intent(getActivity(), ActivityCreate.class);
            startActivity(intent);
        } else if (id == R.id.btn_editar) {
            intent = new Intent(getActivity(), ActivityRUD.class);
            startActivity(intent);
        } else if( id == R.id.btn_venta){
            intent = new Intent(getActivity(), ActivityVentas.class);
            startActivity(intent);
        }
    }
}