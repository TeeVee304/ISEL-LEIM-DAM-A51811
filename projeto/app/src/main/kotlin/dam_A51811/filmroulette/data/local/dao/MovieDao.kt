package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dam_A51811.filmroulette.data.local.Movie
import dam_A51811.filmroulette.data.local.MoviePlatformCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Long): Movie?

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>
    
    @Query("DELETE FROM movies")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoviePlatformCrossRef(crossRef: MoviePlatformCrossRef)
}
