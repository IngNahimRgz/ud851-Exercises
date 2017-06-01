package com.example.nahim.appbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nahim.appbus.servicios.GPS_Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.Manifest;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class EditarRuta extends AppCompatActivity {
    FloatingActionButton fabStart;
    FloatingActionButton fabStop;
    CircleProgressView circleProgressView;
    Chronometer chronometer;
    TextView tv_estado_servicio, tv_coordenadas;
    EscribirSD escribirSD = new EscribirSD();
    BroadcastReceiver broadcastReceiver;
    long time = 0;

    StopWatch s = new StopWatch();
    Bundle bundle = new Bundle();

    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    String nombre_ruta = getIntent().getStringExtra("NOMBRE_RUTA");
                   tv_coordenadas.append("\n"+ intent.getExtras().get("COORDENADAS"));
                    String s_coordenadas = (String) intent.getExtras().get("COORDENADAS");
                    escribirSD.guardarCoordenadas(nombre_ruta+".txt", s_coordenadas, getApplicationContext());
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ruta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO Cambiar el titulo segun se haya seleccionado la ruta a modificar
        String nombre_ruta = getIntent().getStringExtra("NOMBRE_RUTA");

        //    if (!runtime_permissions())
      //      enable_buttons();

        circleProgressView = (CircleProgressView) findViewById(R.id.circleProgressView);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setVisibility(View.INVISIBLE);

        circleProgressView.setTextMode(TextMode.VALUE);

        //Vinculacion de los elementos

        tv_coordenadas = (TextView) findViewById(R.id.tv_coordenadas);
        fabStop = (FloatingActionButton) findViewById(R.id.fabStop);
        tv_estado_servicio = (TextView) findViewById(R.id.tv_estado);
        fabStart = (FloatingActionButton) findViewById(R.id.fabStart);

        fabStop.setVisibility(View.INVISIBLE);
        fabStart.setVisibility(View.VISIBLE);


        bundle.putString("NOMBRE_RUTA", nombre_ruta);

        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabStart.setVisibility(View.INVISIBLE);
                fabStop.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime() + time);
                tv_estado_servicio.setText("Ejecutando Servicio");
                chronometer.start();
                s.startCount();
                Intent intent = new Intent(getApplicationContext(), GPS_Service.class);
                intent.putExtras(bundle);
                startService(intent);
            }
        });

        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabStart.setVisibility(View.VISIBLE);
                fabStop.setVisibility(View.INVISIBLE);
                chronometer.stop();
                s.stopCount();
                tv_estado_servicio.setText("Servicio Detenido");
                Intent intent = new Intent(getApplicationContext(), GPS_Service.class);
                stopService(intent);

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
               // enable_buttons();
            } else {
                runtime_permissions();
            }
        }
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    class StopWatch {
        private Timer timer;
        private int progressValue = -1;

        public void startCount() {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    progressValue++;
                    circleProgressView.setValue(progressValue);
                    if (progressValue == 59)
                        progressValue = 0;
                }
            }, 0, 1000); // 1 segundo
        }

        public void stopCount() {
            progressValue = 16;
            timer.cancel();
            timer = null;
        }
    }

}
