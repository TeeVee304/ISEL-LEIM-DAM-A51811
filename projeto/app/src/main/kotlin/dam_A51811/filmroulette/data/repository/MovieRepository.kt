package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.data.model.Movie

interface MovieRepository {
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
