package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Junction table for the N:M relationship between [Movie] and [Platform].
 *
 * Each row records that a given movie is available on a given platform.
 * The composite primary key `(movieId, platformId)` prevents duplicates.
 *
 * Foreign keys cascade deletions so orphan rows are never left behind.
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
