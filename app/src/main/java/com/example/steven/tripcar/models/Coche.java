package com.example.steven.tripcar.models;

public class Coche {


    private String MarcaModelo;

    private String Tamanio;

    private String PrecioHora;

    private String uriImagen;

    private String Matricula;

    public  Coche(){

    }

    public Coche(String marcaModelo, String tamanio, String precioHora, String uriImagen, String matricula) {
        MarcaModelo = marcaModelo;
        Tamanio = tamanio;
        PrecioHora = precioHora;
        this.uriImagen = uriImagen;
        Matricula = matricula;
    }

    public String getMarcaModelo() {
        return MarcaModelo;
    }

    public void setMarcaModelo(String marcaModelo) {
        MarcaModelo = marcaModelo;
    }

    public String getTamanio() {
        return Tamanio;
    }

    public void setTamanio(String tamanio) {
        Tamanio = tamanio;
    }

    public String getPrecioHora() {
        return PrecioHora;
    }

    public void setPrecioHora(String precioHora) {
        PrecioHora = precioHora;
    }

    public String getUriImagen() {
        return uriImagen;
    }

    public void setUriImagen(String uriImagen) {
        this.uriImagen = uriImagen;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String matricula) {
        Matricula = matricula;
    }
}