package dam_A51811.filmroulette.data.repository

import dam_A51811.filmroulette.data.model.MovieList
import dam_A51811.filmroulette.data.model.Visibility
import kotlinx.coroutines.flow.Flow


/**
 * Repository interface for managing custom movie watchlists.
 */
interface WatchlistRepository {

    /**
     * Creates a new movie watchlist.
     *
     * @param userId The ID of the user creating the list.
     * @param name The name of the new list.
     * @param visibility The visibility level of the list.
     * @return The unique identifier of the newly created list.
     */
    suspend fun createList(userId: String, name: String, visibility: Visibility): String

    /**
     * Deletes an existing movie watchlist.
     *
     * @param listId The ID of the list to delete.
     */
    suspend fun deleteList(listId: String)

    /**
     * Updates the visibility of an existing watchlist.
     *
     * @param listId The ID of the list to update.
     * @param visibility The new visibility level.
     */
    suspend fun updateVisibility(listId: String, visibility: Visibility)

    /**
     * Adds a movie to a specific watchlist.
     *
     * @param listId The ID of the watchlist.
     * @param movieId The ID of the movie to add.
     */
    suspend fun addMovieToList(listId: String, movieId: Long)

    /**
     * Removes a movie from a specific watchlist.
     *
     * @param listId The ID of the watchlist.
     * @param movieId The ID of the movie to remove.
     */
    suspend fun removeMovieFromList(listId: String, movieId: Long)

    /**
     * Retrieves all watchlists owned by a specific user.
     *
     * @param userId The ID of the user.
     * @return A flow emitting the user's lists.
     */
    fun getOwnLists(userId: String): Flow<List<MovieList>>

    /**
     * Retrieves watchlists owned by a friend, typically excluding private lists.
     *
     * @param ownerId The ID of the friend.
     * @return A flow emitting the friend's visible lists.
     */
    fun getFriendLists(ownerId: String): Flow<List<MovieList>>

    /**
     * Retrieves purely public watchlists owned by a specific user.
     *
     * @param ownerId The ID of the user.
     * @return A flow emitting the public lists.
     */
    fun getPublicLists(ownerId: String): Flow<List<MovieList>>

    /**
     * Retrieves all public watchlists across the application.
     *
     * @return A flow emitting all public lists.
     */
    fun getAllPublicLists(): Flow<List<MovieList>>

    /**
     * Retrieves all movies belonging to a specific watchlist.
     *
     * @param listId The ID of the watchlist.
     * @return A flow emitting the movies in the list.
     */
    fun getMoviesForList(listId: String): Flow<List<dam_A51811.filmroulette.data.model.Movie>>
}
