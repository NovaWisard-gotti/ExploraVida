package com.educalab.exploravida.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val scheme = darkColorScheme(
    primary = LabColors.Lime,
    onPrimary = LabColors.Ink,
    secondary = LabColors.Sky,
    onSecondary = LabColors.Ink,
    tertiary = LabColors.Amber,
    onTertiary = LabColors.Ink,
    background = LabColors.Deep,
    onBackground = LabColors.Paper,
    surface = LabColors.Glass,
    onSurface = LabColors.Paper,
    surfaceVariant = LabColors.GlassSoft,
    onSurfaceVariant = LabColors.Sand,
    error = LabColors.Coral,
    onError = LabColors.Ink
)

private val lightScheme = lightColorScheme(
    primary = LabColors.Deep,
    onPrimary = LabColors.Paper,
    background = LabColors.Paper,
    onBackground = LabColors.Ink,
    surface = LabColors.Sand,
    onSurface = LabColors.Ink
)

private val exploraTypography = Typography(
    displaySmall = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp),
    headlineMedium = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold),
    headlineSmall = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 23.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.4.sp),
    labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp)
)

@Composable
fun ExploraVidaTheme(
    forceDarkLab: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (forceDarkLab || isSystemInDarkTheme()) scheme else lightScheme
    MaterialTheme(colorScheme = colors, typography = exploraTypography, content = content)
}
