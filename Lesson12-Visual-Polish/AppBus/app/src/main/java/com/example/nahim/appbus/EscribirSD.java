package com.example.nahim.appbus;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Nahim on 19/05/2017.
 */

public class EscribirSD {
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;
    double[] coordenadas;

    String[] ss;


    public EscribirSD() {

    }

    public void guardarCoordenadas(String nombre_ruta, String coordenadas, Context context) {
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
        //Si la memoria externa esta disponible y se puede escribir
        if (sdAccesoEscritura && sdDisponible) {
            try {
                // OutputStreamWriter fout = new OutputStreamWriter(context.openFileOutput(nombre_ruta + ".txt", Context.MODE_APPEND));
                File ruta_sd_global = new File(Environment.getExternalStorageDirectory() + "/Rutas");
                boolean existeCarpeta = true;
                if (!ruta_sd_global.exists()) {
                    existeCarpeta = ruta_sd_global.mkdir();
                }
                if (existeCarpeta) {
                    File f = new File(ruta_sd_global.getAbsolutePath(), nombre_ruta);
                    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f, true));
                    fout.append("" + coordenadas + ",\n");
                    fout.close();
                    Log.e("---------------------", "Se escribio correctamente:" + coordenadas + ruta_sd_global);
                }
            } catch (Exception ex) {
                Log.e("Archivos", "Error al escribir en el archivo");
            }
        }
    }

    public ArrayList<LatLng> leerArchivoSD(String nombre_ruta, Context context) {
        ArrayList<LatLng> coorList = new ArrayList<LatLng>();

        try {
            File ruta_sd_global = new File(Environment.getExternalStorageDirectory() + "/Rutas");
            boolean existeCarpeta = true;
            if (!ruta_sd_global.exists()) {
                existeCarpeta = ruta_sd_global.mkdir();
            }
            if (existeCarpeta) {
                File e = new File(ruta_sd_global.getAbsolutePath(), nombre_ruta);
                OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(e, true));
                fout.append("");
                fout.close();

                File f = new File(ruta_sd_global.getAbsolutePath(), nombre_ruta);

                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                String[] stringCoordenadas = new String[0];
                String readLine = "";
                while (fin.readLine() != null) {
                    readLine += fin.readLine();

                }
                stringCoordenadas = readLine.split(",");
                double[] coordenadasDouble = new double[stringCoordenadas.length];
                for (int valor=0; valor < (stringCoordenadas.length -1 ); valor += 2){
                    coordenadasDouble[valor] = Double.parseDouble(stringCoordenadas[valor]);
                    coordenadasDouble[valor+1] = Double.parseDouble(stringCoordenadas[valor+1]);
                    coorList.add(new LatLng(coordenadasDouble[valor],coordenadasDouble[valor+1]));
                }

            }
            return coorList;

        } catch (IOException e) {
            Log.e("error", "no existe el archivo");
            e.printStackTrace();
        }
        return null;
    }
}
