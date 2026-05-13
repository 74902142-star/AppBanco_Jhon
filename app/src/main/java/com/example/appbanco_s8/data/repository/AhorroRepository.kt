package com.example.appbanco_s8.data.repository

import com.example.appbanco_s8.data.model.CuentaAhorroCP
import com.example.appbanco_s8.data.model.MovimientoAhorro
import com.example.appbanco_s8.data.remote.RetrofitClient

class AhorroRepository {
    private val api = RetrofitClient.api

    suspend fun getCuentasAhorro(token: String, userId: String): Result<List<CuentaAhorroCP>> = try {
        val r = api.getCuentasAhorro("Bearer $token", "eq.$userId")
        if (r.isSuccessful) Result.success(r.body() ?: emptyList())
        else Result.failure(Exception("Error ${r.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getMovimientos(token: String, cuentaId: String): Result<List<MovimientoAhorro>> = try {
        val r = api.getMovimientosAhorro("Bearer $token", "eq.$cuentaId")
        if (r.isSuccessful) Result.success(r.body() ?: emptyList())
        else Result.failure(Exception("Error ${r.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun depositar(token: String, cuentaId: String, monto: Double, nuevoSaldo: Double): Result<Unit> = try {
        val mov = MovimientoAhorro(cuentaId = cuentaId, descripcion = "Depósito", monto = monto, tipo = "deposito")
        val rMov = api.crearMovimientoAhorro("Bearer $token", mov)
        if (rMov.isSuccessful) {
            api.actualizarSaldoAhorro("Bearer $token", "eq.$cuentaId", mapOf("saldo" to nuevoSaldo))
            Result.success(Unit)
        } else Result.failure(Exception("Error al depositar"))
    } catch (e: Exception) { Result.failure(e) }
}
