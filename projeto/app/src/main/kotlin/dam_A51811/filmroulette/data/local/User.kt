package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the local database.
 *
 * @property id The unique identifier of the user.
 * @property username The display name of the user.
 * @property email The unique email address associated with the user.
 * @property passwordHash The hashed password of the user.
 * @property registryDate The timestamp indicating when the user registered.
 * @property avatarUrl The optional URL pointing to the user's avatar image.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password_hash")
    val passwordHash: String,

    @ColumnInfo(name = "registry_date")
    val registryDate: Long,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null
)
