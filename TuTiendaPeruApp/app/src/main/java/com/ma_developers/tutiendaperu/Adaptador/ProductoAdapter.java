package com.ma_developers.tutiendaperu.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ma_developers.tutiendaperu.Activity.ProductoDetalle;
import com.ma_developers.tutiendaperu.Entidad.Producto;
import com.ma_developers.tutiendaperu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class ProductoAdapter extends RecyclerView.Adapter {
    private Context contex;
    private ArrayList<Producto> listaProductos;
    private ArrayList<Producto> listaProductosSec, arrayListaProductos;
    private String categoria;

    public ProductoAdapter(Context contex, ArrayList<Producto> listaProductos, String categoria) {
        this.contex = contex;
        this.listaProductos = listaProductos;
        this.categoria = categoria;

        arrayListaProductos = new ArrayList<>();
        arrayListaProductos.addAll(listaProductos);

        listaProductosSec = new ArrayList<>();
        listaProductosSec.addAll(listaProductos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(contex).inflate(R.layout.item_producto, null);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ProductoViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Producto item = listaProductos.get(position);

        ProductoViewHolder tragoViewHolder = (ProductoViewHolder) holder;

        Picasso.with(contex).load(item.getImgFoto())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(tragoViewHolder.imgFoto);

        tragoViewHolder.tvTitulo.setText(item.getNombre().toString());

        tragoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contex, ProductoDetalle.class);
                intent.putExtra("categoria", categoria);
                intent.putExtra("ItemProducto", item);
                contex.startActivity(intent);

                ((Activity) contex).overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }


    public class ProductoViewHolder extends RecyclerView.ViewHolder{
        ImageView imgFoto;
        TextView tvTitulo;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            imgFoto = (ImageView) itemView.findViewById(R.id.imgProducto);
            tvTitulo = (TextView) itemView.findViewById(R.id.tvNombre);
        }
    }


    public void filter(String charText) {
        try {
            charText = charText.toLowerCase(Locale.getDefault());

            listaProductos.clear();
            if (charText.length() == 0) {
                //setCategoriaVisible(false);
                listaProductos.addAll(listaProductosSec);
            } else {
                for (Producto canal : arrayListaProductos) {
                    if (charText.length() != 0 && canal.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                        listaProductos.add(canal);
                    }
                }
            }
            notifyDataSetChanged();
        }
        catch (Exception ex){}
    }

}
