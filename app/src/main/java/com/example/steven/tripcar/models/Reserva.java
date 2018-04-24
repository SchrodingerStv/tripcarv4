package com.example.steven.tripcar.models;

/**
 * Created by Steven on 23/04/2018.
 */

public class Reserva {


    private String fInicio;
    private String fFinal;
    private String usuario;
    private String coche;
    private String precioTotal;

    public Reserva(){

    }

    public Reserva( String fInicio, String fFinal, String usuario, String coche, String precioTotal) {

        this.fInicio = fInicio;
        this.fFinal = fFinal;
        this.usuario = usuario;
        this.coche = coche;
        this.precioTotal = precioTotal;
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
}
