package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.Rating
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: Rating)

    @Query("SELECT * FROM ratings WHERE user_id = :userId")
    fun getUserRatings(userId: Long): Flow<List<Rating>>

    @Query("SELECT * FROM ratings WHERE movie_id = :movieId")
    fun getMovieRatings(movieId: Long): Flow<List<Rating>>

    @Query("SELECT * FROM ratings WHERE user_id = :userId AND movie_id = :movieId LIMIT 1")
    suspend fun getRating(userId: Long, movieId: Long): Rating?
}
