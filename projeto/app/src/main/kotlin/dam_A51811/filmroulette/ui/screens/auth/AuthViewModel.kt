package dam_A51811.filmroulette.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.model.Visibility
import dam_A51811.filmroulette.data.repository.AuthRepository
import dam_A51811.filmroulette.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represents the various states of authentication processes.
 */
sealed class AuthState {
    /** The default idle state where no authentication process is active. */
    data object Idle : AuthState()
    /** The state indicating an authentication process is in progress. */
    data object Loading : AuthState()
    /**
     * The state indicating an authentication error occurred.
     *
     * @param message The error message detailing the failure.
     */
    data class Error(val message: String) : AuthState()
    /** The state indicating a successful authentication. */
    data object Success : AuthState()
}

/**
 * ViewModel responsible for managing authentication state and actions.
 *
 * @param authRepository The repository handling authentication data operations.
 */
class AuthViewModel(
    private val authRepository: AuthRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    /** The observable state flow for authentication processes. */
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /**
     * Attempts to log in the user with the provided email and password.
     *
     * @param email The user's email address.
     * @param pass The user's password.
     */
    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.loginWithEmailAndPassword(email, pass)
            if (result.isSuccess) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    /**
     * Attempts to register a new user with the provided details.
     *
     * @param email The new user's email address.
     * @param pass The new user's password.
     * @param username The new user's desired username.
     * @param avatarUrl An optional URL to the user's avatar image.
     */
    fun register(email: String, pass: String, username: String, avatarUrl: String? = null) {
        if (email.isBlank() || pass.isBlank() || username.isBlank()) {
            _authState.value = AuthState.Error("Username, email and password cannot be empty")
            return
        }
        if (pass.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.registerWithEmailAndPassword(email, pass, username, avatarUrl)
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    try {
                        watchlistRepository.createList(user.uid, "Liked", Visibility.PRIVATE)
                    } catch (e: Exception) {
                        // Continue to success state even if list creation fails
                    }
                }
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    /**
     * Logs the current user out.
     */
    fun logout() {
        authRepository.logout()
        _authState.value = AuthState.Idle
    }

    /**
     * Resets the authentication state back to idle.
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    /**
     * Factory for creating instances of [AuthViewModel].
     *
     * @param authRepository The authentication repository to inject into the ViewModel.
     */
    class Factory(
        private val authRepository: AuthRepository,
        private val watchlistRepository: WatchlistRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(authRepository, watchlistRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
