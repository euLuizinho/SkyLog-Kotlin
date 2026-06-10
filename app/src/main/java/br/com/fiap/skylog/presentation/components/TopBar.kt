package br.com.fiap.skylog.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.skylog.R
import br.com.fiap.skylog.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onToggleDarkMode: () -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(4.dp),
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
                    style = Typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Coral,
                    letterSpacing = (-0.03).sp
                )
            }
        },
        actions = {
            IconButton(onClick = onToggleDarkMode) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Alternar Tema",
                    tint = Coral
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}
