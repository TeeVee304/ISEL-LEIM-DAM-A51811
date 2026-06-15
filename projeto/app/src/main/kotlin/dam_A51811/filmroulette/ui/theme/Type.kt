package dam_A51811.filmroulette.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dam_A51811.filmroulette.R


/**
 * Font family defining the Spline Sans typeface used for headings and displays.
 */
val SplineSans = FontFamily(
    Font(R.font.spline_sans_light,    FontWeight.Light),
    Font(R.font.spline_sans_regular,  FontWeight.Normal),
    Font(R.font.spline_sans_medium,   FontWeight.Medium),
    Font(R.font.spline_sans_semibold, FontWeight.SemiBold),
    Font(R.font.spline_sans_bold,     FontWeight.Bold),
    
    Font(R.font.spline_sans_bold,     FontWeight.ExtraBold),
    Font(R.font.spline_sans_bold,     FontWeight.Black),
)


/**
 * Font family defining the Be Vietnam Pro typeface used for body text and labels.
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
 * Typography scale for the FilmRoulette application, mapping predefined text styles
 * to the Material Design typography system.
 */
val FilmRouletteTypography = Typography(
    
    displayLarge = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.96).sp,   
    ),
    
    headlineLarge = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.32).sp,   
    ),
    
    headlineMedium = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    
    headlineSmall = TextStyle(
        fontFamily = SplineSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    
    bodyLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
    ),
    
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
    
    labelLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.7.sp,   
    ),
    
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
