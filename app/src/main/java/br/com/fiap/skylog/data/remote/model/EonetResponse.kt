package br.com.fiap.skylog.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class EonetResponse(
    val title: String? = null,
    val description: String? = null,
    val link: String? = null,
    val events: List<EventoDto> = emptyList()
)
