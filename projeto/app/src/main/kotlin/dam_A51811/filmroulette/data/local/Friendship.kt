package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Room entity representing a bilateral friendship between two users.
 *
 * A friendship always needs to be accepted by the receiving party before
 * either user can see each other's [MovieList]s with visibility [dam_A51811.filmroulette.data.model.Visibility.FRIENDS_ONLY].
 *
 * ### Friendship lifecycle
 * ```
 * User A sends request → status = "PENDING"  (userId1 = A, userId2 = B)
 * User B accepts       → status = "ACCEPTED"
 * Either party deletes → row is removed
 * ```
 *
 * ### Ordering convention
 * To avoid duplicate rows (A→B and B→A), [userId1] **must always be
 * the smaller** of the two IDs. The repository enforces this invariant
 * when inserting or querying.
 *
 * ### Firebase note
 * When Firebase is introduced, mirror this table as a Firestore
 * sub-collection (`/friendships/{docId}`) with the same fields.
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
    /** The smaller of the two user IDs (canonical ordering). */
    @ColumnInfo(name = "user_id_1")
    val userId1: Long,

    /** The larger of the two user IDs (canonical ordering). */
    @ColumnInfo(name = "user_id_2")
    val userId2: Long,

    /**
     * Current state of the friendship.
     * Use [FriendshipStatus] constants for safety.
     */
    @ColumnInfo(name = "status")
    val status: String = FriendshipStatus.PENDING,

    /**
     * Who sent the original friend request.
     * Needed to correctly display "You sent a request to X" vs "X sent you a request".
     */
    @ColumnInfo(name = "requester_id")
    val requesterId: Long
)

/** String constants for the [Friendship.status] column. */
object FriendshipStatus {
    const val PENDING  = "PENDING"
    const val ACCEPTED = "ACCEPTED"
}
