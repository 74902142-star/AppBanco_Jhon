package com.example.appbanco_s8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanco_s8.data.model.Credito
import com.example.appbanco_s8.data.model.CronogramaPago
import com.example.appbanco_s8.data.repository.CreditoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreditoUiState {
    object Loading : CreditoUiState()
    data class Success(val creditos: List<Credito>) : CreditoUiState()
    data class Error(val message: String) : CreditoUiState()
}

class CreditoViewModel : ViewModel() {
    private val repository = CreditoRepository()
    private val _uiState = MutableStateFlow<CreditoUiState>(CreditoUiState.Loading)
    val uiState: StateFlow<CreditoUiState> = _uiState

    private val _cronograma = MutableStateFlow<List<CronogramaPago>>(emptyList())
    val cronograma: StateFlow<List<CronogramaPago>> = _cronograma

    fun cargarCreditos(token: String, userId: String) {
        viewModelScope.launch {
            _uiState.value = CreditoUiState.Loading
            val res = repository.getCreditos(token, userId)
            _uiState.value = if (res.isSuccess) {
                CreditoUiState.Success(res.getOrDefault(emptyList()))
            } else {
                CreditoUiState.Error("Error al cargar créditos")
            }
        }
    }

    fun cargarCronograma(token: String, creditoId: String) {
        viewModelScope.launch {
            val res = repository.getCronograma(token, creditoId)
            _cronograma.value = res.getOrDefault(emptyList())
        }
    }
}
