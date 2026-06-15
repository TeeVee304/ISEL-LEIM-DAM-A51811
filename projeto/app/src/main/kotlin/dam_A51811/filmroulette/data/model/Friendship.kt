package dam_A51811.filmroulette.data.model

/**
 * Represents a friendship relationship between two users.
 *
 * @param userId1 The unique identifier of the first user.
 * @param userId2 The unique identifier of the second user.
 * @param status The current state of the friendship.
 * @param requesterId The unique identifier of the user who initiated the friendship request.
 */
data class Friendship(
    val userId1: String,
    val userId2: String,
    val status: FriendshipStatus = FriendshipStatus.PENDING,
    val requesterId: String
)

/**
 * Defines the possible states of a friendship.
 */
enum class FriendshipStatus {
    PENDING,
    ACCEPTED
}
