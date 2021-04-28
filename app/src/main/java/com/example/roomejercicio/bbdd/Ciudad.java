package com.example.roomejercicio.bbdd;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//definir la tabla Ciudad, que es el array con todos los datos
@Entity(tableName = "ciudad")
    /*
        Serializable, la cual no cuenta con ningún método, por lo que es una clase que sirve solamente para especificar que todo el estado de un objeto instanciado
    */
public class Ciudad implements Parcelable,Comparable<Ciudad> {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "NombreCiudad")
    private String nombreCiudad;
    @ColumnInfo(name = "NombrePais")
    private String nombrePais;
    @ColumnInfo(name = "latitud")
    private double latitud;
    @ColumnInfo(name = "longitud")
    private double longitud;
    @ColumnInfo (name = "Visitada")
    private boolean visited;
    @ColumnInfo(name="Imagen")
    private String imagen;



    public Ciudad(String nombreCiudad, String nombrePais, boolean visited, double latitud, double longitud, String imagen) {
        this.nombreCiudad = nombreCiudad;
        this.nombrePais = nombrePais;
        this.visited = visited;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
    }

    @Ignore
    protected Ciudad(Parcel in) {
        id = in.readInt();
        nombreCiudad = in.readString();
        nombrePais = in.readString();
        imagen = in.readString();
        latitud = in.readDouble();
        longitud = in.readDouble();
        visited = in.readByte() != 0;
    }

    public static final Creator<Ciudad> CREATOR = new Creator<Ciudad>() {
        @Override
        public Ciudad createFromParcel(Parcel in) {
            return new Ciudad(in);
        }

        @Override
        public Ciudad[] newArray(int size) {
            return new Ciudad[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombreCiudad);
        dest.writeString(imagen);
        dest.writeString(nombrePais);
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
        dest.writeByte((byte) (visited ? 1 : 0));
    }

    @Override
    public int compareTo(Ciudad o) {
        String o_nombreciudad = o.getNombreCiudad().toUpperCase();
        String nombreCiudad_this = this.nombreCiudad.toUpperCase();
        return nombreCiudad_this.compareTo(o_nombreciudad);
    }
}
