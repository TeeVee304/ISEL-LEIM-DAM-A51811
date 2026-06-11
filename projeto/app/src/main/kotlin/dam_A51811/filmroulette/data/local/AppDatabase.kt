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
 * Room database for the FilmRoulette application.
 *
 * ### Entities
 * | Entity               | Table                | Notes                          |
 * |----------------------|----------------------|--------------------------------|
 * | [Movie]              | `movies`             | API-supplied PK                |
 * | [Platform]           | `platforms`          | API-supplied PK                |
 * | [MoviePlatformCrossRef] | `movie_platform`  | N:M Movie ↔ Platform           |
 * | [User]               | `users`              | Auto-generated PK              |
 * | [Rating]             | `ratings`            | Composite PK (userId, movieId) |
 * | [MovieList]          | `movie_lists`        | Auto-generated PK; has `visibility` column |
 * | [MovieListCrossRef]  | `movie_list_entries` | N:M MovieList ↔ Movie          |
 * | [Friendship]         | `friendships`        | Composite PK (user_id_1, user_id_2) |
 *
 * ### Type converters
 * - [GenreConverter] — `List<Genre>` ↔ comma-separated String
 *
 * ### Firebase note
 * This database acts as the local cache / offline store.
 * When Firebase is introduced, [Friendship] maps to a Firestore
 * collection and [MovieList] maps to a user sub-collection.
 * The repository layer will be the single point responsible for
 * keeping both in sync.
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
        Friendship::class        // ← NEW
    ],
    version = 2,                 // bumped from 1 → 2
    exportSchema = false
)
@TypeConverters(GenreConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDAO
    abstract fun platformDao(): PlatformDAO
    abstract fun userDao(): UserDAO
    abstract fun ratingDao(): RatingDAO
    abstract fun movieListDao(): MovieListDAO
    abstract fun friendshipDao(): FriendshipDAO   // ← NEW

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
                // Destructive migration is acceptable during development.
                // Replace with a proper Migration object before shipping to production.
                // `dropAllTables = true` silences the deprecation warning on the new Room API.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
