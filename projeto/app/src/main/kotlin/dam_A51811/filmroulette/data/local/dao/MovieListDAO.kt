package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.MovieList
import dam_A51811.filmroulette.data.local.MovieListCrossRef
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for managing custom movie lists.
 */
@Dao
interface MovieListDAO {

    
    /**
     * Inserts a new movie list. Replaces existing list on conflict.
     *
     * @param movieList The movie list entity to insert.
     * @return The ID of the newly inserted movie list.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(movieList: MovieList): Long

    /**
     * Inserts a cross-reference linking a movie to a specific movie list.
     *
     * @param crossRef The cross-reference entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieToList(crossRef: MovieListCrossRef)

    /**
     * Removes a specific movie from a given movie list.
     *
     * @param listId The ID of the movie list.
     * @param movieId The ID of the movie to remove.
     */
    @Query("DELETE FROM movie_list_entries WHERE list_id = :listId AND movie_id = :movieId")
    suspend fun removeMovieFromList(listId: Long, movieId: Long)

    /**
     * Deletes a movie list by its ID.
     *
     * @param listId The ID of the movie list to delete.
     */
    @Query("DELETE FROM movie_lists WHERE id = :listId")
    suspend fun deleteList(listId: Long)

    /**
     * Updates the visibility status of a movie list.
     *
     * @param listId The ID of the movie list to update.
     * @param visibility The new visibility status.
     */
    @Query("UPDATE movie_lists SET visibility = :visibility WHERE id = :listId")
    suspend fun updateVisibility(listId: Long, visibility: String)

    
    
    /**
     * Retrieves a stream of all movie lists owned by a specific user.
     *
     * @param userId The ID of the user whose lists are to be retrieved.
     * @return A flow emitting the user's movie lists ordered by creation date descending.
     */
    @Query("SELECT * FROM movie_lists WHERE user_id = :userId ORDER BY created_at DESC")
    fun getUserLists(userId: Long): Flow<List<MovieList>>

    
    
    /**
     * Retrieves a stream of movie lists owned by a specific user that are visible to friends.
     *
     * @param ownerId The ID of the user whose friend-visible lists are to be retrieved.
     * @return A flow emitting the friend-visible movie lists ordered by creation date descending.
     */
    @Query("""
        SELECT * FROM movie_lists
        WHERE user_id = :ownerId
          AND visibility IN ('PUBLIC', 'FRIENDS_ONLY')
        ORDER BY created_at DESC
    """)
    fun getFriendVisibleLists(ownerId: Long): Flow<List<MovieList>>

    
    
    /**
     * Retrieves a stream of public movie lists owned by a specific user.
     *
     * @param ownerId The ID of the user whose public lists are to be retrieved.
     * @return A flow emitting the public movie lists ordered by creation date descending.
     */
    @Query("""
        SELECT * FROM movie_lists
        WHERE user_id = :ownerId
          AND visibility = 'PUBLIC'
        ORDER BY created_at DESC
    """)
    fun getPublicLists(ownerId: Long): Flow<List<MovieList>>

    
    /**
     * Retrieves a stream of all public movie lists across all users.
     *
     * @return A flow emitting all public movie lists ordered by creation date descending.
     */
    @Query("""
        SELECT * FROM movie_lists
        WHERE visibility = 'PUBLIC'
        ORDER BY created_at DESC
    """)
    fun getAllPublicLists(): Flow<List<MovieList>>

    /**
     * Retrieves a stream of movies contained within a specific movie list.
     *
     * @param listId The ID of the movie list.
     * @return A flow emitting the list of movies ordered by the time they were added.
     */
    @Query("""
        SELECT movies.* FROM movies
        INNER JOIN movie_list_entries ON movies.id = movie_list_entries.movie_id
        WHERE movie_list_entries.list_id = :listId
        ORDER BY movie_list_entries.added_at ASC
    """)
    fun getMoviesForList(listId: Long): Flow<List<dam_A51811.filmroulette.data.local.Movie>>
}
