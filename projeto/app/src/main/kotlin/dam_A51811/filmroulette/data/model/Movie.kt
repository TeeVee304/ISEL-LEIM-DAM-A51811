package dam_A51811.filmroulette.data.model

/**
 * Domain model representing a movie.
 *
 * This is a plain Kotlin class used throughout the application layer (ViewModel, UI).
 * It is decoupled from Room — the persistence counterpart is [dam_A51811.filmroulette.data.local.Movie].
 *
 * @property id               Unique identifier supplied by the remote API.
 * @property title            Title of the movie.
 * @property duration         Duration in minutes.
 * @property synopsys         Plot summary.
 * @property imgUrl           URL of the movie poster.
 * @property avgRating        Global average rating (0.0–10.0).
 * @property releaseDate      Release date in ISO 8601 format ("YYYY-MM-DD"), or null if unknown.
 * @property originalLanguage ISO 639-1 language code (e.g. "en", "pt"), or null if unknown.
 * @property genres           List of genres that describe this movie.
 */
data class Movie(
    val id: Long,
    val title: String,
    val duration: Int,
    val synopsys: String,
    val imgUrl: String,
    val avgRating: Double,
    val releaseDate: String?,
    val originalLanguage: String?,
    val genres: List<Genre>
)