package com.example.tiendadbx.BD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConectedBD extends SQLiteOpenHelper {

    public ConectedBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticulosContract.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ArticulosContract.DROP_ARTICULOS_TABLE);

        onCreate(db);
    }

    //CRUD
    public void createArticulo(ContentValues values) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Realizando la ejecucion del query
        db.insert(ArticulosContract.TABLE_NAME, null, values);


        db.close();
    }

    public Articulos obtenArticulo(String id) {
        //Aperturamos nuestra base de datos para lectura
        SQLiteDatabase db = this.getReadableDatabase();
        Articulos articulo = new Articulos();

        //Creamos campos que desamos obtener
        String[] columns = {ArticulosContract._ID, ArticulosContract.NOMBRE, ArticulosContract.PRECIO, ArticulosContract.CANTIDAD, ArticulosContract.DESCRIPCION};
        //La condicion que requerimos
        String selection = ArticulosContract._ID + " = ?";
        //Y el argumento que recibimos
        String[] args = {id};

        //Realizamos el Query
        Cursor cursor = db.query(ArticulosContract.TABLE_NAME, columns, selection, args, null, null, null);

        //Si tenemos una concidencia se cumple la condicion y agregamos los campos encontrados en sus respectivos EditText
        if (cursor.moveToFirst()) {
            articulo.setId(cursor.getString(0));
            articulo.setNombre(cursor.getString(1));
            articulo.setPrecio(Float.parseFloat(cursor.getString(2)));
            articulo.setCantidad(Integer.parseInt(cursor.getString(3)));
            articulo.setDescripcion(cursor.getString(4));
        }else {
            articulo = null;
        }

        //Cerrando el objecto tipo Cursor y la base de datos
        cursor.close();
        db.close();

        return articulo;
    }


    public boolean updateArticulo(Articulos articulo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            String whereClause = ArticulosContract._ID + "= ?";
            String[] whereArgs = {articulo.getId()};

            //Guardamos los datos del articulo en el contentValue
            values.put(ArticulosContract.NOMBRE, articulo.getNombre());
            values.put(ArticulosContract.PRECIO, articulo.getPrecio());
            values.put(ArticulosContract.CANTIDAD, articulo.getCantidad());
            values.put(ArticulosContract.DESCRIPCION, articulo.getDescripcion());


            //Actualizamos el elemento con el nombre de la tabla, un objecto contentValues y una clausula WHRERE
            db.update(ArticulosContract.TABLE_NAME, values, whereClause, whereArgs);

            db.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void descArticulo(String id, int cantidad) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            String whereClause = ArticulosContract._ID + "= ?";
            String[] whereArgs = {id};

            //Guardamos los datos del articulo en el contentValue
            values.put(ArticulosContract.CANTIDAD, cantidad);

            //Actualizamos el elemento con el nombre de la tabla, un obje   cto contentValues y una clausula WHRERE
            db.update(ArticulosContract.TABLE_NAME, values, whereClause, whereArgs);

            db.close();

        } catch (Exception e) {
        }
    }

    public boolean deleteArticulo(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String whereClause = ArticulosContract._ID + "= ?";
            String[] whereArgs = {id};

            db.delete(ArticulosContract.TABLE_NAME, whereClause, whereArgs);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Articulos> allArticulos() {
        List<Articulos> listaArticulos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + ArticulosContract.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Articulos articulo = new Articulos();

                articulo.setNombre(cursor.getString(1));
                articulo.setPrecio(Float.parseFloat(cursor.getString(2)));

                listaArticulos.add(articulo);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return listaArticulos;
    }

    public int countArticulos(){
        String query = "SELECT * FROM "+ ArticulosContract.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int cantidad = cursor.getCount();

        cursor.close();
        db.close();

        return cantidad;
    }
}
