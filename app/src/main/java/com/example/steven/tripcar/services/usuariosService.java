package com.example.steven.tripcar.services;


import com.example.steven.tripcar.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface usuariosService {
    @POST("api/usuarios/")
    Call<Usuario> insertrUsuario(@Body Usuario usuario);
    @POST("api/usuarios/obtenerusario/")
    Call<Usuario> obtenerusario(@Body Usuario usuario );
}
