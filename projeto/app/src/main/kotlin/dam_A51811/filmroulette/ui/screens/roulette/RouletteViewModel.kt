package dam_A51811.filmroulette.ui.screens.roulette

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

/**
 * Represents the various states of the roulette UI.
 */
sealed class RouletteUiState {
    object Idle    : RouletteUiState()
    object Loading : RouletteUiState()
    data class Success(val movies: List<Movie>, val currentIndex: Int) : RouletteUiState()
    data class Error(val message: String? = null, val messageResId: Int? = null) : RouletteUiState()
}

/**
 * ViewModel responsible for managing the state and logic of the movie roulette screen,
 * including fetching recommendations and navigating through them.
 *
 * @param movieRepository The repository handling movie data operations.
 */
class RouletteViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<RouletteUiState>(RouletteUiState.Idle)
    val uiState: StateFlow<RouletteUiState> = _uiState.asStateFlow()

    
    private var lastMaxDuration:    Int          = 120
    private var lastGenres:         List<Genre>  = emptyList()
    private var lastLanguages:      List<String> = emptyList()
    private var lastReleaseDateGte: String?      = null
    private var lastReleaseDateLte: String?      = null
    private var lastProviderIds:    String?      = null

    
    private val seenMovieIds = mutableSetOf<Long>()

    /**
     * Loads movie recommendations based on the specified filters.
     *
     * @param maxDuration The maximum duration of the movies in minutes.
     * @param genres A list of genres to filter by.
     * @param languages A list of language codes to filter by.
     * @param releaseDateGte The minimum release date (e.g., "YYYY-MM-DD").
     * @param releaseDateLte The maximum release date (e.g., "YYYY-MM-DD").
     * @param providerIds A comma-separated string of streaming provider IDs.
     * @param isNewSession Whether this request starts a new recommendation session.
     */
    fun loadRecommendations(
        maxDuration: Int,
        genres: List<Genre>,
        languages: List<String> = emptyList(),
        releaseDateGte: String? = null,
        releaseDateLte: String? = null,
        providerIds: String? = null,
        isNewSession: Boolean = true   
    ) {
        if (isNewSession) {
            
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

    /**
     * Advances to the next movie in the current list of recommendations.
     * If the end of the list is reached, it loads more recommendations.
     */
    fun nextMovie() {
        val currentState = _uiState.value
        if (currentState is RouletteUiState.Success) {
            val nextIndex = currentState.currentIndex + 1
            if (nextIndex < currentState.movies.size) {
                _uiState.value = currentState.copy(currentIndex = nextIndex)
            } else {
                
                loadRecommendations(
                    maxDuration = lastMaxDuration,
                    genres      = lastGenres,
                    isNewSession = false
                )
            }
        }
    }

    /**
     * Returns to the previous movie in the current list of recommendations.
     */
    fun previousMovie() {
        val currentState = _uiState.value
        if (currentState is RouletteUiState.Success) {
            val prevIndex = currentState.currentIndex - 1
            if (prevIndex >= 0) {
                _uiState.value = currentState.copy(currentIndex = prevIndex)
            }
        }
    }

    /**
     * Factory class for creating instances of RouletteViewModel.
     *
     * @param repository The repository handling movie data operations.
     */
    class Factory(private val repository: MovieRepository) : ViewModelProvider.Factory {
        /**
         * Creates a new instance of the given ViewModel class.
         *
         * @param modelClass The class of the ViewModel to create.
         * @return A newly created ViewModel instance.
         */
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RouletteViewModel(repository) as T
        }
    }
}