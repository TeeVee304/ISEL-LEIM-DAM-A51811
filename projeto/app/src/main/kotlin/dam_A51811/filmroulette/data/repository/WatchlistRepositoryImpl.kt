package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.local.MovieListCrossRef
import dam_A51811.filmroulette.data.local.dao.MovieListDAO
import dam_A51811.filmroulette.data.model.MovieList
import dam_A51811.filmroulette.data.model.Visibility
import dam_A51811.filmroulette.data.utils.VisibilityMapper
import dam_A51811.filmroulette.data.utils.WatchlistMapper.toDomainMovieList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Room-backed implementation of [WatchlistRepository].
 *
 * Converts between the local [dam_A51811.filmroulette.data.local.MovieList]
 * entity (String visibility) and the domain [MovieList] model (enum [Visibility])
 * via [VisibilityMapper] and [WatchlistMapper].
 *
 * ### Firebase note
 * When Firebase is introduced, create `WatchlistRepositoryFirebase` implementing
 * the same interface.  Firestore Security Rules will enforce the visibility tiers
 * at the server level, complementing the client-side logic here.
 */
class WatchlistRepositoryImpl(
    private val movieListDao: MovieListDAO
) : WatchlistRepository {

    // ─────────────────────────────────────────────────────
    // Write operations
    // ─────────────────────────────────────────────────────

    override suspend fun createList(userId: Long, name: String, visibility: Visibility): Long =
        withContext(Dispatchers.IO) {
            val entity = dam_A51811.filmroulette.data.local.MovieList(
                userId     = userId,
                name       = name,
                createdAt  = System.currentTimeMillis(),
                visibility = VisibilityMapper.toString(visibility)
            )
            movieListDao.insertList(entity)
        }

    override suspend fun deleteList(listId: Long) =
        withContext(Dispatchers.IO) { movieListDao.deleteList(listId) }

    override suspend fun updateVisibility(listId: Long, visibility: Visibility) =
        withContext(Dispatchers.IO) {
            movieListDao.updateVisibility(listId, VisibilityMapper.toString(visibility))
        }

    override suspend fun addMovieToList(listId: Long, movieId: Long) =
        withContext(Dispatchers.IO) {
            movieListDao.insertMovieToList(
                MovieListCrossRef(listId = listId, movieId = movieId, addedAt = System.currentTimeMillis())
            )
        }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) =
        withContext(Dispatchers.IO) { movieListDao.removeMovieFromList(listId, movieId) }

    // ─────────────────────────────────────────────────────
    // Read operations (visibility-aware)
    // ─────────────────────────────────────────────────────

    override fun getOwnLists(userId: Long): Flow<List<MovieList>> =
        movieListDao.getUserLists(userId).map { list -> list.map { it.toDomainMovieList() } }

    override fun getFriendLists(ownerId: Long): Flow<List<MovieList>> =
        movieListDao.getFriendVisibleLists(ownerId).map { list -> list.map { it.toDomainMovieList() } }

    override fun getPublicLists(ownerId: Long): Flow<List<MovieList>> =
        movieListDao.getPublicLists(ownerId).map { list -> list.map { it.toDomainMovieList() } }

    override fun getAllPublicLists(): Flow<List<MovieList>> =
        movieListDao.getAllPublicLists().map { list -> list.map { it.toDomainMovieList() } }
}
