package br.com.fiap.skylog.data.repository

import android.content.Context
import br.com.fiap.skylog.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {

    private val sharedPrefs by lazy {
        context.getSharedPreferences("skylog_prefs", Context.MODE_PRIVATE)
    }

    override fun isOnboardingComplete(): Boolean {
        return sharedPrefs.getBoolean("onboarding_complete", false)
    }

    override fun setOnboardingComplete(complete: Boolean) {
        sharedPrefs.edit().putBoolean("onboarding_complete", complete).apply()
    }

    override fun isDarkMode(): Boolean {
        return sharedPrefs.getBoolean("dark_mode", false)
    }

    override fun setDarkMode(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("dark_mode", enabled).apply()
    }

    override fun getUserName(): String {
        return sharedPrefs.getString("user_name", "Motorista SkyLog") ?: "Motorista SkyLog"
    }

    override fun setUserName(name: String) {
        sharedPrefs.edit().putString("user_name", name).apply()
    }

    override fun getUserCompany(): String {
        return sharedPrefs.getString("user_company", "TransLog Transportes") ?: "TransLog Transportes"
    }

    override fun setUserCompany(company: String) {
        sharedPrefs.edit().putString("user_company", company).apply()
    }

    override fun getLimiarAlerta(): String {
        return sharedPrefs.getString("limiar_alerta", "BAIXO") ?: "BAIXO"
    }

    override fun setLimiarAlerta(limiar: String) {
        sharedPrefs.edit().putString("limiar_alerta", limiar).apply()
    }

    override fun getFavoriteAlertIds(): Set<String> {
        return sharedPrefs.getStringSet("favoritos", emptySet()) ?: emptySet()
    }

    override fun toggleFavoriteAlert(id: String) {
        val current = getFavoriteAlertIds().toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        sharedPrefs.edit().putStringSet("favoritos", current).apply()
    }

    override fun isFavorite(id: String): Boolean {
        return getFavoriteAlertIds().contains(id)
    }

    override fun clearPreferences() {
        sharedPrefs.edit().clear().apply()
    }
}
