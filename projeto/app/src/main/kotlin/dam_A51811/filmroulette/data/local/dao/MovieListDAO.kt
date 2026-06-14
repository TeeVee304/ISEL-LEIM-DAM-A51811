package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.MovieList
import dam_A51811.filmroulette.data.local.MovieListCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieListDAO {

    // =====< ESCRITA >=====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(movieList: MovieList): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieToList(crossRef: MovieListCrossRef)

    @Query("DELETE FROM movie_list_entries WHERE list_id = :listId AND movie_id = :movieId")
    suspend fun removeMovieFromList(listId: Long, movieId: Long)

    @Query("DELETE FROM movie_lists WHERE id = :listId")
    suspend fun deleteList(listId: Long)

    @Query("UPDATE movie_lists SET visibility = :visibility WHERE id = :listId")
    suspend fun updateVisibility(listId: Long, visibility: String)

    // =====> QUERIES - Vista do Utilizador >=====
    /** All lists belonging to [userId], ordered by creation date descending. */
    @Query("SELECT * FROM movie_lists WHERE user_id = :userId ORDER BY created_at DESC")
    fun getUserLists(userId: Long): Flow<List<MovieList>>

    // =====< QUERIES - Vista de Amigos >======
    /**
     * Lists of [ownerId] that a **friend** can see:
     * those with visibility PUBLIC or FRIENDS_ONLY.
     *
     * The caller (repository) is responsible for first confirming
     * that the viewer is indeed an accepted friend of [ownerId].
     */
    @Query("""
        SELECT * FROM movie_lists
        WHERE user_id = :ownerId
          AND visibility IN ('PUBLIC', 'FRIENDS_ONLY')
        ORDER BY created_at DESC
    """)
    fun getFriendVisibleLists(ownerId: Long): Flow<List<MovieList>>

    // ======< QUERIES - Vista Pública >=====
    /** Lists of [ownerId] that any user (or anonymous viewer) can see. */
    @Query("""
        SELECT * FROM movie_lists
        WHERE user_id = :ownerId
          AND visibility = 'PUBLIC'
        ORDER BY created_at DESC
    """)
    fun getPublicLists(ownerId: Long): Flow<List<MovieList>>

    /**
     * All PUBLIC lists across all users – useful for a discovery feed.
     * Ordered by most recently created first.
     */
    @Query("""
        SELECT * FROM movie_lists
        WHERE visibility = 'PUBLIC'
        ORDER BY created_at DESC
    """)
    fun getAllPublicLists(): Flow<List<MovieList>>
}
