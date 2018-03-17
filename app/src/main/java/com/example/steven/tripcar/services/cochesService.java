package com.example.steven.tripcar.services;

import com.example.steven.tripcar.models.Coche;
import com.example.steven.tripcar.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface cochesService {
    @GET("api/coches/")
    Call<List<Coche>> obtenerCoches();
    @GET("api/coches/obtenerCoche/{matricula}")
    Call<Coche> obtenerCoche(@Path("matricula") String matricula);
}
