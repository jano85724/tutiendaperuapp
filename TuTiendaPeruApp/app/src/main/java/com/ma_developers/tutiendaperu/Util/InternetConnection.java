package com.ma_developers.tutiendaperu.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Elvis on 02 ago 2017.
 */

public class InternetConnection {

    private static Context context;

    public InternetConnection(Context context) {
        this.context = context;
    }

    public boolean ConexionWifi(){
        boolean wifi = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo internetActivado = connectivity.getActiveNetworkInfo();

            if (internetActivado != null && internetActivado.isConnected()) {
                if (internetActivado.getType() == ConnectivityManager.TYPE_WIFI) {
                    wifi = true;
                    //Toast.makeText(context, "Tu conexion es por Wifi", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){}

        return wifi;
    }

    public boolean ConexionDatos(){
        boolean datos = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo internetActivado = connectivity.getActiveNetworkInfo();

            if (internetActivado != null && internetActivado.isConnected()) {
                if (internetActivado.getType() == ConnectivityManager.TYPE_MOBILE) {
                    datos = true;
                    //Toast.makeText(context, "Tu conexion es por Datos Moviles", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception ex){}

        return datos;
    }

}
