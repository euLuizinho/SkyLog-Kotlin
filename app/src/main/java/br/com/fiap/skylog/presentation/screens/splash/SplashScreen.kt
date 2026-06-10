package br.com.fiap.skylog.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.skylog.R
import br.com.fiap.skylog.core.theme.Charcoal
import br.com.fiap.skylog.core.theme.Coral
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.presentation.navigation.NavRoutes
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun SplashScreen(
    navController: NavController,
    preferencesRepository: PreferencesRepository = koinInject()
) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(1000)

        val onboardingComplete = preferencesRepository.isOnboardingComplete()
        if (onboardingComplete) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(NavRoutes.ONBOARDING) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Charcoal),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.scale(scale.value)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "SkyLog Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = "SkyLog",
                color = Coral,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.04).sp
            )
            Text(
                text = "Logística & Segurança Preventiva",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }
    }
}
