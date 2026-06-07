package com.mindeck.core.ui.theme

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.nunito_variable
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
        surfaceDim = md_light_surfaceDim,
        surfaceBright = md_light_surfaceBright,
        surfaceContainerLowest = md_light_surfaceContainerLowest,
        surfaceContainerLow = md_light_surfaceContainerLow,
        surfaceContainer = md_light_surfaceContainer,
        surfaceContainerHigh = md_light_surfaceContainerHigh,
        surfaceContainerHighest = md_light_surfaceContainerHighest,
        outline = md_light_outline,
        outlineVariant = md_light_outlineVariant,
        inverseSurface = md_light_inverseSurface,
        inverseOnSurface = md_light_inverseOnSurface,
        inversePrimary = md_light_inversePrimary,
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
        surfaceDim = md_dark_surfaceDim,
        surfaceBright = md_dark_surfaceBright,
        surfaceContainerLowest = md_dark_surfaceContainerLowest,
        surfaceContainerLow = md_dark_surfaceContainerLow,
        surfaceContainer = md_dark_surfaceContainer,
        surfaceContainerHigh = md_dark_surfaceContainerHigh,
        surfaceContainerHighest = md_dark_surfaceContainerHighest,
        outline = md_dark_outline,
        outlineVariant = md_dark_outlineVariant,
        inverseSurface = md_dark_inverseSurface,
        inverseOnSurface = md_dark_inverseOnSurface,
        inversePrimary = md_dark_inversePrimary,
        scrim = md_dark_scrim,
    )

@Composable
private fun nunitoFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.nunito_variable, weight = FontWeight.Normal),
        Font(Res.font.nunito_variable, weight = FontWeight.Medium),
        Font(Res.font.nunito_variable, weight = FontWeight.SemiBold),
        Font(Res.font.nunito_variable, weight = FontWeight.Bold),
        Font(Res.font.nunito_variable, weight = FontWeight.ExtraBold),
    )

@Composable
private fun appTypography(): Typography {
    val nunito = nunitoFontFamily()
    return Typography(
        displayLarge =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
    )
}

private val AppShapes =
    Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(28.dp),
    )

object MindeckTheme {
    val dimensions: AppDimensions
        @Composable
        get() = LocalDimensions.current

    val shapes: SpecialAppShapes
        @Composable
        get() = LocalShapes.current

    val extraColors: AppExtraColors
        @Composable get() = LocalExtraColors.current
}

@Composable
fun MindeckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = appTypography(),
        shapes = AppShapes,
    ) {
        CompositionLocalProvider(
            LocalDimensions provides AppDimensions(),
            LocalShapes provides SpecialAppShapes(),
            LocalExtraColors provides if (darkTheme) DarkExtraColors else LightExtraColors,
        ) {
            content()
        }
    }
}
