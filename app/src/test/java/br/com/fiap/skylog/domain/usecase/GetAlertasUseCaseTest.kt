package br.com.fiap.skylog.domain.usecase

import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.model.NivelRisco
import br.com.fiap.skylog.domain.repository.AlertaRepository
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAlertasUseCaseTest {

    private class FakeAlertaRepository : AlertaRepository {
        var alertas = listOf<Alerta>()
        
        override suspend fun getAlertas(forceRefresh: Boolean): List<Alerta> {
            return alertas
        }

        override suspend fun getAlertaById(id: String): Alerta? {
            return alertas.find { it.id == id }
        }
    }

    private class FakePreferencesRepository : PreferencesRepository {
        var limiar = "BAIXO"
        
        override fun isOnboardingComplete() = true
        override fun setOnboardingComplete(complete: Boolean) {}
        override fun isDarkMode() = false
        override fun setDarkMode(enabled: Boolean) {}
        override fun getUserName() = ""
        override fun setUserName(name: String) {}
        override fun getUserCompany() = ""
        override fun setUserCompany(company: String) {}
        override fun getLimiarAlerta() = limiar
        override fun setLimiarAlerta(limiar: String) { this.limiar = limiar }
        override fun getFavoriteAlertIds() = setOf<String>()
        override fun toggleFavoriteAlert(id: String) {}
        override fun isFavorite(id: String) = false
        override fun clearPreferences() {}
    }

    @Test
    fun invoke_withLimiarCritico_filtersOnlyCriticalAlerts() = runBlocking {
        val fakeAlerts = listOf(
            Alerta("1", "Title 1", "wildfires", "Queimada", NivelRisco.LOW, 0.0, 0.0, "", "NASA", ""),
            Alerta("2", "Title 2", "wildfires", "Queimada", NivelRisco.MEDIUM, 0.0, 0.0, "", "NASA", ""),
            Alerta("3", "Title 3", "severeStorms", "Tempestade", NivelRisco.HIGH, 0.0, 0.0, "", "NASA", ""),
            Alerta("4", "Title 4", "severeStorms", "Tempestade", NivelRisco.CRITICAL, 0.0, 0.0, "", "NASA", "")
        )

        val repo = FakeAlertaRepository().apply { alertas = fakeAlerts }
        val prefs = FakePreferencesRepository().apply { limiar = "CRÍTICO" }
        val useCase = GetAlertasUseCase(repo, prefs)

        val result = useCase()
        assertEquals(1, result.size)
        assertEquals(NivelRisco.CRITICAL, result[0].nivelRisco)
    }

    @Test
    fun invoke_withLimiarAlto_filtersHighAndCriticalAlerts() = runBlocking {
        val fakeAlerts = listOf(
            Alerta("1", "Title 1", "wildfires", "Queimada", NivelRisco.LOW, 0.0, 0.0, "", "NASA", ""),
            Alerta("2", "Title 2", "wildfires", "Queimada", NivelRisco.MEDIUM, 0.0, 0.0, "", "NASA", ""),
            Alerta("3", "Title 3", "severeStorms", "Tempestade", NivelRisco.HIGH, 0.0, 0.0, "", "NASA", ""),
            Alerta("4", "Title 4", "severeStorms", "Tempestade", NivelRisco.CRITICAL, 0.0, 0.0, "", "NASA", "")
        )

        val repo = FakeAlertaRepository().apply { alertas = fakeAlerts }
        val prefs = FakePreferencesRepository().apply { limiar = "ALTO" }
        val useCase = GetAlertasUseCase(repo, prefs)

        val result = useCase()
        assertEquals(2, result.size)
        assert(result.any { it.nivelRisco == NivelRisco.CRITICAL })
        assert(result.any { it.nivelRisco == NivelRisco.HIGH })
    }
}
