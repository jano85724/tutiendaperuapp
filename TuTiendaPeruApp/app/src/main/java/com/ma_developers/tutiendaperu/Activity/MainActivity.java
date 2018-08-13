package com.ma_developers.tutiendaperu.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ma_developers.tutiendaperu.Adaptador.MenuAdapter;
import com.ma_developers.tutiendaperu.BD_SQLite.AdminSQLiteOpenHelper;
import com.ma_developers.tutiendaperu.Entidad.ItemProducto;
import com.ma_developers.tutiendaperu.R;
import com.ma_developers.tutiendaperu.Util.InternetConnection;
import com.ma_developers.tutiendaperu.Util.Urls;
import com.ma_developers.tutiendaperu.conexionVolley.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvMenu;
    //private RelativeLayout rlSinConexion;
    private ArrayList<ItemProducto> listaMenu;
    private MenuAdapter adapter;
    private ProgressDialog pDialog;
    private InternetConnection internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMenu = (ListView) findViewById(R.id.lvMenu);
        //rlSinConexion = (RelativeLayout) findViewById(R.id.rlSinconexion);
        internet = new InternetConnection(this);
        if(internet.ConexionWifi() || internet.ConexionDatos()) {
            CargarOpcionesMenu();
        }
        else{
            mostrarSinConexion();
        }
    }

    private void CargarOpcionesMenu(){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, Urls.GetListarCategorias(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() > 0) {
                            listaMenu = new ArrayList<>();
                            try{
                                for (int i=0; i<response.length(); i++){
                                    JSONObject object = response.getJSONObject(i);
                                    ItemProducto menu = new ItemProducto(object.getInt("Id"), Urls.getDominioCategoria() + object.getString("Imagen"), object.getString("Descripcion"));
                                    listaMenu.add(menu);
                                }

                                mostrarCategorias();

                                pDialog.dismiss();
                                //rlSinConexion.setVisibility(View.GONE);

                                Task newTask = new Task(MainActivity.this, listaMenu);
                                newTask.execute();
                            } catch (JSONException e) {
                                pDialog.dismiss();
                            }
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //rlSinConexion.setVisibility(View.VISIBLE);
                mostrarSinConexion();
            }
        });

        VolleyController.getInstance().addToRequestQueue(request);
    }

    private void mostrarCategorias(){
        try {
            adapter = new MenuAdapter(MainActivity.this, listaMenu);
            lvMenu.setAdapter(adapter);
        }
        catch (Exception ex){}
    }

    private void mostrarSinConexion(){
        try {
            Snackbar.make(lvMenu, getString(R.string.sin_internet), Snackbar.LENGTH_LONG).show();

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
            listaMenu = admin.getMenuCategorias();
            mostrarCategorias();

            admin.cerrarConexion();
        }
        catch (Exception ex){}
    }




    private class Task extends AsyncTask<Void,Void, Void>{
        private Context context;
        private ArrayList<ItemProducto> listaMenu;
        private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

        public Task(Context context,ArrayList<ItemProducto> listaMenu) {
            this.context = context;
            this.listaMenu = listaMenu;
            adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            GuardarEnSQLite();
            return null;
        }

        private void GuardarEnSQLite(){
            try {
                for (ItemProducto item : listaMenu) {
                    ContentValues registro = new ContentValues();
                    registro.put("id", item.getId());
                    registro.put("nombre", item.getNombre());
                    registro.put("imgFoto", item.getImgItem());

                    adminSQLiteOpenHelper.InsertarCategorias(registro);
                }
            }
            catch (Exception ex){}

            adminSQLiteOpenHelper.cerrarConexion();
        }
    }
}
