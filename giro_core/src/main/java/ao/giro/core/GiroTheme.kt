package ao.giro.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta Estrita Uber
val UberBlack = Color(0xFF000000)
val UberDarkSurface = Color(0xFF121212)
val UberDarkCard = Color(0xFF1E1E1E)
val UberDarkBorder = Color(0xFF2C2C2C)
val UberGreen = Color(0xFF06C167) // Confirmação / Online
val UberEmergencyRed = Color(0xFFE11900) // Alertas / SOS Pânico
val UberWhite = Color(0xFFFFFFFF)
val UberGrayText = Color(0xFFAFAFAF)
val UberLightGrayBg = Color(0xFFF6F6F6)

private val GiroDarkColorScheme = darkColorScheme(
    primary = UberWhite,
    onPrimary = UberBlack,
    secondary = UberGreen,
    onSecondary = UberBlack,
    error = UberEmergencyRed,
    onError = UberWhite,
    background = UberBlack,
    onBackground = UberWhite,
    surface = UberDarkSurface,
    onSurface = UberWhite
)

@Composable
fun GiroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GiroDarkColorScheme,
        content = content
    )
}
