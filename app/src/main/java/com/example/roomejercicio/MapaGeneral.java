package com.example.roomejercicio;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.roomejercicio.bbdd.Ciudad;
import com.example.roomejercicio.bbdd.RoomDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapaGeneral extends AppCompatActivity implements OnMapReadyCallback {
    private Marker Mciudad;
    RoomDB database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapageneral);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.todasciudadesmap);
        fragment.getMapAsync(this);
        database = RoomDB.getInstance(this);


        database.ciudadDao().getAll().observe(this, new Observer<List<Ciudad>>() {
            @Override
            public void onChanged(List<Ciudad> ciudadList) {

            }
        });

    }
    @Override
    public void onMapReady(GoogleMap map) {



        map.setTrafficEnabled(true); //trafico activado

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); //controles de zoom
        uiSettings.setCompassEnabled(true); //mostrar la brújula
        uiSettings.setZoomGesturesEnabled(true); //gestos de zoom
        uiSettings.setScrollGesturesEnabled(true); //Gestos de scroll
        uiSettings.setTiltGesturesEnabled(true); //Gestos de ángulo
        uiSettings.setRotateGesturesEnabled(true); //Gestos de rotación

        MarkerOptions ciudad = new MarkerOptions()
                .position(new LatLng(22.55,22.55))
                .title("Estas aquí")
                .snippet("Pulsa aquí para acceder")
                .flat(true)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        Mciudad = map.addMarker(ciudad);
        Mciudad.setTag("Sitios del mundo");

    }
}