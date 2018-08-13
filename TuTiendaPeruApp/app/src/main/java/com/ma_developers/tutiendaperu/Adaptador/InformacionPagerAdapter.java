package com.ma_developers.tutiendaperu.Adaptador;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ma_developers.tutiendaperu.Entidad.Producto;
import com.ma_developers.tutiendaperu.Fragmento.FragmentoInformacion;
import com.ma_developers.tutiendaperu.Fragmento.FragmentoEspecificacion;
import com.ma_developers.tutiendaperu.R;


public class InformacionPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Producto itemTrago;
    private Bundle bundle;

    public InformacionPagerAdapter(Context context, FragmentManager fm, Producto itemTrago) {
        super(fm);
        this.context = context;
        this.itemTrago = itemTrago;
        bundle = new Bundle();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentoInformacion();
                break;
            case 1:
                fragment = new FragmentoEspecificacion();
                break;
            default:
                fragment = new Fragment();
        }
        try {
            bundle.putSerializable("ItemProducto", itemTrago);
            fragment.setArguments(bundle);
        }
        catch (Exception ex){};

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.informacion);
            case 1:
                return context.getResources().getString(R.string.especificaciones);
            default:
                return null;
        }
    }
}
