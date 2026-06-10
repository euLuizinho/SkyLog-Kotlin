package br.com.fiap.skylog.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Coral,
    background = DarkBg,
    surface = DarkCard,
    onPrimary = Color.White,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Coral,
    background = Sand,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Charcoal,
    onSurface = Charcoal,
    outline = GrayLight
)

@Composable
fun SkyLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
