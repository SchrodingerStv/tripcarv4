package com.example.steven.tripcar.models;

/**
 * Created by Steven on 24/04/2018.
 */

public class usuarioCocheReserva {
    private String fInicio;
    private String fFinal;
    private String usuario;
    private String coche;
    private String precioTotal;
    private String idReserva;
    private String fFinalizacion;

    private String MarcaModelo;
    private String Tamanio;
    private String PrecioHora;
    private String uriImagen;
    private String Matricula;

    public usuarioCocheReserva(){

    }



    public usuarioCocheReserva(String fInicio, String fFinal, String usuario, String coche, String precioTotal, String marcaModelo, String tamanio, String precioHora, String uriImagen, String matricula, String idReserva,String fFinalizacion) {

        this.fInicio = fInicio;
        this.fFinal = fFinal;
        this.usuario = usuario;
        this.coche = coche;
        this.precioTotal = precioTotal;
        this.fFinalizacion = fFinalizacion;

        this.MarcaModelo = marcaModelo;
        this.Tamanio = tamanio;
        this.PrecioHora = precioHora;
        this.uriImagen = uriImagen;
        this.Matricula = matricula;
        this.idReserva = idReserva;
    }
    public String getfFinalizacion() {
        return fFinalizacion;
    }

    public void setfFinalizacion(String fFinalizacion) {
        this.fFinalizacion = fFinalizacion;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getfInicio() {
        return fInicio;
    }

    public void setfInicio(String fInicio) {
        this.fInicio = fInicio;
    }

    public String getfFinal() {
        return fFinal;
    }

    public void setfFinal(String fFinal) {
        this.fFinal = fFinal;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCoche() {
        return coche;
    }

    public void setCoche(String coche) {
        this.coche = coche;
    }

    public String getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(String precioTotal) {
        this.precioTotal = precioTotal;
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
