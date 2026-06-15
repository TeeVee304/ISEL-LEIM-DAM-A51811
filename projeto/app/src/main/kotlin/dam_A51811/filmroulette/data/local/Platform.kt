package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents a streaming or media platform entity in the local database.
 *
 * @property id The unique identifier of the platform.
 * @property name The display name of the platform.
 * @property logoUrl The URL pointing to the platform's logo image.
 */
@Entity(tableName = "platforms")
data class Platform(
    @PrimaryKey
    val id: Long,

    
    val name: String,

    
    @ColumnInfo(name = "logo_url")
    val logoUrl: String
)
