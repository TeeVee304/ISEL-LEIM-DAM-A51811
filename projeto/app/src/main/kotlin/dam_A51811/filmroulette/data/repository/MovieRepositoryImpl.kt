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

class MovieRepositoryImpl(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDAO
) : MovieRepository {

    override suspend fun getRecommendations(maxDuration: Int, genres: List<Genre>): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val tmdbGenreIds = genres.mapNotNull { GenreMapper.toTmdbId(it) }
            val withGenres = if (tmdbGenreIds.isEmpty()) null else tmdbGenreIds.joinToString(",")

            val response = apiService.discoverMovies(
                genreIds = withGenres,
                maxRuntime = maxDuration,
                providerIds = null
            )

            val results = response.results
            val recommendations = mutableListOf<Movie>()

            for (movieDto in results) {
                var localMovie = movieDao.getMovieById(movieDto.id)
                if (localMovie == null) {
                    try {
                        val details = apiService.getMovieDetails(movieDto.id)
                        localMovie = details.toLocalMovie()
                        movieDao.insert(localMovie)
                    } catch (e: Exception) {
                        Log.e("MovieRepository", "Failed to fetch details for movie ${movieDto.id}", e)
                    }
                }
                
                if (localMovie != null) {
                    recommendations.add(localMovie.toDomainMovie())
                }
            }

            if (recommendations.isNotEmpty()) {
                return@withContext recommendations
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "API Error, falling back to local database cache", e)
        }

        val cachedMovies = movieDao.getAllMovies().firstOrNull() ?: emptyList()
        val filtered = cachedMovies.filter { movie ->
            movie.duration <= maxDuration && (genres.isEmpty() || movie.genres.any { it in genres })
        }
        return@withContext filtered.map { it.toDomainMovie() }
    }
}
