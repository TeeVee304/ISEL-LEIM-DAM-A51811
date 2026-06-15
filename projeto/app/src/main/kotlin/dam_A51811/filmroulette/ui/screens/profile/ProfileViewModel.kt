package dam_A51811.filmroulette.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.model.User
import dam_A51811.filmroulette.data.repository.AuthRepository
import dam_A51811.filmroulette.data.repository.FriendshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the user's profile, including authentication status,
 * friendship connections, and profile updates.
 *
 * @param authRepository The repository handling authentication operations.
 * @param friendshipRepository The repository handling friendship data and user interactions.
 */
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {

    private val firebaseUser = authRepository.getCurrentUser()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadProfile()
    }

    /**
     * Loads the current user's profile data from the remote server.
     */
    private fun loadProfile() {
        val email = firebaseUser?.email ?: return
        val displayName = firebaseUser.displayName
        val photoUrl = firebaseUser.photoUrl?.toString()
        viewModelScope.launch {
            try {
                val user = friendshipRepository.getOrCreateUser(email, displayName, photoUrl)
                _currentUser.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load local profile"
            }
        }
    }

    /**
     * A state flow emitting the list of accepted friends for the current user.
     */
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val friendsList: StateFlow<List<User>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else friendshipRepository.getFriends(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * A state flow emitting the list of users who have sent friend requests to the current user.
     */
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val receivedRequests: StateFlow<List<User>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else friendshipRepository.getPendingRequests(user.id).map { friendships ->
            friendships.filter { it.requesterId != user.id }
                .mapNotNull { friendship ->
                    val otherId = if (user.id == friendship.userId1) friendship.userId2 else friendship.userId1
                    friendshipRepository.getUserById(otherId)
                }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * A state flow emitting the list of users to whom the current user has sent friend requests.
     */
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val sentRequests: StateFlow<List<User>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else friendshipRepository.getPendingRequests(user.id).map { friendships ->
            friendships.filter { it.requesterId == user.id }
                .mapNotNull { friendship ->
                    val otherId = if (user.id == friendship.userId1) friendship.userId2 else friendship.userId1
                    friendshipRepository.getUserById(otherId)
                }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Sends a friend request to a user identified by their email address.
     *
     * @param friendEmail The email address of the target user.
     */
    fun sendFriendRequest(friendEmail: String) {
        val user = _currentUser.value ?: return
        _errorMessage.value = null
        _successMessage.value = null

        val trimmedEmail = friendEmail.trim()
        if (trimmedEmail.equals(user.email, ignoreCase = true)) {
            _errorMessage.value = "self_error"
            return
        }

        viewModelScope.launch {
            try {
                val targetUser = friendshipRepository.getUserByEmail(trimmedEmail)
                if (targetUser == null) {
                    _errorMessage.value = "not_found"
                    return@launch
                }

                
                val friendship = friendshipRepository.getFriendship(user.id, targetUser.id)
                if (friendship != null) {
                    if (friendship.status == dam_A51811.filmroulette.data.model.FriendshipStatus.ACCEPTED) {
                        _errorMessage.value = "already_friends"
                    } else {
                        _errorMessage.value = "already_requested"
                    }
                    return@launch
                }

                friendshipRepository.sendFriendRequest(user.id, targetUser.id)
                _successMessage.value = "success"
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to send request"
            }
        }
    }

    /**
     * Accepts an incoming friend request from the specified user.
     *
     * @param friendId The unique identifier of the user whose request is being accepted.
     */
    fun acceptFriendRequest(friendId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.acceptFriendRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to accept request"
            }
        }
    }

    /**
     * Declines an incoming friend request from the specified user.
     *
     * @param friendId The unique identifier of the user whose request is being declined.
     */
    fun declineFriendRequest(friendId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriendOrRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to decline request"
            }
        }
    }

    /**
     * Cancels an outgoing friend request sent to the specified user.
     *
     * @param friendId The unique identifier of the target user.
     */
    fun cancelFriendRequest(friendId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriendOrRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to cancel request"
            }
        }
    }

    /**
     * Removes an existing friend connection with the specified user.
     *
     * @param friendId The unique identifier of the friend to remove.
     */
    fun removeFriend(friendId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriendOrRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to remove friend"
            }
        }
    }

    /**
     * Logs out the current user and clears authentication tokens.
     */
    fun logout() {
        authRepository.logout()
    }

    /**
     * Deletes the current user's account permanently from the server.
     */
    fun deleteAccount() {
        viewModelScope.launch {
            try {
                val result = authRepository.deleteAccount()
                if (result.isSuccess) {
                    authRepository.logout()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to delete account"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to delete account"
            }
        }
    }

    /**
     * Updates the user's profile with a new username or avatar URL.
     *
     * @param newUsername The new username to apply, or null to keep the existing one.
     * @param newAvatarUrl The new avatar URL to apply, or null to keep the existing one.
     */
    fun updateProfile(newUsername: String?, newAvatarUrl: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                val result = authRepository.updateProfile(newUsername, newAvatarUrl)
                if (result.isSuccess) {
                    val updatedUser = friendshipRepository.getOrCreateUser(
                        email = user.email,
                        username = newUsername ?: user.username,
                        avatarUrl = newAvatarUrl ?: user.avatarUrl
                    )
                    _currentUser.value = updatedUser
                    _successMessage.value = "Profile updated successfully"
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to update profile"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update profile"
            }
        }
    }

    /**
     * Updates the user's profile with a new username and uploads a new profile image.
     *
     * @param newUsername The new username to apply, or null to keep the existing one.
     * @param newImageUri The URI of the new profile image to upload, or null to skip upload.
     */
    fun updateProfileWithImage(newUsername: String?, newImageUri: android.net.Uri?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                var photoUrlToUse = user.avatarUrl
                if (newImageUri != null) {
                    val uploadResult = authRepository.uploadProfileImage(newImageUri)
                    if (uploadResult.isSuccess) {
                        photoUrlToUse = uploadResult.getOrNull()
                    } else {
                        _errorMessage.value = uploadResult.exceptionOrNull()?.message ?: "Failed to upload image"
                        return@launch
                    }
                }
                
                val result = authRepository.updateProfile(newUsername, photoUrlToUse)
                if (result.isSuccess) {
                    val updatedUser = friendshipRepository.getOrCreateUser(
                        email = user.email,
                        username = newUsername ?: user.username,
                        avatarUrl = photoUrlToUse ?: user.avatarUrl
                    )
                    _currentUser.value = updatedUser
                    _successMessage.value = "Profile updated successfully"
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to update profile"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            }
        }
    }

    /**
     * Clears any active error or success messages from the UI state.
     */
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    /**
     * Factory class for creating instances of ProfileViewModel.
     *
     * @param authRepository The repository handling authentication operations.
     * @param friendshipRepository The repository handling friendship data and user interactions.
     */
    class Factory(
        private val authRepository: AuthRepository,
        private val friendshipRepository: FriendshipRepository
    ) : ViewModelProvider.Factory {
        /**
         * Creates a new instance of the given ViewModel class.
         *
         * @param modelClass The class of the ViewModel to create.
         * @return A newly created ViewModel instance.
         * @throws IllegalArgumentException If the specified modelClass is unknown.
         */
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(authRepository, friendshipRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
