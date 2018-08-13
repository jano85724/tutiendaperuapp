package com.ma_developers.tutiendaperu.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ma_developers.tutiendaperu.Adaptador.ProductoAdapter;
import com.ma_developers.tutiendaperu.BD_SQLite.AdminSQLiteOpenHelper;
import com.ma_developers.tutiendaperu.Entidad.ItemProducto;
import com.ma_developers.tutiendaperu.Entidad.Producto;
import com.ma_developers.tutiendaperu.R;
import com.ma_developers.tutiendaperu.Util.InternetConnection;
import com.ma_developers.tutiendaperu.Util.Urls;
import com.ma_developers.tutiendaperu.Util.Util;
import com.ma_developers.tutiendaperu.conexionVolley.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaProductos extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView rvListarProductos;
    private ArrayList<Producto> listaProducto;
    private ProductoAdapter adapter;
    private GridLayoutManager manager;
    private ItemProducto categoria;
    private ProgressDialog pDialog;

    private InternetConnection internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoria = (ItemProducto) getIntent().getExtras().getSerializable("categoria");
        setTitle(categoria.getNombre().toString());

        internet = new InternetConnection(this);
        rvListarProductos = (RecyclerView) findViewById(R.id.rvListarProductos);

        manager = new GridLayoutManager(this, 2);
        rvListarProductos.setLayoutManager(manager);

        if(internet.ConexionDatos() || internet.ConexionWifi()) {
            CargarListaProductos(categoria);
        }
        else{
            mostrarSinConexion();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //manager.setSpanCount(3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu_buscar, menu);
            final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) myActionMenuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Util.OcultarTeclado(ListaProductos.this);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    try {
                        adapter.filter(s.toString().trim());
                    }
                    catch (Exception ex){}
                    return true;
                }
            });
        }
        catch (Exception ex){
            System.out.println("Exception : " + ex);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                onBackPressed();
                return true;
            default:
                return  true;
        }
    }

    private void CargarListaProductos(final ItemProducto categoria){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Urls.GetListarProductos(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listaProducto = new ArrayList<>();
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i=0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                Producto producto = new Producto(object.getInt("Id"), object.getInt("idCategoria"), Urls.getDominioProducto() + object.getString("Imagen"), object.getString("Nombre"),
                                        object.getString("Informacion"), object.getString("Especificacion"));
                                listaProducto.add(producto);
                            }

                            mostrarProductos();

                            Task task = new Task(ListaProductos.this, listaProducto);
                            task.execute();

                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        mostrarSinConexion();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("idCategoria", String.valueOf(categoria.getId()));
                return map;
            }
        };

        VolleyController.getInstance().addToRequestQueue(request);
    }

    private void mostrarProductos(){
        adapter = new ProductoAdapter(ListaProductos.this, listaProducto, categoria.getNombre());
        rvListarProductos.setAdapter(adapter);
    }

    private void mostrarSinConexion(){
        try {
            Snackbar.make(rvListarProductos, getString(R.string.sin_internet), Snackbar.LENGTH_LONG).show();

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
            listaProducto = admin.getProductosCategoria(categoria.getId());
            mostrarProductos();

            admin.cerrarConexion();
        }
        catch (Exception ex){}
    }


    

    private class Task extends AsyncTask<Void,Void, Void> {
        private Context context;
        private ArrayList<Producto> listaProductos;
        private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

        public Task(Context context,ArrayList<Producto> listaProductos) {
            this.context = context;
            this.listaProductos = listaProductos;
            adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            GuardarEnSQLite();
            return null;
        }

        private void GuardarEnSQLite(){
            try {
                for (Producto item : listaProductos) {
                    ContentValues registro = new ContentValues();
                    registro.put("id", item.getId());
                    registro.put("idCategoria", item.getIdCategoria());
                    registro.put("imgFoto", item.getImgFoto());
                    registro.put("nombre", item.getNombre());
                    registro.put("informacion", item.getInformacion());
                    registro.put("especificaciones", item.getEspecificaciones());

                    adminSQLiteOpenHelper.InsertarProducto(registro);
                }
            }
            catch (Exception ex){}

            adminSQLiteOpenHelper.cerrarConexion();
        }
    }
}
