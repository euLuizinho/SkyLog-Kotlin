package br.com.fiap.skylog.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.skylog.presentation.screens.splash.SplashScreen
import br.com.fiap.skylog.presentation.screens.onboarding.OnboardingScreen
import br.com.fiap.skylog.presentation.screens.home.HomeScreen
import br.com.fiap.skylog.presentation.screens.alertas.AlertasScreen
import br.com.fiap.skylog.presentation.screens.detalhe.DetalheAlertaScreen
import br.com.fiap.skylog.presentation.screens.rota.RotaAlternativaScreen
import br.com.fiap.skylog.presentation.screens.perfil.PerfilScreen

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.ONBOARDING) {
            OnboardingScreen(navController = navController)
        }

        composable(NavRoutes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(NavRoutes.ALERTAS) {
            AlertasScreen(navController = navController)
        }

        composable(
            route = NavRoutes.DETALHE_ALERTA,
            arguments = listOf(
                navArgument("alertaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val alertaId = backStackEntry.arguments?.getString("alertaId") ?: ""
            DetalheAlertaScreen(alertaId = alertaId, navController = navController)
        }

        composable(NavRoutes.ROTA_ALTERNATIVA) {
            RotaAlternativaScreen(navController = navController)
        }

        composable(NavRoutes.PERFIL) {
            PerfilScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        }
    }
}
