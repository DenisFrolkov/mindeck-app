package com.mindeck.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mindeck_app.ui.generated.resources.Res
import mindeck_app.ui.generated.resources.nunito_variable
import org.jetbrains.compose.resources.Font

private val LightColorScheme =
    lightColorScheme(
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

private val DarkColorScheme =
    darkColorScheme(
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

@Composable
private fun nunitoFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.nunito_variable, weight = FontWeight.Normal),
        Font(Res.font.nunito_variable, weight = FontWeight.Medium),
        Font(Res.font.nunito_variable, weight = FontWeight.SemiBold),
        Font(Res.font.nunito_variable, weight = FontWeight.Bold),
    )

@Composable
private fun appTypography(): Typography {
    val nunito = nunitoFontFamily()
    return Typography(
        titleLarge = TextStyle(fontFamily = nunito, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
        titleMedium = TextStyle(fontFamily = nunito, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        bodyLarge = TextStyle(fontFamily = nunito, fontWeight = FontWeight.Medium, fontSize = 16.sp),
        bodyMedium = TextStyle(fontFamily = nunito, fontWeight = FontWeight.Medium, fontSize = 14.sp),
        bodySmall = TextStyle(fontFamily = nunito, fontWeight = FontWeight.Normal, fontSize = 12.sp),
        labelMedium = TextStyle(fontFamily = nunito, fontWeight = FontWeight.Normal, fontSize = 12.sp),
        labelSmall = TextStyle(fontFamily = nunito, fontWeight = FontWeight.Normal, fontSize = 11.sp),
    )
}

private val AppShapesOld =
    Shapes(
        extraSmall = RoundedCornerShape(AppDimensions().dp4),
        small = RoundedCornerShape(AppDimensions().dp6),
        medium = RoundedCornerShape(AppDimensions().dp10),
        large = RoundedCornerShape(AppDimensions().dp12),
        extraLarge = RoundedCornerShape(AppDimensions().dp28),
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
        typography = appTypography(),
        shapes = AppShapesOld,
    ) {
        CompositionLocalProvider(
            LocalDimensions provides AppDimensions(),
            LocalShapes provides AppShapes(),
        ) {
            content()
        }
    }
}
