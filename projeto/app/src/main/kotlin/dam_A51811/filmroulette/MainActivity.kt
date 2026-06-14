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

class MainActivity : ComponentActivity() {
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
            val updatedContext = context.createConfigurationContext(configuration)

            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.ui.platform.LocalContext provides updatedContext,
                androidx.compose.ui.platform.LocalConfiguration provides configuration
            ) {
                FilmRouletteTheme(themePreference = themePreference) {
                    FilmRouletteApp()
                }
            }
        }
    }
}