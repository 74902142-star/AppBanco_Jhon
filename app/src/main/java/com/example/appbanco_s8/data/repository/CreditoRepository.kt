package com.example.appbanco_s8.data.repository

import com.example.appbanco_s8.data.model.Credito
import com.example.appbanco_s8.data.model.CronogramaPago
import com.example.appbanco_s8.data.remote.RetrofitClient

class CreditoRepository {
    private val api = RetrofitClient.api

    suspend fun getCreditos(token: String, userId: String): Result<List<Credito>> = try {
        val r = api.getCreditos("Bearer $token", "eq.$userId")
        if (r.isSuccessful) Result.success(r.body() ?: emptyList())
        else Result.failure(Exception("Error ${r.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getCronograma(token: String, creditoId: String): Result<List<CronogramaPago>> = try {
        val r = api.getCronograma("Bearer $token", "eq.$creditoId")
        if (r.isSuccessful) Result.success(r.body() ?: emptyList())
        else Result.failure(Exception("Error ${r.code()}"))
    } catch (e: Exception) { Result.failure(e) }
}
