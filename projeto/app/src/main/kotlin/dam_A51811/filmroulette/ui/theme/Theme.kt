package dam_A51811.filmroulette.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import dam_A51811.filmroulette.data.repository.ThemePreference

private val FilmRouletteDarkColorScheme = darkColorScheme(
    primary              = Primary,
    onPrimary            = OnPrimary,
    primaryContainer     = PrimaryContainer,
    onPrimaryContainer   = OnPrimaryContainer,
    inversePrimary       = InversePrimary,

    secondary            = Secondary,
    onSecondary          = OnSecondary,
    secondaryContainer   = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary             = Tertiary,
    onTertiary           = OnTertiary,
    tertiaryContainer    = TertiaryContainer,
    onTertiaryContainer  = OnTertiaryContainer,

    error                = Error,
    onError              = OnError,
    errorContainer       = ErrorContainer,
    onErrorContainer     = OnErrorContainer,

    background           = Background,
    onBackground         = OnBackground,

    surface              = Surface,
    onSurface            = OnSurface,
    surfaceVariant       = SurfaceVariant,
    onSurfaceVariant     = OnSurfaceVariant,
    inverseSurface       = InverseSurface,
    inverseOnSurface     = InverseOnSurface,

    outline              = Outline,
    outlineVariant       = OutlineVariant,

    surfaceTint          = Primary,
)

private val FilmRouletteLightColorScheme = lightColorScheme(
    primary              = LightPrimary,
    onPrimary            = LightOnPrimary,
    primaryContainer     = LightPrimaryContainer,
    onPrimaryContainer   = LightOnPrimaryContainer,
    inversePrimary       = InversePrimary,

    secondary            = LightSecondary,
    onSecondary          = LightOnSecondary,
    secondaryContainer   = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary             = LightTertiary,
    onTertiary           = LightOnTertiary,
    tertiaryContainer    = LightTertiaryContainer,
    onTertiaryContainer  = LightOnTertiaryContainer,

    error                = LightError,
    onError              = LightOnError,
    errorContainer       = LightErrorContainer,
    onErrorContainer     = LightOnErrorContainer,

    background           = LightBackground,
    onBackground         = LightOnBackground,

    surface              = LightSurface,
    onSurface            = LightOnSurface,
    surfaceVariant       = LightSurfaceVariant,
    onSurfaceVariant     = LightOnSurfaceVariant,
    inverseSurface       = LightInverseSurface,
    inverseOnSurface     = LightInverseOnSurface,

    outline              = LightOutline,
    outlineVariant       = LightOutlineVariant,

    surfaceTint          = LightPrimary,
)


/**
 * Applies the FilmRoulette design system theme to the provided composable content.
 *
 * @param themePreference The preferred theme mode (light, dark, or system default).
 * @param content The composable content to be displayed within the theme.
 */
@Composable
fun FilmRouletteTheme(
    themePreference: ThemePreference = ThemePreference.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themePreference) {
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
        ThemePreference.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) {
        FilmRouletteDarkColorScheme
    } else {
        FilmRouletteLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = FilmRouletteTypography,
        content     = content,
    )
}
