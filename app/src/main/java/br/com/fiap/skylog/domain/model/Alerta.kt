package br.com.fiap.skylog.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Alerta(
    val id: String,
    val title: String,
    val category: String,
    val mappedType: String,
    val nivelRisco: NivelRisco,
    val latitude: Double,
    val longitude: Double,
    val dateString: String,
    val source: String,
    val description: String,
    val isFavorite: Boolean = false
)
