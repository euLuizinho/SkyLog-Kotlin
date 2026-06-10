package br.com.fiap.skylog.domain.usecase

import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.model.NivelRisco
import org.junit.Assert.assertEquals
import org.junit.Test

class FiltrarAlertasUseCaseTest {

    private val useCase = FiltrarAlertasUseCase()

    private val testAlerts = listOf(
        Alerta("1", "Queimada na BR-101", "wildfires", "Queimada", NivelRisco.HIGH, 0.0, 0.0, "", "NASA EONET", "", false),
        Alerta("2", "Tempestade em SC", "severeStorms", "Tempestade", NivelRisco.CRITICAL, 0.0, 0.0, "", "NASA EONET", "", true),
        Alerta("3", "Deslizamento no RJ", "landslides", "Deslizamento", NivelRisco.MEDIUM, 0.0, 0.0, "", "INPE", "", false)
    )

    @Test
    fun invoke_withCategoryTodos_returnsAll() {
        val result = useCase(testAlerts, query = "", selectedCategory = "TODOS")
        assertEquals(3, result.size)
    }

    @Test
    fun invoke_withQuery_returnsMatching() {
        val result = useCase(testAlerts, query = "RJ", selectedCategory = "TODOS")
        assertEquals(1, result.size)
        assertEquals("3", result[0].id)
    }

    @Test
    fun invoke_withCategory_returnsOnlyOfThatCategory() {
        val result = useCase(testAlerts, query = "", selectedCategory = "QUEIMADA")
        assertEquals(1, result.size)
        assertEquals("Queimada", result[0].mappedType)
    }

    @Test
    fun invoke_showOnlyFavorites_returnsOnlyFavorites() {
        val result = useCase(testAlerts, query = "", selectedCategory = "TODOS", showOnlyFavorites = true)
        assertEquals(1, result.size)
        assertEquals("2", result[0].id)
    }
}
