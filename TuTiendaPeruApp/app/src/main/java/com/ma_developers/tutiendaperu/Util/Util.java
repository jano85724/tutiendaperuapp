package com.ma_developers.tutiendaperu.Util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class Util {
    public static void OcultarTeclado(Context context){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception ex){}
    }
}
