package com.example.nahim.appbus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    double[] coordenadas;
//    ArrayList<LatLng> coorList = new ArrayList<LatLng>();
    String[] ss;
    EscribirSD leerSD = new EscribirSD();
    /**
     * 1° Paso
     * Este activity se genero automaticamente, agregaremos un objeto Maker y dos variables de tipo double para guardar la Latitud y la longitu
     * para despues mostrar el marcador en el mapa. Que sera nuestra ubicacion actual
     */
    double lat = 0, lng = 0;
    public GoogleMap mMap;
    public Marker mMarcador;
    /**
     * 4° implementaremos un objeto de tipo LocationListener, el cual tiene la funcion de estar
     * siempre atento a cualquier cambio de ubicacion recibido por el GPS del dispositivo,
     * este metodo nos crea automaticamente una serie de metodos asociados a los distintos
     * eventos que podemos recibir de nuestro proveedor de localizacion
     */
    LocationListener locationListener = new LocationListener() {
        //Este es el metodo mas interesante, ya que se lanza cada vez que recibe una
        //actualizacion de ubicacion, dentro de este metodo llamaremos a nuestro metodo
        //actualizarUbicacion()
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //noinspection MissingPermission
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        miUbicacion();
        leerArchivo();
        moverSobreChihuahua();
    }

    private void moverSobreChihuahua() {
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(28.6678986, -106.0796748), 12);
        mMap.moveCamera(camUpd1);
    }
    private void MostrarLineas(ArrayList<LatLng> coorList) {

        PolylineOptions lineas = new PolylineOptions().addAll(coorList);
        lineas.width(10);
        lineas.color(Color.BLUE);
        mMap.addPolyline(lineas);



    }
    private void leerArchivo() {
        String nombre_ruta = getIntent().getStringExtra("NOMBRE_RUTA");
        ArrayList<LatLng> coordenadasLatLng = leerSD.leerArchivoSD(nombre_ruta+".txt",getApplicationContext());
       MostrarLineas(coordenadasLatLng);
    }

    /**
     * 2° Paso
     * Ahora creamos un metodo que nos servira para agregar un marcador en el mapa, crearemos un objeto LatLng,
     * en el cual incluiremos la latitud y la longitud, luego utilizaremos el elemento CameraUpdate para centrar
     * la camara a la posicion de nuestro marcador
     */

    public void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);

        //Si el marcador es diferente de Null se debera eliminar, despues agregamos algunas propiedades a nuestro Marker
        // como un titulo y una imagen,
        // por ultimo agregamos el metodo animateCamera para mover la camara desde una posicion a otra con animacion
        if (mMarcador != null)
            mMarcador.remove();
      // mMarcador = mMap.addMarker(new MarkerOptions()
        //      .position(coordenadas)
          //     .title("Mi ubicacion")
            //    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_room_red_600_24dp)));
        mMap.animateCamera(miUbicacion);
    }

    /**
     * 3° Creamos un metodo que nos servira para obtener la latitud y longitud de nuestra
     * ubicacion actual, utilizando metodos proporcionados por la clase Location, y la cual
     * utilizaremos como parametro de nuestro metodo.
     * <p>
     * Comprobamos si la ubicacion recibida es diferente a Null, antes de asignar el valor a
     * las variable, para asi evitar que la se vaya a crashear.
     *
     * @param location
     */
   public void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }


    /** 5° creamos un metodo, en el cual hacemos referencia a la clase LocationManager, la
     * cual es utilizada para obtener servicios de Geo-posicionamiento en el dispositivo.
     * Mediante el metodo getLastKnowLocation obtenemos la ultima posicion conocida, luego
     * llamamos a nuestro metodo actualizarUbicacion, mediante el metodo requestLocationUpdates
     * solicitamos al GPS actualizaciones de ubicacion cada 30 segundos
     */
    public void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000,0,locationListener);
    }
}
