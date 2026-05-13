package com.example.appbanco_s8.data.remote

import com.example.appbanco_s8.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApi {

    @GET("rest/v1/cuentas")
    suspend fun getCuentas(
        @Header("Authorization") token: String,
        @Query("select") select: String = "*",
        @Query("order")  order:  String = "tipo.asc"
    ): Response<List<Cuenta>>

    @GET("rest/v1/transacciones")
    suspend fun getTransacciones(
        @Header("Authorization") token:    String,
        @Query("cuenta_id")      cuentaId: String,
        @Query("select")         select:   String = "*",
        @Query("order")          order:    String = "fecha.desc",
        @Query("limit")          limit:    Int    = 10
    ): Response<List<Transaccion>>

    // ── Módulo Ahorros ──────────────────────
    @GET("rest/v1/cuentas_ahorro")
    suspend fun getCuentasAhorro(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String,
        @Query("select") select: String = "*"
    ): Response<List<CuentaAhorroCP>>

    @GET("rest/v1/movimientos_ahorro")
    suspend fun getMovimientosAhorro(
        @Header("Authorization") token: String,
        @Query("cuenta_id") cuentaId: String,
        @Query("select") select: String = "*",
        @Query("order") order: String = "fecha.desc"
    ): Response<List<MovimientoAhorro>>

    @POST("rest/v1/movimientos_ahorro")
    suspend fun crearMovimientoAhorro(
        @Header("Authorization") token: String,
        @Body movimiento: MovimientoAhorro
    ): Response<Unit>

    @PATCH("rest/v1/cuentas_ahorro")
    suspend fun actualizarSaldoAhorro(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        @Body updates: Map<String, Double>
    ): Response<Unit>

    // ── Módulo Créditos ──────────────────────
    @GET("rest/v1/creditos")
    suspend fun getCreditos(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String,
        @Query("select") select: String = "*"
    ): Response<List<Credito>>

    @GET("rest/v1/cronograma_pagos")
    suspend fun getCronograma(
        @Header("Authorization") token: String,
        @Query("credito_id") creditoId: String,
        @Query("select") select: String = "*",
        @Query("order") order: String = "fecha_vencimiento.asc"
    ): Response<List<CronogramaPago>>

    // ── Módulo Perfil ──────────────────────
    @GET("rest/v1/perfiles")
    suspend fun getPerfil(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String,
        @Query("select") select: String = "*"
    ): Response<List<Perfil>>

    @PATCH("rest/v1/perfiles")
    suspend fun actualizarPerfil(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String,
        @Body updates: Map<String, String>
    ): Response<Unit>

    // ── Transferencias y Pagos ────────────────
    @POST("rest/v1/transferencias")
    suspend fun registrarTransferencia(
        @Header("Authorization") token: String,
        @Body transferencia: TransferenciaInsert
    ): Response<Unit>

    @POST("rest/v1/pagos_servicios")
    suspend fun registrarPagoServicio(
        @Header("Authorization") token: String,
        @Body pago: PagoServicioInsert
    ): Response<Unit>

    // ── Métodos Legacy (para compatibilidad) ──
    @GET("rest/v1/cuentas_ahorro")
    suspend fun getCuentaAhorro(
        @Header("Authorization") token: String,
        @Query("select") select: String = "*",
        @Query("limit") limit: Int = 1
    ): Response<List<CuentaAhorro>>

    @GET("rest/v1/tarjetas")
    suspend fun getTarjetas(
        @Header("Authorization") token: String,
        @Query("select") select: String = "*"
    ): Response<List<Tarjeta>>

    @GET("rest/v1/prestamos")
    suspend fun getPrestamos(
        @Header("Authorization") token: String,
        @Query("select") select: String = "*"
    ): Response<List<Prestamo>>

    @GET("rest/v1/pagos")
    suspend fun getPagos(
        @Header("Authorization") token: String,
        @Query("select") select: String = "*",
        @Query("order") order: String = "fecha.desc",
        @Query("limit") limit: Int = 20
    ): Response<List<Pago>>
}
