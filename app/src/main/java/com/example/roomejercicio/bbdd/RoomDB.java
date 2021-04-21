package com.example.roomejercicio.bbdd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Se añaden las entidades a la base de datos
@Database(version = 1 , entities = {Ciudad.class})
public abstract class RoomDB extends RoomDatabase {
    //Instanciamos la base de datos
    private static RoomDB database;
    //Define la base de datos
    private  static String DATABASE_NAME = "baseDatosCiudad";

    public synchronized static RoomDB getInstance(Context context){
        //comprueba la base de datos
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        //devuelve la base de datos
        return database;
    }

    //Se crea Dao
    public abstract CiudadDao ciudadDao();

}
