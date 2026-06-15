package dam_A51811.filmroulette.data.local

import androidx.room.TypeConverter
import dam_A51811.filmroulette.data.model.Genre


/**
 * Provides type conversion between a list of [Genre] and a string representation
 * for storage in the local database.
 */
class GenreConverter {

    /**
     * Converts a list of [Genre] into a comma-separated string.
     *
     * @param genres The list of genres to be converted.
     * @return A string containing the comma-separated names of the genres.
     */
    @TypeConverter
    fun fromGenres(genres: List<Genre>): String =
        genres.joinToString(separator = ",") { it.name }

    /**
     * Converts a comma-separated string into a list of [Genre].
     *
     * @param value The string containing comma-separated genre names.
     * @return A list of genres parsed from the string.
     */
    @TypeConverter
    fun toGenres(value: String): List<Genre> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { token ->
            runCatching { Genre.valueOf(token.trim()) }.getOrDefault(Genre.OTHER)
        }
}
