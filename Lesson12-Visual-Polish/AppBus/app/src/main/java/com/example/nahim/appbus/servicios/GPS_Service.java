package com.example.nahim.appbus.servicios;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nahim.appbus.EscribirSD;

public class GPS_Service extends Service {

    private LocationListener locationListener;
    private LocationManager locationManager;


    @Override
    public void onCreate() {
        Toast.makeText(this,"Servicio corriendo",Toast.LENGTH_SHORT).show();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("COORDENADAS",location.getLatitude()+","+location.getLongitude());
                sendBroadcast(intent);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,20,locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Servicio detenido",Toast.LENGTH_SHORT).show();
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
