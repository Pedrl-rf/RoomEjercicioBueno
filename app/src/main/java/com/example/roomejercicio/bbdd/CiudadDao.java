package com.example.roomejercicio.bbdd;

import android.widget.CheckBox;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.roomejercicio.bbdd.Ciudad;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CiudadDao {
    //Insertar
    @Insert(onConflict = REPLACE)
    void insert(Ciudad ciudad);
    //Borrar uno
    @Delete
    void delete (Ciudad ciudad);
    //Borrar todos
    @Delete
    void reset(List<Ciudad> ciudad);

    @Query("UPDATE ciudad SET NombreCiudad = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    //Obtener todos los datos
    @Query("SELECT * FROM ciudad")
    LiveData<List<Ciudad>> getAll();
    //Borrar la tabla
    @Query("DELETE FROM ciudad")
    void deleteall();

    
}
