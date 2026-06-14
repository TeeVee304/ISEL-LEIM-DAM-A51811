package dam_A51811.filmroulette.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemePreference {
    SYSTEM, LIGHT, DARK
}

enum class LanguagePreference {
    SYSTEM, EN, PT
}

class SettingsRepository(private val context: Context) {
    private val THEME_KEY    = stringPreferencesKey("theme_preference")
    private val LANGUAGE_KEY = stringPreferencesKey("language_preference")

    val themePreferenceFlow: Flow<ThemePreference> = context.dataStore.data.map { preferences ->
        val themeString = preferences[THEME_KEY] ?: ThemePreference.DARK.name
        try {
            ThemePreference.valueOf(themeString)
        } catch (e: IllegalArgumentException) {
            ThemePreference.DARK
        }
    }

    val languagePreferenceFlow: Flow<LanguagePreference> = context.dataStore.data.map { preferences ->
        val langString = preferences[LANGUAGE_KEY] ?: LanguagePreference.SYSTEM.name
        try {
            LanguagePreference.valueOf(langString)
        } catch (e: IllegalArgumentException) {
            LanguagePreference.SYSTEM
        }
    }

    suspend fun setThemePreference(themePreference: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themePreference.name
        }
    }

    suspend fun setLanguagePreference(languagePreference: LanguagePreference) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languagePreference.name
        }
    }
}
