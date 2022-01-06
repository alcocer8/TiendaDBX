package com.example.tiendadbx;

import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tiendadbx.BD.Articulos;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class VentaFragment extends Fragment {

    ConectedBD db;
    EditText et_nombre, et_id, et_cantidad, et_precio, et_descripcion;
    SwitchCompat btn_flash;
    boolean flash = false;
    FloatingActionButton btn_scanner, btn_agregar, btn_buscar;

    public VentaFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ConectedBD(getContext(), ArticulosContract.DATABASE_NAME, null, ArticulosContract.DATABASE_VERSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venta, container, false);
        componentes(view);
        eventos();
        return view;
    }

    private void componentes(View view) {
        et_id = view.findViewById(R.id.et_id);
        et_nombre = view.findViewById(R.id.et_nombre);
        et_nombre.setEnabled(false);
        et_cantidad = view.findViewById(R.id.et_cantidad);
        et_precio = view.findViewById(R.id.et_precio);
        et_precio.setEnabled(false);
        et_descripcion = view.findViewById(R.id.et_descripcion);
        et_descripcion.setEnabled(false);
        btn_scanner = view.findViewById(R.id.btn_scanner);
        btn_agregar = view.findViewById(R.id.btn_agregar);
        btn_buscar = view.findViewById(R.id.btn_buscar);
        btn_flash = view.findViewById(R.id.flash);
    }

    private void eventos() {
        btn_agregar.setOnClickListener(e -> agregaLista());
        btn_flash.setOnCheckedChangeListener((buttonView, isChecked) -> flash = isChecked);
        btn_scanner.setOnClickListener(e -> scanner());
        btn_buscar.setOnClickListener(e -> busca(et_id.getText().toString()));
    }

    private void agregaLista() {
        if (Integer.parseInt(et_cantidad.getText().toString()) < 1 || et_cantidad.getText().toString().equals("")){
            Toast.makeText(getContext(), "Ingresa un cantidad.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Articulos articulo = new Articulos();
            articulo.setId(et_id.getText().toString());
            articulo.setNombre(et_nombre.getText().toString());
            articulo.setPrecio(Float.parseFloat(et_precio.getText().toString()));
            articulo.setCantidad(Integer.parseInt(et_cantidad.getText().toString()));
            articulo.setDescripcion(et_descripcion.getText().toString());
            ActivityVentas.listaCarrito.add(articulo);

            limpiaET();
            Toast.makeText(getContext(), "Datos Agregados", Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e){
            Toast.makeText(getContext(), "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
        }
    }



    private void scanner() {
        barcodeLauncher.launch(new ScanOptions());
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);
        options.setTorchEnabled(flash);
        barcodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    busca(result.getContents());
                }
            });

    private void busca(String id) {
        Articulos articulo = db.obtenArticulo(id);

        if (articulo != null) {
            et_id.setText(articulo.getId());
            et_nombre.setText(articulo.getNombre());
            et_precio.setText(String.valueOf(articulo.getPrecio()));
            et_descripcion.setText(articulo.getDescripcion());
        } else {
            Toast.makeText(getContext(), "Error.", Toast.LENGTH_SHORT).show();
        }
    }
    private void limpiaET() {
        et_id.setText("");
        et_nombre.setText("");
        et_cantidad.setText("");
        et_precio.setText("");
        et_descripcion.setText("");
    }





}