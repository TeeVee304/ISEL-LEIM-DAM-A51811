package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.model.Friendship
import dam_A51811.filmroulette.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing friendships and user-related data.
 */
interface FriendshipRepository {

    /**
     * Sends a friend request from one user to another.
     *
     * @param requesterId The unique identifier of the user sending the request.
     * @param receiverId The unique identifier of the user receiving the request.
     */
    suspend fun sendFriendRequest(requesterId: String, receiverId: String)

    /**
     * Accepts a pending friend request between two users.
     *
     * @param userId The unique identifier of the user accepting the request.
     * @param otherUserId The unique identifier of the user who sent the request.
     */
    suspend fun acceptFriendRequest(userId: String, otherUserId: String)

    /**
     * Removes an existing friendship or cancels a pending friend request between two users.
     *
     * @param userId The unique identifier of the current user.
     * @param otherUserId The unique identifier of the other user.
     */
    suspend fun removeFriendOrRequest(userId: String, otherUserId: String)

    /**
     * Retrieves a continuous flow of the user's friends.
     *
     * @param userId The unique identifier of the user.
     * @return A [Flow] emitting a list of friends as [User] objects.
     */
    fun getFriends(userId: String): Flow<List<User>>

    /**
     * Retrieves a continuous flow of pending friend requests for the user.
     *
     * @param userId The unique identifier of the user.
     * @return A [Flow] emitting a list of pending [Friendship] requests.
     */
    fun getPendingRequests(userId: String): Flow<List<Friendship>>

    /**
     * Retrieves the friendship status between two users.
     *
     * @param userId The unique identifier of the first user.
     * @param otherUserId The unique identifier of the second user.
     * @return The [Friendship] details if it exists, otherwise null.
     */
    suspend fun getFriendship(userId: String, otherUserId: String): Friendship?

    /**
     * Retrieves the list of friend IDs for a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list containing the IDs of the user's friends.
     */
    suspend fun getFriendIds(userId: String): List<String>

    /**
     * Retrieves an existing user by email, or creates a new one if it does not exist.
     * Updates the username and avatar URL if they differ from the existing values.
     *
     * @param email The email address of the user.
     * @param username The optional username of the user.
     * @param avatarUrl The optional avatar URL of the user.
     * @return The retrieved or newly created [User].
     */
    suspend fun getOrCreateUser(email: String, username: String? = null, avatarUrl: String? = null): User

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user.
     * @return The corresponding [User] if found, otherwise null.
     */
    suspend fun getUserByEmail(email: String): User?

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return The corresponding [User] if found, otherwise null.
     */
    suspend fun getUserById(id: String): User?
}
