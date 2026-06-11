package dam_A51811.filmroulette.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dam_A51811.filmroulette.data.local.dao.MovieDAO
import dam_A51811.filmroulette.data.local.dao.MovieListDAO
import dam_A51811.filmroulette.data.local.dao.PlatformDAO
import dam_A51811.filmroulette.data.local.dao.RatingDAO
import dam_A51811.filmroulette.data.local.dao.UserDAO

/**
 * Room database for the FilmRoulette application.
 *
 * ### Entities
 * | Entity               | Table               | Notes                          |
 * |----------------------|---------------------|--------------------------------|
 * | [Movie]              | `movies`            | API-supplied PK                |
 * | [Platform]           | `platforms`         | API-supplied PK                |
 * | [MoviePlatformCrossRef] | `movie_platform` | N:M Movie ↔ Platform           |
 * | [User]               | `users`             | Auto-generated PK              |
 * | [Rating]             | `ratings`           | Composite PK (userId, movieId) |
 * | [MovieList]          | `movie_lists`       | Auto-generated PK              |
 * | [MovieListCrossRef]  | `movie_list_entries`| N:M MovieList ↔ Movie          |
 *
 * ### Type converters
 * - [GenreConverter] — `List<Genre>` ↔ comma-separated String
 */
@Database(
    entities = [
        Movie::class,
        Platform::class,
        MoviePlatformCrossRef::class,
        User::class,
        Rating::class,
        MovieList::class,
        MovieListCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(GenreConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDAO
    abstract fun platformDao(): PlatformDAO
    abstract fun userDao(): UserDAO
    abstract fun ratingDao(): RatingDAO
    abstract fun movieListDao(): MovieListDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "filmroulette_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
