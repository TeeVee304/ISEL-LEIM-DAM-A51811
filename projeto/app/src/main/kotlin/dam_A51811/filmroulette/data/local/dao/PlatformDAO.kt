package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.Platform
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for performing database operations on the platforms table.
 */
@Dao
interface PlatformDAO {
    /**
     * Inserts a single platform into the database.
     * Replaces the existing platform if a conflict occurs.
     *
     * @param platform The platform entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(platform: Platform)

    /**
     * Inserts a list of platforms into the database.
     * Replaces existing platforms if conflicts occur.
     *
     * @param platforms The list of platform entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(platforms: List<Platform>)

    /**
     * Retrieves a platform by its unique identifier.
     *
     * @param id The unique identifier of the platform.
     * @return The platform entity if found, or null otherwise.
     */
    @Query("SELECT * FROM platforms WHERE id = :id")
    suspend fun getPlatformById(id: Long): Platform?

    /**
     * Retrieves all platforms from the database as a reactive stream.
     *
     * @return A flow emitting the list of all platform entities.
     */
    @Query("SELECT * FROM platforms")
    fun getAllPlatforms(): Flow<List<Platform>>
}
