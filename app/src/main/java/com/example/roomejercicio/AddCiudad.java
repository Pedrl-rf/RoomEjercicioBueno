package com.example.roomejercicio;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddCiudad extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_GALLERY = 10;
    private static final int REQUEST_CAMERA = 11;
    private static final int REQUEST_PERMISSION_CAMERA = 5;
    private static final int REQUEST_ALL_CODE = 111;
    private static final String FOLDER_NAME = "RoomDBImages";


    private TextInputEditText tiet_nombreciudad, tiet_nombrepais, tiet_latitud, tiet_longitud;
    private TextView tv_camara, tv_galeria;
    private CheckBox ch_visited;
    private Button bt_enviarciudad;
    private double longitude;
    private double latitude;
    private Marker marker4;


    private ImageView imageView;
    private String mImageString;
    private Uri mImageUri;

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
        tv_camara = findViewById(R.id.tv_camara);
        tv_galeria = findViewById(R.id.tv_galeria);
        imageView = findViewById(R.id.imageView);


        // si se ha almacenado algo en el Bundle de estado, lo recuperamos
        if (savedInstanceState != null && savedInstanceState.containsKey("foto")) {
            mImageString = savedInstanceState.getString("foto");
            mImageUri = Uri.parse(mImageString);
            imageView.setImageURI(mImageUri);
        }

        checkPermissions();


        //Metodo que implementa OnMapReadyCallback => SupportMapFragment
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dialog_map);
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

        tv_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        tv_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromGallery();
            }
        });


    }

    private void checkPermissions() {
        // si hay permisos, se inicia la cámara, si no se han concedido permisos, se piden al usuario

        //Permisos Almacenamiento
        int ubicacion = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED))
        {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                String[] arrayPermisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};
                requestPermissions(arrayPermisos,REQUEST_ALL_CODE);
                fromCamera();
            }
        }

        else {
            String[] arrayPermisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, arrayPermisos, REQUEST_PERMISSION_CAMERA);
        }
    }

    private void fromGallery() {
        Intent contentSelector = new Intent(Intent.ACTION_OPEN_DOCUMENT); // permite al usuario seleccionar cualquier tipo de dato del dispositivo
        contentSelector.addCategory(Intent.CATEGORY_OPENABLE); // se indica que los resultados deben venir en formato Uri
        contentSelector.setType("image/*"); // se indica el tipo de datos y el formato (imagenes en cualquier formato)
        startActivityForResult(contentSelector, REQUEST_GALLERY); //inicia el intent esperando un resultado, que vendrá en onActivityResult
    }

    public File getPhotoFileUri(String fileName) {
        // path será la ruta donde se va a crear el archivo. Con el método getExternalStorage... se accede al almacenamiento público
        // la ruta final será: almacenamiento internt/Pictures/SampleGetImages
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + FOLDER_NAME;
        File mediaStorageDir = new File(path); //crea un archivo en el directorio de la ruta anterior

        // crea el directorio si no existe
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        // Devuelve el archivo donde se almacenará la imagen
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void fromCamera() {

        // intent para tomar fotografías y devolver la imagen a la app
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // crea el archivo donde se almacenará la imagen de la cámara
        String nombreArchivo = createFileName();
        File photoFile = getPhotoFileUri(nombreArchivo);

        // Si la versión de la App es superior a la 29 (Android X)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // clase que permite agrupar un conjunto de valores para que puedan ser procesados
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo); //nombre del archivo
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png"); // formato
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/" + FOLDER_NAME); // ruta donde se almacenará la imagen

            // se inserta la información en el almacenamiento externo. getContentResolver devuelve la uri con la ruta tipo "content" a la imagen
            mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            // para el almacenamiento interno, sería  mImageUri = getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        } else {
            // Si la versión de la App es superior a la 24 (Nougat)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // insertar el archivo en el provider (proveedor de contenido)
                mImageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
            } else {
                mImageUri = Uri.fromFile(photoFile);
            }
        }
        // se pasa el uri con la info donde se almacenará la imagen, al intent,
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        // si hay apps en el dispositivo que puedan hacer fotos
//        if (intent.resolveActivity(getPackageManager()) != null) {
        // inicia el intent para tomar la foto
        startActivityForResult(intent, REQUEST_CAMERA);
//        }
    }

    private String createFileName() {
        String formato = "yyyyMMdd_HHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());

        Date ahora = new Date();
        String timeStamp = sdf.format(ahora);
        return "IMG_" + timeStamp + ".png";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                // si los datos no son nulos
                if (data != null && data.getDataString() != null) {
                    // se obtiene la ruta de la imagen como String y despues la uri
                    mImageString = data.getDataString();
                    Uri uri2 = Uri.parse(mImageString);
                    // o se puede obtener directamente la uri
                    Uri uri = data.getData();

                    // se pone la imagen en el imageview a traves de la uri
                    imageView.setImageURI(uri2);

                    // alternativa usando la librería picasso
                    // Picasso.get().load(dataString).into(imageView);
                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
//                Picasso.get().load(miImagen).into(imageView);

                mImageString = mImageUri.toString();
                // ya teniamos la uri donde se iba a almacenar la imagen de la cámara
                imageView.setImageURI(mImageUri);
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fromCamera();
            } else {
                Toast.makeText(this, "No puede utilizar esta funcionalidad sin aceptar los TODOS los permisos", Toast.LENGTH_SHORT).show();
            }
        }

    }*/

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // si ya se ha almacenado la ruta como String en la variable, la almacenamos en el bundle
        if (mImageString != null && !mImageString.isEmpty()) {
            outState.putString("foto", mImageString);
        }
    }
}


