package br.com.fiap.skylog.presentation.screens.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.skylog.core.theme.*
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.presentation.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    viewModel: PerfilViewModel = koinViewModel()
) {
    val name by viewModel.name.collectAsState()
    val company by viewModel.company.collectAsState()
    val limiar by viewModel.limiar.collectAsState()
    val favoritesCount by viewModel.favoritesCount.collectAsState()

    var isDropdownExpanded by remember { mutableStateOf(false) }
    val thresholds = listOf("BAIXO", "MÉDIO", "ALTO", "CRÍTICO")

    LaunchedEffect(isDarkMode) {
        viewModel.updateDarkMode(isDarkMode)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Configurações do Perfil", style = Typography.headlineMedium, color = Coral) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                    selected = false,
                    onClick = {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
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
                    selected = true,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Coral, indicatorColor = Color.Transparent),
                    onClick = {}
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Coral,
                            modifier = Modifier.size(64.dp)
                        )
                        Column {
                            Text(
                                text = "Identificação do Motorista",
                                style = Typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Dados exibidos nos relatórios de rota",
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("Nome do Motorista", style = Typography.bodyMedium) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Coral),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = company,
                        onValueChange = { viewModel.updateCompany(it) },
                        label = { Text("Empresa Logística", style = Typography.bodyMedium) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Coral),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RiskMedium,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = "Alertas Favoritados",
                            style = Typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$favoritesCount eventos salvos para acompanhamento",
                            style = Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Preferências do Aplicativo",
                        style = Typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = Coral)
                            Column {
                                Text(
                                    text = "Modo Escuro",
                                    style = Typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Alterar a paleta visual do app",
                                    style = Typography.bodyMedium,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onToggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Coral, checkedTrackColor = Coral.copy(alpha = 0.5f))
                        )
                    }

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.List, contentDescription = null, tint = Coral)
                            Column {
                                Text(
                                    text = "Limiar Mínimo de Alerta",
                                    style = Typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Exibir apenas riscos iguais ou maiores que o limiar",
                                    style = Typography.bodyMedium,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = limiar,
                                onValueChange = {},
                                readOnly = true,
                                shape = RoundedCornerShape(10.dp),
                                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Coral,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isDropdownExpanded = true }
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { isDropdownExpanded = true }
                            )

                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                thresholds.forEach { threshold ->
                                    DropdownMenuItem(
                                        text = { Text(threshold) },
                                        onClick = {
                                            viewModel.updateLimiar(threshold)
                                            isDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(NavRoutes.SPLASH) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RiskCritical),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Limpar Dados & Sair do App", color = Color.White)
            }
        }
    }
}
