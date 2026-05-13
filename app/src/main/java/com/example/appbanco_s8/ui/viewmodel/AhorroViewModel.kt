package com.example.appbanco_s8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanco_s8.data.model.CuentaAhorroCP
import com.example.appbanco_s8.data.model.MovimientoAhorro
import com.example.appbanco_s8.data.repository.AhorroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AhorroUiState {
    object Loading : AhorroUiState()
    data class Success(val cuenta: CuentaAhorroCP, val movimientos: List<MovimientoAhorro>) : AhorroUiState()
    data class Error(val message: String) : AhorroUiState()
}

class AhorroViewModel : ViewModel() {
    private val repository = AhorroRepository()
    private val _uiState = MutableStateFlow<AhorroUiState>(AhorroUiState.Loading)
    val uiState: StateFlow<AhorroUiState> = _uiState

    fun cargarAhorros(token: String, userId: String) {
        viewModelScope.launch {
            _uiState.value = AhorroUiState.Loading
            val resCuenta = repository.getCuentasAhorro(token, userId)
            if (resCuenta.isSuccess) {
                val cuenta = resCuenta.getOrNull()?.firstOrNull()
                if (cuenta != null) {
                    val resMovs = repository.getMovimientos(token, cuenta.id)
                    _uiState.value = AhorroUiState.Success(cuenta, resMovs.getOrDefault(emptyList()))
                } else {
                    _uiState.value = AhorroUiState.Error("No se encontró cuenta de ahorros")
                }
            } else {
                _uiState.value = AhorroUiState.Error("Error al cargar datos")
            }
        }
    }

    fun depositar(token: String, userId: String, monto: Double) {
        val currentState = _uiState.value
        if (currentState is AhorroUiState.Success) {
            viewModelScope.launch {
                val nuevoSaldo = currentState.cuenta.saldo + monto
                val res = repository.depositar(token, currentState.cuenta.id, monto, nuevoSaldo)
                if (res.isSuccess) {
                    cargarAhorros(token, userId)
                }
            }
        }
    }
}
