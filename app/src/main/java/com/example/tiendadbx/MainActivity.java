package com.example.tiendadbx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.tiendadbx.BD.Articulos;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ConectedBD db;
    ArrayList<Articulos> articulosList;
    RecyclerView recyclerView;
    ActivityMainFragment activityMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new ConectedBD(this, ArticulosContract.DATABASE_NAME,null,ArticulosContract.DATABASE_VERSION);

        //Fragment Botones
        activityMainFragment = new ActivityMainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_activity_main, activityMainFragment).commit();

        //Productos Recientes
        articulosList = new ArrayList<>();
        recyclerView = findViewById(R.id.articulosList);
        recyclerView.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onResume() {
        super.onResume();
        articulosList.clear();
        articulosList.addAll(db.allArticulos());
        recyclerView.setAdapter(new MyArticulosAdapter(this, articulosList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_login){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Class Adaptaer
    static class MyArticulosAdapter extends RecyclerView.Adapter<MyArticulosVH> {
        private final Context context;
        private final ArrayList<Articulos> articulos;

        MyArticulosAdapter (Context context, ArrayList<Articulos> articulos) {
            this.articulos = articulos;
            this.context = context;
        }

        @NonNull
        @Override
        public MyArticulosVH onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from (context);
            View view = inflater.inflate (R.layout.item, viewGroup, false);

            return new MyArticulosVH(view);
        }

        @Override
        public void onBindViewHolder (@NonNull MyArticulosVH myArticulosVH, int i) {
            Articulos articulo = articulos.get (i);
            myArticulosVH.bind( articulo.getNombre(), String.valueOf(articulo.getPrecio()) );
        }

        @Override
        public int getItemCount () {
            return articulos.size ();
        }
    }

    //Class ViewHolder
    static class MyArticulosVH extends RecyclerView.ViewHolder{

        private final TextView tv_nombre;
        private final TextView tv_cantidad;


        public MyArticulosVH(@NonNull View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.item_productoM);
            tv_cantidad = itemView.findViewById(R.id.item_cantidadM);
        }

        void bind(String nombre, String cantidad){
            tv_nombre.setText(tv_nombre.getText() + nombre);
            tv_cantidad.setText(tv_cantidad.getText() + cantidad);
        }
    }
    

}