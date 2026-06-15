package dam_A51811.filmroulette.ui.screens.aiguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.remote.ai.AIAssistant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch





/**
 * Represents the various UI states for AI guide operations.
 */
sealed class AiGuideUiState {
    /** Represents the idle state where no operation is ongoing. */
    object Idle    : AiGuideUiState()
    /** Represents the loading state where an operation is in progress. */
    object Loading : AiGuideUiState()
    /**
     * Represents a successful operation.
     *
     * @param result The result string returned by the AI.
     */
    data class Success(val result: String)  : AiGuideUiState()
    /**
     * Represents a failed operation.
     *
     * @param message The error message detailing the failure.
     */
    data class Error(val message: String)   : AiGuideUiState()
}






/**
 * ViewModel responsible for managing the state and interactions for the AI guide feature.
 *
 * @param assistant The AI assistant used to process describe and recommend operations.
 */
class AiGuideViewModel(private val assistant: AIAssistant) : ViewModel() {

    
    private val _describeState = MutableStateFlow<AiGuideUiState>(AiGuideUiState.Idle)
    /** The observable state flow for describing a movie. */
    val describeState: StateFlow<AiGuideUiState> = _describeState.asStateFlow()

    
    private val _recommendState = MutableStateFlow<AiGuideUiState>(AiGuideUiState.Idle)
    /** The observable state flow for recommending movies. */
    val recommendState: StateFlow<AiGuideUiState> = _recommendState.asStateFlow()

    
    
    

    
    /**
     * Requests a description for the given movie title.
     *
     * Updates [describeState] with loading, success, or error states accordingly.
     *
     * @param movieTitle The title of the movie to describe.
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

    /**
     * Resets the movie description state to idle.
     */
    fun resetDescribe() { _describeState.value = AiGuideUiState.Idle }

    
    
    

    
    /**
     * Requests movie recommendations based on the provided mood description.
     *
     * Updates [recommendState] with loading, success, or error states accordingly.
     *
     * @param moodDescription A text describing the desired mood or genre.
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

    /**
     * Resets the movie recommendation state to idle.
     */
    fun resetRecommend() { _recommendState.value = AiGuideUiState.Idle }

    
    
    

    /**
     * Factory for creating instances of [AiGuideViewModel].
     *
     * @param assistant The AI assistant to inject into the ViewModel.
     */
    class Factory(private val assistant: AIAssistant) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AiGuideViewModel(assistant) as T
    }
}
