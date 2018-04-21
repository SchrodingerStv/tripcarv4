package com.example.steven.tripcar.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {


     String email;

     String contrasenia;

     Integer DNI;

     String imagenUri;

    public String getImagenUri() {
        return imagenUri;
    }

    public void setImagenUri(String imagenUri) {
        this.imagenUri = imagenUri;
    }

    public Usuario(){

    }

    public Usuario(String email, String contrasenia, Integer DNI,String imagenUri) {
        this.email = email;
        this.contrasenia = contrasenia;
        this.DNI = DNI;
        this.imagenUri=imagenUri;

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setDNI(Integer DNI) {
        this.DNI = DNI;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public Integer getDNI() {
        return DNI;
    }



    /*
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Integer getDNI() {
        return dNI;
    }

    public void setDNI(Integer dNI) {
        this.dNI = dNI;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }*/


}