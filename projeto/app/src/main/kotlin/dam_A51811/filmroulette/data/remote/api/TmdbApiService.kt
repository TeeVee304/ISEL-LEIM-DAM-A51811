package dam_A51811.filmroulette.data.remote.api

import dam_A51811.filmroulette.data.remote.dto.TmdbDiscoverResponse
import dam_A51811.filmroulette.data.remote.dto.TmdbMovieDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the API endpoints for the TMDB (The Movie Database) service.
 */
interface TmdbApiService {
    /**
     * Discovers movies based on various filtering criteria.
     *
     * @param genreIds A comma-separated list of genre IDs to include.
     * @param maxRuntime The maximum runtime of the movie in minutes.
     * @param providerIds A pipe-separated or comma-separated list of watch provider IDs.
     * @param watchRegion The ISO 3166-1 code for the region to filter watch providers (e.g., "PT").
     * @param languages A pipe-separated or comma-separated list of original language ISO 639-1 codes.
     * @param releaseDateGte The minimum release date in YYYY-MM-DD format.
     * @param releaseDateLte The maximum release date in YYYY-MM-DD format.
     * @param includeAdult Whether to include adult (NSFW) content.
     * @param page The page number of the results to retrieve.
     * @param minVoteCount The minimum number of votes required for a movie.
     * @param minVoteAverage The minimum average vote score required for a movie.
     * @return A response containing a list of discovered movies.
     */
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

    /**
     * Retrieves the details of a specific movie by its ID.
     *
     * @param movieId The unique identifier of the movie.
     * @return A data transfer object containing the movie details.
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long
    ): TmdbMovieDetailsDto
}
