package com.example.steven.tripcar.services;

import com.example.steven.tripcar.models.Coche;
import com.example.steven.tripcar.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Steven on 17/03/2018.
 */

public interface cochesService {
    @POST("api/Coches/")
    Call<Coche> obtenerCoches();
    @POST("api/Usuarios/")
    Call<Coche> obtenerCoche(@Body String matricula );
}
