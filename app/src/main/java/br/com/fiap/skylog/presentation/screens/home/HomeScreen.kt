package br.com.fiap.skylog.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.skylog.core.ui.UiState
import br.com.fiap.skylog.core.theme.*
import br.com.fiap.skylog.domain.model.Alerta
import br.com.fiap.skylog.domain.model.NivelRisco
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.presentation.components.AlertCard
import br.com.fiap.skylog.presentation.components.MetricCard
import br.com.fiap.skylog.presentation.components.RiskBadge
import br.com.fiap.skylog.presentation.components.TopBar
import br.com.fiap.skylog.presentation.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
    preferencesRepository: PreferencesRepository = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkMode = preferencesRepository.isDarkMode()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                onToggleDarkMode = {
                    preferencesRepository.setDarkMode(!isDarkMode)

                },
                isDarkMode = isDarkMode
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", style = Typography.labelSmall) },
                    selected = true,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Coral, indicatorColor = Color.Transparent),
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Alertas") },
                    label = { Text("Alertas", style = Typography.labelSmall) },
                    selected = false,
                    onClick = {
                        navController.navigate(NavRoutes.ALERTAS) {
                            popUpTo(NavRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", style = Typography.labelSmall) },
                    selected = false,
                    onClick = {
                        navController.navigate(NavRoutes.PERFIL) {
                            popUpTo(NavRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is UiState.Initial, is UiState.Loading -> {
                    CircularProgressIndicator(
                        color = Coral,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Erro",
                            tint = RiskCritical,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message,
                            style = Typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadHomeData(forceRefresh = true) },
                            colors = ButtonDefaults.buttonColors(containerColor = Coral)
                        ) {
                            Text("Tentar Novamente", color = Color.White)
                        }
                    }
                }
                is UiState.Success -> {
                    val data = state.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Olá, ${data.driverName}",
                                style = Typography.displayLarge,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = data.driverCompany,
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }

                        item {
                            Text(
                                text = "Alerta em Destaque",
                                style = Typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (data.urgentAlert != null) {
                                val borderAccent = when (data.urgentAlert.nivelRisco) {
                                    NivelRisco.CRITICAL -> RiskCritical
                                    NivelRisco.HIGH -> RiskHigh
                                    else -> RiskMedium
                                }

                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, borderAccent),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RiskBadge(nivelRisco = data.urgentAlert.nivelRisco)
                                            Text(
                                                text = "URGENTE",
                                                color = borderAccent,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                style = Typography.labelSmall
                                            )
                                        }

                                        Text(
                                            text = data.urgentAlert.title,
                                            style = Typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Text(
                                            text = "Ocorrência ativa nas proximidades da sua rota. Recomendamos atenção redobrada ou uso de rota alternativa.",
                                            style = Typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )

                                        Button(
                                            onClick = {
                                                navController.navigate(NavRoutes.detalheAlertaRoute(data.urgentAlert.id))
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = borderAccent),
                                            shape = RoundedCornerShape(999.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Ver Rota Alternativa", color = Color.White)
                                        }
                                    }
                                }
                            } else {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = RiskLow,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Nenhum alerta crítico ativo",
                                                style = Typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Todas as rotas principais estão liberadas.",
                                                style = Typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricCard(
                                    titulo = "Ativos",
                                    valor = data.activeAlertsCount.toString(),
                                    icone = Icons.Default.Notifications,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    titulo = "Críticos",
                                    valor = data.criticalAlertsCount.toString(),
                                    icone = Icons.Default.Warning,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    titulo = "Rotas OK",
                                    valor = data.safeRoutesCount.toString(),
                                    icone = Icons.Default.CheckCircle,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Alertas Recentes",
                                    style = Typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                                TextButton(
                                    onClick = {
                                        navController.navigate(NavRoutes.ALERTAS)
                                    }
                                ) {
                                    Text("Ver todos", color = Coral, style = Typography.labelSmall)
                                }
                            }
                        }

                        if (data.recentAlerts.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Nenhum alerta recente encontrado.",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        } else {
                            items(data.recentAlerts) { alerta ->
                                AlertCard(
                                    alerta = alerta,
                                    onClick = {
                                        navController.navigate(NavRoutes.detalheAlertaRoute(alerta.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

