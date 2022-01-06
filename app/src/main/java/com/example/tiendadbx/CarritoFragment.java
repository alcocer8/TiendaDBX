package com.example.tiendadbx;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendadbx.BD.Articulos;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import java.util.ArrayList;

public class CarritoFragment extends Fragment {

    ConectedBD db;
    TextView tv_carritoActual, tv_precio;
    RecyclerView recyclerView;
    float precioCarrito = 0;

    public CarritoFragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        db = new ConectedBD(getContext(), ArticulosContract.DATABASE_NAME, null, ArticulosContract.DATABASE_VERSION);
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        this.precioCarrito = 0;
        tv_precio.setText(tv_precio.getText().toString() + precioCarrito);
        recyclerView.setAdapter(new CarritoFragment.MyArticulosAdapter(getContext(), ActivityVentas.listaCarrito));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);
        componentes(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    private void componentes(View view) {
        tv_carritoActual = view.findViewById(R.id.tv_carrito);
        tv_precio = view.findViewById(R.id.tv_precio);

        recyclerView = view.findViewById(R.id.carrito_actual);
    }


    //Clase Adapter
    class MyArticulosAdapter extends RecyclerView.Adapter<MyArticulosVH> {
        private final Context context;
        private final ArrayList<Articulos> articulos;

        MyArticulosAdapter(Context context, ArrayList<Articulos> articulos) {
            this.articulos = articulos;
            this.context = context;
        }

        @NonNull
        @Override
        public MyArticulosVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_carrito, viewGroup, false);
            return new MyArticulosVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyArticulosVH myArticulosVH, int i) {
            Articulos articulo = articulos.get(i);
            myArticulosVH.bind(articulo.getId(), articulo.getNombre(), articulo.getPrecio(), articulo.getCantidad());
        }

        @Override
        public int getItemCount() {
            return articulos.size();
        }
    }

    //Class ViewHolder
    class MyArticulosVH extends RecyclerView.ViewHolder {

        private TextView item_codigo, item_producto, item_precio, item_cantidad;

        private final Button btn_accion;

        public MyArticulosVH(@NonNull View itemView) {
            super(itemView);

            item_codigo = itemView.findViewById(R.id.item_codigo);
            item_producto = itemView.findViewById(R.id.item_producto);
            item_precio = itemView.findViewById(R.id.item_precio);
            item_cantidad = itemView.findViewById(R.id.item_cantidad);
            btn_accion = itemView.findViewById(R.id.btn_elminar);
        }

        void bind(String codigo, String producto, double precio, int cantidad) {
            precioCarrito += (precio*cantidad);
            item_codigo.setText(item_codigo.getText() + codigo);
            item_producto.setText(item_producto.getText() + producto);
            item_precio.setText(item_precio.getText() + String.valueOf(precio*cantidad));
            item_cantidad.setText(String.valueOf(cantidad));


            tv_precio.setText("Total: $"+precioCarrito);
            btn_accion.setOnClickListener(e -> eliminaArticulo(codigo));

        }
    }

    //Funciones
    private void eliminaArticulo(String codigo) {
        ArrayList<Articulos> nuevaLista = new ArrayList<>();
        precioCarrito = 0;
        for (int i = 0; i < ActivityVentas.listaCarrito.size(); i++) {
            if (!codigo.equals(ActivityVentas.listaCarrito.get(i).getId())) {
                precioCarrito += ActivityVentas.listaCarrito.get(i).getPrecio()*ActivityVentas.listaCarrito.get(i).getCantidad();
                nuevaLista.add(ActivityVentas.listaCarrito.get(i));
            }
        }
        tv_precio.setText("Total: "+ precioCarrito);
        ActivityVentas.listaCarrito.clear();
        ActivityVentas.listaCarrito.addAll(nuevaLista);
    }


}