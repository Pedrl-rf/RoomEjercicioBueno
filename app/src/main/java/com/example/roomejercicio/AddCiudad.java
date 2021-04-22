package com.example.roomejercicio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.material.textfield.TextInputEditText;

public class AddCiudad extends AppCompatActivity implements OnMapReadyCallback {

    private TextInputEditText tiet_nombreciudad, tiet_nombrepais, tiet_latitud, tiet_longitud;
    private CheckBox ch_visited;
    private Button bt_enviarciudad;
    private double longitude;
    private double latitude;
    private Marker marker4;

    @Override
    public void onMapReady(GoogleMap map) {
        /* TIPO DE MAPA */
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        GoogleMap.MAP_TYPE_NONE
//        GoogleMap.MAP_TYPE_NORMAL
//        GoogleMap.MAP_TYPE_SATELLITE
//        GoogleMap.MAP_TYPE_TERRAIN


        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setTrafficEnabled(true); //trafico activado

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); //controles de zoom
        uiSettings.setCompassEnabled(true); //mostrar la brújula
        uiSettings.setZoomGesturesEnabled(true); //gestos de zoom
        uiSettings.setScrollGesturesEnabled(true); //Gestos de scroll
        uiSettings.setTiltGesturesEnabled(true); //Gestos de ángulo
        uiSettings.setRotateGesturesEnabled(true); //Gestos de rotación
        MarkerOptions options4 = new MarkerOptions()
            .position(new LatLng(36.2048, 138.2529))
            .title("Estas aquí")
            .snippet("Pulsa aquí para acceder")
            .flat(true)
            .draggable(true)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        marker4 = map.addMarker(options4);
        marker4.setTag("mimarker");

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (marker.equals(marker4)) {
                    LatLng latLng1 = marker.getPosition();
                     latitude = latLng1.latitude;
                     longitude = latLng1.longitude;
                }
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                marker4.remove();

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title("Estas aqui")
                        .snippet("Pulsa aquí para acceder")
                        .flat(true)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                longitude = latLng.longitude;
                latitude = latLng.latitude;

                Toast.makeText(AddCiudad.this, "Latitud: " + latitude + ", Longitud: " + longitude, Toast.LENGTH_SHORT).show();
                String latitud = String.valueOf(latitude);
                tiet_latitud.setText(latitud);
                String longitud = String.valueOf(longitude);
                tiet_longitud.setText(longitud);
                marker4 = map.addMarker(options);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addciudad);
        tiet_nombreciudad = findViewById(R.id.tiet_NombreCiudad);
        tiet_nombrepais = findViewById(R.id.tiet_NombrePais);
        tiet_latitud = findViewById(R.id.tiet_latitud);
        tiet_longitud = findViewById(R.id.tiet_longitud);
        ch_visited = findViewById(R.id.visited);
        bt_enviarciudad = findViewById(R.id.bt_enviarciudad);

        //Metodo que implementa OnMapReadyCallback => SupportMapFragment
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.todasciudadesmap);
        fragment.getMapAsync(this);

        bt_enviarciudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCiudad = tiet_nombreciudad.getText().toString();
                String nombrePais = tiet_nombrepais.getText().toString();
                boolean visitado = ch_visited.isChecked();

                Ciudad ciudad = new Ciudad(nombreCiudad,nombrePais,visitado,latitude,longitude);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RoomDB.getInstance(AddCiudad.this).ciudadDao().insert(ciudad);
                    }
                }).start();
                finish();
            }
        });
    }
}
