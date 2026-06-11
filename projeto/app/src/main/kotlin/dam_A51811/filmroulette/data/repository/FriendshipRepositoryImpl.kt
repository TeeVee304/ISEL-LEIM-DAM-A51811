package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.local.Friendship
import dam_A51811.filmroulette.data.local.FriendshipStatus
import dam_A51811.filmroulette.data.local.dao.FriendshipDAO
import dam_A51811.filmroulette.data.local.dao.UserDAO
import dam_A51811.filmroulette.data.model.User
import dam_A51811.filmroulette.data.utils.UserMapper.toDomainUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Room-backed implementation of [FriendshipRepository].
 *
 * ### Canonical ordering
 * Every pair `(a, b)` is stored with `user_id_1 = min(a, b)` and
 * `user_id_2 = max(a, b)`.  All public methods call [canonicalise]
 * before delegating to the DAO, so callers never need to think about it.
 *
 * ### Firebase note
 * When Firebase is introduced, create a `FriendshipRepositoryFirebase`
 * that implements the same interface and writes to Firestore.
 * The Application / DI layer decides which implementation to inject —
 * ViewModels are unaffected.
 */
class FriendshipRepositoryImpl(
    private val friendshipDao: FriendshipDAO,
    private val userDao: UserDAO
) : FriendshipRepository {

    // ─────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────

    /** Returns the pair `(smaller, larger)` to enforce canonical ordering. */
    private fun canonicalise(a: Long, b: Long): Pair<Long, Long> =
        if (a < b) a to b else b to a

    // ─────────────────────────────────────────────────────
    // Write operations
    // ─────────────────────────────────────────────────────

    override suspend fun sendFriendRequest(requesterId: Long, receiverId: Long) =
        withContext(Dispatchers.IO) {
            val (id1, id2) = canonicalise(requesterId, receiverId)
            friendshipDao.insertFriendship(
                Friendship(
                    userId1     = id1,
                    userId2     = id2,
                    status      = FriendshipStatus.PENDING,
                    requesterId = requesterId
                )
            )
        }

    override suspend fun acceptFriendRequest(userId: Long, otherUserId: Long) =
        withContext(Dispatchers.IO) {
            val (id1, id2) = canonicalise(userId, otherUserId)
            friendshipDao.acceptFriendRequest(id1, id2)
        }

    override suspend fun removeFriendOrRequest(userId: Long, otherUserId: Long) =
        withContext(Dispatchers.IO) {
            val (id1, id2) = canonicalise(userId, otherUserId)
            friendshipDao.removeFriendOrRequest(id1, id2)
        }

    // ─────────────────────────────────────────────────────
    // Read operations
    // ─────────────────────────────────────────────────────

    override fun getFriends(userId: Long): Flow<List<User>> =
        friendshipDao.getFriends(userId).map { list ->
            list.map { it.toDomainUser() }
        }

    override fun getPendingRequests(userId: Long): Flow<List<Friendship>> =
        friendshipDao.getPendingRequests(userId)

    override suspend fun getFriendship(userId: Long, otherUserId: Long): Friendship? =
        withContext(Dispatchers.IO) {
            val (id1, id2) = canonicalise(userId, otherUserId)
            friendshipDao.getFriendship(id1, id2)
        }

    override suspend fun getFriendIds(userId: Long): List<Long> =
        withContext(Dispatchers.IO) {
            friendshipDao.getFriendIds(userId)
        }
}
