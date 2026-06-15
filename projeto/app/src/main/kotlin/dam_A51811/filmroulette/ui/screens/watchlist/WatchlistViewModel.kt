package dam_A51811.filmroulette.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.model.MovieList
import dam_A51811.filmroulette.data.model.User
import dam_A51811.filmroulette.data.model.Visibility
import dam_A51811.filmroulette.data.repository.AuthRepository
import dam_A51811.filmroulette.data.repository.FriendshipRepository
import dam_A51811.filmroulette.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing and displaying the user's watchlists.
 * It interacts with various repositories to fetch user details and their associated watchlists.
 *
 * @param watchlistRepository The repository handling watchlist data operations.
 * @param authRepository The repository managing authentication state.
 * @param friendshipRepository The repository for managing user friendships and profiles.
 */
class WatchlistViewModel(
    private val watchlistRepository: WatchlistRepository,
    private val authRepository: AuthRepository,
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {

    private val firebaseUser = authRepository.getCurrentUser()

    private val _currentUser = MutableStateFlow<User?>(null)
    /**
     * The currently authenticated user, or null if no user is authenticated or data is loading.
     */
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    /**
     * An error message that represents any failures that occurred during operations, or null if no error exists.
     */
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUser()
    }

    /**
     * Fetches the current user's profile information from the authentication and friendship repositories.
     * If the user cannot be loaded, the error message state is updated.
     */
    private fun loadUser() {
        val email = firebaseUser?.email ?: return
        val displayName = firebaseUser.displayName
        val photoUrl = firebaseUser.photoUrl?.toString()
        viewModelScope.launch {
            try {
                val user = friendshipRepository.getOrCreateUser(email, displayName, photoUrl)
                _currentUser.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load user"
            }
        }
    }

    /**
     * A flow emitting the list of watchlists owned by the currently authenticated user.
     */
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val watchlists: StateFlow<List<MovieList>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else watchlistRepository.getOwnLists(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Retrieves the movies associated with a given watchlist.
     *
     * @param listId The unique identifier of the watchlist.
     * @return A flow emitting the list of movies in the specified watchlist.
     */
    fun getMoviesForList(listId: String) = watchlistRepository.getMoviesForList(listId)

    /**
     * Creates a new watchlist for the current user.
     *
     * @param name The name of the new watchlist.
     * @param visibility The visibility setting for the new watchlist.
     */
    fun createList(name: String, visibility: Visibility) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                watchlistRepository.createList(user.id, name, visibility)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to create list"
            }
        }
    }

    /**
     * Deletes an existing watchlist.
     *
     * @param listId The unique identifier of the watchlist to be deleted.
     */
    fun deleteList(listId: String) {
        viewModelScope.launch {
            try {
                watchlistRepository.deleteList(listId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to delete list"
            }
        }
    }

    /**
     * Adds a specific movie to a given watchlist.
     *
     * @param listId The unique identifier of the target watchlist.
     * @param movieId The unique identifier of the movie to add.
     */
    fun addMovieToList(listId: String, movieId: Long) {
        viewModelScope.launch {
            try {
                watchlistRepository.addMovieToList(listId, movieId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to add movie to list"
            }
        }
    }

    /**
     * Removes a specific movie from a given watchlist.
     *
     * @param listId The unique identifier of the target watchlist.
     * @param movieId The unique identifier of the movie to remove.
     */
    fun removeMovieFromList(listId: String, movieId: Long) {
        viewModelScope.launch {
            try {
                watchlistRepository.removeMovieFromList(listId, movieId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to remove movie from list"
            }
        }
    }

    /**
     * Automatically adds a movie to the user's "Liked" watchlist.
     * If the "Liked" watchlist doesn't exist, it creates it first.
     *
     * @param movieId The unique identifier of the movie to add.
     */
    fun addMovieToLikedList(movieId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                val userLists = watchlistRepository.getOwnLists(user.id).firstOrNull() ?: emptyList()
                val likedList = userLists.find { it.name == "Liked" }
                if (likedList == null) {
                    val newListId = watchlistRepository.createList(user.id, "Liked", Visibility.PRIVATE)
                    watchlistRepository.addMovieToList(newListId, movieId)
                } else {
                    watchlistRepository.addMovieToList(likedList.id, movieId)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to add movie to Liked list"
            }
        }
    }
    
    /**
     * Clears the current error message state.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Factory class for creating instances of [WatchlistViewModel].
     *
     * @property watchlistRepository The repository handling watchlist data operations.
     * @property authRepository The repository managing authentication state.
     * @property friendshipRepository The repository for managing user friendships and profiles.
     */
    class Factory(
        private val watchlistRepository: WatchlistRepository,
        private val authRepository: AuthRepository,
        private val friendshipRepository: FriendshipRepository
    ) : ViewModelProvider.Factory {
        /**
         * Creates a new instance of the requested ViewModel class.
         *
         * @param modelClass The class of the ViewModel to create.
         * @return A newly created ViewModel instance.
         * @throws IllegalArgumentException If the given class is not assignable from [WatchlistViewModel].
         */
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
                return WatchlistViewModel(watchlistRepository, authRepository, friendshipRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}