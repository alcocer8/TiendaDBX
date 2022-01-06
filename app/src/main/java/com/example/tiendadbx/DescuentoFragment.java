package com.example.tiendadbx;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendadbx.BD.Articulos;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;

public class DescuentoFragment extends Fragment {

    TextView tv_total, tv_cambio;
    EditText et_dinero;
    Button btn_pagar, btn_calcular;
    ConectedBD bd;
    float total = 0;
    public DescuentoFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = new ConectedBD(getActivity(), ArticulosContract.DATABASE_NAME, null, ArticulosContract.DATABASE_VERSION);
        total = 0;
        for (int i = 0; i < ActivityVentas.listaCarrito.size(); i++){
            float precio = ActivityVentas.listaCarrito.get(i).getPrecio();
            int cantidad = ActivityVentas.listaCarrito.get(i).getCantidad();
            total += (precio*cantidad);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descuento, container, false);
        componentes(view);

        return view;
    }

    private void componentes(View view) {
        tv_cambio = view.findViewById(R.id.tv_cambio);
        tv_total = view.findViewById(R.id.tv_total);
        et_dinero = view.findViewById(R.id.et_dinero);
        btn_pagar = view.findViewById(R.id.btn_pagar);
        btn_pagar.setEnabled(false);
        btn_calcular = view.findViewById(R.id.btn_calculo);


        tv_total.setText("Total a pagar: $"+this.total);
        btn_pagar.setOnClickListener(e -> descuento());
        btn_calcular.setOnClickListener(e -> restante());
    }

    private void restante() {
        float restante = Float.parseFloat(et_dinero.getText().toString()) - this.total;
        if (restante >= 0 ){
            tv_cambio.setText("Cambio: $"+(restante));
            btn_pagar.setEnabled(true);
        }else{
            tv_cambio.setText("Restante: $"+(restante));
            btn_pagar.setEnabled(false);
        }
    }

    private void descuento() {
        for (int i = 0; i< ActivityVentas.listaCarrito.size(); i++){
            Articulos articulo = bd.obtenArticulo(ActivityVentas.listaCarrito.get(i).getId());
            String id = ActivityVentas.listaCarrito.get(i).getId();
            int CantidadInventario = articulo.getCantidad()-ActivityVentas.listaCarrito.get(i).getCantidad();

            if (ActivityVentas.listaCarrito.get(i).getCantidad() < 1 || ( CantidadInventario < 1)){
                Toast.makeText(getActivity(), "Revisa tu inventario de :"+ActivityVentas.listaCarrito.get(i).getNombre(), Toast.LENGTH_SHORT).show();
            }

            bd.descArticulo(id, CantidadInventario);
            Toast.makeText(getActivity(), "Compra terminada", Toast.LENGTH_SHORT).show();
        }
        ActivityVentas.listaCarrito.clear();
        getActivity().finish();

    }
}