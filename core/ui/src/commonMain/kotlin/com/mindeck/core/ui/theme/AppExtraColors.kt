package com.mindeck.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtraColors = staticCompositionLocalOf<AppExtraColors> {
    error("No AppExtraColors provided")
}

val LightExtraColors = AppExtraColors(
    ratingAgainBackground = rating_again_background_light,
    ratingAgainOn = rating_again_on_light,
    ratingHardBackground = rating_hard_background_light,
    ratingHardOn = rating_hard_on_light,
    ratingGoodBackground = rating_good_background_light,
    ratingGoodOn = rating_good_on_light,
    ratingEasyBackground = rating_easy_background_light,
    ratingEasyOn = rating_easy_on_light,
)

val DarkExtraColors = AppExtraColors(
    ratingAgainBackground = rating_again_background_dark,
    ratingAgainOn = rating_again_on_dark,
    ratingHardBackground = rating_hard_background_dark,
    ratingHardOn = rating_hard_on_dark,
    ratingGoodBackground = rating_good_background_dark,
    ratingGoodOn = rating_good_on_dark,
    ratingEasyBackground = rating_easy_background_dark,
    ratingEasyOn = rating_easy_on_dark,
)

data class AppExtraColors(
    val ratingAgainBackground: Color,
    val ratingAgainOn: Color,
    val ratingHardBackground: Color,
    val ratingHardOn: Color,
    val ratingGoodBackground: Color,
    val ratingGoodOn: Color,
    val ratingEasyBackground: Color,
    val ratingEasyOn: Color,
)
