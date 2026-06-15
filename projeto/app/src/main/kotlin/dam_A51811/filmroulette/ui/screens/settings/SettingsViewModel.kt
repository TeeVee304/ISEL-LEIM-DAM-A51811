package dam_A51811.filmroulette.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.repository.LanguagePreference
import dam_A51811.filmroulette.data.repository.SettingsRepository
import dam_A51811.filmroulette.data.repository.ThemePreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Manages the UI state and preferences for the settings screen.
 *
 * @property settingsRepository The repository used to access and mutate settings data.
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    /**
     * A state flow representing the user's current theme preference.
     */
    val themePreference: StateFlow<ThemePreference> = settingsRepository.themePreferenceFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemePreference.SYSTEM
        )

    /**
     * A state flow representing the user's current language preference.
     */
    val languagePreference: StateFlow<LanguagePreference> = settingsRepository.languagePreferenceFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LanguagePreference.SYSTEM
        )

    /**
     * Updates the application's theme preference.
     *
     * @param preference The new theme preference to be applied.
     */
    fun setThemePreference(preference: ThemePreference) {
        viewModelScope.launch {
            settingsRepository.setThemePreference(preference)
        }
    }

    /**
     * Updates the application's language preference.
     *
     * @param preference The new language preference to be applied.
     */
    fun setLanguagePreference(preference: LanguagePreference) {
        viewModelScope.launch {
            settingsRepository.setLanguagePreference(preference)
        }
    }

    /**
     * Factory class for creating instances of [SettingsViewModel].
     *
     * @property settingsRepository The repository to be injected into the ViewModel.
     */
    class Factory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {
        /**
         * Creates a new instance of the given ViewModel class.
         *
         * @param modelClass The class of the ViewModel to create.
         * @return A new instance of the ViewModel.
         * @throws IllegalArgumentException If the provided class is not recognized.
         */
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(settingsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
