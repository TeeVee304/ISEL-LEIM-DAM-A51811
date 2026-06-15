package dam_A51811.filmroulette.ui.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dam_A51811.filmroulette.data.model.GroupSession
import dam_A51811.filmroulette.data.model.SessionInvite
import dam_A51811.filmroulette.data.model.SessionMember
import dam_A51811.filmroulette.data.model.SharedFilters
import dam_A51811.filmroulette.data.model.User
import dam_A51811.filmroulette.data.model.Movie
import dam_A51811.filmroulette.data.repository.AuthRepository
import dam_A51811.filmroulette.data.repository.FriendshipRepository
import dam_A51811.filmroulette.data.repository.GroupSessionRepository
import dam_A51811.filmroulette.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the group session screen.
 *
 * @property session The currently active group session, if any.
 * @property members The list of members currently in the session.
 * @property error An optional error message to display.
 * @property isLoading Indicates whether a network or processing operation is currently underway.
 * @property currentUserId The unique identifier of the currently authenticated user.
 * @property inviteDialogOpen Whether the invite-friends bottom sheet is currently visible.
 * @property pendingInvites The list of session invites received by the current user.
 * @property sentInviteIds The set of friend IDs to whom the host has already sent an invite in the current session.
 */
data class GroupSessionUiState(
    val session: GroupSession? = null,
    val members: List<SessionMember> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val currentUserId: String? = null,
    val inviteDialogOpen: Boolean = false,
    val pendingInvites: List<SessionInvite> = emptyList(),
    val sentInviteIds: Set<String> = emptySet()
)

/**
 * ViewModel responsible for managing group session operations, including creating,
 * joining, and leaving sessions, as well as observing state changes and triggering matches.
 * Also handles sending and receiving friend invites for active sessions.
 *
 * @property repository The repository for managing group session data and synchronization.
 * @property authRepository The repository handling user authentication.
 * @property movieRepository The repository for fetching movie recommendations.
 * @property friendshipRepository The repository for accessing the user's friend list.
 */
class GroupSessionViewModel(
    private val repository: GroupSessionRepository,
    private val authRepository: AuthRepository,
    private val movieRepository: MovieRepository,
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupSessionUiState())
    val uiState: StateFlow<GroupSessionUiState> = _uiState.asStateFlow()

    private var currentSessionId: String? = null

    /**
     * The current user's friend list, kept live via the friendship repository.
     * Updates automatically when [_currentUserIdFlow] changes.
     */
    private val _currentUserIdFlow = MutableStateFlow<String?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val friendsList: StateFlow<List<User>> = _currentUserIdFlow.flatMapLatest { uid ->
        if (uid == null) flowOf(emptyList())
        else friendshipRepository.getFriends(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeInvites()
    }

    /**
     * Starts observing incoming session invites for the current authenticated user.
     * Runs as a persistent background coroutine for the lifetime of the ViewModel.
     */
    private fun observeInvites() {
        viewModelScope.launch {
            val firebaseUser = authRepository.getCurrentUser() ?: return@launch
            val userId = firebaseUser.uid
            _currentUserIdFlow.value = userId
            repository.observeInvites(userId)
                .catch { /* Silently ignore invite observation errors. */ }
                .collectLatest { invites ->
                    _uiState.value = _uiState.value.copy(pendingInvites = invites)
                }
        }
    }

    /**
     * Creates a new group session, sets the current authenticated user as the host,
     * and automatically joins the newly created session.
     */
    fun createAndJoinSession() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val firebaseUser = authRepository.getCurrentUser() ?: throw Exception("User not logged in")
                val user = User(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName ?: "User",
                    email = firebaseUser.email ?: "",
                    avatarUrl = firebaseUser.photoUrl?.toString()
                )
                _uiState.value = _uiState.value.copy(currentUserId = user.id)
                _currentUserIdFlow.value = user.id
                val sessionId = repository.createSession(user.id)
                repository.joinSession(sessionId, user)
                observeSession(sessionId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    /**
     * Joins an existing group session using the provided session code.
     *
     * @param sessionId The unique identifier or code of the session to join.
     */
    fun joinSession(sessionId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val firebaseUser = authRepository.getCurrentUser() ?: throw Exception("User not logged in")
                val user = User(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName ?: "User",
                    email = firebaseUser.email ?: "",
                    avatarUrl = firebaseUser.photoUrl?.toString()
                )
                _uiState.value = _uiState.value.copy(currentUserId = user.id)
                _currentUserIdFlow.value = user.id
                repository.joinSession(sessionId, user)
                observeSession(sessionId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    /**
     * Leaves the current active session. If the current user is the host,
     * the session is destroyed for all members. Otherwise, the user simply leaves.
     */
    fun leaveSession() {
        val sessionId = currentSessionId ?: return
        val currentSession = _uiState.value.session
        viewModelScope.launch {
            try {
                val firebaseUser = authRepository.getCurrentUser()
                if (firebaseUser != null) {
                    if (currentSession != null && currentSession.hostId == firebaseUser.uid) {
                        repository.destroySession(sessionId)
                    } else {
                        repository.leaveSession(sessionId, firebaseUser.uid)
                    }
                }
                currentSessionId = null
                _uiState.value = _uiState.value.copy(
                    session = null,
                    members = emptyList(),
                    error = null,
                    isLoading = false,
                    inviteDialogOpen = false,
                    sentInviteIds = emptySet()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Starts observing real-time updates for the session metadata and its members.
     *
     * @param sessionId The ID of the session to observe.
     */
    private fun observeSession(sessionId: String) {
        currentSessionId = sessionId
        viewModelScope.launch {
            repository.observeSession(sessionId)
                .catch { e -> _uiState.value = _uiState.value.copy(error = e.message) }
                .collectLatest { session ->
                    _uiState.value = _uiState.value.copy(session = session, isLoading = false)
                    checkMatchCondition()
                }
        }
        viewModelScope.launch {
            repository.observeMembers(sessionId)
                .catch { e -> _uiState.value = _uiState.value.copy(error = e.message) }
                .collectLatest { members ->
                    _uiState.value = _uiState.value.copy(members = members)
                    checkMatchCondition()
                }
        }
    }

    /**
     * Checks if a match condition has been met by comparing the number of right swipes
     * on any movie to the total number of members in the session. If all members have swiped
     * right on a movie, it marks that movie as the matched result.
     */
    private fun checkMatchCondition() {
        val session = _uiState.value.session ?: return
        val members = _uiState.value.members
        val currentUserId = _uiState.value.currentUserId ?: return

        if (session.hostId != currentUserId) return
        if (session.status == dam_A51811.filmroulette.data.model.SessionStatus.MATCHED) return
        if (members.isEmpty()) return

        for ((movieId, swipers) in session.rightSwipes) {
            if (swipers.size >= members.size) {
                val matchedMovie = session.moviesDeck.find { it.id.toString() == movieId }
                if (matchedMovie != null) {
                    viewModelScope.launch {
                        try {
                            repository.setMatchMovie(session.id, matchedMovie)
                        } catch (e: Exception) {
                            // Silently ignore; the host-only check prevents duplicate attempts.
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the shared filters for the current session.
     *
     * @param newFilters The new set of shared filters to apply to the session.
     */
    fun updateSharedFilters(newFilters: SharedFilters) {
        val sessionId = currentSessionId ?: return
        viewModelScope.launch {
            try {
                repository.updateSharedFilters(sessionId, newFilters)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Starts the movie roulette for the group. Only the host can trigger this action.
     * It fetches recommendations based on the shared filters and updates the session state.
     */
    fun startRoulette() {
        val session = _uiState.value.session ?: return
        val currentUserId = _uiState.value.currentUserId ?: return
        if (session.hostId != currentUserId) return

        viewModelScope.launch {
            try {
                val filters = session.sharedFilters
                val movies = movieRepository.getRecommendations(
                    maxDuration = filters.maxMinutes,
                    genres = emptyList(),
                )
                repository.startSpinning(session.id, movies)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Records a positive swipe (right swipe) for a specific movie in the current session.
     *
     * @param movie The movie that the user has swiped right on.
     */
    fun swipeRight(movie: Movie) {
        val sessionId = currentSessionId ?: return
        val currentUserId = _uiState.value.currentUserId ?: return
        viewModelScope.launch {
            try {
                repository.swipeRight(sessionId, movie, currentUserId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Opens the invite-friends dialog for the host.
     */
    fun openInviteDialog() {
        _uiState.value = _uiState.value.copy(inviteDialogOpen = true)
    }

    /**
     * Closes the invite-friends dialog.
     */
    fun closeInviteDialog() {
        _uiState.value = _uiState.value.copy(inviteDialogOpen = false)
    }

    /**
     * Sends a session invite to a friend, and marks that friend as already invited.
     *
     * @param friend The [User] representing the friend to invite.
     */
    fun inviteFriend(friend: User) {
        val sessionId = currentSessionId ?: return
        val firebaseUser = authRepository.getCurrentUser() ?: return
        viewModelScope.launch {
            try {
                repository.sendInvite(
                    sessionId = sessionId,
                    friendId = friend.id,
                    inviterName = firebaseUser.displayName ?: "Someone",
                    inviterAvatarUrl = firebaseUser.photoUrl?.toString()
                )
                _uiState.value = _uiState.value.copy(
                    sentInviteIds = _uiState.value.sentInviteIds + friend.id
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Joins the session from an incoming invite and clears that invite from Firestore.
     *
     * @param invite The [SessionInvite] to accept.
     */
    fun acceptInvite(invite: SessionInvite) {
        val userId = _uiState.value.currentUserId ?: authRepository.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            try {
                repository.clearInvite(userId, invite.sessionId)
            } catch (e: Exception) {
                // Clearing the invite is best-effort; proceed regardless.
            }
        }
        joinSession(invite.sessionId)
    }

    /**
     * Dismisses an incoming invite without joining the session.
     *
     * @param invite The [SessionInvite] to dismiss.
     */
    fun dismissInvite(invite: SessionInvite) {
        val userId = _uiState.value.currentUserId ?: authRepository.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            try {
                repository.clearInvite(userId, invite.sessionId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Factory for creating instances of [GroupSessionViewModel].
     *
     * @property repository The repository for group session operations.
     * @property authRepository The repository for authentication data.
     * @property movieRepository The repository for fetching movie recommendations.
     * @property friendshipRepository The repository for accessing the user's friend list.
     */
    class Factory(
        private val repository: GroupSessionRepository,
        private val authRepository: AuthRepository,
        private val movieRepository: MovieRepository,
        private val friendshipRepository: FriendshipRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupSessionViewModel(repository, authRepository, movieRepository, friendshipRepository) as T
        }
    }
}
