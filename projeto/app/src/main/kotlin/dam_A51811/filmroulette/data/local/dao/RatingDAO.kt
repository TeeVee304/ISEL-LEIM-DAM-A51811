package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.Rating
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for performing database operations on the ratings table.
 */
@Dao
interface RatingDAO {
    /**
     * Inserts a single rating into the database.
     * Replaces the existing rating if a conflict occurs.
     *
     * @param rating The rating entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: Rating)

    /**
     * Retrieves all ratings submitted by a specific user as a reactive stream.
     *
     * @param userId The unique identifier of the user.
     * @return A flow emitting the list of rating entities associated with the user.
     */
    @Query("SELECT * FROM ratings WHERE user_id = :userId")
    fun getUserRatings(userId: Long): Flow<List<Rating>>

    /**
     * Retrieves all ratings for a specific movie as a reactive stream.
     *
     * @param movieId The unique identifier of the movie.
     * @return A flow emitting the list of rating entities associated with the movie.
     */
    @Query("SELECT * FROM ratings WHERE movie_id = :movieId")
    fun getMovieRatings(movieId: Long): Flow<List<Rating>>

    /**
     * Retrieves a specific rating given a user identifier and a movie identifier.
     *
     * @param userId The unique identifier of the user.
     * @param movieId The unique identifier of the movie.
     * @return The rating entity if found, or null otherwise.
     */
    @Query("SELECT * FROM ratings WHERE user_id = :userId AND movie_id = :movieId LIMIT 1")
    suspend fun getRating(userId: Long, movieId: Long): Rating?
}
