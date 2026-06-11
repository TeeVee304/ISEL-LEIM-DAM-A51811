package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.local.Friendship
import dam_A51811.filmroulette.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Contract for managing bilateral friendships.
 *
 * All methods that receive two user IDs accept them in any order —
 * implementations must canonicalise (smaller first) before hitting the DAO.
 *
 * ### Firebase note
 * When Firebase is introduced, create a [FriendshipRepository] implementation
 * that writes to Firestore in addition to (or instead of) Room, keeping the
 * interface identical so ViewModels need no changes.
 */
interface FriendshipRepository {

    /**
     * Sends a friend request from [requesterId] to [receiverId].
     * Creates a PENDING row.  No-ops if a row already exists.
     */
    suspend fun sendFriendRequest(requesterId: Long, receiverId: Long)

    /**
     * Accepts a PENDING request between the two users.
     * Idempotent: calling on an already-ACCEPTED pair is safe.
     */
    suspend fun acceptFriendRequest(userId: Long, otherUserId: Long)

    /**
     * Removes a friendship or declines / cancels a pending request.
     */
    suspend fun removeFriendOrRequest(userId: Long, otherUserId: Long)

    /**
     * Reactive stream of all accepted friends of [userId].
     * Emits a new list whenever the friendships table changes.
     */
    fun getFriends(userId: Long): Flow<List<User>>

    /**
     * Reactive stream of pending requests involving [userId].
     * Use [Friendship.requesterId] to distinguish sent vs. received.
     */
    fun getPendingRequests(userId: Long): Flow<List<Friendship>>

    /**
     * Returns the current [Friendship] row between the two users, or null
     * if they are not connected at all.
     */
    suspend fun getFriendship(userId: Long, otherUserId: Long): Friendship?

    /**
     * Returns a snapshot list of accepted friend IDs for [userId].
     * Lightweight — does not load full [User] objects.
     */
    suspend fun getFriendIds(userId: Long): List<Long>
}
