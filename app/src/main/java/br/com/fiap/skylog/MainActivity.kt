package br.com.fiap.skylog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import br.com.fiap.skylog.core.theme.SkyLogTheme
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.presentation.navigation.AppNavigation
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(preferencesRepository.isDarkMode()) }

            SkyLogTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { enabled ->
                            preferencesRepository.setDarkMode(enabled)
                            isDarkMode = enabled
                        }
                    )
                }
            }
        }
    }
}
