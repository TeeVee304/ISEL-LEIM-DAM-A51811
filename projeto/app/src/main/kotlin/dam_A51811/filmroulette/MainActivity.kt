package dam_A51811.filmroulette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dam_A51811.filmroulette.ui.navigation.FilmRouletteApp
import dam_A51811.filmroulette.ui.theme.FilmRouletteTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dam_A51811.filmroulette.data.repository.ThemePreference
import kotlinx.coroutines.launch

/**
 * Main entry point of the Film Roulette application.
 * Sets up the content view, configures the theme and language preferences, and initializes the root composable.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     * Applies theme and locale configurations based on user settings before setting the content.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as FilmRouletteApplication
            val themePreference by app.settingsRepository.themePreferenceFlow.collectAsState(initial = ThemePreference.SYSTEM)
            val languagePreference by app.settingsRepository.languagePreferenceFlow.collectAsState(initial = dam_A51811.filmroulette.data.repository.LanguagePreference.SYSTEM)

            val locale = if (languagePreference == dam_A51811.filmroulette.data.repository.LanguagePreference.SYSTEM) {
                val systemLang = java.util.Locale.getDefault().language
                if (systemLang == "pt") java.util.Locale("pt") else java.util.Locale("en")
            } else {
                java.util.Locale(languagePreference.name.lowercase())
            }

            val configuration = android.content.res.Configuration(androidx.compose.ui.platform.LocalConfiguration.current)
            configuration.setLocale(locale)
            
            val context = androidx.compose.ui.platform.LocalContext.current
            val configurationContext = context.createConfigurationContext(configuration)
            val updatedContext = object : android.content.ContextWrapper(context) {
                override fun getResources(): android.content.res.Resources {
                    return configurationContext.resources
                }
            }

            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.ui.platform.LocalContext provides updatedContext,
                androidx.compose.ui.platform.LocalConfiguration provides configuration,
                androidx.activity.compose.LocalActivityResultRegistryOwner provides this
            ) {
                FilmRouletteTheme(themePreference = themePreference) {
                    FilmRouletteApp()
                }
            }
        }
    }

    /**
     * Called when the activity is destroyed.
     * If the activity is finishing (meaning the user is explicitly closing it or pressing back to exit),
     * we attempt to remove the user from any active group sessions as a cleanup measure.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            val app = application as FilmRouletteApplication
            val currentUser = app.authRepository.getCurrentUser()
            if (currentUser != null) {
                // Launch on IO dispatcher since this is a fire-and-forget network call 
                // while the activity is dying.
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        app.groupSessionRepository.leaveAllSessionsForUser(currentUser.uid)
                    } catch (e: Exception) {
                        // Silently ignore errors during app teardown
                    }
                }
            }
        }
    }
}