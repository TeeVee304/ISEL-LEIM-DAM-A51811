package dam_A51811.filmroulette.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore instance for managing application settings preferences.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Represents the user's preferred theme for the application.
 */
enum class ThemePreference {
    SYSTEM, LIGHT, DARK
}

/**
 * Represents the user's preferred language for the application.
 */
enum class LanguagePreference {
    SYSTEM, EN, PT
}

/**
 * Repository responsible for managing application settings, such as theme and language preferences.
 *
 * @param context The application context used to access DataStore.
 */
class SettingsRepository(private val context: Context) {
    private val THEME_KEY    = stringPreferencesKey("theme_preference")
    private val LANGUAGE_KEY = stringPreferencesKey("language_preference")

    /**
     * A flow emitting the current theme preference. Defaults to DARK if not set or invalid.
     */
    val themePreferenceFlow: Flow<ThemePreference> = context.dataStore.data.map { preferences ->
        val themeString = preferences[THEME_KEY] ?: ThemePreference.DARK.name
        try {
            ThemePreference.valueOf(themeString)
        } catch (e: IllegalArgumentException) {
            ThemePreference.DARK
        }
    }

    /**
     * A flow emitting the current language preference. Defaults to SYSTEM if not set or invalid.
     */
    val languagePreferenceFlow: Flow<LanguagePreference> = context.dataStore.data.map { preferences ->
        val langString = preferences[LANGUAGE_KEY] ?: LanguagePreference.SYSTEM.name
        try {
            LanguagePreference.valueOf(langString)
        } catch (e: IllegalArgumentException) {
            LanguagePreference.SYSTEM
        }
    }

    /**
     * Updates the application theme preference in DataStore.
     *
     * @param themePreference The new theme preference to save.
     */
    suspend fun setThemePreference(themePreference: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themePreference.name
        }
    }

    /**
     * Updates the application language preference in DataStore.
     *
     * @param languagePreference The new language preference to save.
     */
    suspend fun setLanguagePreference(languagePreference: LanguagePreference) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languagePreference.name
        }
    }
}
