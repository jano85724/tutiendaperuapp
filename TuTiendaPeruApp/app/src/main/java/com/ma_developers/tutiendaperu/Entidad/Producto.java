package com.ma_developers.tutiendaperu.Entidad;

import java.io.Serializable;

public class Producto implements Serializable {
    private int id;
    private int idCategoria;
    private String imgFoto;
    private String nombre;
    private String informacion;
    private String especificaciones;

    public Producto(int id, int idCategoria, String imgFoto, String nombre, String informacion, String especificaciones) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.imgFoto = imgFoto;
        this.nombre = nombre;
        this.informacion = informacion;
        this.especificaciones = especificaciones;
    }

    public int getId() {
        return id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getImgFoto() {
        return imgFoto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getInformacion() {
        return informacion;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }
}
