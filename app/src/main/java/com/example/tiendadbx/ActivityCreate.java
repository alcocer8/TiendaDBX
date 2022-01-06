package com.example.tiendadbx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ActivityCreate extends AppCompatActivity {
    ConectedBD db;
    EditText et_nombre, et_id, et_cantidad, et_precio, et_descripcion;
    SwitchCompat btn_flash;
    boolean flash = false;
    FloatingActionButton btn_scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);


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
        btn_flash.setOnCheckedChangeListener((buttonView, isChecked) -> flash = isChecked);
        btn_scanner.setOnClickListener(e -> scanner());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_save) {
            try {
                db = new ConectedBD(this, ArticulosContract.DATABASE_NAME, null, ArticulosContract.DATABASE_VERSION);
                //ingresando los datos a un content value
                ContentValues values = new ContentValues();
                values.put(ArticulosContract._ID, et_id.getText().toString());
                values.put(ArticulosContract.NOMBRE, et_nombre.getText().toString());
                values.put(ArticulosContract.PRECIO, et_precio.getText().toString());
                values.put(ArticulosContract.CANTIDAD, Integer.parseInt(et_cantidad.getText().toString()));
                values.put(ArticulosContract.DESCRIPCION, et_descripcion.getText().toString());

                db.createArticulo(values);
                limpiaET();
                Toast.makeText(this, "Datos Insertados", Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(getBaseContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void scanner() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
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
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    et_id.setText(result.getContents());
                }
            });

    private void limpiaET() {
        et_id.setText("");
        et_nombre.setText("");
        et_cantidad.setText("");
        et_precio.setText("");
        et_descripcion.setText("");
    }

}