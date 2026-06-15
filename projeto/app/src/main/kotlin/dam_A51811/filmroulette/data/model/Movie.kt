package dam_A51811.filmroulette.data.model


/**
 * Represents a movie entity.
 *
 * @param id The unique identifier of the movie.
 * @param title The title of the movie.
 * @param duration The duration of the movie in minutes.
 * @param synopsys A brief summary of the movie's plot.
 * @param imgUrl The URL of the movie's poster image.
 * @param avgRating The average user rating of the movie.
 * @param releaseDate The release date of the movie, if available.
 * @param originalLanguage The original language code of the movie, if available.
 * @param genres The list of genres associated with the movie.
 */
data class Movie(
    val id: Long = 0L,
    val title: String = "",
    val duration: Int = 0,
    val synopsys: String = "",
    val imgUrl: String = "",
    val avgRating: Double = 0.0,
    val releaseDate: String? = null,
    val originalLanguage: String? = null,
    val genres: List<Genre> = emptyList()
)