package com.example.steven.tripcar.services;


import com.example.steven.tripcar.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface usuariosService {
    @POST("api/usuarios/")
    Call<Usuario> insertrUsuario(@Body Usuario usuario);
    @GET("api/usuarios/obtenerUsuario/{email}/{contrasenia}")
    Call<Usuario> obtenerusario( @Path("email") String email, @Path("contrasenia")String contrasenia);
}
