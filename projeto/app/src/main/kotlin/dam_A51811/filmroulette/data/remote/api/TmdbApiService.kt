package dam_A51811.filmroulette.data.remote.api

import dam_A51811.filmroulette.data.remote.dto.TmdbDiscoverResponse
import dam_A51811.filmroulette.data.remote.dto.TmdbMovieDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreIds: String?,
        @Query("with_runtime.lte") maxRuntime: Int?,
        @Query("with_watch_providers", encoded = true) providerIds: String?,
        @Query("watch_region") watchRegion: String? = "PT",
        @Query("with_original_language", encoded = true) languages: String? = null,
        @Query("primary_release_date.gte") releaseDateGte: String? = null,
        @Query("primary_release_date.lte") releaseDateLte: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("vote_count.gte") minVoteCount: Int? = null,
        @Query("vote_average.gte") minVoteAverage: Double? = null
    ): TmdbDiscoverResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long
    ): TmdbMovieDetailsDto
}
