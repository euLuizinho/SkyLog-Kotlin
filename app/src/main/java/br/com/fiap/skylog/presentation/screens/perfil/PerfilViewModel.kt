package br.com.fiap.skylog.presentation.screens.perfil

import androidx.lifecycle.ViewModel
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PerfilViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _company = MutableStateFlow("")
    val company: StateFlow<String> = _company.asStateFlow()

    private val _limiar = MutableStateFlow("")
    val limiar: StateFlow<String> = _limiar.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _favoritesCount = MutableStateFlow(0)
    val favoritesCount: StateFlow<Int> = _favoritesCount.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _name.value = preferencesRepository.getUserName()
        _company.value = preferencesRepository.getUserCompany()
        _limiar.value = preferencesRepository.getLimiarAlerta()
        _isDarkMode.value = preferencesRepository.isDarkMode()
        _favoritesCount.value = preferencesRepository.getFavoriteAlertIds().size
    }

    fun updateName(newValue: String) {
        _name.value = newValue
        preferencesRepository.setUserName(newValue)
    }

    fun updateCompany(newValue: String) {
        _company.value = newValue
        preferencesRepository.setUserCompany(newValue)
    }

    fun updateLimiar(newValue: String) {
        _limiar.value = newValue
        preferencesRepository.setLimiarAlerta(newValue)
    }

    fun updateDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        preferencesRepository.setDarkMode(enabled)
    }

    fun logout() {
        preferencesRepository.clearPreferences()
        loadData()
    }
}
