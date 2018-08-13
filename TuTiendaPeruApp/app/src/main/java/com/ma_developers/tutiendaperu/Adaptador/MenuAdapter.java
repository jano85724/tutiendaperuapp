package com.ma_developers.tutiendaperu.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ma_developers.tutiendaperu.Activity.ListaProductos;
import com.ma_developers.tutiendaperu.Entidad.ItemProducto;
import com.ma_developers.tutiendaperu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ELVIS on 24/10/2017.
 */

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemProducto> opcionesMenu;

    public MenuAdapter(Context context, ArrayList<ItemProducto> opcionesMenu) {
        this.context = context;
        this.opcionesMenu = opcionesMenu;
    }

    @Override
    public int getCount() {
        return opcionesMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return opcionesMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemProducto item = (ItemProducto) getItem(position);

        //if(view == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, null);

        CircleImageView img = (CircleImageView) convertView.findViewById(R.id.imgLogo);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);

        Picasso.with(context).load(item.getImgItem())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(img);

        //img.setImageResource(item.getImgItem());
        tvNombre.setText(item.getNombre().toString());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListaProductos.class);
                intent.putExtra("categoria", item);

                context.startActivity(intent);

                ((Activity) context).overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
            }
        });

        return convertView;
    }
}
