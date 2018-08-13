package com.ma_developers.tutiendaperu.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ma_developers.tutiendaperu.Adaptador.InformacionPagerAdapter;
import com.ma_developers.tutiendaperu.Entidad.Producto;
import com.ma_developers.tutiendaperu.R;
import com.squareup.picasso.Picasso;

public class ProductoDetalle extends AppCompatActivity {
    private ImageView imgProducto;
    private TextView tvNombre;
    private Producto itemProducto;
    private TabLayout tabLayout;
    private ViewPager vpInformacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getExtras().getString("categoria"));

        itemProducto = (Producto) getIntent().getExtras().getSerializable("ItemProducto");
        imgProducto = (ImageView) findViewById(R.id.imgProducto);
        tvNombre = (TextView) findViewById(R.id.tvNombre);

        MostrarViewPagerEnTabLayout();
        CargarVista();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_item_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        itemProducto.getNombre() + "\n\n" + getString(R.string.informacion) + ":\n" + itemProducto.getInformacion() +
                                "\n\n" + getString(R.string.especificaciones) +":\n" + itemProducto.getEspecificaciones() + "\n\n" + getString(R.string.app_compartir));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.compartir_con)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compartir, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void MostrarViewPagerEnTabLayout() {
        try {
            InformacionPagerAdapter adapter = new InformacionPagerAdapter(this, getSupportFragmentManager(), itemProducto);
            vpInformacion = (ViewPager) findViewById(R.id.vpPager);
            vpInformacion.setAdapter(adapter);

            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(vpInformacion);
        }
        catch (Exception ex){}
    }

    private void CargarVista() {
        try {
            Picasso.with(this).load(itemProducto.getImgFoto())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgProducto);
            tvNombre.setText(itemProducto.getNombre().toString());
        }
        catch (Exception ex){}
    }
}