package com.example.appbanco_s8.data.model

import com.google.gson.annotations.SerializedName

data class Perfil(
    val id: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",
    val telefono: String = "",
    val direccion: String = "",
    @SerializedName("fecha_registro")
    val fechaRegistro: String = ""
)
