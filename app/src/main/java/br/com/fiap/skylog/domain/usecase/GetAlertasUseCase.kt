package br.com.fiap.skylog.domain.usecase

import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.model.NivelRisco
import br.com.fiap.skylog.domain.repository.AlertaRepository
import br.com.fiap.skylog.domain.repository.PreferencesRepository

class GetAlertasUseCase(
    private val alertaRepository: AlertaRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): List<Alerta> {
        val list = alertaRepository.getAlertas(forceRefresh)
        val limiar = preferencesRepository.getLimiarAlerta()

        return list.filter { alerta ->
            val riscodeAlerta = alerta.nivelRisco
            when (limiar) {
                "CRÍTICO" -> riscodeAlerta == NivelRisco.CRITICAL
                "ALTO" -> riscodeAlerta == NivelRisco.CRITICAL || riscodeAlerta == NivelRisco.HIGH
                "MÉDIO" -> riscodeAlerta == NivelRisco.CRITICAL || riscodeAlerta == NivelRisco.HIGH || riscodeAlerta == NivelRisco.MEDIUM
                else -> true
            }
        }
    }
}
