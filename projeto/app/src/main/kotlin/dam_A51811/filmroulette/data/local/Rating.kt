package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Room entity representing a user's rating of a movie.
 *
 * The composite primary key `(userId, movieId)` enforces that each user
 * can rate any given movie only once. To update a rating, use `INSERT OR REPLACE`.
 *
 * [rating] is constrained to [0, 10] at the application layer — Room does not
 * support CHECK constraints directly, so validation must be enforced in the DAO
 * or repository before insertion.
 *
 * Foreign keys cascade deletions: if a user or movie is deleted, their ratings
 * are removed automatically.
 */
@Entity(
    tableName = "ratings",
    primaryKeys = ["user_id", "movie_id"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Rating(
    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,

    @ColumnInfo(name = "movie_id", index = true)
    val movieId: Long,

    /**
     * User rating for the movie, in the range [0, 10].
     * Validated at the repository level before insertion.
     */
    val rating: Int,

    /**
     * Unix timestamp (milliseconds) of when the rating was last submitted or updated.
     */
    @ColumnInfo(name = "rated_at")
    val ratedAt: Long
)
