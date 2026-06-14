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
    object Idle    : RouletteUiState()
    object Loading : RouletteUiState()
    data class Success(val movies: List<Movie>, val currentIndex: Int) : RouletteUiState()
    data class Error(val message: String? = null, val messageResId: Int? = null) : RouletteUiState()
}

class RouletteViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<RouletteUiState>(RouletteUiState.Idle)
    val uiState: StateFlow<RouletteUiState> = _uiState.asStateFlow()

    // Last-used filter params — replayed when the pool runs out
    private var lastMaxDuration:    Int          = 120
    private var lastGenres:         List<Genre>  = emptyList()
    private var lastLanguages:      List<String> = emptyList()
    private var lastReleaseDateGte: String?      = null
    private var lastReleaseDateLte: String?      = null
    private var lastProviderIds:    String?      = null

    // Seen-movie deduplication: cleared on every new filter session
    private val seenMovieIds = mutableSetOf<Long>()

    fun loadRecommendations(
        maxDuration: Int,
        genres: List<Genre>,
        languages: List<String> = emptyList(),
        releaseDateGte: String? = null,
        releaseDateLte: String? = null,
        providerIds: String? = null,
        isNewSession: Boolean = true   // true when called from Filters, false when refilling pool
    ) {
        if (isNewSession) {
            // New filter session — reset everything so the user starts fresh
            seenMovieIds.clear()
            lastMaxDuration    = maxDuration
            lastGenres         = genres
            lastLanguages      = languages
            lastReleaseDateGte = releaseDateGte
            lastReleaseDateLte = releaseDateLte
            lastProviderIds    = providerIds
        }

        viewModelScope.launch {
            _uiState.value = RouletteUiState.Loading
            try {
                val movies = movieRepository.getRecommendations(
                    maxDuration    = lastMaxDuration,
                    genres         = lastGenres,
                    languages      = lastLanguages,
                    releaseDateGte = lastReleaseDateGte,
                    releaseDateLte = lastReleaseDateLte,
                    providerIds    = lastProviderIds,
                    seenMovieIds   = seenMovieIds.toSet()
                )
                if (movies.isEmpty()) {
                    _uiState.value = RouletteUiState.Error(messageResId = R.string.error_no_movies)
                } else {
                    // Record every returned movie as seen for this session
                    movies.forEach { seenMovieIds.add(it.id) }
                    _uiState.value = RouletteUiState.Success(movies, 0)
                }
            } catch (e: Exception) {
                _uiState.value = RouletteUiState.Error(
                    message     = e.message,
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
                // Pool exhausted — refill without resetting the seen-list
                loadRecommendations(
                    maxDuration = lastMaxDuration,
                    genres      = lastGenres,
                    isNewSession = false
                )
            }
        }
    }

    fun previousMovie() {
        val currentState = _uiState.value
        if (currentState is RouletteUiState.Success) {
            val prevIndex = currentState.currentIndex - 1
            if (prevIndex >= 0) {
                _uiState.value = currentState.copy(currentIndex = prevIndex)
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