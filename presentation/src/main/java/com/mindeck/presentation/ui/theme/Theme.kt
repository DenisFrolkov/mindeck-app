package com.mindeck.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R

private val LightColorScheme = lightColorScheme(
    primary = md_light_primary,
    onPrimary = md_light_onPrimary,
    primaryContainer = md_light_primaryContainer,
    onPrimaryContainer = md_light_onPrimaryContainer,
    secondary = md_light_secondary,
    onSecondary = md_light_onSecondary,
    secondaryContainer = md_light_secondaryContainer,
    onSecondaryContainer = md_light_onSecondaryContainer,
    tertiary = md_light_tertiary,
    onTertiary = md_light_onTertiary,
    tertiaryContainer = md_light_tertiaryContainer,
    onTertiaryContainer = md_light_onTertiaryContainer,
    error = md_light_error,
    onError = md_light_onError,
    errorContainer = md_light_errorContainer,
    onErrorContainer = md_light_onErrorContainer,
    background = md_light_background,
    onBackground = md_light_onBackground,
    surface = md_light_surface,
    onSurface = md_light_onSurface,
    surfaceVariant = md_light_surfaceVariant,
    onSurfaceVariant = md_light_onSurfaceVariant,
    outline = md_light_outline,
    outlineVariant = md_light_outlineVariant,
    scrim = md_light_scrim,
)

private val DarkColorScheme = darkColorScheme(
    primary = md_dark_primary,
    onPrimary = md_dark_onPrimary,
    primaryContainer = md_dark_primaryContainer,
    onPrimaryContainer = md_dark_onPrimaryContainer,
    secondary = md_dark_secondary,
    onSecondary = md_dark_onSecondary,
    secondaryContainer = md_dark_secondaryContainer,
    onSecondaryContainer = md_dark_onSecondaryContainer,
    tertiary = md_dark_tertiary,
    onTertiary = md_dark_onTertiary,
    tertiaryContainer = md_dark_tertiaryContainer,
    onTertiaryContainer = md_dark_onTertiaryContainer,
    error = md_dark_error,
    onError = md_dark_onError,
    errorContainer = md_dark_errorContainer,
    onErrorContainer = md_dark_onErrorContainer,
    background = md_dark_background,
    onBackground = md_dark_onBackground,
    surface = md_dark_surface,
    onSurface = md_dark_onSurface,
    surfaceVariant = md_dark_surfaceVariant,
    onSurfaceVariant = md_dark_onSurfaceVariant,
    outline = md_dark_outline,
    outlineVariant = md_dark_outlineVariant,
    scrim = md_dark_scrim,
)

@OptIn(ExperimentalTextApi::class)
private val Nunito = FontFamily(
    Font(
        resId = R.font.nunito_variable,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400)),
    ),
    Font(
        resId = R.font.nunito_variable,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500)),
    ),
    Font(
        resId = R.font.nunito_variable,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600)),
    ),
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
    ),
)

private val AppShapesOld = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

object MindeckTheme {
    val dimensions: AppDimensions
        @Composable
        get() = LocalDimensions.current

    val shapes: AppShapes
        @Composable
        get() = LocalShapes.current
}

@Composable
fun MindeckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        shapes = AppShapesOld,
    ) {
        CompositionLocalProvider(
            LocalDimensions provides AppDimensions(),
            LocalShapes provides AppShapes()
        ) {
            content()
        }
    }
}
