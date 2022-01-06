package com.example.tiendadbx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tiendadbx.BD.Articulos;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ActivityRUD extends AppCompatActivity {
    ConectedBD db;
    EditText et_nombre, et_id, et_cantidad, et_precio, et_descripcion;
    SwitchCompat btn_flash;
    boolean flash = false;
    FloatingActionButton btn_scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        db = new ConectedBD(this,ArticulosContract.DATABASE_NAME,null,1);
        componentes();
        eventos();
    }

    private void componentes() {
        et_id = findViewById(R.id.et_id);
        et_nombre = findViewById(R.id.et_nombre);
        et_cantidad = findViewById(R.id.et_cantidad);
        et_precio = findViewById(R.id.et_precio);
        et_descripcion = findViewById(R.id.et_descripcion);

        btn_flash = findViewById(R.id.flash);
        btn_scanner = findViewById(R.id.btn_scanner);
    }

    private void eventos() {
        btn_scanner.setOnClickListener( e -> scanner() );
        btn_flash.setOnCheckedChangeListener((buttonView, isChecked) -> flash = isChecked);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rud, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //Busca el IC el cual fue llamado
        if(id == R.id.ic_search){
            Toast.makeText(getBaseContext(), "Consulta", Toast.LENGTH_SHORT).show();
            consulta();
        }else if(id == R.id.ic_save){
            Toast.makeText(getBaseContext(), "Actualiza", Toast.LENGTH_SHORT).show();
            update();
        }else if(id == R.id.ic_delete){
            Toast.makeText(getBaseContext(), "Elimina", Toast.LENGTH_SHORT).show();
            delete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void consulta() {
        String id = et_id.getText().toString();
        if (!id.isEmpty()){
            Articulos articulo = db.obtenArticulo(et_id.getText().toString());
            if(articulo != null){
                et_nombre.setText(articulo.getNombre());
                et_cantidad.setText(String.valueOf(articulo.getCantidad()));
                et_precio.setText(String.valueOf(articulo.getPrecio()));
                et_descripcion.setText(articulo.getDescripcion());
            }else{
                Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese un ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void update(){
        String id = et_id.getText().toString();
        if (!id.isEmpty()){
            Articulos articulo = new Articulos();
            articulo.setId(id);
            articulo.setNombre(et_nombre.getText().toString());
            articulo.setCantidad(Integer.parseInt(et_cantidad.getText().toString()));
            articulo.setPrecio(Float.parseFloat(et_precio.getText().toString()));
            articulo.setDescripcion(et_descripcion.getText().toString());
            if (db.updateArticulo(articulo)){
                Toast.makeText(getBaseContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Ingrese un ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(){
        String id = et_id.getText().toString();
        if (!id.isEmpty()){
            if (db.deleteArticulo(id)){
                Toast.makeText(this, "Articulo Eliminado", Toast.LENGTH_SHORT).show();
                limpiaET();
            }else{
                Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Ingrese un ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiaET() {
        et_id.setText("");
        et_nombre.setText("");
        et_cantidad.setText("");
        et_precio.setText("");
        et_descripcion.setText("");
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

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    et_id.setText(result.getContents());
                }
            });

}