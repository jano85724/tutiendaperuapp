package com.ma_developers.tutiendaperu.BD_SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ma_developers.tutiendaperu.Entidad.ItemProducto;
import com.ma_developers.tutiendaperu.Entidad.Producto;

import java.util.ArrayList;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    private static String BD_nombre = "tutiendaperu";
    private static int BD_version = 1;
    private String TABLA_CATEGORIA = "CATEGORIA";
    private String TABLA_PRODUCTO = "PRODUCTO";

    private SQLiteDatabase SQLiteDB;

    public AdminSQLiteOpenHelper(Context context) {
        super(context, BD_nombre, null, BD_version);
        SQLiteDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_CATEGORIA + "(id INT PRIMARY KEY, imgFoto TEXT, nombre TEXT)");
        db.execSQL("CREATE TABLE " + TABLA_PRODUCTO + "(id INT PRIMARY KEY, idCategoria INT, imgFoto TEXT, nombre TEXT, ingredientes TEXT, preparacion TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLA_CATEGORIA);
        db.execSQL("drop table if exists " + TABLA_PRODUCTO);

        db.execSQL("CREATE TABLE " + TABLA_CATEGORIA + "(id INT PRIMARY KEY, imgFoto TEXT, nombre TEXT)");
        db.execSQL("CREATE TABLE " + TABLA_PRODUCTO + "(id INT PRIMARY KEY, idCategoria INT, imgFoto TEXT, nombre TEXT, ingredientes TEXT, preparacion TEXT)");
    }

    public void InsertarCategorias(ContentValues registro){
        SQLiteDB.insert(TABLA_CATEGORIA, null, registro);
    }

    public void InsertarProducto(ContentValues registro){
        SQLiteDB.insert(TABLA_PRODUCTO, null, registro);
    }

    public ArrayList<ItemProducto> getMenuCategorias(){
        ArrayList<ItemProducto> Categorias = new ArrayList<>();
        Cursor registros = SQLiteDB.rawQuery("SELECT * FROM " + TABLA_CATEGORIA, null);
        while (registros.moveToNext()){
            Categorias.add(new ItemProducto(registros.getInt(0), registros.getString(1), registros.getString(2)));
        }
        return Categorias;
    }

    public ArrayList<Producto> getProductosCategoria(int idCategoria){
        ArrayList<Producto> Productos = new ArrayList<>();
        Cursor registros = SQLiteDB.rawQuery("SELECT * FROM " + TABLA_PRODUCTO + " WHERE idCategoria = " + idCategoria, null);
        while (registros.moveToNext()){
            Productos.add(new Producto(registros.getInt(0), registros.getInt(1), registros.getString(2), registros.getString(3), registros.getString(4), registros.getString(5)));
        }
        return Productos;
    }

    public void cerrarConexion(){
        SQLiteDB.close();
    }
}
