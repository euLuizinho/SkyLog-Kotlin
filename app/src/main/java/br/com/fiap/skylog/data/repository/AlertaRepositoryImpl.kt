package br.com.fiap.skylog.data.repository

import br.com.fiap.skylog.data.remote.NasaEonetApi
import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.model.NivelRisco
import br.com.fiap.skylog.domain.repository.AlertaRepository
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import kotlin.math.abs

class AlertaRepositoryImpl(
    private val api: NasaEonetApi,
    private val preferencesRepository: PreferencesRepository
) : AlertaRepository {

    private var cachedAlerts: List<Alerta> = emptyList()

    override suspend fun getAlertas(forceRefresh: Boolean): List<Alerta> {
        if (cachedAlerts.isNotEmpty() && !forceRefresh) {
            return applyFavoriteState(cachedAlerts)
        }

        return try {
            val response = api.getEvents()
            val mapped = response.events.map { event ->
                val categoryId = event.categories.firstOrNull()?.id ?: "unknown"

                val mappedType = when (categoryId) {
                    "wildfires" -> "Queimada"
                    "severeStorms" -> "Tempestade"
                    "floods" -> "Enchente"
                    "landslides" -> "Deslizamento"
                    "volcanoes" -> "Vulcão"
                    else -> "Outro"
                }

                val hash = abs(event.id.hashCode())
                val nivelRisco = when (categoryId) {
                    "wildfires", "severeStorms" -> {
                        if (hash % 2 == 0) NivelRisco.CRITICAL else NivelRisco.HIGH
                    }
                    "floods", "landslides" -> {
                        if (hash % 2 == 0) NivelRisco.HIGH else NivelRisco.MEDIUM
                    }
                    else -> {
                        if (hash % 2 == 0) NivelRisco.MEDIUM else NivelRisco.LOW
                    }
                }

                val coords = event.geometry.firstOrNull()?.coordinates ?: emptyList()
                val longitude = coords.getOrNull(0) ?: 0.0
                val latitude = coords.getOrNull(1) ?: 0.0

                val dateStr = event.geometry.firstOrNull()?.date ?: "Desconhecida"
                val sourceStr = event.sources.firstOrNull()?.id ?: "NASA EONET"

                Alerta(
                    id = event.id,
                    title = event.title,
                    category = categoryId,
                    mappedType = mappedType,
                    nivelRisco = nivelRisco,
                    latitude = latitude,
                    longitude = longitude,
                    dateString = formatDate(dateStr),
                    source = sourceStr,
                    description = event.description ?: "Nenhum detalhe adicional fornecido pela NASA.",
                    isFavorite = false
                )
            }
            cachedAlerts = mapped
            applyFavoriteState(cachedAlerts)
        } catch (e: Exception) {
            e.printStackTrace()
            if (cachedAlerts.isNotEmpty()) {
                applyFavoriteState(cachedAlerts)
            } else {
                val mockAlerts = getMockAlertas()
                cachedAlerts = mockAlerts
                applyFavoriteState(cachedAlerts)
            }
        }
    }

    override suspend fun getAlertaById(id: String): Alerta? {
        val list = getAlertas(forceRefresh = false)
        return list.firstOrNull { it.id == id }
    }

    private fun applyFavoriteState(alerts: List<Alerta>): List<Alerta> {
        val favorites = preferencesRepository.getFavoriteAlertIds()
        return alerts.map { alert ->
            alert.copy(isFavorite = favorites.contains(alert.id))
        }
    }

    private fun formatDate(rawDate: String): String {
        return try {
            val datePart = rawDate.split("T").firstOrNull() ?: rawDate
            val parts = datePart.split("-")
            if (parts.size == 3) {
                "${parts[2]}/${parts[1]}/${parts[0]}"
            } else {
                rawDate
            }
        } catch (e: Exception) {
            rawDate
        }
    }

    private fun getMockAlertas(): List<Alerta> {
        return listOf(
            Alerta(
                id = "MOCK_FIRE_001",
                title = "Incêndio Florestal em Parque Nacional",
                category = "wildfires",
                mappedType = "Queimada",
                nivelRisco = NivelRisco.CRITICAL,
                latitude = -22.9068,
                longitude = -43.1729,
                dateString = "10/06/2026",
                source = "INPE",
                description = "Foco de incêndio de grandes proporções detectado em área de preservação. Ventos fortes na região dificultam o controle das chamas. Recomenda-se desvio imediato da BR-101.",
                isFavorite = false
            ),
            Alerta(
                id = "MOCK_FLOOD_002",
                title = "Enchente Crítica na Rodovia Régis Bittencourt",
                category = "floods",
                mappedType = "Enchente",
                nivelRisco = NivelRisco.HIGH,
                latitude = -23.5505,
                longitude = -46.6333,
                dateString = "09/06/2026",
                source = "Defesa Civil",
                description = "Alagamento transitório na altura do km 280 da BR-116. Pista sentido sul totalmente interditada. Desvio preventivo sugerido na Rota Alternativa.",
                isFavorite = false
            ),
            Alerta(
                id = "MOCK_STORM_003",
                title = "Tempestade de Granizo e Ventos Fortes",
                category = "severeStorms",
                mappedType = "Tempestade",
                nivelRisco = NivelRisco.MEDIUM,
                latitude = -25.4290,
                longitude = -49.2671,
                dateString = "08/06/2026",
                source = "INMET",
                description = "Precipitação de granizo com ventos de até 70km/h afetando a visibilidade na BR-376. Reduza a velocidade e acenda os faróis.",
                isFavorite = false
            ),
            Alerta(
                id = "MOCK_LAND_004",
                title = "Deslizamento de Terra na Serra do Mar",
                category = "landslides",
                mappedType = "Deslizamento",
                nivelRisco = NivelRisco.CRITICAL,
                latitude = -23.9618,
                longitude = -46.3322,
                dateString = "07/06/2026",
                source = "DNIT",
                description = "Queda de barreira na Rodovia dos Imigrantes (SP-160), km 45. Trânsito operando em meia pista. Risco de novos deslizamentos devido às chuvas persistentes.",
                isFavorite = false
            ),
            Alerta(
                id = "MOCK_VOLC_005",
                title = "Atividade Vulcânica - Nuvem de Cinzas",
                category = "volcanoes",
                mappedType = "Vulcão",
                nivelRisco = NivelRisco.LOW,
                latitude = -0.6838,
                longitude = -78.4385,
                dateString = "05/06/2026",
                source = "SIVolcano",
                description = "Emissão moderada de cinzas vulcânicas afetando a visibilidade no tráfego aéreo e terrestre nas proximidades da cordilheira.",
                isFavorite = false
            )
        )
    }
}
