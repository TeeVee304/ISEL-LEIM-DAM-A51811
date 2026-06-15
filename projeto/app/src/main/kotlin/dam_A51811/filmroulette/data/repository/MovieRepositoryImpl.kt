package dam_A51811.filmroulette.data.repository

import android.util.Log
import dam_A51811.filmroulette.data.local.dao.MovieDAO
import dam_A51811.filmroulette.data.model.Genre
import dam_A51811.filmroulette.data.model.Movie
import dam_A51811.filmroulette.data.remote.api.TmdbApiService
import dam_A51811.filmroulette.data.utils.GenreMapper
import dam_A51811.filmroulette.data.utils.MovieMapper.toLocalMovie
import dam_A51811.filmroulette.data.utils.MovieMapper.toDomainMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Implementation of the MovieRepository interface.
 * Responsible for fetching movie recommendations either from the TMDB API or from a local database cache.
 *
 * @param apiService The TMDB API service for fetching remote movies.
 * @param movieDao The Data Access Object for local movie storage.
 */
class MovieRepositoryImpl(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDAO
) : MovieRepository {

    companion object {
        
        private const val MIN_VOTE_COUNT   = 150
        private const val MIN_VOTE_AVERAGE = 5.5

        
        
        private const val MAX_PAGE_WINDOW  = 8

        
        
        private const val RANDOM_WEIGHT    = 0.5
    }

    /**
     * Retrieves a list of recommended movies based on user-defined criteria.
     * Fetches from the network and caches the results, falling back to local cache on error.
     *
     * @param maxDuration The maximum duration of the movie in minutes.
     * @param genres A list of preferred genres.
     * @param languages A list of preferred languages.
     * @param releaseDateGte The earliest release date in YYYY-MM-DD format.
     * @param releaseDateLte The latest release date in YYYY-MM-DD format.
     * @param providerIds Comma-separated list of watch provider IDs.
     * @param seenMovieIds A set of movie IDs that the user has already seen and should be excluded.
     * @return A randomized list of movies matching the criteria.
     */
    override suspend fun getRecommendations(
        maxDuration: Int,
        genres: List<Genre>,
        languages: List<String>,
        releaseDateGte: String?,
        releaseDateLte: String?,
        providerIds: String?,
        seenMovieIds: Set<Long>
    ): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val tmdbGenreIds = genres.mapNotNull { GenreMapper.toTmdbId(it) }
            val withGenres   = if (tmdbGenreIds.isEmpty()) null else tmdbGenreIds.joinToString(",")
            val withLanguages = if (languages.isEmpty()) null else languages.joinToString("|")
            val maxRuntime   = if (maxDuration == Int.MAX_VALUE) null else maxDuration

            
            val probe = apiService.discoverMovies(
                genreIds       = withGenres,
                maxRuntime     = maxRuntime,
                providerIds    = providerIds,
                languages      = withLanguages,
                releaseDateGte = releaseDateGte,
                releaseDateLte = releaseDateLte,
                minVoteCount   = MIN_VOTE_COUNT,
                minVoteAverage = MIN_VOTE_AVERAGE,
                page           = 1
            )

            val totalPages  = probe.totalPages.coerceAtMost(MAX_PAGE_WINDOW).coerceAtLeast(1)
            val startPage   = Random.nextInt(1, totalPages + 1)   
            val secondPage  = if (startPage < totalPages) startPage + 1 else 1

            
            val rawResults = probe.results.toMutableList()

            if (startPage != 1) {
                val page1 = apiService.discoverMovies(
                    genreIds       = withGenres,
                    maxRuntime     = maxRuntime,
                    providerIds    = providerIds,
                    languages      = withLanguages,
                    releaseDateGte = releaseDateGte,
                    releaseDateLte = releaseDateLte,
                    minVoteCount   = MIN_VOTE_COUNT,
                    minVoteAverage = MIN_VOTE_AVERAGE,
                    page           = startPage
                )
                rawResults.clear()
                rawResults.addAll(page1.results)
            }

            if (totalPages > 1) {
                val page2 = apiService.discoverMovies(
                    genreIds       = withGenres,
                    maxRuntime     = maxRuntime,
                    providerIds    = providerIds,
                    languages      = withLanguages,
                    releaseDateGte = releaseDateGte,
                    releaseDateLte = releaseDateLte,
                    minVoteCount   = MIN_VOTE_COUNT,
                    minVoteAverage = MIN_VOTE_AVERAGE,
                    page           = secondPage
                )
                rawResults.addAll(page2.results)
            }

            
            val pool = mutableListOf<Movie>()

            for (movieDto in rawResults) {
                
                if (movieDto.id in seenMovieIds) continue

                var localMovie = movieDao.getMovieById(movieDto.id)
                if (localMovie == null) {
                    try {
                        val details = apiService.getMovieDetails(movieDto.id)
                        localMovie  = details.toLocalMovie()
                        movieDao.insert(localMovie)
                    } catch (e: Exception) {
                        Log.e("MovieRepository", "Failed to fetch details for ${movieDto.id}", e)
                    }
                }
                if (localMovie != null) {
                    pool.add(localMovie.toDomainMovie())
                }
            }

            
            
            
            val shuffled = pool.sortedByDescending { movie ->
                (movie.avgRating / 10.0) + Random.nextDouble(0.0, RANDOM_WEIGHT)
            }

            if (shuffled.isNotEmpty()) return@withContext shuffled

        } catch (e: Exception) {
            Log.e("MovieRepository", "API Error, falling back to local cache", e)
        }

        
        val cachedMovies = movieDao.getAllMovies().firstOrNull() ?: emptyList()
        val filtered = cachedMovies.filter { movie ->
            val durationOk = movie.duration == 0 || movie.duration <= maxDuration
            val genreOk    = genres.isEmpty() || movie.genres.any { it in genres }
            val langOk     = languages.isEmpty() || (movie.originalLanguage != null && movie.originalLanguage in languages)
            val dateGteOk  = releaseDateGte == null || (movie.releaseDate != null && movie.releaseDate >= releaseDateGte)
            val dateLteOk  = releaseDateLte == null || (movie.releaseDate != null && movie.releaseDate <= releaseDateLte)
            val notSeen    = movie.id !in seenMovieIds
            durationOk && genreOk && langOk && dateGteOk && dateLteOk && notSeen
        }
        return@withContext filtered.map { it.toDomainMovie() }.shuffled()
    }
}
