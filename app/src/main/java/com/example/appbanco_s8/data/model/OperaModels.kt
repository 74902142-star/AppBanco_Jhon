package com.example.appbanco_s8.data.model

import com.google.gson.annotations.SerializedName

data class TransferenciaInsert(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("cuenta_destino")
    val cuentaDestino: String,
    val monto: Double,
    val descripcion: String,
    val fecha: String = ""
)

data class PagoServicioInsert(
    @SerializedName("user_id")
    val userId: String,
    val servicio: String,
    val monto: Double,
    val contrato: String,
    val estado: String = "completado",
    val fecha: String = ""
)

data class Pago(
    val id: String = "",
    val servicio: String = "",
    val monto: Double = 0.0,
    val fecha: String = "",
    val estado: String = ""
)
