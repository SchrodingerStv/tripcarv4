package com.example.steven.tripcar.models;

import java.util.UUID;




public class FirebaseReferences {

    final public static String USUARIOS_REFERENCE = "usuarios";
    final public static String IMAGENES_USUARIOS_REFERENCE = "usuarios/"+ UUID.randomUUID().toString();
    final public static String COCHES_REFERENCE = "coches";
    final public static String RESERVAS_REFERENCE = "reservas";
}
