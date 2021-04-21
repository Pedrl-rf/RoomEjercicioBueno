package com.example.roomejercicio;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomejercicio.bbdd.Ciudad;
import com.example.roomejercicio.bbdd.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

//No se donde se declara holder.lo que sea

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ciudad> {
    //Variables del adaptador
    private ArrayList<Ciudad>ciudadList;
    private Activity context;
    private RoomDB database;

    public MainAdapter(Activity context ){
        //        //llama a la base de datos UNA UNICA VEZ
        database = RoomDB.getInstance(context);
        this.context = context;
        this.ciudadList = new ArrayList<>();
        //notifyDataSetChanged(); se usa cada vez que se actualiza la base de datos

    }

    @NonNull

    //dar Layout a cada elemento del array de ciudades

    @Override
    public ciudad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tajeta,parent,false);
        return new ciudad(view);
    }
    //dar los datos al viewHolder
    @Override
    public void onBindViewHolder(@NonNull ciudad holder, int position) {
        //No se lo que hace
        Ciudad ciudad = ciudadList.get(position);
        //Iniciar base de datos
        //database = RoomDB.getInstance(context);
        //Valor a los textView
        holder.tv_nombreCiudad.setText(ciudad.getNombreCiudad());
        holder.tv_nombrePais.setText(ciudad.getNombrePais());
        holder.tv_latitud.setText(String.valueOf(ciudad.getLatitud()));
        holder.tv_longitud.setText(String.valueOf(ciudad.getLongitud()));



        //¿Parse a string para SetText?
        //holder.tv_latitud.setText(ciudad.getLatitud());
        //holder.tv_longitud.setText(ciudad.getLongitud());
        //¿holder que es en realidad?

        holder.img_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ciudad c = ciudadList.get(holder.getAdapterPosition());
                //obtengo id de la base de datos
                int sID = c.getId();

                String sText = c.getNombreCiudad();
                //Crear Diálogo
                Dialog dialog = new Dialog(context);
                // Asignar view
                dialog.setContentView(R.layout.dialog_editar);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width,height);
                dialog.show();
                //Asignar variables del dialog

                TextInputEditText tiet_nombre = dialog.findViewById(R.id.tiet_nombreCiudad);
                Button bt_actualizar = dialog.findViewById(R.id.bt_actualizar);


                //Button bt_borrarCiudad = dialog.findViewById(R.id.img_borrar);

                tiet_nombre.setText(sText);

                bt_actualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //Actualizar el texto para la base de datos
                        //.trim sirve para quitar los espacios en blanco
                        String uText = tiet_nombre.getText().toString();
                        database.ciudadDao().update(sID,uText);
                        /*ciudadList.clear();
                        ciudadList.addAll(database.ciudadDao().getAll());
                        notifyDataSetChanged();*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RoomDB.getInstance(context).ciudadDao().update(sID,uText);
                            }
                        }).start();
                    }
                });
            }
        });

        holder.img_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RoomDB.getInstance(context).ciudadDao().delete(ciudad);
                    }
                }).start();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.fab_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapaGeneral.class);
                intent.putExtra("ciudad",ciudadList);
                context.startActivity(intent);
            }
        });

        //comprueba que este checked visitado y bloquea la pulsacion
        holder.ch_visitada.setChecked(ciudad.isVisited());
        holder.ch_visitada.setEnabled(false);


        if(holder.tv_longitud.getText().toString().isEmpty()){
            holder.tv_longitud.setVisibility(View.GONE);
        }
        if(holder.tv_latitud.getText().toString().isEmpty()){
            holder.tv_latitud.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ciudadList.size();
    }

    public class ciudad extends RecyclerView.ViewHolder {

        //Variables de donde se encuentra la tarjeta, es decir las variables del lugar del adaptador

        TextView tv_nombreCiudad,tv_nombrePais,tv_longitud,tv_latitud;
        ImageView img_ciudad,img_editar, img_borrar;
        CheckBox ch_visitada;
        FloatingActionButton fab_mapa;

        //cambiar nombre viewHolder

        public ciudad(@NonNull View itemView) {
            super(itemView);
            tv_nombrePais = itemView.findViewById(R.id.tv_nombre);
            tv_nombreCiudad = itemView.findViewById(R.id.tv_pais);
            tv_latitud = itemView.findViewById(R.id.tv_latitud);
            tv_longitud = itemView.findViewById(R.id.tv_longitud);

            img_editar = itemView.findViewById(R.id.img_editar);
            img_borrar = itemView.findViewById(R.id.img_borrar);
            img_ciudad = itemView.findViewById(R.id.img_ciudad);
            ch_visitada = itemView.findViewById(R.id.ch_visitada);
            fab_mapa = itemView.findViewById(R.id.fab_mapa);
        }
    }
    public void updateList(ArrayList<Ciudad> lista) {
        ciudadList = lista;
        notifyDataSetChanged();
    }
}
