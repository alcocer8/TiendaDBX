package com.example.tiendadbx.BD;

import android.provider.BaseColumns;

public class ArticulosContract implements BaseColumns {

    public static final String _ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String DESCRIPCION = "descripcion";
    public static final String CANTIDAD = "cantidad";
    public static final String PRECIO = "precio";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Articulos.db";
    public static final String TABLE_NAME = "articulos";
    public static final String TABLE_NAME_REGISTRO = "registro";

    public static String DROP_ARTICULOS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String CREATE_TABLE = "CREATE TABLE " + ArticulosContract.TABLE_NAME + "(" + ArticulosContract._ID + " VARCHAR(20) NOT NULL, " + ArticulosContract.NOMBRE + " VARCHAR(30) NOT NULL, " + ArticulosContract.CANTIDAD + " INT NOT NULL, " + ArticulosContract.PRECIO + " VARCHAR(10) NOT NULL, " + ArticulosContract.DESCRIPCION + " TEXT, PRIMARY KEY(" + ArticulosContract._ID + "))";


}
