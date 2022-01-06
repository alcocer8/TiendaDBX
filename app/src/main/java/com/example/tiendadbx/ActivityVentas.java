package com.example.tiendadbx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tiendadbx.BD.Articulos;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;

import java.util.ArrayList;

public class ActivityVentas extends AppCompatActivity {
    int cont = 0;
    static ArrayList<Articulos> listaCarrito = new ArrayList<>();
    VentaFragment ventaFragment;
    CarritoFragment carritoFragment;
    DescuentoFragment descuentoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        ventaFragment = new VentaFragment();
        carritoFragment = new CarritoFragment();
        descuentoFragment = new DescuentoFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedor_ventas, ventaFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ventas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (item.getItemId() == R.id.menu_carrito){
            if (cont == 0){
                cont++;
                transaction.replace(R.id.contenedor_ventas, carritoFragment);

            }else{
                cont--;
                transaction.replace(R.id.contenedor_ventas, ventaFragment);
            }
        }else if(item.getItemId() == R.id.menu_descuento){
            transaction.replace(R.id.contenedor_ventas, descuentoFragment);
        }

        transaction.commit();

        return super.onOptionsItemSelected(item);
    }

}