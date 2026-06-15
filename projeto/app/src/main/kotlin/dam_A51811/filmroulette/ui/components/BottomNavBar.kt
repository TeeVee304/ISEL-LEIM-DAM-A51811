package dam_A51811.filmroulette.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MovieFilter
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.ui.navigation.Screen
import dam_A51811.filmroulette.ui.theme.NeonRed
import dam_A51811.filmroulette.ui.theme.SplineSans

private data class NavItem(
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val labelResId: Int,
)

private val navItems = listOf(
    NavItem(Screen.Roulette,  Icons.Filled.MovieFilter,  Icons.Outlined.MovieFilter,  R.string.nav_roulette),
    NavItem(Screen.Filters,   Icons.Filled.Tune,          Icons.Outlined.Tune,          R.string.nav_filters),
    NavItem(Screen.Groups,    Icons.Filled.Group,          Icons.Outlined.Group,         R.string.nav_groups),
    NavItem(Screen.Watchlist, Icons.Filled.Bookmarks,      Icons.Outlined.Bookmarks,     R.string.nav_watchlist),
    NavItem(Screen.Profile,   Icons.Filled.Person,         Icons.Outlined.Person,        R.string.nav_profile),
)


/**
 * A bottom navigation bar for the FilmRoulette application.
 *
 * @param currentRoute The currently active navigation route.
 * @param onNavigate Callback invoked when a navigation item is selected.
 * @param modifier The modifier to be applied to the navigation bar.
 */
@Composable
fun FilmRouletteBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier
            .shadow(
                elevation = 24.dp,
                ambientColor = NeonRed.copy(alpha = 0.15f),
                spotColor   = NeonRed.copy(alpha = 0.10f),
            )
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
        containerColor      = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
        contentColor        = MaterialTheme.colorScheme.onSurface,
        windowInsets        = WindowInsets.navigationBars,
        tonalElevation      = 0.dp,
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.screen.route
            val label = stringResource(id = item.labelResId)
            NavigationBarItem(
                selected = selected,
                onClick  = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = label,
                    )
                },
                label = {
                    Text(
                        text       = label.uppercase(),
                        fontFamily = SplineSans,
                        fontWeight = FontWeight.Black,
                        fontSize   = 8.sp,
                        letterSpacing = 1.sp,
                        textAlign  = TextAlign.Center,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = NeonRed,
                    selectedTextColor   = NeonRed,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f),
                    indicatorColor      = Color.Transparent,
                ),
            )
        }
    }
}
