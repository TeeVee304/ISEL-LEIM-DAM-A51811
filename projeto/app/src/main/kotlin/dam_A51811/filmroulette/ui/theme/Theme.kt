package dam_A51811.filmroulette.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

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

/**
 * Top-level theme composable for the entire FilmRoulette app.
 *
 * Always renders in dark mode — no light variant is provided.
 */
@Composable
fun FilmRouletteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FilmRouletteDarkColorScheme,
        typography  = FilmRouletteTypography,
        content     = content,
    )
}
