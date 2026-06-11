package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Room entity representing a named list of movies created by a user,
 * with a configurable [visibility] level.
 *
 * Named [MovieList] to avoid collision with [kotlin.collections.List].
 *
 * The films contained in the list are managed via the [MovieListCrossRef]
 * junction table (N:M relationship with [Movie]).
 *
 * Deleting the owning [User] cascades and removes all their lists.
 *
 * ### Firebase note
 * When Firebase is introduced, store this as a Firestore document
 * under `/users/{userId}/movie_lists/{listId}`. The [visibility]
 * field drives Firestore Security Rules for read access.
 */
@Entity(
    tableName = "movie_lists",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** The user who owns this list. */
    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,

    /** Display name of the list (e.g. "Ver mais tarde"). */
    val name: String,

    /**
     * Unix timestamp (milliseconds) of when the list was created.
     * Use [System.currentTimeMillis] at insertion time.
     */
    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    /**
     * Visibility level stored as a String (e.g. "PRIVATE", "FRIENDS_ONLY", "PUBLIC").
     *
     * Use [dam_A51811.filmroulette.data.utils.VisibilityMapper] to convert
     * to/from the [dam_A51811.filmroulette.data.model.Visibility] domain enum.
     *
     * Default is "PRIVATE" so that a new list is never accidentally exposed.
     */
    @ColumnInfo(name = "visibility")
    val visibility: String = "PRIVATE"
)
