package br.com.fiap.skylog.domain.repository

import br.com.fiap.skylog.domain.model.Alerta

interface AlertaRepository {
    suspend fun getAlertas(forceRefresh: Boolean = false): List<Alerta>
    suspend fun getAlertaById(id: String): Alerta?
}
