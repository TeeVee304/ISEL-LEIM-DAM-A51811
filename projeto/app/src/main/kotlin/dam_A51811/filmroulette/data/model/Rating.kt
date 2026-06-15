package dam_A51811.filmroulette.data.model

/**
 * Represents a rating given to a movie by a user.
 *
 * @property userId The unique identifier of the user who rated the movie.
 * @property movieId The unique identifier of the rated movie.
 * @property rating The integer value of the rating given by the user.
 * @property ratedAt The timestamp indicating when the rating was submitted, in milliseconds since the epoch.
 */
data class Rating(
    val userId: Long,
    val movieId: Long,
    val rating: Int,
    val ratedAt: Long
)