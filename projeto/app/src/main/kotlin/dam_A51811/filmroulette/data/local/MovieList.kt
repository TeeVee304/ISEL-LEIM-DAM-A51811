package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a custom movie list created by a user.
 *
 * @property id The unique identifier of the movie list.
 * @property userId The identifier of the user who owns the list.
 * @property name The name of the movie list.
 * @property createdAt The timestamp indicating when the list was created.
 * @property visibility The visibility status of the list.
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

    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,

    val name: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "visibility")
    val visibility: String = "PRIVATE"
)
