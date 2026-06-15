package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.Movie
import dam_A51811.filmroulette.data.local.MoviePlatformCrossRef
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for managing movie entities.
 */
@Dao
interface MovieDAO {
    /**
     * Inserts a movie record. Replaces existing records on conflict.
     *
     * @param movie The movie entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    /**
     * Inserts a list of movie records. Replaces existing records on conflict.
     *
     * @param movies The list of movie entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    /**
     * Retrieves a specific movie by its ID.
     *
     * @param id The ID of the movie to retrieve.
     * @return The movie entity, or null if it does not exist.
     */
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Long): Movie?

    /**
     * Retrieves a stream of all stored movies.
     *
     * @return A flow emitting the list of all movie entities.
     */
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>
    
    /**
     * Deletes all movie records from the database.
     */
    @Query("DELETE FROM movies")
    suspend fun clearAll()

    /**
     * Inserts a cross-reference between a movie and a platform.
     * Ignores conflicts if the reference already exists.
     *
     * @param crossRef The cross-reference entity linking a movie to a platform.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoviePlatformCrossRef(crossRef: MoviePlatformCrossRef)
}
