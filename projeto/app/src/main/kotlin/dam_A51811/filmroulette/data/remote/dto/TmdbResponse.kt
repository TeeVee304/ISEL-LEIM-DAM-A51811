package dam_A51811.filmroulette.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object representing a movie retrieved from the TMDB API.
 *
 * @property id The unique identifier of the movie.
 * @property title The title of the movie.
 * @property overview A brief summary or description of the movie.
 * @property posterPath The path to the poster image.
 * @property voteAverage The average vote score from users.
 * @property genreIds A list of genre identifiers associated with the movie.
 * @property releaseDate The release date of the movie in YYYY-MM-DD format.
 * @property originalLanguage The ISO 639-1 code of the original language.
 */
data class TmdbMovieDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("original_language") val originalLanguage: String?
)

/**
 * Data transfer object representing the response from the TMDB discover endpoint.
 *
 * @property results A list of movies matching the discover criteria.
 * @property totalPages The total number of pages available for the given criteria.
 */
data class TmdbDiscoverResponse(
    @SerializedName("results") val results: List<TmdbMovieDto>,
    @SerializedName("total_pages") val totalPages: Int = 1
)

/**
 * Data transfer object representing a movie genre.
 *
 * @property id The unique identifier of the genre.
 * @property name The name of the genre.
 */
data class TmdbGenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

/**
 * Data transfer object representing detailed information about a specific movie.
 *
 * @property id The unique identifier of the movie.
 * @property title The title of the movie.
 * @property overview A brief summary or description of the movie.
 * @property posterPath The path to the poster image.
 * @property voteAverage The average vote score from users.
 * @property genres A list of genres associated with the movie.
 * @property releaseDate The release date of the movie in YYYY-MM-DD format.
 * @property runtime The runtime of the movie in minutes.
 * @property originalLanguage The ISO 639-1 code of the original language.
 */
data class TmdbMovieDetailsDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genres") val genres: List<TmdbGenreDto>?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("original_language") val originalLanguage: String?
)
