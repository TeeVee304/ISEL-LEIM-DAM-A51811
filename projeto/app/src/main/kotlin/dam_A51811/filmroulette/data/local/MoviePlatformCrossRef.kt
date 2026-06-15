package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Cross-reference entity associating movies with streaming platforms.
 *
 * @property movieId The unique identifier of the movie.
 * @property platformId The unique identifier of the platform.
 */
@Entity(
    tableName = "movie_platform",
    primaryKeys = ["movie_id", "platform_id"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Platform::class,
            parentColumns = ["id"],
            childColumns = ["platform_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MoviePlatformCrossRef(
    @ColumnInfo(name = "movie_id", index = true)
    val movieId: Long,

    @ColumnInfo(name = "platform_id", index = true)
    val platformId: Long
)
