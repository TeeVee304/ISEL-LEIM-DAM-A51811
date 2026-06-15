package dam_A51811.filmroulette.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A51811.filmroulette.data.local.User

/**
 * Data Access Object for performing database operations on the users table.
 */
@Dao
interface UserDAO {
    /**
     * Inserts a single user into the database.
     * Replaces the existing user if a conflict occurs.
     *
     * @param user The user entity to insert.
     * @return The row ID of the newly inserted user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return The user entity if found, or null otherwise.
     */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): User?

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user.
     * @return The user entity if found, or null otherwise.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Deletes a user from the database by their unique identifier.
     *
     * @param id The unique identifier of the user to delete.
     */
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Long)
}
