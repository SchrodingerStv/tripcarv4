package com.example.steven.tripcar.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Nombre")
    @Expose
    private String nombre;
    @SerializedName("Contrasenia")
    @Expose
    private String contrasenia;
    @SerializedName("DNI")
    @Expose
    private Integer dNI;
    @SerializedName("IdUsuario")
    @Expose
    private Integer idUsuario;

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
    }

}