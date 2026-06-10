package br.com.fiap.skylog.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class EventoDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val link: String? = null,
    val closed: String? = null,
    val categories: List<CategoryDto> = emptyList(),
    val sources: List<SourceDto> = emptyList(),
    val geometry: List<GeometryDto> = emptyList()
)

@Serializable
data class CategoryDto(
    val id: String,
    val title: String
)

@Serializable
data class SourceDto(
    val id: String,
    val url: String? = null
)

@Serializable
data class GeometryDto(
    val date: String,
    val type: String,
    val coordinates: List<Double> = emptyList()
)
