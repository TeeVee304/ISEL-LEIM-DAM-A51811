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

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val friendsList: StateFlow<List<User>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else friendshipRepository.getFriends(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

                // Check existing status
                val friendship = friendshipRepository.getFriendship(user.id, targetUser.id)
                if (friendship != null) {
                    if (friendship.status == dam_A51811.filmroulette.data.local.FriendshipStatus.ACCEPTED) {
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

    fun acceptFriendRequest(friendId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.acceptFriendRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to accept request"
            }
        }
    }

    fun declineFriendRequest(friendId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriendOrRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to decline request"
            }
        }
    }

    fun cancelFriendRequest(friendId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriendOrRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to cancel request"
            }
        }
    }

    fun removeFriend(friendId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriendOrRequest(user.id, friendId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to remove friend"
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val friendshipRepository: FriendshipRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(authRepository, friendshipRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
