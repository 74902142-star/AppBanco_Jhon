package com.example.appbanco_s8.data.repository

import com.example.appbanco_s8.data.model.Perfil
import com.example.appbanco_s8.data.remote.RetrofitClient

class PerfilRepository {
    private val api = RetrofitClient.api

    suspend fun getPerfil(token: String, userId: String): Result<Perfil?> = try {
        val r = api.getPerfil("Bearer $token", "eq.$userId")
        if (r.isSuccessful) Result.success(r.body()?.firstOrNull())
        else Result.failure(Exception("Error ${r.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun actualizarPerfil(token: String, userId: String, telefono: String, direccion: String): Result<Unit> = try {
        val r = api.actualizarPerfil("Bearer $token", "eq.$userId", mapOf("telefono" to telefono, "direccion" to direccion))
        if (r.isSuccessful) Result.success(Unit)
        else Result.failure(Exception("Error ${r.code()}"))
    } catch (e: Exception) { Result.failure(e) }
}
