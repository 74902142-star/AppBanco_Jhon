package com.example.appbanco_s8.data.model

import com.google.gson.annotations.SerializedName

data class Credito(
    val id: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    val monto: Double = 0.0,
    @SerializedName("saldo_restante")
    val saldoRestante: Double = 0.0,
    val cuotas: Int = 0,
    @SerializedName("cuotas_pagadas")
    val cuotasPagadas: Int = 0,
    @SerializedName("tasa_interes")
    val tasaInteres: Double = 0.0
)

data class CronogramaPago(
    val id: String = "",
    @SerializedName("credito_id")
    val creditoId: String = "",
    @SerializedName("fecha_vencimiento")
    val fechaVencimiento: String = "",
    @SerializedName("monto_cuota")
    val montoCuota: Double = 0.0,
    val estado: String = "" // "pendiente" o "pagado"
)
