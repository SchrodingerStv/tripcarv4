package com.example.steven.tripcar.services;


import com.example.steven.tripcar.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface usuariosService {
    @POST("api/Usuarios/")
    Call<Usuario> insertarUsuario(@Body Usuario usuario );
    @POST("api/Usuarios/")
    Call<Usuario> obtenerusario(@Body String email, String contrasenia );
}
