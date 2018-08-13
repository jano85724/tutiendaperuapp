package com.ma_developers.tutiendaperu.Entidad;

import java.io.Serializable;


public class ItemProducto implements Serializable{
    private int id;
    private String imgItem;
    private String nombre;

    public ItemProducto(int id, String imgItem, String nombre) {
        this.id = id;
        this.imgItem = imgItem;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getImgItem() {
        return imgItem;
    }

    public String getNombre() {
        return nombre;
    }
}
