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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dam_A51811.filmroulette.FilmRouletteApplication
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideViewModel
import dam_A51811.filmroulette.ui.screens.roulette.RouletteViewModel
import dam_A51811.filmroulette.ui.components.FilmRouletteBottomBar
import dam_A51811.filmroulette.ui.components.FilmRouletteTopBar
import dam_A51811.filmroulette.ui.screens.aiguide.AiGuideScreen
import dam_A51811.filmroulette.ui.screens.filters.FiltersScreen
import dam_A51811.filmroulette.ui.screens.groups.GroupSessionScreen
import dam_A51811.filmroulette.ui.screens.groups.GroupSessionViewModel
import dam_A51811.filmroulette.ui.screens.roulette.RouletteScreen
import dam_A51811.filmroulette.ui.screens.settings.SettingsScreen
import dam_A51811.filmroulette.ui.screens.settings.SettingsViewModel
import dam_A51811.filmroulette.ui.screens.watchlist.WatchlistScreen
import dam_A51811.filmroulette.ui.screens.watchlist.WatchlistViewModel
import dam_A51811.filmroulette.ui.screens.auth.AuthViewModel
import dam_A51811.filmroulette.ui.screens.auth.LoginScreen
import dam_A51811.filmroulette.ui.screens.auth.RegisterScreen
import dam_A51811.filmroulette.ui.screens.profile.ProfileScreen
import dam_A51811.filmroulette.ui.screens.profile.ProfileViewModel
import androidx.compose.runtime.collectAsState


/**
 * Sets up the main navigation graph for the application.
 * Manages the authentication state and switches between the authentication flow and the main app flow.
 */
@Composable
fun FilmRouletteApp() {
    val context = LocalContext.current.applicationContext as FilmRouletteApplication
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(
            authRepository = context.authRepository,
            watchlistRepository = context.watchlistRepository
        )
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
        val watchlistViewModel: WatchlistViewModel = viewModel(
            factory = WatchlistViewModel.Factory(
                watchlistRepository = context.watchlistRepository,
                authRepository = context.authRepository,
                friendshipRepository = context.friendshipRepository
            )
        )
        val groupSessionViewModel: GroupSessionViewModel = viewModel(
            factory = GroupSessionViewModel.Factory(
                repository = context.groupSessionRepository,
                authRepository = context.authRepository,
                movieRepository = context.movieRepository,
                friendshipRepository = context.friendshipRepository
            )
        )

        Scaffold(
            topBar = {
                FilmRouletteTopBar(
                    avatarUrl = null,
                    onExitClick = { authViewModel.logout() },
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
                    RouletteScreen(
                        viewModel = rouletteViewModel, 
                        aiGuideViewModel = aiGuideViewModel,
                        watchlistViewModel = watchlistViewModel,
                        groupSessionViewModel = groupSessionViewModel
                    )
                }
                composable(Screen.Filters.route) {
                    FiltersScreen(
                        groupSessionViewModel = groupSessionViewModel,
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
                composable(
                    route = Screen.Groups.route + "?sessionId={sessionId}",
                    deepLinks = listOf(navDeepLink { uriPattern = "https://filmroulette.com/join/{sessionId}" }),
                    arguments = listOf(navArgument("sessionId") { nullable = true })
                ) { backStackEntry ->
                    val sessionId = backStackEntry.arguments?.getString("sessionId")
                    LaunchedEffect(sessionId) {
                        if (sessionId != null) {
                            groupSessionViewModel.joinSession(sessionId)
                        }
                    }
                    GroupSessionScreen(
                        viewModel = groupSessionViewModel,
                        onNavigateToRoulette = {
                            navController.navigate(Screen.Roulette.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Screen.Watchlist.route) {
                    WatchlistScreen(viewModel = watchlistViewModel)
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
