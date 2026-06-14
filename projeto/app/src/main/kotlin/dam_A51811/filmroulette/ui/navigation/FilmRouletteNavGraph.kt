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
import dam_A51811.filmroulette.data.ui.aiguide.AiGuideViewModel
import dam_A51811.filmroulette.data.ui.roulette.RouletteViewModel
import dam_A51811.filmroulette.ui.components.FilmRouletteBottomBar
import dam_A51811.filmroulette.ui.components.FilmRouletteTopBar
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideScreen
import dam_A51811.filmroulette.ui.screens.filters.FiltersScreen
import dam_A51811.filmroulette.ui.screens.groups.GroupSessionScreen
import dam_A51811.filmroulette.ui.screens.roulette.RouletteScreen
import dam_A51811.filmroulette.ui.screens.settings.SettingsScreen
import dam_A51811.filmroulette.ui.screens.settings.SettingsViewModel
import dam_A51811.filmroulette.ui.screens.watchlist.WatchlistScreen
import dam_A51811.filmroulette.ui.screens.auth.AuthViewModel
import dam_A51811.filmroulette.ui.screens.auth.LoginScreen
import dam_A51811.filmroulette.ui.screens.auth.RegisterScreen
import dam_A51811.filmroulette.ui.screens.profile.ProfileScreen
import dam_A51811.filmroulette.ui.screens.profile.ProfileViewModel
import androidx.compose.runtime.collectAsState

/**
 * Root composable — sets up the [Scaffold] with the shared [FilmRouletteTopBar]
 * and [FilmRouletteBottomBar], then hosts the navigation graph for all four
 * top-level destinations.
 */
@Composable
fun FilmRouletteApp() {
    val context = LocalContext.current.applicationContext as FilmRouletteApplication
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(context.authRepository)
    )
    val currentUser by context.authRepository.currentUserFlow.collectAsState(
        initial = context.authRepository.getCurrentUser()
    )

    if (currentUser == null) {
        val authNavController = rememberNavController()
        NavHost(
            navController = authNavController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { authNavController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = { authNavController.navigate(Screen.Login.route) }
                )
            }
        }
    } else {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val rouletteViewModel: RouletteViewModel = viewModel(
            factory = RouletteViewModel.Factory(context.movieRepository)
        )
        val aiGuideViewModel: AiGuideViewModel = viewModel(
            factory = AiGuideViewModel.Factory(context.aiAssistant)
        )
        val settingsViewModel: SettingsViewModel = viewModel(
            factory = SettingsViewModel.Factory(context.settingsRepository)
        )

        Scaffold(
            topBar = {
                FilmRouletteTopBar(
                    avatarUrl = null,
                    onMenuClick = { /* open drawer in a later iteration */ },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) }
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
                    RouletteScreen(viewModel = rouletteViewModel, aiGuideViewModel = aiGuideViewModel)
                }
                composable(Screen.Filters.route) {
                    FiltersScreen(
                        onStartRoulette = { duration, genres, languages, dateGte, dateLte, providerIds ->
                            rouletteViewModel.loadRecommendations(duration, genres, languages, dateGte, dateLte, providerIds)
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
                composable(Screen.Profile.route) {
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ProfileViewModel.Factory(
                            authRepository = context.authRepository,
                            friendshipRepository = context.friendshipRepository
                        )
                    )
                    ProfileScreen(viewModel = profileViewModel)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        onLogout = { authViewModel.logout() },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
