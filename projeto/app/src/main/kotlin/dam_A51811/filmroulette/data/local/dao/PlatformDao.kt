package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.Platform
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatformDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(platform: Platform)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(platforms: List<Platform>)

    @Query("SELECT * FROM platforms WHERE id = :id")
    suspend fun getPlatformById(id: Long): Platform?

    @Query("SELECT * FROM platforms")
    fun getAllPlatforms(): Flow<List<Platform>>
}
