package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Cross-reference entity associating movies with movie lists.
 *
 * @property listId The unique identifier of the movie list.
 * @property movieId The unique identifier of the movie.
 * @property addedAt The timestamp indicating when the movie was added to the list.
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

    @ColumnInfo(name = "added_at")
    val addedAt: Long
)
