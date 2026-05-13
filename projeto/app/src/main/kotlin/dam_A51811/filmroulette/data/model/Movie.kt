package dam_A51811.filmroulette.data.model

/**
 * Domain model representing a movie.
 *
 * This is a plain Kotlin class used throughout the application layer (ViewModel, UI).
 * It is decoupled from Room — the persistence counterpart is [dam_A51811.filmroulette.data.local.Movie].
 *
 * @property id        Unique identifier supplied by the remote API.
 * @property title     Title of the movie.
 * @property duration  Duration in minutes.
 * @property synopsys  Plot summary.
 * @property imgUrl    URL of the movie poster.
 * @property avgRating Global average rating.
 * @property genres    List of genres that describe this movie.
 */
data class Movie(
    val id: Long,
    val title: String,
    val duration: Int,
    val synopsys: String,
    val imgUrl: String,
    val avgRating: Double,
    val genres: List<Genre>
)