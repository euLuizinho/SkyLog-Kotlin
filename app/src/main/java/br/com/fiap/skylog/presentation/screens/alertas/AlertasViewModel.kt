package br.com.fiap.skylog.presentation.screens.alertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.skylog.core.ui.UiState
import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.domain.usecase.FiltrarAlertasUseCase
import br.com.fiap.skylog.domain.usecase.GetAlertasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AlertasViewModel(
    private val getAlertasUseCase: GetAlertasUseCase,
    private val filtrarAlertasUseCase: FiltrarAlertasUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _rawAlerts = MutableStateFlow<List<Alerta>>(emptyList())
    private val _uiState = MutableStateFlow<UiState<List<Alerta>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Alerta>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("TODOS")
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        loadAlerts()

        viewModelScope.launch {
            combine(_rawAlerts, _searchQuery, _selectedCategory) { alerts, query, category ->
                Triple(alerts, query, category)
            }.collect { (alerts, query, category) ->
                if (_uiState.value is UiState.Loading) return@collect

                val filtered = filtrarAlertasUseCase(
                    alertas = alerts,
                    query = query,
                    selectedCategory = category
                )
                _uiState.value = UiState.Success(filtered)
            }
        }
    }

    fun loadAlerts(forceRefresh: Boolean = false) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val alerts = getAlertasUseCase(forceRefresh)
                _rawAlerts.value = alerts

                val filtered = filtrarAlertasUseCase(
                    alertas = alerts,
                    query = _searchQuery.value,
                    selectedCategory = _selectedCategory.value
                )
                _uiState.value = UiState.Success(filtered)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar lista de alertas")
            }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }
}
