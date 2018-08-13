package com.ma_developers.tutiendaperu.Fragmento;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma_developers.tutiendaperu.Entidad.Producto;
import com.ma_developers.tutiendaperu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoEspecificacion extends Fragment {

    //private JustifyTextView tvPreparacion;
    private TextView tvEspecificacion;

    public FragmentoEspecificacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragmento_especificaciones, container, false);

        tvEspecificacion = (TextView) convertView.findViewById(R.id.tvEspecificacion);
        tvEspecificacion.setText(((Producto)getArguments().getSerializable("ItemProducto")).getEspecificaciones().toString());
        return convertView;
    }
}
