package br.com.fiap.skylog.presentation.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val ALERTAS = "alertas"
    const val DETALHE_ALERTA = "detalhe/{alertaId}"
    const val ROTA_ALTERNATIVA = "rota_alternativa"
    const val PERFIL = "perfil"

    fun detalheAlertaRoute(alertaId: String) = "detalhe/$alertaId"
}
