package dam_A51811.filmroulette.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector

/** Represents a top-level destination in the bottom navigation bar. */
sealed class Screen(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    data object Roulette : Screen(
        route = "roulette",
        label = "Roulette",
        selectedIcon   = Icons.Filled.Bookmarks,   // replaced in BottomNavBar with custom painter
        unselectedIcon = Icons.Outlined.Bookmarks,
    )

    data object Filters : Screen(
        route = "filters",
        label = "Filters",
        selectedIcon   = Icons.Filled.Tune,
        unselectedIcon = Icons.Outlined.Tune,
    )

    data object Groups : Screen(
        route = "groups",
        label = "Groups",
        selectedIcon   = Icons.Filled.Group,
        unselectedIcon = Icons.Outlined.Group,
    )

    data object Watchlist : Screen(
        route = "watchlist",
        label = "Watchlist",
        selectedIcon   = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks,
    )
}

/** All bottom-nav destinations in display order. */
val bottomNavScreens = listOf(
    Screen.Roulette,
    Screen.Filters,
    Screen.Groups,
    Screen.Watchlist,
)
