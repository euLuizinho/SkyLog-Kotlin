package br.com.fiap.skylog.presentation.screens.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.skylog.core.ui.UiState
import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.repository.AlertaRepository
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalheAlertaViewModel(
    private val alertaRepository: AlertaRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Alerta>>(UiState.Initial)
    val uiState: StateFlow<UiState<Alerta>> = _uiState.asStateFlow()

    fun loadAlerta(id: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val alerta = alertaRepository.getAlertaById(id)
                if (alerta != null) {
                    _uiState.value = UiState.Success(alerta)
                } else {
                    _uiState.value = UiState.Error("Alerta não encontrado no banco de dados.")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar detalhes do alerta.")
            }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            val alert = currentState.data
            viewModelScope.launch {
                preferencesRepository.toggleFavoriteAlert(alert.id)
                val updatedAlert = alert.copy(isFavorite = preferencesRepository.isFavorite(alert.id))
                _uiState.value = UiState.Success(updatedAlert)
            }
        }
    }
}
