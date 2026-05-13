package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.MovieList
import dam_A51811.filmroulette.data.local.MovieListCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(movieList: MovieList): Long

    @Query("SELECT * FROM movie_lists WHERE user_id = :userId")
    fun getUserLists(userId: Long): Flow<List<MovieList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieToList(crossRef: MovieListCrossRef)

    @Query("DELETE FROM movie_list_entries WHERE list_id = :listId AND movie_id = :movieId")
    suspend fun removeMovieFromList(listId: Long, movieId: Long)

    @Query("DELETE FROM movie_lists WHERE id = :listId")
    suspend fun deleteList(listId: Long)
}
