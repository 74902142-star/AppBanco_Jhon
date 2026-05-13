package com.example.appbanco_s8.data.model

import com.google.gson.annotations.SerializedName

data class CuentaAhorroCP(
    val id: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("numero_cuenta")
    val numeroCuenta: String = "",
    val saldo: Double = 0.0,
    val moneda: String = "PEN"
)

data class MovimientoAhorro(
    val id: String = "",
    @SerializedName("cuenta_id")
    val cuentaId: String = "",
    val descripcion: String = "",
    val monto: Double = 0.0,
    val tipo: String = "", // "deposito" o "retiro"
    val fecha: String = ""
)
