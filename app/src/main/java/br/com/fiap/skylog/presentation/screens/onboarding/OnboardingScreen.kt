package br.com.fiap.skylog.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.skylog.core.theme.*
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.presentation.navigation.NavRoutes
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    preferencesRepository: PreferencesRepository = koinInject()
) {
    val pages = listOf(
        OnboardingPage(
            title = "A natureza não avisa",
            description = "Eventos climáticos e desastres naturais acontecem a qualquer momento. Bloqueios de pistas e desvios de última hora geram atrasos e riscos desnecessários para os motoristas.",
            icon = Icons.Default.Warning,
            iconColor = RiskCritical
        ),
        OnboardingPage(
            title = "A SkyLog chega antes",
            description = "Conectado diretamente à base de dados espacial da NASA EONET, o SkyLog monitora focos de incêndio, tempestades severas e enchentes ao longo das rodovias em tempo real.",
            icon = Icons.Default.Info,
            iconColor = Coral
        ),
        OnboardingPage(
            title = "Simples e seguro",
            description = "Filtre os alertas, salve as ocorrências críticas que impactam sua rota nos favoritos e visualize desvios alternativos sugeridos para chegar em segurança ao seu destino.",
            icon = Icons.Default.Home,
            iconColor = RiskLow
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    val onFinish = {
        preferencesRepository.setOnboardingComplete(true)
        navController.navigate(NavRoutes.HOME) {
            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (pagerState.currentPage > 0) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Text("Voltar", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Coral else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage == pages.size - 1) {
                            onFinish()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Coral),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.size - 1) "Começar" else "Avançar",
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (pagerState.currentPage < pages.size - 1) {
                TextButton(
                    onClick = onFinish,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text("Pular", color = Coral, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = page.iconColor,
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = page.title,
                        style = Typography.displayLarge,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.03).sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = page.description,
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
