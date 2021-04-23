package com.example.roomejercicio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.roomejercicio.bbdd.Ciudad;
import com.example.roomejercicio.bbdd.RoomDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapaGeneral extends AppCompatActivity implements OnMapReadyCallback {
    private Marker Mciudad;
    RoomDB database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapageneral);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.general_map);
        fragment.getMapAsync(this);
/*        database = RoomDB.getInstance(this);

        database.ciudadDao().getAll().observe(this, new Observer<List<Ciudad>>() {
            @Override
            public void onChanged(List<Ciudad> ciudadList) {

            }


        });*/

    }
    @Override
    public void onMapReady(GoogleMap map) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);

        ArrayList<Ciudad>todasCiudades = getIntent().getParcelableArrayListExtra("ciudad");
        for(int i = 0; i < todasCiudades.size();i++){
            MarkerOptions todasLasCiudades = new MarkerOptions()
                    .position(new LatLng(todasCiudades.get(i).getLatitud(),todasCiudades.get(i).getLongitud()))
                    .title(todasCiudades.get(i).getNombreCiudad())
                    .snippet(String.valueOf(todasCiudades.get(i).getLatitud())+" , "+String.valueOf(todasCiudades.get(i).getLongitud()))
                    .flat(true)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            Mciudad = map.addMarker(todasLasCiudades);
        }



        map.setTrafficEnabled(true); //trafico activado

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); //controles de zoom
        uiSettings.setCompassEnabled(true); //mostrar la brújula
        uiSettings.setZoomGesturesEnabled(true); //gestos de zoom
        uiSettings.setScrollGesturesEnabled(true); //Gestos de scroll
        uiSettings.setTiltGesturesEnabled(true); //Gestos de ángulo
        uiSettings.setRotateGesturesEnabled(true); //Gestos de rotación



    }
}
