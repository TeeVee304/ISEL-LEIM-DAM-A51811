package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a streaming platform (e.g. Netflix, HBO Max).
 *
 * The [id] is supplied by the remote API.
 * The relationship between platforms and movies is N:M and is modelled
 * via the [MoviePlatformCrossRef] junction table.
 */
@Entity(tableName = "platforms")
data class Platform(
    @PrimaryKey
    val id: Long,

    /** Display name of the platform. */
    val name: String,

    /** URL of the platform's logo image. */
    @ColumnInfo(name = "logo_url")
    val logoUrl: String
)
