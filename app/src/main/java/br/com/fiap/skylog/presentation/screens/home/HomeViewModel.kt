package br.com.fiap.skylog.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.skylog.core.ui.UiState
import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.model.NivelRisco
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.domain.usecase.GetAlertasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeData(
    val activeAlertsCount: Int,
    val criticalAlertsCount: Int,
    val safeRoutesCount: Int,
    val urgentAlert: Alerta?,
    val recentAlerts: List<Alerta>,
    val driverName: String,
    val driverCompany: String
)

class HomeViewModel(
    private val getAlertasUseCase: GetAlertasUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeData>>(UiState.Initial)
    val uiState: StateFlow<UiState<HomeData>> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData(forceRefresh: Boolean = false) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val alerts = getAlertasUseCase(forceRefresh)

                val urgent = alerts.firstOrNull { it.nivelRisco == NivelRisco.CRITICAL }
                    ?: alerts.firstOrNull { it.nivelRisco == NivelRisco.HIGH }
                    ?: alerts.firstOrNull { it.nivelRisco == NivelRisco.MEDIUM }
                    ?: alerts.firstOrNull()

                val criticalCount = alerts.count { it.nivelRisco == NivelRisco.CRITICAL || it.nivelRisco == NivelRisco.HIGH }
                val recent = alerts.take(5)

                val driverName = preferencesRepository.getUserName()
                val driverCompany = preferencesRepository.getUserCompany()

                _uiState.value = UiState.Success(
                    HomeData(
                        activeAlertsCount = alerts.size,
                        criticalAlertsCount = criticalCount,
                        safeRoutesCount = 12,
                        urgentAlert = urgent,
                        recentAlerts = recent,
                        driverName = driverName,
                        driverCompany = driverCompany
                    )
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro desconhecido ao carregar alertas")
            }
        }
    }
}
