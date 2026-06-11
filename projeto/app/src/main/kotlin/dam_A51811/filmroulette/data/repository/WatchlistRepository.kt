package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.model.MovieList
import dam_A51811.filmroulette.data.model.Visibility
import kotlinx.coroutines.flow.Flow

/**
 * Contract for managing [MovieList] (watchlists), including visibility-aware access.
 *
 * ### Visibility rules (enforced by this repository, not by the DAO alone)
 *
 * | Viewer role        | Lists returned                           |
 * |--------------------|------------------------------------------|
 * | Owner              | ALL (PRIVATE + FRIENDS_ONLY + PUBLIC)    |
 * | Accepted friend    | FRIENDS_ONLY + PUBLIC of that owner      |
 * | Anyone else        | PUBLIC only                              |
 *
 * ### Firebase note
 * When Firebase is introduced, Firestore Security Rules will enforce
 * these same tiers server-side. The repository implementation will
 * delegate to Firestore queries and keep Room as an offline cache.
 */
interface WatchlistRepository {

    /** Creates a new watchlist for [userId] with the given [name] and [visibility]. */
    suspend fun createList(userId: Long, name: String, visibility: Visibility): Long

    /** Permanently deletes list [listId]. */
    suspend fun deleteList(listId: Long)

    /** Changes the visibility of [listId] to [visibility]. */
    suspend fun updateVisibility(listId: Long, visibility: Visibility)

    /** Adds movie [movieId] to list [listId]. */
    suspend fun addMovieToList(listId: Long, movieId: Long)

    /** Removes movie [movieId] from list [listId]. */
    suspend fun removeMovieFromList(listId: Long, movieId: Long)

    // ─── Visibility-aware read operations ──────────────────────────────────

    /**
     * Returns ALL lists of [userId] (owner view).
     * Should only be called when the viewer IS [userId].
     */
    fun getOwnLists(userId: Long): Flow<List<MovieList>>

    /**
     * Returns lists of [ownerId] visible to an **accepted friend**.
     * Includes FRIENDS_ONLY and PUBLIC.
     *
     * The caller must have already confirmed the friendship before invoking this.
     */
    fun getFriendLists(ownerId: Long): Flow<List<MovieList>>

    /**
     * Returns only PUBLIC lists of [ownerId].
     * Safe to call for any viewer, including non-friends.
     */
    fun getPublicLists(ownerId: Long): Flow<List<MovieList>>

    /**
     * Returns all PUBLIC lists across the entire app — for a discovery feed.
     */
    fun getAllPublicLists(): Flow<List<MovieList>>
}
