package dam_A51811.filmroulette.data.ui.roulette

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.R
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.data.model.Movie
import dam_A51811.filmroulette.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RouletteUiState {
    object Loading : RouletteUiState()
    data class Success(val movies: List<Movie>, val currentIndex: Int) : RouletteUiState()
    data class Error(val message: String? = null, val messageResId: Int? = null) : RouletteUiState()
}

class RouletteViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<RouletteUiState>(RouletteUiState.Loading)
    val uiState: StateFlow<RouletteUiState> = _uiState.asStateFlow()

    private var lastMaxDuration: Int = 120
    private var lastGenres: List<Genre> = emptyList()

    fun loadRecommendations(maxDuration: Int, genres: List<Genre>) {
        lastMaxDuration = maxDuration
        lastGenres = genres
        
        viewModelScope.launch {
            _uiState.value = RouletteUiState.Loading
            try {
                val movies = movieRepository.getRecommendations(maxDuration, genres)
                if (movies.isEmpty()) {
                    _uiState.value = RouletteUiState.Error(messageResId = R.string.error_no_movies)
                } else {
                    _uiState.value = RouletteUiState.Success(movies, 0)
                }
            } catch (e: Exception) {
                _uiState.value = RouletteUiState.Error(
                    message = e.message,
                    messageResId = R.string.error_unexpected
                )
            }
        }
    }

    fun nextMovie() {
        val currentState = _uiState.value
        if (currentState is RouletteUiState.Success) {
            val nextIndex = currentState.currentIndex + 1
            if (nextIndex < currentState.movies.size) {
                _uiState.value = currentState.copy(currentIndex = nextIndex)
            } else {
                loadRecommendations(lastMaxDuration, lastGenres)
            }
        }
    }

    class Factory(private val repository: MovieRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RouletteViewModel(repository) as T
        }
    }
}