package dam_A51811.filmroulette.data.local

import androidx.room.TypeConverter
import dam_A51811.filmroulette.data.model.Genre

/**
 * Room [TypeConverter] for `List<Genre>`.
 *
 * Converts between a list of [Genre] enum constants and a comma-separated [String]
 * for storage in the database. Registered on [AppDatabase] via `@TypeConverters(GenreConverter::class)`.
 *
 * Example stored value: `"ACTION,COMEDY,THRILLER"`
 */
class GenreConverter {

    /**
     * Converts a [List] of [Genre] values to a comma-separated String for storage.
     * An empty list is stored as an empty string.
     */
    @TypeConverter
    fun fromGenres(genres: List<Genre>): String =
        genres.joinToString(separator = ",") { it.name }

    /**
     * Converts a stored comma-separated String back to a [List] of [Genre] values.
     * Unrecognised values are mapped to [Genre.OTHER]. An empty string yields an empty list.
     */
    @TypeConverter
    fun toGenres(value: String): List<Genre> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { token ->
            runCatching { Genre.valueOf(token.trim()) }.getOrDefault(Genre.OTHER)
        }
}
