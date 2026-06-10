package br.com.fiap.skylog.domain.usecase

import br.com.fiap.skylog.domain.model.Alerta

class FiltrarAlertasUseCase {
    operator fun invoke(
        alertas: List<Alerta>,
        query: String,
        selectedCategory: String,
        showOnlyFavorites: Boolean = false
    ): List<Alerta> {
        return alertas.filter { alerta ->
            val matchesQuery = query.isBlank() || 
                    alerta.title.contains(query, ignoreCase = true) || 
                    alerta.mappedType.contains(query, ignoreCase = true) ||
                    alerta.source.contains(query, ignoreCase = true)

            val matchesCategory = when (selectedCategory) {
                "TODOS" -> true
                "QUEIMADA" -> alerta.mappedType.equals("Queimada", ignoreCase = true)
                "TEMPESTADE" -> alerta.mappedType.equals("Tempestade", ignoreCase = true)
                "ENCHENTE" -> alerta.mappedType.equals("Enchente", ignoreCase = true)
                "DESLIZAMENTO" -> alerta.mappedType.equals("Deslizamento", ignoreCase = true)
                "VULCÃO" -> alerta.mappedType.equals("Vulcão", ignoreCase = true)
                "FAVORITOS" -> alerta.isFavorite
                else -> true
            }

            val matchesFavorites = !showOnlyFavorites || alerta.isFavorite

            matchesQuery && matchesCategory && matchesFavorites
        }
    }
}
