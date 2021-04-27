package com.example.roomejercicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.roomejercicio.bbdd.Ciudad;
import com.example.roomejercicio.bbdd.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int RESULT_ACTIVITY = 5;
    //variables
    Button bt_añadir,bt_borrar,bt_todasciudades;
    FloatingActionButton fab_botonMapa;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //asignar variables
        bt_añadir = findViewById(R.id.bt_añadir);
        bt_borrar = findViewById(R.id.bt_borrar);
        fab_botonMapa = findViewById(R.id.fab_mapa);
        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(this);
//        ciudadList = (List<Ciudad>) database.ciudadDao().getAll();

        //LinearLayoutManager que es?
        //Es como se va a mostrar el recyclerView. en este caso de tipo lineal
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Iniciar Adaptador
        //¿por que no se pone aqui el arrayList, por que es más eficiente?
        adapter = new MainAdapter(MainActivity.this);

        //Utilizar Adaptador
        recyclerView.setAdapter(adapter);


        //Utilizar la database para obtener todas las ciudades y con el
        //bucle for recorrer todas las ciudades y obtener sus datos
        database.ciudadDao().getAll().observe(this, new Observer<List<Ciudad>>() {
            @Override
            public void onChanged(List<Ciudad> ciudadList) {
                Collections.sort(ciudadList,Ciudad::compareTo);
                ArrayList<Ciudad>ciudads = new ArrayList<>();
                ciudads.addAll(ciudadList);
                adapter.updateList(ciudads);
            }
        });

        bt_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, AddCiudad.class);
                startActivity(i);
            }
        });

        bt_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Attempt to get length of null array
                //Como si no existiera el array
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RoomDB.getInstance(MainActivity.this).ciudadDao().deleteall();
                    }
                }).start();
            }
        });
    }


}