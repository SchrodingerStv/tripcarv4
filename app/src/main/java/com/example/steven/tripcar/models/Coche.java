package com.example.steven.tripcar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coche {

    @SerializedName("idCoche")
    @Expose
    private Integer idCoche;
    @SerializedName("MarcaModelo")
    @Expose
    private String marcaModelo;
    @SerializedName("Tamanio")
    @Expose
    private String tamanio;
    @SerializedName("PrecioDia")
    @Expose
    private Double precioDia;
    @SerializedName("Imagen")
    @Expose
    private String imagen;
    @SerializedName("Matricula")
    @Expose
    private String matricula;

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public String getMarcaModelo() {
        return marcaModelo;
    }

    public void setMarcaModelo(String marcaModelo) {
        this.marcaModelo = marcaModelo;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public Double getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(Double precioDia) {
        this.precioDia = precioDia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

}