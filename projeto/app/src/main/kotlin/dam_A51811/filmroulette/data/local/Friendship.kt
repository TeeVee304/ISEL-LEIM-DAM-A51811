package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


/**
 * Represents a friendship relationship between two users in the local database.
 *
 * @property userId1 The unique identifier of the first user.
 * @property userId2 The unique identifier of the second user.
 * @property status The current status of the friendship.
 * @property requesterId The unique identifier of the user who initiated the request.
 */
@Entity(
    tableName = "friendships",
    primaryKeys = ["user_id_1", "user_id_2"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id_1"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id_2"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("user_id_1"),
        Index("user_id_2")
    ]
)
data class Friendship(
    
    @ColumnInfo(name = "user_id_1")
    val userId1: Long,

    
    @ColumnInfo(name = "user_id_2")
    val userId2: Long,

    
    @ColumnInfo(name = "status")
    val status: String = FriendshipStatus.PENDING,

    
    @ColumnInfo(name = "requester_id")
    val requesterId: Long
)


/**
 * Defines the possible status values for a friendship.
 */
object FriendshipStatus {
    /**
     * Indicates that the friendship request is pending and awaiting acceptance.
     */
    const val PENDING  = "PENDING"

    /**
     * Indicates that the friendship request has been accepted.
     */
    const val ACCEPTED = "ACCEPTED"
}
