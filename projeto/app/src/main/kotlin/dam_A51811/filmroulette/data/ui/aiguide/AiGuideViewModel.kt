package dam_A51811.filmroulette.data.ui.aiguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.remote.ai.AIAssistant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// UI State
// ─────────────────────────────────────────────────────────────────────────────

sealed class AiGuideUiState {
    object Idle    : AiGuideUiState()
    object Loading : AiGuideUiState()
    data class Success(val result: String)  : AiGuideUiState()
    data class Error(val message: String)   : AiGuideUiState()
}

// ─────────────────────────────────────────────────────────────────────────────
// ViewModel
// ─────────────────────────────────────────────────────────────────────────────

/**
 * ViewModel for the AI Guide screen.
 *
 * Exposes two separate [StateFlow]s — one per feature — so each tab can
 * display its own loading/error/success independently.
 */
class AiGuideViewModel(private val assistant: AIAssistant) : ViewModel() {

    /** State for the Movie Describer tab. */
    private val _describeState = MutableStateFlow<AiGuideUiState>(AiGuideUiState.Idle)
    val describeState: StateFlow<AiGuideUiState> = _describeState.asStateFlow()

    /** State for the Smart Recommendations tab. */
    private val _recommendState = MutableStateFlow<AiGuideUiState>(AiGuideUiState.Idle)
    val recommendState: StateFlow<AiGuideUiState> = _recommendState.asStateFlow()

    // ─────────────────────────────────────────────────────────────────────────
    // Feature: Describe Movie
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Asks the LLM to describe [movieTitle] (plot, themes, trivia, audience).
     * Updates [describeState].
     */
    fun describeMovie(movieTitle: String) {
        if (movieTitle.isBlank()) return
        viewModelScope.launch {
            _describeState.value = AiGuideUiState.Loading
            _describeState.value = try {
                AiGuideUiState.Success(assistant.describeMovie(movieTitle.trim()))
            } catch (e: Exception) {
                AiGuideUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetDescribe() { _describeState.value = AiGuideUiState.Idle }

    // ─────────────────────────────────────────────────────────────────────────
    // Feature: Recommend Movies
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Asks the LLM for 3 movie recommendations that match [moodDescription].
     * Updates [recommendState].
     */
    fun recommendMovies(moodDescription: String) {
        if (moodDescription.isBlank()) return
        viewModelScope.launch {
            _recommendState.value = AiGuideUiState.Loading
            _recommendState.value = try {
                AiGuideUiState.Success(assistant.recommendMovies(moodDescription.trim()))
            } catch (e: Exception) {
                AiGuideUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetRecommend() { _recommendState.value = AiGuideUiState.Idle }

    // ─────────────────────────────────────────────────────────────────────────
    // Factory
    // ─────────────────────────────────────────────────────────────────────────

    class Factory(private val assistant: AIAssistant) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AiGuideViewModel(assistant) as T
    }
}
