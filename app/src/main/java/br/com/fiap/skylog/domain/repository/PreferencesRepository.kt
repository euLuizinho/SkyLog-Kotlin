package br.com.fiap.skylog.domain.repository

interface PreferencesRepository {
    fun isOnboardingComplete(): Boolean
    fun setOnboardingComplete(complete: Boolean)

    fun isDarkMode(): Boolean
    fun setDarkMode(enabled: Boolean)

    fun getUserName(): String
    fun setUserName(name: String)

    fun getUserCompany(): String
    fun setUserCompany(company: String)

    fun getLimiarAlerta(): String
    fun setLimiarAlerta(limiar: String)

    fun getFavoriteAlertIds(): Set<String>
    fun toggleFavoriteAlert(id: String)
    fun isFavorite(id: String): Boolean
    fun clearPreferences()
}
