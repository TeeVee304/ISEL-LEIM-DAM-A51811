package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.data.model.Movie

/**
 * Repository interface for fetching movie recommendations from a remote or local data source.
 */
interface MovieRepository {
    /**
     * Retrieves a list of movie recommendations based on the provided filters.
     *
     * @param maxDuration The maximum runtime duration in minutes.
     * @param genres A list of [Genre]s to include in the recommendations.
     * @param languages A list of ISO 639-1 language codes to filter by (optional).
     * @param releaseDateGte The earliest release date to include, formatted as "YYYY-MM-DD" (optional).
     * @param releaseDateLte The latest release date to include, formatted as "YYYY-MM-DD" (optional).
     * @param providerIds A comma-separated string of streaming provider IDs to filter by (optional).
     * @param seenMovieIds A set of movie IDs to exclude from the recommendations (optional).
     * @return A list of [Movie] objects matching the criteria.
     */
    suspend fun getRecommendations(
        maxDuration: Int,
        genres: List<Genre>,
        languages: List<String> = emptyList(),
        releaseDateGte: String? = null,
        releaseDateLte: String? = null,
        providerIds: String? = null,
        seenMovieIds: Set<Long> = emptySet()
    ): List<Movie>
}
