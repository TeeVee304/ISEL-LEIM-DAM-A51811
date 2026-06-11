package dam_A51811.filmroulette.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TmdbMovieDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("release_date") val releaseDate: String?
)

data class TmdbDiscoverResponse(
    @SerializedName("results") val results: List<TmdbMovieDto>
)

data class TmdbGenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class TmdbMovieDetailsDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genres") val genres: List<TmdbGenreDto>?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("runtime") val runtime: Int?
)
