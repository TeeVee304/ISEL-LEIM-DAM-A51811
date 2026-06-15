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
 * Data Access Object for managing friendships and friend requests between users.
 */
@Dao
interface FriendshipDAO {

    
    
    /**
     * Inserts a new friendship record. Ignores conflicts if the friendship already exists.
     *
     * @param friendship The friendship entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriendship(friendship: Friendship)

    
    /**
     * Accepts a pending friend request by updating its status to accepted.
     *
     * @param userId1 The first user's ID in the friendship.
     * @param userId2 The second user's ID in the friendship.
     */
    @Query("""
        UPDATE friendships
        SET status = '${FriendshipStatus.ACCEPTED}'
        WHERE user_id_1 = :userId1 AND user_id_2 = :userId2
    """)
    suspend fun acceptFriendRequest(userId1: Long, userId2: Long)

    
    /**
     * Removes an existing friendship or a pending friend request.
     *
     * @param userId1 The first user's ID in the friendship.
     * @param userId2 The second user's ID in the friendship.
     */
    @Query("""
        DELETE FROM friendships
        WHERE user_id_1 = :userId1 AND user_id_2 = :userId2
    """)
    suspend fun removeFriendOrRequest(userId1: Long, userId2: Long)

    
    
    /**
     * Retrieves a stream of all accepted friends for a specific user.
     *
     * @param userId The ID of the user whose friends are to be retrieved.
     * @return A flow emitting the list of accepted friends as User entities.
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
     * Retrieves a stream of pending friend requests involving a specific user.
     *
     * @param userId The ID of the user whose pending requests are to be retrieved.
     * @return A flow emitting the list of pending friendships.
     */
    @Query("""
        SELECT * FROM friendships
        WHERE (user_id_1 = :userId OR user_id_2 = :userId)
          AND status = '${FriendshipStatus.PENDING}'
    """)
    fun getPendingRequests(userId: Long): Flow<List<Friendship>>

    
    /**
     * Retrieves a specific friendship between two users if it exists.
     *
     * @param userId1 The first user's ID.
     * @param userId2 The second user's ID.
     * @return The friendship entity, or null if no such friendship exists.
     */
    @Query("""
        SELECT * FROM friendships
        WHERE user_id_1 = :userId1 AND user_id_2 = :userId2
        LIMIT 1
    """)
    suspend fun getFriendship(userId1: Long, userId2: Long): Friendship?

    
    /**
     * Retrieves the IDs of all accepted friends for a specific user.
     *
     * @param userId The ID of the user whose friend IDs are to be retrieved.
     * @return A list containing the IDs of the accepted friends.
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
