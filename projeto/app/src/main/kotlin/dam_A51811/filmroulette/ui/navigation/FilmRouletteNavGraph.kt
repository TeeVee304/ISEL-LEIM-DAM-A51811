package dam_A51811.filmroulette.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_A51811.filmroulette.FilmRouletteApplication
import dam_A51811.filmroulette.data.ui.roulette.RouletteViewModel
import dam_A51811.filmroulette.ui.components.FilmRouletteBottomBar
import dam_A51811.filmroulette.ui.components.FilmRouletteTopBar
import dam_A51811.filmroulette.ui.screens.filters.FiltersScreen
import dam_A51811.filmroulette.ui.screens.groups.GroupSessionScreen
import dam_A51811.filmroulette.ui.screens.roulette.RouletteScreen
import dam_A51811.filmroulette.ui.screens.watchlist.WatchlistScreen

/**
 * Root composable — sets up the [Scaffold] with the shared [FilmRouletteTopBar]
 * and [FilmRouletteBottomBar], then hosts the navigation graph for all four
 * top-level destinations.
 */
@Composable
fun FilmRouletteApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val context = LocalContext.current.applicationContext as FilmRouletteApplication
    val rouletteViewModel: RouletteViewModel = viewModel(
        factory = RouletteViewModel.Factory(context.movieRepository)
    )

    Scaffold(
        topBar = {
            FilmRouletteTopBar(
                avatarUrl = null,
                onMenuClick = { /* open drawer in a later iteration */ },
            )
        },
        bottomBar = {
            FilmRouletteBottomBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Roulette.route,
            modifier         = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Roulette.route) {
                RouletteScreen(viewModel = rouletteViewModel)
            }
            composable(Screen.Filters.route) {
                FiltersScreen(
                    onStartRoulette = { duration, genres ->
                        rouletteViewModel.loadRecommendations(duration, genres)
                        navController.navigate(Screen.Roulette.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
            composable(Screen.Groups.route) {
                GroupSessionScreen()
            }
            composable(Screen.Watchlist.route) {
                WatchlistScreen()
            }
        }
    }
}
