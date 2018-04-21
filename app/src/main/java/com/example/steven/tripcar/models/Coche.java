package com.example.steven.tripcar.models;

public class Coche {


    private String marcaModelo;

    private String tamanio;

    private Double precioDia;

    private String uriImagen;

    private String matricula;

    public Coche(){

    }
    public Coche(String marcaModelo, String tamanio, Double precioDia, String uriImagen, String matricula) {
        this.marcaModelo = marcaModelo;
        this.tamanio = tamanio;
        this.precioDia = precioDia;
        this.uriImagen = uriImagen;
        this.matricula = matricula;
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

    public String getUriImagen() {
        return uriImagen;
    }

    public void setUriImagen(String uriImagen) {
        this.uriImagen = uriImagen;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}