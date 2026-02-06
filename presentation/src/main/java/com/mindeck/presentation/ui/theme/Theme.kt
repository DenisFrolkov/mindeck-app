package com.mindeck.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R

private val LightColorScheme = lightColorScheme(
    primary = primary_blue,
    onPrimary = on_primary_white,
    secondary = secondary_light_blue,
    onSecondary = secondary_blue,
    tertiary = tertiary_lime_green,
    onTertiary = on_tertiary_lime_green,
    background = background_light_blue,
    error = error_red,
    onError = on_error_light_red,
    outline = outline_medium_gray,
    outlineVariant = outline_variant_blue,
    scrim = scrim_black,
)

private val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.opensans_medium)),
        fontSize = 16.sp,
        color = text_black,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.opensans_medium)),
        fontSize = 16.sp,
        color = text_black,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.opensans_medium)),
        fontSize = 14.sp,
        color = text_black,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.opensans_medium)),
        fontSize = 12.sp,
        color = text_black,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.opensans_medium)),
        fontSize = 8.sp,
        color = text_black,
    ),
)

private val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4),
    small = RoundedCornerShape(6),
    medium = RoundedCornerShape(10),
    large = RoundedCornerShape(12),
    extraLarge = RoundedCornerShape(50),
)

@Composable
fun MindeckTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
