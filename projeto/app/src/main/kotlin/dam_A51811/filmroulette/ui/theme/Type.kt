package dam_A51811.filmroulette.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.R

/**
 * "Spline Sans" — used for display text, headlines, and the app logo.
 * Weights: Light(300), Regular(400), Medium(500), SemiBold(600), Bold(700), ExtraBold(800), Black(900)
 */
val SplineSans = FontFamily(
    Font(R.font.spline_sans_light,    FontWeight.Light),
    Font(R.font.spline_sans_regular,  FontWeight.Normal),
    Font(R.font.spline_sans_medium,   FontWeight.Medium),
    Font(R.font.spline_sans_semibold, FontWeight.SemiBold),
    Font(R.font.spline_sans_bold,     FontWeight.Bold),
    // Spline Sans tops out at 700 — Bold is used for all heavier-weight slots
    Font(R.font.spline_sans_bold,     FontWeight.ExtraBold),
    Font(R.font.spline_sans_bold,     FontWeight.Black),
)

/**
 * "Be Vietnam Pro" — used for body copy, labels, and captions.
 * Weights: Thin(100), ExtraLight(200), Light(300), Regular(400), Medium(500), SemiBold(600), Bold(700), ExtraBold(800), Black(900)
 */
val BeVietnamPro = FontFamily(
    Font(R.font.be_vietnam_pro_thin,       FontWeight.Thin),
    Font(R.font.be_vietnam_pro_extralight, FontWeight.ExtraLight),
    Font(R.font.be_vietnam_pro_light,      FontWeight.Light),
    Font(R.font.be_vietnam_pro_regular,    FontWeight.Normal),
    Font(R.font.be_vietnam_pro_medium,     FontWeight.Medium),
    Font(R.font.be_vietnam_pro_semibold,   FontWeight.SemiBold),
    Font(R.font.be_vietnam_pro_bold,       FontWeight.Bold),
    Font(R.font.be_vietnam_pro_extrabold,  FontWeight.ExtraBold),
    Font(R.font.be_vietnam_pro_black,      FontWeight.Black),
)

/**
 * FilmRoulette typography scale, mapped onto Material 3 slots.
 *
 * | Design token  | M3 slot          | Family        | Size | Weight    |
 * |---------------|------------------|---------------|------|-----------|
 * | headline-xl   | displayLarge     | Spline Sans   | 48sp | ExtraBold |
 * | headline-lg   | headlineLarge    | Spline Sans   | 32sp | Bold      |
 * | headline-md   | headlineMedium   | Spline Sans   | 24sp | Bold      |
 * | body-lg       | bodyLarge        | Be Vietnam Pro| 18sp | Normal    |
 * | body-md       | bodyMedium       | Be Vietnam Pro| 16sp | Normal    |
 * | label-lg      | labelLarge       | Be Vietnam Pro| 14sp | SemiBold  |
 * | label-md      | labelMedium      | Be Vietnam Pro| 12sp | Medium    |
 */
val FilmRouletteTypography = Typography(
    // headline-xl → displayLarge
    displayLarge = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.96).sp,   // ≈ −0.02em at 48sp
    ),
    // headline-lg → headlineLarge
    headlineLarge = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.32).sp,   // ≈ −0.01em
    ),
    // headline-md → headlineMedium
    headlineMedium = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    // headline-sm → headlineSmall (general usage)
    headlineSmall = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    // body-lg → bodyLarge
    bodyLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
    ),
    // body-md → bodyMedium
    bodyMedium = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // label-lg → labelLarge
    labelLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.7.sp,   // ≈ +0.05em
    ),
    // label-md → labelMedium
    labelMedium = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.5.sp,
    ),
)
