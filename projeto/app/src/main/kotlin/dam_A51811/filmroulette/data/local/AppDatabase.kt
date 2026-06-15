package dam_A51811.filmroulette.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dam_A51811.filmroulette.data.local.dao.FriendshipDAO
import dam_A51811.filmroulette.data.local.dao.MovieDAO
import dam_A51811.filmroulette.data.local.dao.MovieListDAO
import dam_A51811.filmroulette.data.local.dao.PlatformDAO
import dam_A51811.filmroulette.data.local.dao.RatingDAO
import dam_A51811.filmroulette.data.local.dao.UserDAO


/**
 * The main Room database class for the application.
 * Defines the database configuration and serves as the main access point to the persisted data.
 */
@Database(
    entities = [
        Movie::class,
        Platform::class,
        MoviePlatformCrossRef::class,
        User::class,
        Rating::class,
        MovieList::class,
        MovieListCrossRef::class,
        Friendship::class        
    ],
    version = 4,                 
    exportSchema = false
)
@TypeConverters(GenreConverter::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Retrieves the Data Access Object for Movie entities.
     *
     * @return The MovieDAO instance.
     */
    abstract fun movieDao(): MovieDAO

    /**
     * Retrieves the Data Access Object for Platform entities.
     *
     * @return The PlatformDAO instance.
     */
    abstract fun platformDao(): PlatformDAO

    /**
     * Retrieves the Data Access Object for User entities.
     *
     * @return The UserDAO instance.
     */
    abstract fun userDao(): UserDAO

    /**
     * Retrieves the Data Access Object for Rating entities.
     *
     * @return The RatingDAO instance.
     */
    abstract fun ratingDao(): RatingDAO

    /**
     * Retrieves the Data Access Object for MovieList entities.
     *
     * @return The MovieListDAO instance.
     */
    abstract fun movieListDao(): MovieListDAO

    /**
     * Retrieves the Data Access Object for Friendship entities.
     *
     * @return The FriendshipDAO instance.
     */
    abstract fun friendshipDao(): FriendshipDAO   

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the AppDatabase.
         * Creates the database if it does not exist.
         *
         * @param context The application context used to create or access the database.
         * @return The singleton instance of AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "filmroulette_database"
                )
                
                
                
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
