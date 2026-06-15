package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


/**
 * Represents a user's rating for a specific movie in the local database.
 *
 * @property userId The unique identifier of the user who submitted the rating.
 * @property movieId The unique identifier of the movie being rated.
 * @property rating The integer value of the rating given by the user.
 * @property ratedAt The timestamp indicating when the rating was submitted.
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

    
    val rating: Int,

    
    @ColumnInfo(name = "rated_at")
    val ratedAt: Long
)
