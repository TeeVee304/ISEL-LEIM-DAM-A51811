package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.Friendship
import dam_A51811.filmroulette.data.local.FriendshipStatus
import dam_A51811.filmroulette.data.local.User
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing bilateral friendships.
 */
@Dao
interface FriendshipDAO {

    // =====< ESCRITA >=====
    /**
     * Inserts a new friendship row with status [FriendshipStatus.PENDING].
     *
     * The caller must ensure [Friendship.userId1] < [Friendship.userId2].
     * Use the repository helper [dam_A51811.filmroulette.data.repository.FriendshipRepository]
     * which enforces this automatically.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriendship(friendship: Friendship)

    /**
     * Marks an existing PENDING request as ACCEPTED.
     *
     * @param userId1 The smaller of the two user IDs.
     * @param userId2 The larger of the two user IDs.
     */
    @Query("""
        UPDATE friendships
        SET status = '${FriendshipStatus.ACCEPTED}'
        WHERE user_id_1 = :userId1 AND user_id_2 = :userId2
    """)
    suspend fun acceptFriendRequest(userId1: Long, userId2: Long)

    /**
     * Deletes a friendship row (regardless of status).
     * Used both for declining a PENDING request and for unfriending.
     */
    @Query("""
        DELETE FROM friendships
        WHERE user_id_1 = :userId1 AND user_id_2 = :userId2
    """)
    suspend fun removeFriendOrRequest(userId1: Long, userId2: Long)

    // // =====< LEITURA >=====
    /**
     * Returns all accepted friends of [userId] as [User] objects.
     *
     * The JOIN covers both directions of the canonical pair so the caller
     * does not need to think about ordering.
     */
    @Query("""
        SELECT u.* FROM users u
        INNER JOIN friendships f
          ON (f.user_id_1 = :userId AND f.user_id_2 = u.id)
          OR (f.user_id_2 = :userId AND f.user_id_1 = u.id)
        WHERE f.status = '${FriendshipStatus.ACCEPTED}'
    """)
    fun getFriends(userId: Long): Flow<List<User>>

    /**
     * Returns all pending requests where [userId] is involved,
     * together with the full [Friendship] row so the UI can tell
     * whether the current user sent or received each request.
     */
    @Query("""
        SELECT * FROM friendships
        WHERE (user_id_1 = :userId OR user_id_2 = :userId)
          AND status = '${FriendshipStatus.PENDING}'
    """)
    fun getPendingRequests(userId: Long): Flow<List<Friendship>>

    /**
     * Returns the single [Friendship] row between the two users, if any.
     * Useful for checking the current status (PENDING / ACCEPTED / null).
     */
    @Query("""
        SELECT * FROM friendships
        WHERE user_id_1 = :userId1 AND user_id_2 = :userId2
        LIMIT 1
    """)
    suspend fun getFriendship(userId1: Long, userId2: Long): Friendship?

    /**
     * Returns all friend IDs of [userId] with ACCEPTED status.
     * Lightweight query used by visibility checks to avoid loading full [User] objects.
     */
    @Query("""
        SELECT CASE
            WHEN user_id_1 = :userId THEN user_id_2
            ELSE user_id_1
        END AS friend_id
        FROM friendships
        WHERE (user_id_1 = :userId OR user_id_2 = :userId)
          AND status = '${FriendshipStatus.ACCEPTED}'
    """)
    suspend fun getFriendIds(userId: Long): List<Long>
}
