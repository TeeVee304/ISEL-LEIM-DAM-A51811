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

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val themePreference: StateFlow<ThemePreference> = settingsRepository.themePreferenceFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemePreference.SYSTEM
        )

    val languagePreference: StateFlow<LanguagePreference> = settingsRepository.languagePreferenceFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LanguagePreference.SYSTEM
        )

    fun setThemePreference(preference: ThemePreference) {
        viewModelScope.launch {
            settingsRepository.setThemePreference(preference)
        }
    }

    fun setLanguagePreference(preference: LanguagePreference) {
        viewModelScope.launch {
            settingsRepository.setLanguagePreference(preference)
        }
    }

    class Factory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(settingsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
