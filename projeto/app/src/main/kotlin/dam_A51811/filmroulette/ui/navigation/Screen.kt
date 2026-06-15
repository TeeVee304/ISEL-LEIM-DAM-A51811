package dam_A51811.filmroulette.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector


/**
 * Represents a screen within the application's navigation graph.
 *
 * @param route The unique route identifier for the screen.
 * @param label The display label for the screen.
 * @param selectedIcon The icon to display when the screen is selected.
 * @param unselectedIcon The icon to display when the screen is not selected.
 */
sealed class Screen(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    data object Roulette : Screen(
        route = "roulette",
        label = "Roulette",
        selectedIcon   = Icons.Filled.Bookmarks,   
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

    data object AiGuide : Screen(
        route = "ai_guide",
        label = "AI Guide",
        selectedIcon   = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
    )

    data object Profile : Screen(
        route = "profile",
        label = "Profile",
        selectedIcon   = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    )

    data object Settings : Screen(
        route = "settings",
        label = "Settings",
        selectedIcon   = Icons.Filled.AutoAwesome, 
        unselectedIcon = Icons.Outlined.AutoAwesome,
    )

    data object Login : Screen(
        route = "login",
        label = "Login",
        selectedIcon   = Icons.Filled.AutoAwesome, 
        unselectedIcon = Icons.Outlined.AutoAwesome,
    )

    data object Register : Screen(
        route = "register",
        label = "Register",
        selectedIcon   = Icons.Filled.AutoAwesome, 
        unselectedIcon = Icons.Outlined.AutoAwesome,
    )
}


/**
 * A list of screens that are displayed in the bottom navigation bar.
 */
val bottomNavScreens = listOf(
    Screen.Roulette,
    Screen.Filters,
    Screen.Groups,
    Screen.Watchlist,
    Screen.Profile,
)
