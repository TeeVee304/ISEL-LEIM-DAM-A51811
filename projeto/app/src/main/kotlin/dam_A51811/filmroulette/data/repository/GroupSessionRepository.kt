package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.model.GroupSession
import dam_A51811.filmroulette.data.model.SessionMember
import dam_A51811.filmroulette.data.model.SharedFilters
import dam_A51811.filmroulette.data.model.Movie
import dam_A51811.filmroulette.data.model.User
import kotlinx.coroutines.flow.Flow

import dam_A51811.filmroulette.data.model.SessionStatus
import dam_A51811.filmroulette.data.model.SessionInvite

/**
 * Repository interface for managing group sessions.
 */
interface GroupSessionRepository {
    /**
     * Creates a new group session.
     *
     * @param hostId The unique identifier of the user hosting the session.
     * @return The randomly generated unique session code.
     */
    suspend fun createSession(hostId: String): String

    /**
     * Adds a user to an existing group session.
     *
     * @param sessionId The unique identifier of the target session.
     * @param user The [User] object representing the member to add.
     */
    suspend fun joinSession(sessionId: String, user: User)

    /**
     * Removes a user from a group session.
     *
     * @param sessionId The unique identifier of the target session.
     * @param userId The unique identifier of the user leaving the session.
     */
    suspend fun leaveSession(sessionId: String, userId: String)

    /**
     * Destroys an entire group session, removing it and all associated members.
     *
     * @param sessionId The unique identifier of the session to destroy.
     */
    suspend fun destroySession(sessionId: String)

    /**
     * Updates the overall status of the group session.
     *
     * @param sessionId The unique identifier of the target session.
     * @param status The new [SessionStatus] to apply.
     */
    suspend fun updateSessionStatus(sessionId: String, status: SessionStatus)

    /**
     * Updates the shared filters configurations for the group session.
     *
     * @param sessionId The unique identifier of the target session.
     * @param filters The updated [SharedFilters] to apply.
     */
    suspend fun updateSharedFilters(sessionId: String, filters: SharedFilters)

    /**
     * Observes real-time changes to the session state.
     *
     * @param sessionId The unique identifier of the session to observe.
     * @return A [Flow] emitting the updated [GroupSession] state or null if it does not exist.
     */
    fun observeSession(sessionId: String): Flow<GroupSession?>

    /**
     * Observes real-time changes to the session members.
     *
     * @param sessionId The unique identifier of the session to observe.
     * @return A [Flow] emitting the updated list of [SessionMember]s.
     */
    fun observeMembers(sessionId: String): Flow<List<SessionMember>>

    /**
     * Starts the spinning phase of the session, making the provided movie deck available.
     *
     * @param sessionId The unique identifier of the target session.
     * @param deck The list of [Movie]s available for the participants to swipe on.
     */
    suspend fun startSpinning(sessionId: String, deck: List<Movie>)

    /**
     * Registers a right swipe (like) on a movie by a specific user.
     *
     * @param sessionId The unique identifier of the target session.
     * @param movie The [Movie] that was swiped right.
     * @param userId The unique identifier of the user who swiped.
     */
    suspend fun swipeRight(sessionId: String, movie: Movie, userId: String)

    /**
     * Sets the successfully matched movie for the session and updates its status.
     *
     * @param sessionId The unique identifier of the target session.
     * @param movie The [Movie] that all members have agreed upon.
     */
    suspend fun setMatchMovie(sessionId: String, movie: Movie)

    /**
     * Sends a session invite to a friend.
     * Writes an invite document under the friend's user record in Firestore.
     *
     * @param sessionId The unique identifier of the session to invite the friend to.
     * @param friendId The unique identifier of the friend being invited.
     * @param inviterName The display name of the user sending the invite.
     * @param inviterAvatarUrl The avatar URL of the user sending the invite, or null if unavailable.
     */
    suspend fun sendInvite(sessionId: String, friendId: String, inviterName: String, inviterAvatarUrl: String?)

    /**
     * Observes real-time incoming session invites for the given user.
     *
     * @param userId The unique identifier of the user whose invites are being observed.
     * @return A [Flow] emitting the current list of pending [SessionInvite]s.
     */
    fun observeInvites(userId: String): Flow<List<SessionInvite>>

    /**
     * Removes a specific session invite for a user, dismissing it from the inbox.
     *
     * @param userId The unique identifier of the user whose invite is being cleared.
     * @param sessionId The session ID of the invite to remove.
     */
    suspend fun clearInvite(userId: String, sessionId: String)

    /**
     * Removes the given user from every session they currently belong to.
     * If the user is the host of a session, that session is destroyed entirely.
     * Intended to be called on app close to prevent stale session membership.
     *
     * @param userId The unique identifier of the user to remove from all sessions.
     */
    suspend fun leaveAllSessionsForUser(userId: String)
}

