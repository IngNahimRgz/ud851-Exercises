package com.example.nahim.appbus;

/**
 * Created by Nahim on 16/05/2017.
 */

public class Header {
    private String nombre_ruta;
    private String tiempo_recorrido;
    private String frecuencia_paso;
    private String longitud_ruta;

    public Header(String nombre_ruta, String tiempo_recorrido, String frecuencia_paso, String longitud_ruta) {
        this.nombre_ruta = nombre_ruta;
        this.tiempo_recorrido = tiempo_recorrido;
        this.frecuencia_paso = frecuencia_paso;
        this.longitud_ruta = longitud_ruta;
    }

    public String getNombre_ruta() {
        return nombre_ruta;
    }

    public void setNombre_ruta(String nombre_ruta) {
        this.nombre_ruta = nombre_ruta;
    }

    public String getTiempo_recorrido() {
        return tiempo_recorrido;
    }

    public void setTiempo_recorrido(String tiempo_recorrido) {
        this.tiempo_recorrido = tiempo_recorrido;
    }

    public String getFrecuencia_paso() {
        return frecuencia_paso;
    }

    public void setFrecuencia_paso(String frecuencia_paso) {
        this.frecuencia_paso = frecuencia_paso;
    }

    public String getLongitud_ruta() {
        return longitud_ruta;
    }

    public void setLongitud_ruta(String longitud_ruta) {
        this.longitud_ruta = longitud_ruta;
    }
}
