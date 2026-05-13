package com.example.appbanco_s8.data.model

import com.google.gson.annotations.SerializedName

data class Tarjeta(
    val id: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    val numero: String = "",
    val tipo: String = "",
    val marca: String = "",
    @SerializedName("fecha_vencimiento")
    val fechaVencimiento: String = ""
)
