package com.example.nahim.appbus;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.SearchManager;

import android.widget.SearchView.OnQueryTextListener;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.jar.Manifest;


public class MainActivity extends AppCompatActivity {

    private FrameLayout mContent;
    private RecyclerView mRecyclerView;
    private HeaderAdapter headerAdapter;
    private static final int SOLICITUD_PERMISO_FINE_LOCATION = 3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mContent.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    mContent.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    mContent.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }

    };

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SOLICITUD_PERMISO_FINE_LOCATION){

            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permisos Concedidos",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Permiso denegado",Toast.LENGTH_SHORT).show();
            }
        }
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Solictamos permisos para acceder a la ubicacion, si el permiso esta denegado se lo
         * solicitamos al usuario
         */
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SOLICITUD_PERMISO_FINE_LOCATION);
        }

        mContent = (FrameLayout) findViewById(R.id.content);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        ArrayList<Header> myDataset = new ArrayList<>();
        myDataset.add(new Header("Tec II - Sube Industrial",                 "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("Tec II - Sube  Colon",                     "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("Tarahumara Directo",                       "1 hora 48 min",    "12 min", "35.5 Kms"));
        myDataset.add(new Header("Tarahumara Inverso",                       "2 horas",          "15 min", "38.1 Kms"));
        myDataset.add(new Header("Infonavit - Diego Lucero",                 "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("Ruta 100 - Campo Bello directo",           "2 horas",          "15 min", "38.1 Kms"));
        myDataset.add(new Header("Granjas - Por Colon",                      "2 horas",          "15 min", "38.1 Kms"));
        myDataset.add(new Header("Granjas - Saucito",                        "3 horas",          "15 min", "38.1 Kms"));
        myDataset.add(new Header("Panamericana - Baja Mirador",              "2 horas",          "12 min", "38.1 Kms"));
        myDataset.add(new Header("Panamericana - San Felipe",                "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("Centro Norte - Sube Industrias-Aceros",    "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("20 Aniversario Directo",                   "2 horas",          "12 min", "35.5 Kms"));
        myDataset.add(new Header("Nombre de Dios",                           "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("Riberas de Sacramento",                    "2 horas",          "15 min", "38.5 Kms"));
        myDataset.add(new Header("Plan de Ayala",                            "1 hora 48 min",    "12 min", "35.5 Kms"));


        headerAdapter = new HeaderAdapter(myDataset);
        mRecyclerView.setAdapter(headerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menuExit){
            finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem search = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(headerAdapter != null)
                    headerAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
