package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Junction table for the N:M relationship between [MovieList] and [Movie].
 *
 * Each row records that a given movie belongs to a given list.
 * The composite primary key `(listId, movieId)` prevents a movie from
 * being added to the same list twice.
 *
 * Deleting either a [MovieList] or a [Movie] cascades and cleans up
 * the corresponding cross-reference rows automatically.
 */
@Entity(
    tableName = "movie_list_entries",
    primaryKeys = ["list_id", "movie_id"],
    foreignKeys = [
        ForeignKey(
            entity = MovieList::class,
            parentColumns = ["id"],
            childColumns = ["list_id"],
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
data class MovieListCrossRef(
    @ColumnInfo(name = "list_id", index = true)
    val listId: Long,

    @ColumnInfo(name = "movie_id", index = true)
    val movieId: Long,

    /**
     * Unix timestamp (milliseconds) of when the movie was added to the list.
     * Useful for sorting entries by insertion order.
     */
    @ColumnInfo(name = "added_at")
    val addedAt: Long
)
